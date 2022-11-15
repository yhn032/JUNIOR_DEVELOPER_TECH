## DDL(Data Definition Language)
    DB를 구성하고 있는 다양ㄹ한 객체를 정의/변경/제거하는데 사용한다. 
    물리적인 DB 객체의 """구조를 정의"""하는 데 사용한다.

### 주요 데이터 타입
    테이블에 특정 자료를 입력할 때, 그 자료를 받아들일 공간을 데이터 유형별로 나누는 기준이다.
    선언한 유형이 아닌 다른 종류의 데이터가 들어오려고 하면 에러가 발생한다. 
    
    
    
    
1. CHAR(L)
  * 고정 길이 문자열
  * 저장되는 값의 길이가 L보다 작을 경우 그 차이만큼 공백으로 채워진다. 
  * 저장되는 값의 길이가 L보다 큰 경우 저장되지 않는다. 

2. VARCHAR2(L)
  * 가변 길이 문자열
  * L값 만큼의 최대 길이를 가진다. 
  * L값 보다 작은 길이의 값을 저장하는 경우 그 길이만큼만 공간을 차지한다. 
  * 공백도 문자로 취급한다.
  
4. NUMBER(L, D)
  * 정수 및 실수를 저장한다. 
  * L값은 전체 자릿수, D값은 소수점 자릿수이다. 
  * 만약 NUMBER(12, 2)이면 999999999999.99까지 저장할 수 있다. 
  
5. DATE
  * 날짜와 시각 정보를 저장한다. 
  * 년월일시분초를 저장한다.


번외 - 문자열 비교하기 
CHAR는 고정길이 문자열, VARCHAR2는 가변길이 문자열이기 때문에 타입을 비교하는 경우 사용자가 예상하지 못한 결과가 나올수 있기 때문에
아래와 같은 주의사항을 숙지하자.

1. 양쪽 모두 CHAR타입인 경우 (<b>CHAR데이터 컬럼끼리 비교하는 경우 길이가 서로 달라도 공백만 다르다면 같다고 판단한다.</b>)
비교 대상이 모두 CHAR타입인 경우 CHAR의 길이가 서로 다르다면 데이터 비교 시 주의해야 한다. 
- 길이가 서로 다르다면 작은 쪽에 공백을 추가하여 길이를 같게 해줘야 한다. 
- 서로 다른 문자가 나올때까지 비교한다. 
- 달라진 첫 번째 값에 따라 크기를 결정한다. 첫 번째값의 아스키 코드값으로 비교한다. 
- 공백의 수만 다르다면 같은 값으로 결정한다. 

2. 비교 연산자 중 한쪽이 VARCHAR2인 경우 
- 서로 다른 문자가 나올때까지 비교한다. 
- 길이가 다르다면 짧은 것이 끝날 때까지만 비교한 후 길이가 긴 것이 크다고 판단한다. 
- 길이가 같고 다른 것이 없다면 같다고 판단한다. 
- VARCHAR2는 CHAR와 달리 공백 또한 문자로 판단한다. 

3. 상수값과의 비교 (비교대상의 데이터형을 따라간다.)
- 상수 쪽을 컬럼의 데이터형과 동일하게 바꾸고 비교한다. 
- 컬럼이 CHAR이면 CHAR 타입인 경우를 적용한다.(공백을 문자로 취급하지 않는다.)
- 컬럼이 VARCHAR2이면 VARCHAR2 타입인 경우를 적용한다. (공백을 문자로 취급한다.)

### 테이블 생성 예제 CREATE TABLE
```sql
  CREATE TABLE TEST.TB_RN_TMP					--도로명 임시
  (
      RN_CD VARCHAR2(12) NOT NULL,		--도로명 코드
	RN VARCHAR2(150) NOT NULL			--도로명
  );
```

### 테이블 생성 시 주의사항
  * 테이블 이름은 단수형을 사용할 것을 권고한다. 
  * 테이블 이름은 특정 사용자가 가지고 있는 테이블 내에서 다른 테이블과 중복되면 안된다. 
  * 한 테이블 내에서 동일한 컬럼명이 존재할 수 없다. 
  * 테이블 생성문의 끝은 ';'으로 끝나야 한다. 
  * 칼럼의 데이터형은 필수로 지정한다. 
  * 테이블 명과 칼럼명은 반드시 문자로 시작해야 한다. 
  * 테이블 이름에 사용하는 문자는 A-Za-z0-9,_, $, '#' 문자만 허용된다.


## 제약조건 Constraint  
  테이블에 입력되는 데이터가 사용자가 원하는 조건을 만족하는 데이터만 입력될 수 있도록 보장한다. 
  데이터의 무결성을 보장하기 위한 DBMS의 보편적인 방법으로, 특정 칼럼 하나 혹은 그 이상에 설정하는 제약
  

* 기본키
  * 테이블에 저장된 행들 중에서 특정 행을 고유하게 식별하기 위한 값 
  * 하나의 테이블에는 단 하나의 기본키만이 존재할 수 있다. 
  * 기본키 생성시 DBMS는 <b>Unique</b> index를 자동으로 생성한다. 
  * 기본키 컬럼에는 NULL(<b>NOT NULL</b>)을 입력할 수 없다.
  * 기본키는 UNIQUE 제약조건과 NOT NULL 제약조건을 만족해야만 한다.
* 고유키 
  * 테이블에 저장된 행들 중에서 특정 행을 고유하게 식별하기 위해 사용한다. 
  * 기본키와의 차이점은 고유키는 NULL을 입력할 수 있다. 
  * 즉, UNIQUE 제약조건만 만족하면 NULL을 입력할 수 있다. 
