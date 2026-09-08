# JWT 인증 및 예외 처리 — 학습 정리

> 커밋: `be0bb032` · 2026-05-17  
> 주제: JWT 인증 필터 체인 구성 (jjwt, Spring Security)

---

## 1. 오늘 구현한 것 한눈에 보기

```
HTTP 요청
   │
   ▼
JwtExceptionFilter          ← ② JWT 예외를 잡아서 401 응답으로 변환
   │
   ▼
JwtAuthenticationFilter     ← ① Authorization 헤더에서 토큰 추출 → 검증 → SecurityContext 등록
   │
   ▼
UsernamePasswordAuthenticationFilter (Spring Security 기본)
   │
   ▼
Controller
```

필터가 두 개로 분리된 이유: `JwtAuthenticationFilter`에서 던진 예외를 Spring Security 필터 체인 안에서 잡으려면 **앞에서 감싸는 별도 필터**(`JwtExceptionFilter`)가 필요하다.

---

## 2. 파일별 핵심 내용

### JwtService.java — 토큰 생성 & 검증
```java
private static final SecretKey key = Jwts.SIG.HS256.key().build();
```
- **HS256** 알고리즘으로 서명에 사용할 SecretKey를 런타임에 생성
- 토큰 유효기간: **3시간** (`1000 * 60 * 60 * 3` ms)
- `generateAccessToken(UserDetails)` → username을 subject로 담아 JWT 발급
- `getUsername(String token)` → 토큰 파싱 후 subject(username) 반환, 실패 시 `JwtException` 전파

> ⚠️ 현재 SecretKey가 **재시작 시 매번 새로 생성**된다. 운영 환경에서는 `application.yml`에 고정 비밀키를 두고 주입해야 한다.

---

### JwtAuthenticationFilter.java — 인증 처리 필터
`OncePerRequestFilter`를 상속 → 요청당 정확히 한 번만 실행

처리 순서:
1. `Authorization` 헤더에서 `Bearer ` 접두사 확인
2. 없거나 형식이 틀리면 → `JwtTokenNotFoundException` 던짐
3. 있으면 토큰에서 username 추출 → `UserService.loadUserByUsername()`으로 UserDetails 조회
4. `UsernamePasswordAuthenticationToken` 생성 후 `SecurityContextHolder`에 등록

---

### JwtExceptionFilter.java — JWT 예외 전용 필터
`JwtAuthenticationFilter` **앞에** 위치하여 그 안에서 발생하는 모든 `JwtException`을 catch

예외 발생 시 응답:
```json
{
  "status": "UNAUTHORIZED",
  "message": "Jwt token not found"
}
```
- HTTP 상태: **401 Unauthorized**
- Content-Type: `application/json`

---

### JwtTokenNotFoundException.java — 커스텀 예외
```java
public class JwtTokenNotFoundException extends JwtException
```
- `JwtException`을 상속해서 `JwtExceptionFilter`의 catch 블록에 자동으로 잡힘
- 메시지: `"Jwt token not found"`

---

### WebConfiguration.java — 필터 등록 순서
```java
.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
.addFilterBefore(jwtExceptionFilter, jwtAuthenticationFilter.getClass())
```
- `jwtAuthenticationFilter`는 Spring Security의 `UsernamePasswordAuthenticationFilter` **앞에** 추가
- `jwtExceptionFilter`는 `jwtAuthenticationFilter` **앞에** 추가 → 예외를 감쌀 수 있음

---

## 3. JWT 구조 복습

```
Header.Payload.Signature
```

| 파트 | 내용 |
|------|------|
| Header | 알고리즘 정보 (HS256) |
| Payload | subject(username), issuedAt, expiration 등 |
| Signature | Header + Payload를 SecretKey로 서명 |

- Base64URL 인코딩이라 **디코딩 가능** → Payload에 민감 정보 넣으면 안 됨
- 서버는 Signature 검증으로 위변조 여부만 확인

---

## 4. 이해도 체크 ✅

아래 질문에 스스로 답해보자.

**기초**
- [ ] JWT의 세 파트(Header / Payload / Signature)가 각각 무엇을 담고 있는가?
- [ ] Bearer 토큰을 요청에 담을 때 어떤 HTTP 헤더를 사용하는가?
- [ ] `OncePerRequestFilter`를 사용하는 이유가 무엇인가?

**구현**
- [ ] `JwtExceptionFilter`가 `JwtAuthenticationFilter` **앞에** 위치해야 하는 이유는?
- [ ] `SecurityContextHolder`에 Authentication을 등록하는 이유는 무엇인가?
- [ ] `SessionCreationPolicy.STATELESS`를 설정하면 어떤 의미인가?

**심화**
- [ ] 현재 SecretKey가 서버 재시작 시 매번 바뀐다. 어떻게 고쳐야 하는가?
- [ ] 토큰이 만료됐을 때와 서명이 잘못됐을 때 각각 어떤 예외가 발생하는가?
- [ ] Refresh Token은 왜 필요하며, 어떻게 구현할 수 있는가?

---

## 5. 용어 정리

| 용어 | 설명 |
|------|------|
| JWT (JSON Web Token) | JSON 기반의 자가 포함(self-contained) 인증 토큰 |
| jjwt | Java JWT 라이브러리 (io.jsonwebtoken) |
| Bearer | Authorization 헤더에 사용하는 토큰 타입 접두사 |
| SecurityContext | 현재 요청의 인증 정보를 담는 Spring Security의 저장소 |
| STATELESS | 서버가 세션을 생성/유지하지 않음 — JWT 방식의 핵심 |
| OncePerRequestFilter | 요청당 딱 한 번만 실행되는 필터 베이스 클래스 |
