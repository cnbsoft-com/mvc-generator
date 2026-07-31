# MVC Generator Gradle Plugin

데이터베이스 테이블 메타데이터를 분석하여 **Spring MVC + MyBatis CRUD 코드**를 자동으로 생성하는 Gradle 플러그인입니다.

---

## 목차

- [개요](#개요)
- [프로젝트 구조](#프로젝트-구조)
- [기술 스택](#기술-스택)
- [설치 방법](#설치-방법)
- [설정](#설정)
- [사용법](#사용법)
- [생성 파일 목록](#생성-파일-목록)
- [Gradle Tasks](#gradle-tasks)
- [개발 환경 구성](#개발-환경-구성)
- [예제 앱으로 빠르게 시작하기](#예제-앱으로-빠르게-시작하기)

---

## 개요

`mvc-generator`는 JDBC를 통해 데이터베이스 테이블 스키마를 분석하고, FreeMarker 템플릿을 기반으로 Spring MVC 계층별 코드(Model, Controller, Service, Mapper, View)를 자동 생성합니다.

**주요 특징:**

- 데이터베이스 스키마로부터 MVC 전 계층 코드 자동 생성
- 컨트롤러 방식 선택: `web` (뷰 반환) / `api` (JSON 반환)
- MyBatis 매퍼 방식 선택:
  - `xml` — MyBatis XML 쿼리 파일 (동적 쿼리 지원)
  - `annotation` — Java 어노테이션 기반, 정적 쿼리를 Java text block(`""" """`)으로 생성
- 클래스명 접미사 커스터마이징 (`modelSuffix`, `mapperSuffix` 등)
- 멀티 DB 지원: Oracle, MySQL, PostgreSQL
- JDBC 드라이버 클래스로더 격리로 버전 충돌 방지
- 대화형(Interactive) CLI 모드 지원

---

## 프로젝트 구조

```
mvc-generator/
├── mvc-generator-gradle-plugin/     # 핵심 Gradle 플러그인 (배포 대상)
│   ├── src/main/java/               # 플러그인 소스
│   │   └── com/cnbsoft/generator/
│   │       ├── plugin/              # Gradle 플러그인 진입점 및 DSL 설정
│   │       ├── engine/              # 각 계층별 코드 생성기 및 경로 처리
│   │       ├── db/                  # JDBC 메타데이터 추출 (ColumnInspector)
│   │       ├── task/                # Gradle Task 구현체
│   │       └── template/            # FreeMarker 템플릿 엔진 래퍼
│   ├── src/main/resources/
│   │   └── .../templates/default/   # FreeMarker 템플릿 파일 (.tpl)
│   └── src/test/                    # Gradle TestKit 기반 플러그인 통합 테스트
│
├── mvc-generator-test-app/          # 플러그인 사용 예제 프로젝트
├── mvc-generator-sample-data/       # Liquibase 데이터베이스 마이그레이션
└── mvc-generator-dbms-containers/   # Oracle 개발 DB Docker Compose
```

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| 언어 | Java 21 |
| 빌드 | Gradle 8.5+ |
| 템플릿 엔진 | FreeMarker 2.3.32 |
| DB 커넥션 풀 | Apache Commons DBCP2 2.9.0 |
| 보일러플레이트 감소 | Lombok 1.18.30 |
| 지원 DB | Oracle, MySQL, PostgreSQL |
| 스키마 마이그레이션 | Liquibase 4.30.0 |
| 테스트 | JUnit 4 + Gradle TestKit |

---

## 설치 방법

### 1. 로컬 빌드 및 게시

```bash
cd mvc-generator-gradle-plugin
./gradlew publishToMavenLocal
```

### 2. 대상 프로젝트 `settings.gradle` 설정

```groovy
pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}
```

### 3. 대상 프로젝트 `build.gradle`에 플러그인 적용

```groovy
plugins {
    id 'com.cnbsoft.mvc-generator' version '1.0.0'
}
```

---

## 설정

`build.gradle`에 `mvcGenerator` 블록을 추가합니다.

```groovy
mvcGenerator {
    // [필수] 데이터베이스 연결 정보
    dbDriver   = 'oracle.jdbc.driver.OracleDriver'
    dbUrl      = 'jdbc:oracle:thin:@localhost:1521/xe'
    dbUsername = 'myuser'
    dbPassword = project.findProperty('dbPassword') ?: 'mypassword'

    // [필수] 코드를 생성할 테이블 목록 (생략 시 대화형 모드에서 선택)
    tableNames = ['USER', 'USER_DETAIL', 'USER_GROUP']

    // [필수] 생성 코드의 기본 패키지
    basePackage = 'com.example.myapp'

    // [필수] 출력 디렉터리
    outputDir         = file("${projectDir}/src/generated")
    resourceOutputDir = file("${projectDir}/src/generated/resources")
    viewOutputDir     = file("${projectDir}/src/generated/webapp")

    // [선택] 서브 패키지명 커스터마이징 (기본값)
    modelSubPackage       = 'vo'
    controllerSubPackage  = 'controller'
    serviceSubPackage     = 'service'
    persistenceSubPackage = 'mapper'
    implSubPackage        = 'impl'

    // [선택] 생성 방식
    controllerType = 'web'        // 'web'(@Controller, 기본값) | 'api'(@RestController)
    mapperType     = 'xml'        // 'xml'(쿼리 XML, 기본값) | 'annotation'(어노테이션 + text block)

    // [선택] 클래스명 접미사 커스터마이징 (기본값)
    modelSuffix       = 'Dto'         // 기본값: ""          → UserDto.java
    controllerSuffix  = 'Resource'    // 기본값: "Controller" → UserResource.java
    serviceSuffix     = 'Svc'         // 기본값: "Service"    → UserSvc.java
    serviceImplSuffix = 'SvcImpl'     // 기본값: "ServiceImpl"→ UserSvcImpl.java
    mapperSuffix      = '00Mapper'    // 기본값: "Mapper"     → User00Mapper.java

    // [선택] Model(VO)에 빌더 패턴 적용 여부 (기본값: true)
    // true: private 생성자 + static Builder 클래스
    // false: public 기본 생성자 + getter/setter
    useModelBuilder = true

    // [선택] Mapper SQL의 FROM/INSERT INTO 테이블명 대소문자 (기본값: true, 컬럼명에는 영향 없음)
    // true: FROM USER_ACCOUNT | false: from user_account
    useModelUpperCase = true

    // [선택] 뷰 및 쿼리 파일 설정
    viewPath      = 'WEB-INF/views'
    viewExtension = '.tpl'
    queryPath     = 'mapper'
    queryPrefix   = 'mapper-'
    queryExt      = 'xml'

    // [선택] 기존 파일 덮어쓰기 여부 (기본값: false)
    overwriteExisting = false

    // [선택] DDD(Domain-Driven Design) 패턴 적용 (기본값: false)
    // true: 도메인별 폴더 구조 (com.example.app.${domain}/model, controller, service, mapper)
    // false: 계층별 폴더 구조 (com.example.app/vo, controller, service, mapper)
    useDddPattern = false

    // [선택] 커스텀 템플릿 디렉터리 (기본 템플릿 오버라이드)
    // customTemplateDir = file("src/main/mvcgen/templates")
}

// JDBC 드라이버 의존성 (격리된 클래스로더로 로드, 프로젝트 classpath 미포함)
dependencies {
    // Oracle
    mvcGeneratorJdbc 'com.oracle.database.jdbc:ojdbc11:23.6.0.24.10'

    // MySQL
    // mvcGeneratorJdbc 'com.mysql:mysql-connector-j:9.1.0'

    // PostgreSQL
    // mvcGeneratorJdbc 'org.postgresql:postgresql:42.7.2'
}
```

---

## 사용법

### 전체 코드 일괄 생성

```bash
./gradlew generateMvc
```

### 계층별 개별 생성

```bash
./gradlew generateMvcModel         # Model (VO)
./gradlew generateMvcController    # Controller
./gradlew generateMvcService       # Service + ServiceImpl
./gradlew generateMvcPersistence   # MyBatis Mapper Interface
./gradlew generateMvcQuery         # MyBatis XML 쿼리 파일 (xml 모드)
./gradlew generateMvcFormView      # 입력 폼 뷰
./gradlew generateMvcGetView       # 상세 뷰
./gradlew generateMvcListView      # 목록 뷰
```

### 대화형 모드 (Interactive)

CLI에서 테이블 패턴을 입력하고 생성할 컴포넌트를 대화형으로 선택합니다.

```bash
./gradlew generate --no-daemon --console=plain
```

실행 예시:
```
테이블명 패턴을 입력하세요 (예: USER%): USER%
  1. USER
  2. USER_DETAIL
  3. USER_GROUP
생성할 테이블 번호를 선택하세요: 1
생성 유형을 선택하세요 (1=ALL, 2=MODEL, 3=CONTROLLER, ...): 1
```

---

## 생성 파일 목록

`USER` 테이블에 대해 `basePackage = 'com.example.myapp'` 기본 설정으로 생성 시:

### 계층별 구조 (useDddPattern = false, 기본값)

| 파일 | 경로 | 설명 |
|------|------|------|
| `UserVo.java` | `com/example/myapp/vo/` | MyBatis `@Alias` POJO |
| `UserController.java` | `com/example/myapp/controller/` | Spring Controller |
| `UserService.java` | `com/example/myapp/service/user/` | Service Interface |
| `UserServiceImpl.java` | `com/example/myapp/service/user/impl/` | Service 구현체 |
| `UserMapper.java` | `com/example/myapp/mapper/user/` | MyBatis Mapper Interface |
| `mapper-User.xml` | `resources/mapper/` | MyBatis 쿼리 XML (`xml` 모드) |
| `user_form.tpl` | `webapp/WEB-INF/views/user/` | 입력 폼 뷰 |
| `user_get.tpl` | `webapp/WEB-INF/views/user/` | 상세 뷰 |
| `user_list.tpl` | `webapp/WEB-INF/views/user/` | 목록 뷰 |

### 도메인별 구조 (useDddPattern = true, DDD 패턴)

| 파일 | 경로 | 설명 |
|------|------|------|
| `UserVo.java` | `com/example/myapp/user/model/` | MyBatis `@Alias` POJO |
| `UserController.java` | `com/example/myapp/user/controller/` | Spring Controller |
| `UserService.java` | `com/example/myapp/user/service/` | Service Interface |
| `UserServiceImpl.java` | `com/example/myapp/user/service/impl/` | Service 구현체 |
| `UserMapper.java` | `com/example/myapp/user/mapper/` | MyBatis Mapper Interface |
| `mapper-User.xml` | `resources/mapper/user/` | MyBatis 쿼리 XML (`xml` 모드) |
| `user_form.tpl` | `webapp/WEB-INF/views/user/` | 입력 폼 뷰 |
| `user_get.tpl` | `webapp/WEB-INF/views/user/` | 상세 뷰 |
| `user_list.tpl` | `webapp/WEB-INF/views/user/` | 목록 뷰 |

### annotation 모드 생성 예시

`mapperType = 'annotation'` 설정 시 Mapper Interface는 동적 쿼리 없이 Java text block으로 생성됩니다.

```java
@Mapper
public interface UserMapper {

    @Select("""
            SELECT ID, NAME, EMAIL
            FROM USER
            WHERE ID = #{id}
            """)
    UserVo get(UserVo user);

    @Select("""
            SELECT ID, NAME, EMAIL
            FROM USER
            """)
    List<UserVo> getList(UserVo user);

    @Insert("""
            INSERT INTO USER (ID, NAME, EMAIL)
            VALUES (#{id}, #{name}, #{email})
            """)
    int create(UserVo user);

    @Update("""
            UPDATE USER
            SET NAME = #{name}, EMAIL = #{email}
            WHERE ID = #{id}
            """)
    int update(UserVo user);

    @Delete("""
            DELETE FROM USER
            WHERE ID = #{id}
            """)
    int delete(UserVo user);
}
```

> `getList`는 정적 쿼리(WHERE 절 없음)로 전체 목록을 반환합니다. 조건부 검색이 필요한 경우 `xml` 모드를 사용하거나 별도 메서드를 추가하세요.

---

## Gradle Tasks

| Task | 그룹 | 설명 |
|------|------|------|
| `generate` | MVC Generator | 대화형 모드 (테이블·컴포넌트 선택) |
| `generateMvc` | — | 전체 코드 일괄 생성 (개별 태스크 집계) |
| `generateMvcModel` | — | Model(VO) 생성 |
| `generateMvcController` | — | Controller 생성 |
| `generateMvcService` | — | Service + Impl 생성 |
| `generateMvcPersistence` | — | Mapper Interface 생성 |
| `generateMvcQuery` | — | MyBatis 쿼리 XML 생성 |
| `generateMvcFormView` | — | 입력 폼 뷰 생성 |
| `generateMvcGetView` | — | 상세 뷰 생성 |
| `generateMvcListView` | — | 목록 뷰 생성 |

> `generate` 만 `MVC Generator` 그룹에 노출됩니다. 나머지 태스크는 `./gradlew tasks --all` 로 확인할 수 있습니다.

---

## 개발 환경 구성

### Oracle 개발 DB 실행 (Docker)

```bash
cd mvc-generator-dbms-containers
docker compose up -d
```

- Host: `localhost:1521`
- SID: `xe`
- User: `dino` / Password: `dino123`

### 샘플 스키마 마이그레이션 (Liquibase)

```bash
cd mvc-generator-sample-data
./gradlew update
```

Oracle, MySQL, PostgreSQL 각각에 대한 프로퍼티 파일을 지원합니다.

### 플러그인 테스트 실행

```bash
cd mvc-generator-gradle-plugin
./gradlew test
```

테스트는 Gradle TestKit 기반 통합 테스트로 DB 연결 없이 실행됩니다.

| 테스트 | 검증 내용 |
|--------|-----------|
| `pluginAppliesAndRegistersGroupTask` | `generate` 태스크가 `MVC Generator` 그룹에 등록되는지 확인 |
| `pluginRegistersAllComponentTasks` | 9개 컴포넌트 태스크 전체가 등록되는지 확인 (`tasks --all`) |

---

## 예제 앱으로 빠르게 시작하기

`mvc-generator-test-app`은 플러그인이 적용된 예제 프로젝트입니다.
Oracle DB(`localhost:1521/xe`, user: `dino`)와 Docker 컨테이너가 실행 중인 상태에서 아래 명령을 실행합니다.

> **현재 설정** (`mvc-generator-test-app/build.gradle`)
> - 컨트롤러 방식: `api` (@RestController)
> - 매퍼 방식: `annotation` (정적 쿼리 + Java text block)
> - 접미사: `Dto` / `Resource` / `Svc` / `SvcImpl` / `00Mapper`
> - 출력 경로: `src/generated/`

### 1단계: 플러그인 로컬 게시

```bash
cd mvc-generator-gradle-plugin
./gradlew publishToMavenLocal
```

### 2단계: 예제 앱에서 코드 생성

```bash
cd mvc-generator-test-app

# 대화형 모드 (테이블 및 생성 유형 선택)
./gradlew generate --no-daemon --console=plain
```

### DB 패스워드를 외부에서 주입하는 경우

```bash
./gradlew generateMvc -PdbPassword=mypassword
```
