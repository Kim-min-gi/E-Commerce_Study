# E-commerce-Study

## [프로젝트 설명]

이 프로젝트는 Spring Boot를 사용하여 E-commerce를 만들어보는 프로젝트입니다.

<br/>
<br/>

## [사용한 기술 스택 및 라이브러리]
<div align="center"> 
<img height="30" src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat-square&logo=Spring%20Boot&logoColor=white"/>
<img height="30" src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=flat-square&logo=Spring%20Security&logoColor=white"/>
<img height="30" src="https://img.shields.io/badge/Java-007396?style=flat-square&logo=java&logoColor=white"/>
<img height="30" src="https://img.shields.io/badge/JWT-000000?style=flat-square&logo=JSON%20Web%20Tokens&logoColor=white"/>
<img height="30" src="https://img.shields.io/badge/Hibernate-59666C?style=flat-square&logo=Hibernate&logoColor=white"/>
<img height="30" src="https://img.shields.io/badge/JPA-007396?style=flat-square&logo=JPA&logoColor=white"/>
<br/>
<img height="30" src="https://img.shields.io/badge/Git-F05032?style=flat-square&logo=git&logoColor=white"/>
<img height="30" src="https://img.shields.io/badge/Postman-FF6C37?style=flat-square&logo=Postman&logoColor=white"/>
<img height="30" src="https://img.shields.io/badge/GitHub-black?style=flat-square&logo=GitHub&logoColor=white"/>
</div>

<br/>
<br/>

## [ERD]
[ERD Cloud](https://www.erdcloud.com/d/ydWfKvowAFNPdALg7)

![image](https://github.com/user-attachments/assets/5a70f952-ee4b-4cea-88a4-76a69ae775c6)





<br/>
<br/>

## [프로그램 주요 기능]

### 회원기능
- **회원가입**
  - 사용자는 이메일, 비밀번호, 이름을 이용해 회원가입할 수 있다. <!-- 휴대폰,주소 추가 -->
- **관리자가입**
  - 관리자는 이메일, 비밀번호, 이름, 관리자 인증코드를 이용해 회원가입할 수 있다.
- **회원조회**
  - 관리자는 회원들을 조회할 수 있다.
<!-- - **회원수정** -->
<!-- - 사용자는 휴대폰번호, 집주소를 언제든지 변경 가능하다. -->
<br/>

### 로그인
- **로그인**
  - 회원가입 시 사용한 이메일과 비밀번호를 이용해 로그인할 수 있다.
    
<br/>

### 카테고리
- **카테고리 등록**
  - 관리자는 카테고리를 등록할 수 있다.
- **카테고리 수징 및 삭제**
  - 관리자는 카테고리를 수정 및 삭제할 수 있다.
<br/>

### 상품기능
- **상품등록**
  - 관리자는 상품을 등록할 수 있다.
- **상품조회**
  - 모든 사용자는 상품을 카테고리별로 조회할 수 있다. <!-- 상세정보 포함 -->
- **상품수정 및 삭제**
  - 관리자는 상품을 수정 및 삭제할 수 있다.
 <!-- - 주문량이 많은 상품을 보여줄 수 있다. -->
<br/>

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
<br/>


## [API] (개발할때마다 추가될 예정)

| Domain       | URL                                                                          | Http Method | description   | 접근 권한 |
|:-------------|:-----------------------------------------------------------------------------|:------------|:--------------|:------|
| **Auth**     | auth/signup                                                                  | `POST`      | 사용자 회원가입      | -     |
|              | auth/signup/admin                                                            | `POST`      | 관리자 회원가입      | -     |
|              | auth/login                                                                   | `POST`      | 사용자/관리자 로그인   | -     |
| **MEMBER**   | admin/member                                                                 | `GET`       | 사용자 전체 조회     | ADMIN |
|              | admin/member/{id}                                                            | `GET`       | 사용자 조회        | ADMIN |
| **Category** | admin/category                                                               | `POST`      | 물품 카테고리 생성    | ADMIN |
|              | admin/category/{id}                                                          | `PATCH` `DELETE`    | 물품 카테고리 수정,삭제    | ADMIN |
| **Product**  | /admin/product                                                               | `POST`              | 상품 등록                 | ADMIN |
|              | /admin/products                                                              | `GET`              | 상품 리스트 조회 (관리용)   | ADMIN |
|              | /admin/product/{productId}                                                   | `GET` `PATCH` `DELETE`  | 상품 조회, 수정, 삭제  | ADMIN |
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
|             | /admin/order/cooking-time/{orderId}                                        | `PATCH`                     | 예상 조리 시간 선택       | ADMIN |
|             | /admin/order/receipt-status/{orderId}                                      | `PATCH`                     | 주문 수락 또는 거절       | ADMIN |
| **Cart**    | /auth/cart                                                                 | `GET`                       | 장바구니 상품 목록 조회     | USER  |
|             | /auth/cart/save                                                            | `POST`                      | 장바구니 상품 추가        | USER  |
|             | /auth/cart                                                                 | `POST` `DELETE`             | 장바구니 상품 수량 변경, 삭제 | USER  |
|             | /auth/pay                                                                  | `PUT`                       | 장바구니 전체 결제        | USER  |
| **Review**  | /auth/review                                                               | `POST`                      | 리뷰 등록             | USER  |
-->
<br/>
