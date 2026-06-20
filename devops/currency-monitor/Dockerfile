# 17 버전 Java 개발 도구(JDK) 환경을 가져와서 'build'라고 부릅니다.
FROM eclipse-temurin:17-jdk AS build
# 명령어를 실행할 기본 폴더를 설정
WORKDIR /workspace/app


# 빌드에 필요한 Gradle 설정 파일들과 소스 코드(src)를 컨테이너 안으로 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src src

# Gradle을 이용해 테스트를 클린하고, 최종 실행 파일(JAR)을 빌드
#RUN ./gradlew clean build
RUN ./gradlew clean build --no-daemon --stacktrace
# /workspace/app/build/libs/*-SNAPSHOT.jar

FROM eclipse-temurin:17-jdk

# 핵심! 'build' 스테이지에서 생성된 JAR 파일만 현재 이미지의 'app.jar'로 복사
# 이렇게 하면 소스 코드나 불필요한 빌드 도구는 최종 이미지에 포함되지 않아 용량이 아주 작아짐
COPY --from=build /workspace/app/build/libs/*-SNAPSHOT.jar app.jar

# 컨테이너가 켜질 때 실행할 쉘 스크립트(entrypoint.sh)를 복사하고 실행 권한을 줌
COPY script/entrypoint.sh entrypoint.sh
RUN chmod +x entrypoint.sh

# 컨테이너가 시작되면 자동으로 이 스크립트를 실행하도록 설정
ENTRYPOINT ["/entrypoint.sh"]