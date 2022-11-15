# DML
  Data Manipulation Language은 테이블의 데이터를 입력/수정/삭제/조회하는 역할을 한다. 
  DB를 다루면서 가장 많이 사용하는 SQL문이다. 입력, 수정, 삭제는 SQL을 실행후에 영구적으로 저장(COMMIT)하거나 취소(ROLLBACK)할 수 있다.
  
### 실습을 위한 TEST TABLE 생성.
```sql
CREATE TABLE TEST.TB_SUBWAY_STATN_TMP	--지하철역 임시
(
	SUBWAY_STATN_NO	CHAR(6) NOT NULL,	--지하철역 번호
	LN_NM	VARCHAR2(50) NOT NULL,	--노선명
	STATN_NM	VARCHAR2(50) NOT NULL,	--역명
	CONSTRAINT PK_TB_SUBWAY_STATN_TMP PRIMARY KEY(SUBWAY_STATN_NO)	--지하철역 번호 PK
);
```

### INSERT문 - 테이블에 데이터를 신규로 입력하는 경우 사용
```sql
INSERT INTO TEST.TB_SUBWAY_STATN_TMP VALUES('000032', '2호선', '강남');
COMMIT; --DB에 최종적으로 적용

--삽입이 잘 되었는지 확인하기 
SELECT * FROM TEST.TB_SUBWAY_STATN_TMP;
```

### UPDATE문 - 테이블 내 행의 컬럼값을 수정한다. 한 번의 UPDATE문으로 여러 개의 행에 대한 여러개의 컬럼 수정가능
```sql
UPDATE TEST.TB_SUBWAY_STATN_TMP A 
	SET A.LN_NM = '녹색선', A.STATN_NM ='강남스타일'
	WHERE A.SUBWAY_STATN_NO = '000032';

COMMIT;
```

### DELETE문 - 테이블 내의 행을 삭제할 수 있다. ROLLBACK가능 BUT, TRUNCATE로 삭제한 데이터는 ROLLBACK 불가 
```sql
DELETE FROM TEST.TB_SUBWAY_STATN_TMP A
WHERE A.SUBWAY_STATN_NO = '000032';

COMMIT;

--데이터 삭제가 완료된 후, 테이블의 남은 데이터가 0건으로 하나도 없는 경우를 공집합이라고 한다.
```

### SELECT문 - 테이블에서 데이터를 조회하는데 사용한다.
    SELECT절 맨 앞에 DISTINCT를 사용하면 중복된 행이 제거되어 유일한 값을 가진 행만을 출력한다.
    출력하는 컬럼수가 N개 이고 각 컬럼의 유일값이 K개 라면, 출력되는 결과는 N^K 행이다. 
    여러 개의 컬럼을 출력하는 경우 합성 연산자'||'를 사용해서 연결해 출력 가능하다. 
    오라클 DB를 설치하면 기본적으로 DUAL테이블이 존재한다. 이 테이블에는 단 한건의 더미 데이터가 저장되어 있고, 컬럼 또한 하나로 'X'값이 저장되어 있다. 
    DUAL테이블을 이용하여 연산을 수행한다.
