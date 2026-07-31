# mvc-generator-gradle-plugin 리팩토링 노트

`mvc-generator-gradle-plugin` 모듈 분석 결과 및 개선 항목. 오픈소스 공개 전 코드 품질 관점에서 정리.

**상태: 개선 항목 6/6 완료.** 각 항목 적용 후 `./gradlew test` 매번 재실행, 최종 33건
(`ColumnInspectorTest` 11 · `PathResolverTest` 14 · `PluginApplyTest` 2 · `StringUtilTest` 6)
전부 통과 확인.

## 잘 되어 있는 부분 (참고용, 손대지 않음)
- JDBC 드라이버 클래스로더 격리 (`URLClassLoader` + `DriverShim`)
- 컬럼 코멘트 조회는 Oracle/MySQL/PostgreSQL 전부 `PreparedStatement` 사용
- `GeneratorConfig`를 Builder로 캡슐화해 Gradle `Property`/`Extension`과 분리
- DDD 패턴 on/off에 따른 경로 분기가 `PathResolver`에 일관되게 구현됨

## 개선 항목

- [x] **1. `GeneratorConfig` 빌드 로직 중복 제거**
  `MvcGeneratorBaseTask.buildConfig()`와 `GenerateMvcInteractiveTask.buildConfigBuilder()`가
  `GeneratorConfig.builder()` 체인(~25줄)을 거의 동일하게 반복. `buildJdbcClassLoader()`도
  두 클래스에 중복. 필드 하나 추가 시 두 곳을 다 고쳐야 하는 누락 위험.
  → `MvcGeneratorExtension.toConfigBuilder()`로 공통화, `buildJdbcClassLoader()`는
  `MvcGeneratorBaseTask`의 정적 메서드로 통합.

- [x] **2. 핵심 로직 유닛 테스트 추가**
  현재 테스트는 `PluginApplyTest`(Gradle TestKit, 태스크 등록 여부만 검증) 1개뿐.
  `ColumnInspector`(타입 정규화, 식별자 대소문자 처리), `PathResolver`(경로 조합 규칙),
  `StringUtil`에 대한 유닛 테스트 없음.
  → `StringUtilTest`(6), `PathResolverTest`(14, DDD on/off 전 분기), `ColumnInspectorTest`(7,
  `normalizeClassName` 전 분기) 추가. `normalizeClassName`은 테스트를 위해 `private static` →
  패키지 전용 `static`으로 가시성만 변경(동작 변화 없음). `ColumnInspector`의 DB 연결 필요
  메서드(`normalizeIdentifier`, `getSchema` 등)는 목(mock) 인프라가 없어 범위 밖으로 남김.

- [x] **3. 패키지 네이밍 불일치**
  소스는 `com.cnbsoft.generator.*`, 테스트는 `com.cnbsoft.plugin.generator.PluginApplyTest`로
  계층 구조가 다름.
  → 테스트 대상 클래스(`MvcGeneratorPlugin`)와 동일한 패키지인 `com.cnbsoft.generator.plugin`으로
  `git mv` + 패키지 선언 변경. 빈 디렉토리(`com/cnbsoft/plugin/`) 정리.

- [x] **4. `ColumnInspector.COLUMN_QUERY` 식별자 바인딩 미적용**
  `"select * from %s where 1=1"`을 `String.format`으로 조합. 현재 입력 소스(build.gradle
  설정값, DB 메타데이터)는 신뢰할 수 있어 실질 위험은 낮으나, 오픈소스 공개 시 왜 안전한지
  설계 의도를 문서화하거나 최소한의 식별자 화이트리스트 검증 추가 검토.
  → `SAFE_IDENTIFIER`(`[A-Za-z0-9_$#]+`) 화이트리스트와 `validateIdentifier()` 추가.
  생성자의 `dbSchema`, `getColumnInfos()`의 정규화된 `tableName`을 SQL에 조합하기 전 검증,
  위반 시 `IllegalArgumentException`. 코멘트 조회 3종은 이미 `PreparedStatement` 바인딩이라
  대상에서 제외. `ColumnInspectorTest`에 정상/인젝션 시도/공백/qualified name/null 4건 추가.

- [x] **5. Gradle Plugin Portal 게시 준비 미비**
  `com.gradle.plugin-publish` 플러그인 미적용, `vcsUrl`/`website`/`tags` 메타데이터 없음.
  그룹ID `com.cnbsoft.plugin`의 도메인 소유권 검증 필요 여부 확인.
  → `com.gradle.plugin-publish` 1.3.0 적용, `tags` 추가. `website`/`vcsUrl`은 GitHub 저장소가
  아직 없어(사용자 확인) `TODO` 문자열로 남김 — **공개 저장소 URL 확정 후, 실제 게시 전에
  반드시 채워야 함**. 그룹ID는 `com.cnbsoft.plugin` 유지하기로 결정(사용자 확인) —
  **단 실제 Plugin Portal 게시 시 cnbsoft.com 도메인 소유권 DNS TXT 검증 절차가 별도로 필요.**

- [x] **6. `GenerateMvcInteractiveTask`의 주석 처리된 `QUERY` 생성 코드**
  왜 대화형 모드에서만 비활성화됐는지 불명확한 죽은 코드. 삭제 여부 결정 필요
  (기존 코드라 임의 삭제하지 않음).
  → 원인 확인 후 삭제. `GenerationType` enum에 `QUERY` 값 자체가 없어 주석 해제 시 컴파일
  불가한 잔재 코드였음. 쿼리 XML 생성은 별도 Task(`generateMvcQuery` →
  `GenerateQueryTask` → `QueryCodeGenerator`)로 정상 동작 중이라 기능 손실 없음.

## 후속 조치 필요 (코드 작업 아님)

이번 리팩토링 범위 밖의, 사람이 직접 처리해야 하는 항목:

- **Plugin Portal 게시 전 필수**: `build.gradle`의 `gradlePlugin.website` / `vcsUrl`이 아직
  `TODO` 문자열임. GitHub 저장소 공개 후 실제 URL로 교체 필요 (항목 5).
- **그룹ID 도메인 검증**: `com.cnbsoft.plugin`을 유지하기로 했으므로, 실제 게시 시
  Gradle Plugin Portal에서 `cnbsoft.com` 도메인 소유권 DNS TXT 검증 절차가 필요함 (항목 5).
- **모듈 상위 레벨 항목** (이전 대화에서 별도로 짚었던, 이 모듈 밖 저장소 루트 이슈):
  LICENSE 파일 부재, `mvc-generator-test-app`/`mvc-generator-sample-data`/
  `mvc-generator-dbms-containers`에 커밋된 로컬 DB 자격증명(`dino`/`dino123`) 치환.
