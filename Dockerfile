FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER 380702562@qq.com

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /app

WORKDIR /app

EXPOSE 8234

ADD ./target/*.jar ./app.jar

CMD java  -Djava.security.egd=file:/dev/./urandom -jar app.jar
