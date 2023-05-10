# 필드와 컬럼 매핑 : 레퍼런스 

|분류|매핑 어노테이션|설명|
|---|---|---|
|필드와 컬럼 매핑|@Column|컬럼을 매핑한다.|
||@Column|컬럼을 매핑한다.|
||@Enumerated|자바의 enum 타입을 매핑한다.|
||@Temporal|날짜 타입을 매핑한다.|
||@Lob|BLOB, CLOB 타입을 매핑한다.|
||@Transient|특정 필드를 데이터베이스에 매핑하지 않는다.|
|기타|@Access|JPA가 엔티티에 접근하는 방식을 지정한다.|

## 😁 @Column

|속성|기능|기본값|
|---|---|---|
|name|필드와 매핑할 테이블 컬럼의 이름|객체의 필드 이름|
|insertable(거의 사용하지 않음)|엔티티 저장 시 이 필드도 같이 저장한다. FALSE로 설정하면 이 필드는 데이터베이스에 저장하지 않는다. FALSE는 읽기전용인 경우에 사용한다.|true|
|updatable(거의 사용하지 않음)|엔티티 수정 시 이 필드도 같이 수정한다. FALSE로 설정하면 이 필드는 데이터베이스에 수정하지 않는다. FALSE는 읽기전용인 경우에 사용한다.|true|
|table(거의 사용하지 않음)|하나의 엔티티를 두 개 이상의 테이블에 매핑할 때 사용한다. 지정한 필드를 다른 테이블에 매핑할 수 있다.|현재 클래스가 매핑된 테이블|
|nullable(DDL)|null값의 허용 여부를 설정한다. false로 설정하면 DDL 생성 시에 not null 제약 조건이 붙는다.|true|
|unique(DDL)|@Table의 uniqueConstraints와 같지만 한 컬럼에 간단히 유니크 제약 조건을 걸 때 사용한다. 만약 두 컬럼 이상을 사용해서 유니크 제약 조건을 사용하려면 클래스 레벨에서 @Table.uniqueConstraints를 사용해야 한다.||
|columnDefinition(DDL)|데이터베이스 컬럼 정보를 직접 줄 수 있다.|필드의 자바 타입과 방언 정보를 사용해서 적절한 컬럼 타입을 설정한다.|
|length(DDL)|문자 길이 제약 조건, String 타입에만 사용 한다.|255|
|precision, scale(DDL)|BigDecimal 타입에서 사용한다.(BigInteger도 사용가능) precision은 소수점을 포함한 전체 자릿수를, scale은 소수의 자릿수다. 참고로 double, float 타입에는 적용되지 않는다. 아주 큰 숫자나 정밀한 소수를 다루어야 할 때만 사용한다. |precision=19, scale2|

