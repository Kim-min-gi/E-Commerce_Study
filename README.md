# E-commerce-Study

### 목차

> 1. [프로젝트 설명](#프로젝트-설명)
> 2. [시스템 아키텍처](#시스템-아키텍처)
> 3. [기술 스택](#기술-스택)
> 4. [ERD](#erd)
> 5. [주요 기능](#주요-기능)
>   + 공통 : [회원가입, 로그인](#회원기능) | [상품 목록 및 상품 상세 조회](#상품기능)
>   + 사용자 : [장바구니 관리](#장바구니) | [주문 관리](#주문기능) | [리뷰 관리](#리뷰관리)
>   + 관리자 : [상품 관리](#상품기능) | [카테고리 관리](#카테고리) | [회원 관리](#회원관리)
> 6. [API 명세](#api-명세)
> 6. [트러블슈팅](#트러블-슈팅-및-성능-개선)
<br/>
<br/>


## [프로젝트 설명]

> 이 프로젝트는 Spring Boot를 사용하여 E-commerce를 만들어보는 프로젝트입니다. \
> 공부목적이 강한 프로젝트이므로 만들어보고 싶은 기능, 적용해보고 개발 방식등을 적용해보는 프로젝트입니다.
> 
> 현재 SpringBoot와 Spring Data JPA를 사용해 기본적인 REST API를 구현하고,\
> Spring Scurity + JWT를 사용하여 로그인을 구현했으며, \
> Redis를 사용하여 access/Refresh 토큰을 통해 로그아웃을 구현했습니다. \
> Spring Rest Docs를 통해 api 문서화 작업을 했으며, \
> Github Actions, Docker, AWS Ec2 등을 이용해 서버를 배포했습니다. 
> 
> 더 공부해서 적용해볼 기능 및 기술들에는
> Spring Batch를 통해 하루/일주일/한달 매출을 계산기능과 \
> 대용량 데이터들의 대한 처리 방법들에 대한 기술을 생각하고 있습니다.
<!-- > 카테고리별 판매순위 Top 항목의 상품들을 표시하는 기능도 구현해 볼 생각입니다. -->

<br/>
<br/>


## [시스템 아키텍처]


![아키텍처 수정본 (1)](https://github.com/user-attachments/assets/3e8e515a-2594-48d4-91cd-2174a6bd9a8f)


<br/>
<br/>

## [기술 스택]
| **분류**             | **기술명**                                                                                                      |
|----------------------|-----------------------------------------------------------------------------------------------------------------|
| **Language**         | <img height="30" src="https://img.shields.io/badge/Java%2017-007396?style=flat-square&logo=java&logoColor=white"/>  |
| **Build Tool**       | <img height="30" src="https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=Gradle&logoColor=white"/> |
| **Framework**        | <img height="30" src="https://img.shields.io/badge/Spring%20Boot%203.2.5-6DB33F?style=flat-square&logo=Spring-Boot&logoColor=white"/> <br/> <img height="30" src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=flat-square&logo=Spring-Security&logoColor=white"/> <br/> <img height="30" src="https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=flat-square&logo=Spring&logoColor=white"/> |
| **Security**         | <img height="30" src="https://img.shields.io/badge/JWT-000000?style=flat-square&logo=JSON%20Web%20Tokens&logoColor=white"/> |
| **Database**         | <img height="30" src="https://img.shields.io/badge/H2-59666C?style=flat-square&logo=H2&logoColor=white"/> <br/> <img height="30" src="https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=Redis&logoColor=white"/> |
| **Storage**          | <img height="25" src="https://img.shields.io/badge/Amazon%20S3-2E8B57?style=flat-square&logo=Amazon%20S3&logoColor=white"/> |
| **Docs & Test**      | <img height="30" src="https://img.shields.io/badge/Spring%20REST%20Docs-6DB33F?style=flat-square&logo=Spring&logoColor=white"/> <br/> <img height="30" src="https://img.shields.io/badge/Postman-FF6C37?style=flat-square&logo=Postman&logoColor=white"/> <br/> <img height="30" src="https://img.shields.io/badge/JUnit5-25A162?style=flat-square&logo=JUnit5&logoColor=white"/> |
| **DevOps / Deploy**  | <img height="30" src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=Docker&logoColor=white"/> <br/> <img height="30" src="https://img.shields.io/badge/EC2-FF9900?style=flat-square&logo=amazonaws&logoColor=white"/> <br/> <img height="30" src="https://img.shields.io/badge/GitHub%20Actions-2088FF?style=flat-square&logo=GitHub-Actions&logoColor=white"/> |
| **Version Control**  | <img height="30" src="https://img.shields.io/badge/Git-F05032?style=flat-square&logo=git&logoColor=white"/> |


[//]: # (<img height="30" src="https://img.shields.io/badge/Swagger-85EA2D?style=flat-square&logo=Swagger&logoColor=black"/> )
</div>

<br/>
<br/>

## [ERD]
[ERD Cloud](https://www.erdcloud.com/d/ydWfKvowAFNPdALg7)

<img width="1015" alt="image" src="https://github.com/user-attachments/assets/c79dbcf6-01ca-4cac-a711-959941024010">




<br/>
<br/>

# 주요 기능

## 공통 기능

  ### 회원기능
  - **회원가입**
    - 사용자는 이메일, 비밀번호, 이름을 이용해 회원가입할 수 있다. <!-- 휴대폰,주소 추가 -->
  - **관리자가입**
    - 관리자는 이메일, 비밀번호, 이름, 관리자 인증코드를 이용해 회원가입할 수 있다.
  - **회원탈퇴**
    - 사용자는 회원탈퇴를 할 수 있다.
  <!-- - **회원수정** -->
  <!-- - 사용자는 휴대폰번호, 집주소를 언제든지 변경 가능하다. -->
  <br/>
  
  ### 로그인
  - **로그인**
    - 회원가입 시 사용한 이메일과 비밀번호를 이용해 로그인할 수 있다.
  - **로그아웃**
    - 로그인된 사용자를 로그아웃 시킨다.
  <br/>

  ### 상품기능
  - **상품조회**
    - 모든 사용자는 상품을 카테고리별로 조회할 수 있다. <!-- 상세정보 포함 -->

  <br/>

## 사용자 기능

### 장바구니
- **장바구니 상품추가**
  - 장바구니에 상품을 추가한다.
- **장바구니 상품수정 및 삭제**
  - 상품의 수량을 조절하거나 상품을 삭제할 수 있다.
    
<br/>

### 주문기능
- **상품 주문**
  - 사용자는 여러 상품을 주문할 수 있다.
  - 상품 주문 시 배송 정보를 입력할 수 있습니다.
- **주문내역조회**
  - 3개월간의 주문 내역이 조회되고, 기간을 설정할 시 설정한 기간동안의 내역이 조회된다.
- **주문취소**
  - 배송 시작전 사용자는 주문을 취소할 수 있다.
<br/>

### 리뷰관리
- **리뷰 작성**
  - 사용자는 상품에 대한 리뷰를 작성할 수 있다.
  - 사용자는 상품에 대한 사진을 올릴 수 있다.
- **리뷰 조회**
  - 사용자는 자신이 작성 리뷰들을 볼 수 있다.
  - 사용자는 상품에 대한 리뷰들을 볼 수 있다.
- **리뷰 수정 및 삭제**
  - 사용자는 리뷰를 수정 및 삭제를 할 수 있다.


<br/>

## 관리자 기능

### 상품기능
- **상품등록**
  - 관리자는 상품을 등록할 수 있다.

- **상품수정 및 삭제**
  - 관리자는 상품을 수정 및 삭제할 수 있다.
 <!-- - 주문량이 많은 상품을 보여줄 수 있다. -->
<br/>

### 카테고리
- **카테고리 등록**
  - 관리자는 카테고리를 등록할 수 있다.
- **카테고리 수징 및 삭제**
  - 관리자는 카테고리를 수정 및 삭제할 수 있다.
<br/>

### 회원관리
 - **회원조회**
    - 관리자는 회원들을 조회할 수 있다.
 - **추가예정

 


<br/>
<br/>


## [API 명세]

| Domain       | URL                          | Http Method                   | description                 | 접근 권한 |
|:-------------|:-----------------------------|:------------------------------|:----------------------------|:------|
| **Auth**     | /auth/signup                 | `POST`                        | 사용자 회원가입                    | -     |
|              | /auth/signup/admin           | `POST`                        | 관리자 회원가입                    | -     |
|              | /auth/resign                 | `POST`                        | 회원 탈퇴                       | USER  |
|              | /auth/login                  | `POST`                        | 사용자/관리자 로그인                 | -     |
|              | /auth/logout                 | `POST`                        | 사용자/관리자 로그아웃                | USER  |
|              | /auth/reissue                | `POST`                        | JWT(AccessToken)토큰 재발급      | USER  |
| **Member**   | /admin/member                | `GET`                         | 사용자 전체 조회                   | ADMIN |
|              | /admin/member/{id}           | `GET`                         | 사용자 조회                      | ADMIN |
| **Category** | /admin/category              | `POST`                        | 물품 카테고리 생성                  | ADMIN |
|              | /admin/category/{id}         | `PATCH` `DELETE`              | 물품 카테고리 수정,삭제               | ADMIN |
|              | /categories                  | `GET`                         | 물품 카테고리 리스트 조회              | -     |
|              | /category/{categoryId}       | `GET`                         | 물품 카테고리 단건 조회               | -     |
| **Product**  | /admin/product               | `POST`                        | 상품 등록                       | ADMIN |
|              | /admin/product/{productId}   | `GET`                         | 상품 상세 조회                    | ADMIN |
|              | /product/products            | `GET`                         | 상품 리스트 조회                   | USER  |
|              | /product/{productId}         | `GET`                         | 상품 단건 조회                    | ADMIN |
|              | /product/list/{categoryId}   | `GET`                         | 카테고리별 상품 리스트                | ADMIN |
| **Cart**     | /cart                        | `GET` `PATCH` `POST` `DELETE` | 장바구니 상품 목록 조회, 수량 변경, 추가, 삭제 | USER  |
| **Order**    | /orders                      | `GET`                         | 구매 내역 리스트 조회                | USER  |
|              | /order/{orderId}             | `GET`                         | 주문 단건 조회                    | USER  |
|              | /ordersDate                  | `GET`                         | 주문 날짜로 조회                   | USER  |
|              | /orderCreate                 | `POST`                        | 주문 생성                       | USER  |
|              | /orderCancel/{orderId}       | `PATCH`                       | 주문 취소                       | USER  |
|              | /orderModify                 | `PATCH`                       | 주문 상태 변경                    | ADMIN |
| **Review**   | /reviews/product/{productId} | `GET`                         | 상품 리뷰 조회                    | USER  |
|              | /reviews/member/{memberId}   | `GET`                         | 사용자 리뷰 조회                   | USER |
|              | /review                      | `POST`                        | 리뷰 작성                       | USER |
|              | /reviews/{reviewId}          | `PATCH`,`DELETE`              | 리뷰 삭제,리뷰 수정                 | USER |
| **Payment**  | /payment/ready               | `GET`                         | 카카오페이 테스트 결제 신청             | USER  |
|              | /payment/success             | `GET`                         | 카카오페이 테스트 결제 승인             | USER  |
|              | /payment/cancel              | `GET`                         | 카카오페이 테스트 결제 취소             | USER  |

<!--
| **Product** | /product/list/{categoryId}                                                 | `GET`                       | 카테고리 별 상품 목록 조회   | -     |
|             | /product/best-list                                                         | `GET`                       | 베스트 상품 목록 조회      | -     |
|             | /product/{productId}                                                       | `GET`                       | 상품 상세 조회          | -     |
|             | /admin/product?productId={productId}&soldout={soldOutStatus}               | `PUT`                       | 상품 품절 여부 수정       | ADMIN |
|             | /admin/option/{optionId}                                                   | `GET` `PUT` `POST` `DELETE` | 상품 옵션 CRUD        | ADMIN |
|             | /admin/category/{categoryId}                                               | `GET` `PUT` `POST` `DELETE` | 상품 카테고리 CRUD      | ADMIN |
|             | /admin/option-category                                                     | `GET` `PUT` `POST` `DELETE` | 옵션 카테고리 CRUD      | ADMIN |
| **Order**   | /auth/pay/list?viewType={viewType}&startDate={startDate}&endDate={endDate} | `GET`                       | 구매 내역 조회          | USER  |
|             | /auth/order/elapsed-time/{orderId}                                         | `GET`                       | 주문 경과 시간 조회       | USER  |
|             | /auth/order/cancel/{orderId}                                               | `PATCH`                     | 주문 취소             | USER  |
|             | /admin/order/status/{orderId}                                              | `PATCH`                     | 주문 상태 변경          | ADMIN |
|             | /admin/order/receipt-status/{orderId}                                      | `PATCH`                     | 주문 수락 또는 거절       | ADMIN |
| **Cart**    | /auth/cart                                                                 | `GET`                       | 장바구니 상품 목록 조회     | USER  |
|             | /auth/cart/save                                                            | `POST`                      | 장바구니 상품 추가        | USER  |
|             | /auth/cart                                                                 | `POST` `DELETE`             | 장바구니 상품 수량 변경, 삭제 | USER  |
|             | /auth/pay                                                                  | `PUT`                       | 장바구니 전체 결제        | USER  |
| **Review**  | /auth/review                                                               | `POST`                      | 리뷰 등록             | USER  |
-->
<br/>


## [트러블 슈팅 및 성능 개선]

- Spring Security 테스트 환경 권한 문제
    → `@WithMockUser` 한계로 보안 설정 테스트 실패 test시 403에러 발생. <br/>
해결 : `CustomMockMember` 어노테이션 정의로 역할/권한 주입 보안 테스트 자동화 성공

- JWT 구조 개선 (Access + Refresh 토큰 분리 / Redis 적용) 
    → 수동 빌드/배포로 인한 시간 낭비 및 실수 발생 <br/>
해결 : Redis 기반 Refresh Token 저장, Access Token 자동 재발급 구현 로그아웃 시 토큰 무효화 처리로 보안 강화

- 결제 수단별 처리 분기 로직 복잡 
    → if-else 구조로 결제 수단(KakaoPay, Card 등)을 분기하던 중 조건문이 증가하면서 유지보수가 어려워짐. <br/>
해결 : **전략 패턴(Strategy Pattern)** 적용, `PaymentStrategy` 인터페이스와 각 결제 수단별 구현체로 분리할 수 있도록 구현

- CI/CD 자동화 구축 (GitHub Actions + DockerHub + EC2) 
    → 수동 빌드/배포로 인한 시간 낭비 및 실수 발생 <br/>
해결 : GitHub Actions로 빌드 & 푸시 자동화 구성 
수작업 대비 배포 시간 **90% 이상 단축** (약 10분 → 1분 이내)

- 테스트 환경에서 프로퍼티 로딩 실패 해결 5
    → 로컬로 애플리케이션을 돌릴 때와 테스트를 돌릴 때 그리고 실제 운영을 하기위한 배포를 할 때, 
    각기 다른 설정을 주고 싶을 수가 있다. <br/>
해결 : **@ActiveProfiles 를 사용해서 yml를 적용**

<br/>
<br/>
