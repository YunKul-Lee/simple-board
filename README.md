## simple-board
코틀린 학습을 위한 게시판

### 환경구성
* 스프링부트
* 코틀린
* Spring Web
* Spring JPA
* querydsl
* Swagger
* kotest

#### MYSQL 환경구성
```shell
# Start Mysql use Docker
docker run --name simple-board-mysql -e MYSQL_ROOT_PASSWORD=1234 -p 3306:3306 -d mysql:8.0.32
```

#### REDIS 환경구성
```shell
# Start REDIS use Docker
docker run --name simple-board-redis -p 6379:6379 -d redis:latest
```


#### 배포환경 구성
github actions를 이용한 aws 배포환경 구성

#### 기능
* 게시글 생성, 수정, 삭제
* 게시글 조회 & 목록조회
* 댓글 생성, 수정, 삭제
* 태그 생성, 삭제
* 좋아요
