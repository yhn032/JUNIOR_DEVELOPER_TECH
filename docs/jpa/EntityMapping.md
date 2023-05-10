# 객체와 테이블 매핑 
엔티티와 테이블을 정확히 매핑하는 것이 가장 중요하다!

## 😀 @Entity
|속성|기능|기본 값|
|---|---|---|
|name|JPA에서 사용할 엔티티 이름 지정. 다른 패키지에 같은 이름의 엔티티 클래스가 있다면 변경하여 충동이 일어나지 않도록 해야 한다.|설정하지 않으면 클래스 이름을 그대로 사용한다.|

#### 주의사항
* 기본 생성자는 반드시 있어야 한다. (파라미터가 없는 public or protected)
  * JPA가 엔티티를 생성할 때, 기본 생성자를 사용하는데 보통 기본 생성자가 없다면 자바가 알아서 만들어준다. 
  * 하지만 파라미터가 있는 생성자가 정의된 경우에는 자동생성 되지 않으므로 이 경우 반드시 생성해주어야 한다. 
* final, enum, interface, inner 클래스에는 사용 불가 
* 저장하고자 하는 필드에 final 사용 불가

## 😎 @Table
|속성|기능|기본 값|
|---|---|---|
|name|매핑할 테이블 이름|생략시 엔티티 이름을 사용|
|catalog|catalog 기능이 있는 DB에서 catalog를 매핑||
|schema|schema기능이 있는 DB에서 schema를 매핑||
|uniqueConstraints(DDL)|DDL 생성 시에 유니크 제약 조건 생성. 단, 스키마 자동 생성 기능을 이용해 DDL을  만들때만 사용된다.||

## 🙄 데이터베이스 스키마 자동 생성 기능 
* 애플리케이션 실행 시점에 테이블이 자동으로 생성되므로 개발자가 직접 생성하는 수고를 덜어준다. 
* 하지만 이 기능으로 만든 DDL은 운영 환경에서 사용할 만큼 완벽하지 않다. 
  * 객체와 테이블의 매핑이 익숙하지 않다면 참고용으로만 사용하자. 

|옵션|설명|
|---|---|
|create|기존의 테이블을 삭제하고 매번 새로운 테이블을 생성한다. DROP + CREATE|
|create-drop|create 속성에 추가 버전으로 애플리케이션을 종료할 때 생성한 DDL을 제거한다. DROP + CREATE + DROP|
|update|테이블과 엔티티의 매핑정보를 비교하여 변경된 사항만 수정한다.|
|validate|테이블과 엔티티의 매핑정보를 비교하여 차이가 있다면 경고를 남기고 애플리케이션 종료. DDL 수정(X)|
|none|자동 생성 기능을 사용하지 않겠다고 명시|

### 이름 매핑 전략 변경
    단어와 단어를 구분할 때 관례상 JAVA는 카멜 케이스를, 테이블은 언더 스코어를 사용한다
    1. @Column.name 속성을 사용해 아래와 같이 명시적으로 이름을 지정할 수 있다. 
    @Column(name="role_type")
    String roleType
    
    2. 하이버네이트가 제공하는 클래스를 사용해 이름 매핑 전략을 변경하거나, 직접 구현 가능하다. 
    org.hibernate.cfg.ImprovedNamingStrategy를 사용하여 테이블 명이나 컬럼 명이 생략되면 자바의 카멜케이스 표기법을 언더스코어로 매핑한다.
    
## 😍 기본 키 매핑
1. 직접 할당
* 기본 키를 애플리케이션에 직접 할당한다. @Id 
  * 엔티티를 저장하기 전에 애플리케이션에서 기본 키를 직접 저장해야 한다. 그렇지 않으면 에러 발생 
2. 자동 생성
* 대리키 사용 방식 -> 데이터베이스마다 지원하는 방식이 달라 여러가지 존재. (Ex. Oracle : Sequence, MySql : Auto-Increment)
  * @Id @GeneratedValue에 아래 옵션을 적용하여 사용 가능 
