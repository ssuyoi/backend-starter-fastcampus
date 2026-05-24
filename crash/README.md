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