* NOT NULL
  * 필수 값 컬럼. NULL값을 제외한 어떤 값이라도 들어와야 한다. 
* CHECK
  * 입력할 수 있는 값의 종류 혹은 범위를 제한한다. 
* 외래키 
  * 부모 테이블 또는 참조 테이블의 기본키를 외래키로 지정하는 경우 생성한다. 
  * 참조 무결성 제약조건이라고도 한다. 
* 디폴트 값 
  * 해당 컬럼에 아무런 값도 입력하지 않았을 때, 지정한 디폴트 값으로 데이터가 입력된다.


NULL(ASCII 00) : 아직 정의되지 않는 미지의 값이너가, 현재 데이터를 입력하지 못하는 경우를 의미한다.
공백(ASCII 32)이나 숫자0 (ASCII 48)과 전혀 다른 값이고, 데이터가 없는 공집합과도 구분해야 한다.


### 테이블 생성 및 제약조건 생성 실습 1. 부모 테이블
```sql
CREATE TABLE TEST.TB_SUBWAY_STATN_TMP	--지하철역 임시
(
	SUBWAY_STATN_NO	CHAR(6) NOT NULL,	--지하철역 번호
	LN_NM	VARCHAR2(50) NOT NULL,	--노선명
	STATN_NM	VARCHAR2(50) NOT NULL,	--역명
	CONSTRAINT PK_TB_SUBWAY_STATN_TMP PRIMARY KEY(SUBWAY_STATN_NO)	--지하철역 번호 PK
);
```

### 테이블 생성 및 제약조건 생성 실습 1. 자식 테이블
```sql
CREATE TABLE TEST.TB_SUBWAY_STATN_TK_GFF_TMP	--지하철역 승하차 임시
(
	SUBWAY_STATN_NO	CHAR(6) NOT NULL,	--지하철역 번호
	STD_YM	CHAR(6) NOT NULL,	--기준년월
	BEGIN_TIME	CHAR(6) NOT NULL,	--시작시간
	END_TIME	CHAR(6) NOT NULL,	--종료시간
	TK_GFF_SE_CD	VARCHAR(6) NOT NULL, 	--승하차구분코드
	TK_GFF_CNT	NUMBER(15) NOT NULL,	--승하차 횟수
	CONSTRAINT PK_TB_SUBWAY_STATN_TK_GFF_TMP PRIMARY KEY(SUBWAY_STATN_NO, STD_YM, BEGIN_TIME, END_TIME, TK_GFF_SE_CD)	--지하철역 번호 PK
);
```

### ALTER 
    컬럼 및 테이블의 제약조건을 추가/수정/제거하는 데 이용한다. 

```sql
--실습 1. 컬럼 추가하기
ALTER TABLE TB_SUBWAY_STATN_TMP ADD (OPEN_YN CHAR(1)); --운영여부 컬럼 추가 

--실습 2. 컬럼 삭제하기 
ALTER TABLE TB_SUBWAY_STATN_TMP DROP COLUMN OPEN_YN;

--실습 3. 데이터형 및 제약조건 변경
--제약조건을 MODIFY하여 NOT NULL 조건을 추가하는 경우 이미 데이터가 있는 테이블이라면 에러가 발생한다.
--하지만 NOVALIDATE옵션을 주면 이미 NULL값이 있어도 NOT NULL제약 조건을 주고, 이후에 들어오는 데이터에만 적용할 수 있도록 한다. 
ALTER TABLE TB_SUBWAY_STATN_TMP ADD(OPEN_YN CHAR(1) NULL); --NULL 허용
ALTER TABLE TB_SUBWAY_STATN_TMP MODIFY(OPEN_YN NUMBER(1) DEFAULT 0 NOT NULL NOVALIDATE);
ALTER TABLE TB_SUBWAY_STATN_TMP RENAME OPEN_YN TO OPERATION_YN; --컬럼명 변경
ALTER TABLE TB_SUBWAY_STATN_TK_GFF_TMP DROP FK_TB_SUBWAY_STATN_TK_GFF_TMP1; --외래키 삭제 

RENAME TB_SUBWAY_STATN_TK_GFF_TMP TO TB_SUBWAY_STATN_TK_GFF_TMP_2; --테이블 이름 변경


--실습 4. TRUNCATE
--테이블 객체는 그대로 두고 테이블 내부의 데이터만 영구제거한다. 단, TRUNCATE로 삭제한 데이터는 ROLLBACK으로 복구가 불가능하다.
TRUNCATE TABLE TB_SUBWAY_STATN_TK_GFF_TMP_2;

--테이블 객체를 삭제하는 경우, 부모/자식 관계가 있다면, 자식 테이블을 먼저 제거한 후에 부모 테이블을 삭제해야 한다.
```
### 외래키를  생성하여 두 테이블의 부모/자식 관계를 설정한다. - 참조무결성 제약 조건
```sql
ALTER TABLE TEST.TB_SUBWAY_STATN_TK_GFF_TMP --지하철역 승하차 임시 테이블에 
ADD CONSTRAINT FK_TB_SUBWAY_STATN_TK_GFF_TMP1 -- 참조무결성 제약 조건을 생성
FOREIGN KEY (SUBWAY_STATN_NO)	-- 지하철역 승하차 임시 테이블의 지하철역 번호 컬럼은 
REFERENCES TEST.TB_SUBWAY_STATN_TMP(SUBWAY_STATN_NO) - 지하철역 임시 테이블의 지하철역번호를 참조한다.
-- 지하철역 승하차 임시 테이블에 지하철번호를 입력하는 경우에는 지하철역 임시테이블에 존재하지 데이터만 입력가능하다.
```
