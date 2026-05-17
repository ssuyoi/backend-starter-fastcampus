# Thread Board

게시글 CRUD 및 JWT 인증을 구현한 게시판 백엔드 API 프로젝트

## 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Java 17 |
| Framework | Spring Boot 4.0.6 |
| ORM | Spring Data JPA / Hibernate |
| Security | Spring Security + JWT |
| Database | PostgreSQL |
| Build | Gradle (Kotlin DSL) |

## 실행 방법

### 사전 조건

- Java 17
- PostgreSQL 실행 중 (port 5432)
- `board-db` 데이터베이스 생성

```sql
CREATE DATABASE "board-db";
```

### 환경 변수 설정

```bash
export DB_USERNAME=your_db_username
export DB_PASSWORD=your_db_password
export ADMIN_USERNAME=admin
export ADMIN_PASSWORD=your_admin_password
```

### 백엔드 실행

```bash
cd thread-board
./gradlew bootRun
```

서버는 `http://localhost:8080` 에서 실행

### 프론트엔드 실행

강사님이 제공한 Docker 이미지 사용

```bash
docker run -p 3000:3000 devjayce/board:user-post-apis
```

`http://localhost:3000` 에서 확인

## API 엔드포인트

Base URL: `http://localhost:8080/api/v1`

| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/posts` | 게시글 전체 조회 |
| GET | `/posts/{postId}` | 게시글 단건 조회 |
| POST | `/posts` | 게시글 작성 |
| PATCH | `/posts/{postId}` | 게시글 수정 |
| DELETE | `/posts/{postId}` | 게시글 삭제 |
