# Generator Plugin 아키텍처 분석

## 핵심 컴포넌트

- **MvcGeneratorPlugin**: Gradle Plugin 진입점. DSL 확장(`mvcGenerator {}`) 등록, JDBC 전용 Configuration 등록, 개별 태스크 및 집계 태스크(`generateMvc`) 등록.
- **MvcGeneratorExtension**: `build.gradle`의 DSL 블록. DB 접속 정보, 테이블 목록, 출력 경로, 패키지 구조, 템플릿 설정, 클래스명 접미사 등 모든 설정을 담는다.
- **MvcGeneratorBaseTask**: 모든 개별 코드 생성 태스크의 공통 베이스. Extension → GeneratorConfig 변환, JDBC 격리 ClassLoader 구성, ColumnInspector/TemplateEngine 초기화 후 `executeForTable()`에 위임.
- **GenerateMvcInteractiveTask**: 대화형 태스크(`./gradlew generate`). TableSelector로 테이블 패턴 검색·확인, 생성 종류 선택 후 각 Generator를 직접 호출한다.

## 레이어 구조

| 레이어 | 클래스 |
|--------|--------|
| Plugin / DSL | `MvcGeneratorPlugin`, `MvcGeneratorExtension` |
| Task | `MvcGeneratorBaseTask`, `GenerateMvcInteractiveTask`, `GenerateXxx*Task` (8개) |
| Engine | `GeneratorConfig`, `ColumnInspector`, `TemplateEngine`, `PathResolver` |
| Generator | `ModelCodeGenerator`, `ControllerCodeGenerator`, `ServiceCodeGenerator`, `PersistenceCodeGenerator`, `PersistenceAnnoCodeGenerator`, `ViewCodeGenerator`, `QueryCodeGenerator` |
| Interactive | `TableSelector`, `GenerationType` |
| VO | `ColumnInfo`, `PrimaryInfo` |

## 설계 특징

- **JDBC 격리**: JDBC 드라이버를 `URLClassLoader`로 격리해 플러그인 클래스패스 오염을 방지.
- **불변 설정 객체**: `GeneratorConfig`는 Builder 패턴 기반 불변 POJO로, 모든 Generator에 일관된 설정을 제공.
- **템플릿 오버라이드**: FreeMarker `MultiTemplateLoader`로 커스텀 디렉토리 → 내장 클래스패스 순으로 fallback.
- **DDD 패턴 지원**: `useDddPattern=true` 시 테이블명 기반 서브 패키지(`basePackage.{domain}`)를 자동 생성.
- **Mapper 이중화**: `mapperType`이 `xml`이면 `PersistenceCodeGenerator`, `annotation`이면 `PersistenceAnnoCodeGenerator` 사용.
