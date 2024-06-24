# E-commerce-Study

## 프로젝트 설명

이 프로젝트는 Spring Boot를 사용하여 구현된 온라인 전자상거래 플랫폼입니다. 주요 기능은 다음과 같습니다:

### 회원기능
- **회원가입**: 새로운 회원을 등록합니다.
- **회원조회**: 등록된 회원 정보를 조회합니다.

### 상품기능
- **상품등록**: 새로운 상품을 등록합니다.
- **상품조회 및 수정 삭제**: 등록된 상품을 조회하고 수정하거나 삭제합니다.

### 주문기능
- **상품 주문**: 상품을 주문합니다.
- **주문내역조회**: 주문한 내역을 조회합니다.
- **주문취소**: 주문을 취소합니다.

### 기타 요구 사항
- **상품 제고 관리**: 상품의 재고를 관리합니다.
- **상품 종류**: 다양한 종류의 상품을 관리합니다.
- **상품 카테고리 구분**: 상품을 카테고리로 구분할 수 있습니다.
- **배송 정보 입력**: 상품 주문 시 배송 정보를 입력할 수 있습니다.

| Domain      | URL                                                                        | Http Method                 | description       | 접근 권한 |
|:------------|:---------------------------------------------------------------------------|:----------------------------|:------------------|:------|
| **Auth**    | auth/signup                                                                | `POST`                      | 사용자 회원가입          | -     |
|             | auth/login                                                                 | `POST`                      | 사용자/관리자 로그인       | -     |
<!-- 
|             | auth/signup/admin                                                          | `POST`                      | 관리자 회원가입          | -     |
| **Product** | /product/list/{categoryId}                                                 | `GET`                       | 카테고리 별 상품 목록 조회   | -     |
|             | /product/best-list                                                         | `GET`                       | 베스트 상품 목록 조회      | -     |
|             | /product/{productId}                                                       | `GET`                       | 상품 상세 조회          | -     |
|             | /admin/product                                                             | `POST`                      | 상품 등록             | ADMIN |
|             | /admin/product/{productId}                                                 | `GET` `PUT` `DELETE`        | 상품 조회, 수정, 삭제     | ADMIN |
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