* IDENTITY : 기본 키 생성을 데이터베이스에 위임한다.
```sql
CREATE TABLE BOARD (
  ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
  DATA VARCHAR(255)
)

INSERT INTO BOARD(DATA) VALUES('A');
INSERT INTO BOARD(DATA) VALUES('B');
```
```java
@Entity
public class Board {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    //...
}
```
  * MySQL, PostgreSQL, SQL Server, DB2에서 주로 사용한다. 
  * 직접 할당과는 다르게 엔티티를 저장할 때, 기본 키값을 할당하지 않아도 DB가 자동으로 생성해서 할당한다. 
  * 주의점
    * 엔티티가 영속상태에 되기 위해선 식별자 값이 반드시 필요하다. 
    * 하지만 IDENTITY 전략은 식별자를 제외하고 값을 저장하면 데이터베이스가 식별자를 할당하기 때문에 
    * 커밋이 아닌 em.persist가 실행되는 즉시 insertSQL이 DB에 전달된다.
    * 즉, 이 전략에서는 쓰기지연을 지원하지 않는다. 
* SEQUENCE : 데이터베이스 시퀀스를 사용해서 기본키 할당
```sql
CREATE TABLE BOARD (
  ID BIGINT NOT NULL PRIMARY KEY, 
  DATA VARCHAR(255)
)
CREATE SEQUENCE BOARD_SEQ START WITH 1 INCREMENT BY 1;
```
```java
@Entity
@SequenceGenerator(
        name = "BOARD_SEQ_GENERATOR",
        sequenceName = "BOARD_SEQ", //매핑할 DB 시퀀스 이름
        initialValue = 1, allocationSize = 1
)
public class Board {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE,
                        generator = "BOARD_SEQ_GENERATOR"
    )
    private Long id;

    //...
}
```
  * 오라클, PostgreSQL, DB2, H2 DB에서 주로 사용한다.  
  * ID 식별자 값은 BOARD_SEQ_GENERATOR 시퀀스 생성기가 할당한다.
  * 시퀀스 전략은 em.persist()를 호출할 때 먼저 데이터베이스 시퀀스를 사용해서 식별자를 조회한 후에 엔티티를 영속성 컨텍스트에 저장한다. 
  * 이와 달리 IDENTITY 전략은 엔티티를 먼저 저장한 후에 식별자를 조회해서 엔티티의 식별자에 할당한다.

@SequenceGenerator 속성 정리
|속성|기능|기본값|
|---|---|---|
|name|식별자 생성기 이름|필수|
|sequenceName|데이터베이스에 등록되어 있는 시퀀스 이름|hibernate_sequence|
|initialValue|DDL 생성시에만 사용. 시퀀스 DDL을 처음 생성할 때 처음 시작하는 수를 지정|1|
|allocationSize|시퀀스 한 번 호출에 증가하는 수(성능 최적화에 사용한다)|50| 
|catalog, schema|데이터베이스 catalog, schema 이름||

* TABLE : 키 생성 테이블 사용
  * 키 생성 전용 테이블을 하나 만들고 여기에 이름과 값으로 사용할 컬럼을 만들어 데이터베이스 시퀀스를 흉내내는 전략이다.
  * 모든 데이터베이스에 적용이 가능하다. 
```sql
CREATE TABLE MY_SEQUENCES (
  sequence_name varchar(255) not null, --시퀀스 이름
  next_val bigint,                     --시퀀스 
  primary key(sequence_name)
)
```

```java 
@Entity
@TableGenerator(
        name = "BOARD_SEQ_GENERATOR",
        table = "MY_SEQUENCES",
        pkColumnValue = "BOARD_SEQ",
        allocationSize = 1
)
public class Board {
    @Id @GeneratedValue(strategy = GenerationType.TABLE,
                        generator = "BOARD_SEQ_GENERATOR"
    )
    private Long id;

    //...
}
```
