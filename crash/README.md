# Crash 강의 실습

## PostgreSQL 설정

### Docker로 PostgreSQL 컨테이너 생성

아래 명령어로 로컬 개발용 PostgreSQL 컨테이너를 생성한다.

```bash
docker run --name crash-postgres \
  -e POSTGRES_INITDB_ARGS="--data-checksums -E utf8 --no-locale" \
  -e POSTGRES_USER=<DB_USER> \
  -e POSTGRES_PASSWORD=<DB_PASSWORD> \
  -e POSTGRES_DB=crash-db \
  -e TZ=Asia/Seoul \
  -v $HOME/Desktop/study/postgres/crash-data:/var/lib/postgresql/data \
  -p 15432:5432 \
  -d postgres:17
```

| 옵션 | 설명 |
|------|------|
| `--name crash-postgres` | 컨테이너 이름 |
| `--data-checksums` | 데이터 무결성 체크섬 활성화 |
| `-E utf8 --no-locale` | 문자셋 UTF-8, 로케일 없이 초기화 |
| `POSTGRES_USER` | 접속 유저명 |
| `POSTGRES_PASSWORD` | 접속 비밀번호 |
| `POSTGRES_DB` | 기본 생성 데이터베이스명 |
| `TZ=Asia/Seoul` | 타임존을 한국으로 설정 |
| `-v $HOME/.../crash-data:/var/lib/postgresql/data` | 데이터 볼륨 마운트 (컨테이너 삭제 시에도 데이터 유지) |
| `-p 15432:5432` | 호스트 15432 포트 → 컨테이너 5432 포트 포워딩 |
| `-d postgres:17` | PostgreSQL 17 이미지를 백그라운드로 실행 |

### Spring Boot 연동

`application.properties` 또는 `application.yml`에 아래와 같이 데이터소스를 설정한다.

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:15432/crash-db
    username: <DB_USER>
    password: <DB_PASSWORD>
    driver-class-name: org.postgresql.Driver
```

> 실제 값은 환경변수나 별도의 로컬 설정 파일로 관리하고, Git에 커밋하지 않는다.

---

## 개발환경 분리 (Spring Profile)

### 개요

Spring Boot는 `application-{profile}.yaml` 네이밍 컨벤션으로 환경별 설정을 분리할 수 있다.  
공통 설정은 `application.yaml`에, 환경별 설정은 각 프로파일 파일에 작성한다.

```
src/main/resources/
├── application.yaml          # 공통 설정 (모든 환경에서 항상 로드)
├── application-dev.yaml      # 개발 환경 설정
└── application-prod.yaml     # 운영 환경 설정
```

### 파일별 역할

#### `application.yaml` — 공통 설정

```yaml
spring:
  security:
    user.name: ${USERNAME}
    user.password: ${PASSWORD}
```

- 모든 환경에서 공통으로 적용되는 설정을 작성한다.
- 민감한 값은 `${ENV_VAR}` 형식의 환경변수로 주입받는다.

#### `application-dev.yaml` — 개발 환경

```yaml
spring:
  jpa:
    database: postgresql
    show-sql: true
    hibernate:
      ddl-auto: create-drop
      format_sql: true
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
      properties:
        hibernate:
          dialect: org.hibernate.dialect.PostgreSQLDialect
  dataSource:
    url: jdbc:postgresql://localhost:15432/crash-db
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

#### `application-prod.yaml` — 운영 환경

```yaml
spring:
  jpa:
    database: postgresql
    show-sql: true
    hibernate:
      ddl-auto: create-drop   # 운영 시 none 또는 validate로 변경 권장
      ...
  dataSource:
    url: jdbc:postgresql://localhost:15432/prod-crash-db
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

### dev vs prod 주요 차이점

| 항목 | dev | prod |
|------|-----|------|
| DB 이름 | `crash-db` | `prod-crash-db` |
| `show-sql` | `true` (SQL 로그 출력) | `true` |
| `ddl-auto` | `create-drop` | `create-drop` → 운영 시 `none` 또는 `validate`로 변경 권장 |

> ⚠️ `ddl-auto: create-drop`은 앱 종료 시 테이블을 삭제하므로 운영 환경에서는 반드시 `none` 또는 `validate`로 바꿔야 한다.

### 프로파일 활성화 방법

**Gradle bootRun으로 실행 시**
```bash
# dev 프로파일로 실행
./gradlew bootRun --args='--spring.profiles.active=dev'

# prod 프로파일로 실행
./gradlew bootRun --args='--spring.profiles.active=prod'
```

**IDE (IntelliJ) 실행 시**
```
VM options: -Dspring.profiles.active=dev
```

**CLI (jar) 실행 시**
```bash
java -jar app.jar --spring.profiles.active=dev
```

**환경변수로 지정**
```bash
export SPRING_PROFILES_ACTIVE=dev
```

### JPA 주요 설정 설명

| 설정 | 값 | 설명 |
|------|----|------|
| `show-sql` | `true` | 실행 SQL을 콘솔에 출력 |
| `format_sql` | `true` | SQL을 보기 좋게 포맷팅 |
| `ddl-auto` | `create-drop` | 앱 시작 시 DDL 생성, 종료 시 삭제 |
| `physical-strategy` | `PhysicalNamingStrategyStandardImpl` | 엔티티 필드명을 그대로 컬럼명으로 사용 |
| `dialect` | `PostgreSQLDialect` | PostgreSQL 전용 SQL 방언 사용 |
