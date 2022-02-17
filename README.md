# Spring Boot 2 + Micrometer [![CI](https://github.com/daggerok/spring-boot-micrometer/actions/workflows/ci.yaml/badge.svg)](https://github.com/daggerok/spring-boot-micrometer/actions/workflows/ci.yaml)
This repo demonstrates spring-boot + micrometer + actuator integration basics

## Build, run and test

```bash
setjdk17 ; ./mvnw clean package spring-boot:start
curl -sSi 0:8080/api/failed/job-1
curl -sSi 0:8080/api/failed/job-2
curl -sSi 0:8080/api/succeeded/job-1
curl -sSi 0:8080/api/failed/job-2
curl -sSi 0:8080/api/failed/job-1
curl -sSi 0:8080/api/failed/job-2
curl -sSi "0:8080/actuator/metrics/job-1-success?tag=status:succeeded"
curl -sSi "0:8080/actuator/metrics/job-1-fail?tag=status:failed"
./mvnw spring-boot:stop
```

## RTFM

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.3/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.3/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.6.3/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.6.3/reference/htmlsingle/#production-ready)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.6.3/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
