FROM eclipse-temurin:17-jdk-alpine

RUN apk add --no-cache netcat-openbsd

# application.properties 파일을 위한 디렉토리 생성
RUN mkdir -p /app/resources

# application.properties 파일을 스프링 부트 컨테이너(이미지) 내부의 프로젝트 경로로 복사
COPY src/main/resources/application.properties /home/ubuntu/instagram-server/src/main/resources/application.properties

# 프로젝트의 JAR 파일을 컨테이너 내부로 복사
# build/libs 디렉토리에서 SNAPSHOT.jar로 끝나는 모든 파일을 project.jar라는 이름으로 복사
# *SNAPSHOT.jar 패턴은 Gradle/Maven 빌드 시 생성되는 JAR 파일을 의미
COPY ./build/libs/*SNAPSHOT.jar project.jar

# 컨테이너가 8080 포트를 사용한다는 것을 문서화
# EXPOSE는 단순히 문서화 목적이며, 실제 포트 매핑은 docker run -p 옵션으로 설정해야 함
EXPOSE 8080

# 컨테이너가 시작될 때 실행할 명령어를 정의.
# java -jar project.jar 명령어를 실행하여 스프링 부트 애플리케이션을 시작
# ENTRYPOINT는 컨테이너가 실행될 때 변경할 수 없는 명령어를 지정할 때 사용
#ENTRYPOINT ["java", "-jar", "project.jar"]

# Java 실행 시 properties 파일 위치 지정
ENTRYPOINT ["java", "-jar", "project.jar", "--spring.config.location=file:/home/ubuntu/instagram-server/src/main/resources/application.properties"]