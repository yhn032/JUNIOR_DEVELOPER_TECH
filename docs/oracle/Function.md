# Function

### 1. 단일행 함수
    오라클 DBMS 엔진 내부에 구현된 내장 함수 중 입력값에 대해 단 하나의 출력값을 
    리턴하는 함수를 단일행 함수라고 한다.
    
    * SELECT, WHERE, ORDER BY절에 사용이 가능하다. 
    * 각 행에 대해 개별적으로 적용하여 데이터 값을 조작하고, 각 행에 대한 조작 결과를 리턴한다.
    * 여러 인자를 입력해도 단 하나의 결과만 리턴한다. 
    * 함수의 인자로 상수, 변수, 표현식이 사용 가능하고, 하나의 인수를 가지는 경우도 있지만 여러 개의 인수를 가질 수도 있다. 
    * 함수의 인자로 함수 호출 자체를 사용할 수 있다.(함수 안에서 함수를 호출한다. )

### 2. 단일행 함수의 종류 
1. 문자형 함수 : 문자를 입력하면 문자나 숫자 값을 반환한다.
    * LOWER, UPPER, SUBSTR, LENGTH, LTRIM, RTRIM, TRIM, ASCII 
```sql
SELECT 
	LOWER('SQL Developer'),	--[sql developer] : 소문자로 변환한다.
	UPPER('SQL Developer'), --[SQL DEVELOPER] : 대문자로 변환한다.
	ASCII('A'),	--[65] : 알파벳 'A'의 아스키코드값을 반환한다.
	CHR('65'),	--[A] : 아스키코드 65의 문자를 반환한다.
	CONCAT('SQL', 'Developer'),	--[SQL Developer] : 2개의 문자열을 붙인다.
	SUBSTR('SQL Developer', 1, 3),	--[SQL] : 문자열의 1번쨰 문자부터 3번째 문자까리 잘라낸다.
	LENGTH('SQL'),	--[3] : 문자열의 길이를 구한다.
	LTRIM(' SQL'),	--[SQL] : 왼쪽 공백 제거
	RTRIM('SQL ')	--[SQL] : 오른쪽 공백 제거
FROM DUAL;
```

2. 숫자형 함수 : 숫자를 입력하면 숫자 값을 반환한다. 
    * ABS, MOD, ROUND, TRUNC, SIGN, CHR, CEIL, FLOOR, EXP, LOG, LN, POWER, SIN, COS, TAN
```sql
SELECT 
	ABS(-15),	--[15] : 절대값을 반환한다.
	SIGN(1),	--[1] : 사인값을 반환한다.
	MOD(8,3),	--[2] : 8을 3으로 나눈 나머지를 반환한다.
	CEIL(38.1),	--[39] : 무조건 올림한다.
	FLOOR(38.9),	--[38] : 무조건 내림한다.
	ROUND(38.678,2),	--[38.68] : 소수점 아래 두번쩨까지 표현한다. 소수점 아래 세번째 자리에서 반올림한다.
	ROUND(38.678,1),	--[38.7] : 소수점 아래 첫번쩨까지 표현한다. 소수점 아래 두번째 자리에서 반올림한다.
	ROUND(38.678,0),	--[39] : 소수점 아래를 표현하지 않는다. 소수점 아래 첫번째 자리에서 반올림한다.
	ROUND(38678,-2),	--[38700] : 소수점 위 두번째 자리에서 반올림한다.
	TRUNC(38.678),	--[38] : 0의 자리에서 자른다.
	TRUNC(38.678, 1),	--[38.6] : 소수점 아래 첫번째자리에서 자른다. 
	TRUNC(38.678, 2)	--[38.67] : 소수점 아래 두번쩨 자리에서 자른다.
FROM DUAL;
```
3. 날짜형 함수 : DATE 타입의 값을 연산한다. 
    * SYSDATE, EXTRACT, TO_NUMBER
```sql
SELECT 
	SYSDATE,	--[2022-11-16 11:14:24.000] : 현재 년월일시분초 출력
	EXTRACT(YEAR FROM SYSDATE),	--[2022]	: 년 출력
	EXTRACT(MONTH FROM SYSDATE),	--[11]	: 월 출력
	EXTRACT(DAY FROM SYSDATE),	--[16]	: 일 출력
	TO_NUMBER(TO_CHAR(SYSDATE, 'YYYY')),	--[2022] : 년 출력
	TO_NUMBER(TO_CHAR(SYSDATE, 'MM')),	--[11]	: 월 출력
	TO_NUMBER(TO_CHAR(SYSDATE, 'DD')),	--[16]	: 일 출력
	TO_NUMBER(TO_CHAR(SYSDATE, 'HH24')),	--[11] : 시 출력
	TO_NUMBER(TO_CHAR(SYSDATE, 'MI')),	--[14] : 분 출력
	TO_NUMBER(TO_CHAR(SYSDATE, 'SS')),	--[24] : 초 출력
	TO_CHAR(SYSDATE, 'YYYY'),	--[2022] : 년 출력(문자열)
	TO_CHAR(SYSDATE, 'MM'),	--[11] : 월 출력(문자열)
	TO_CHAR(SYSDATE, 'DD'),	--[16] : 일 출력(문자열)
	TO_CHAR(SYSDATE, 'HH24'),	--[11] : 시 출력(문자열)
	TO_CHAR(SYSDATE, 'MI'),	--[14] : 분 출력(문자열)
	TO_CHAR(SYSDATE, 'SS'),	--[24] : 초 출력(문자열)
	SYSDATE - 1,	--[2022-11-15 11:21:58.000] : 현재에서 하루 뺴기
	SYSDATE - (1/24),	--[2022-11-16 10:21:58.000] : 현재에서 1시간 빼기
	SYSDATE - (1/24/60),	--[2022-11-16 11:20:58.000] : 현재에서 1분 빼기 
	SYSDATE - (1/24/60/60),	--[2022-11-16 11:21:57.000] : 현재에서 1초 빼기
	SYSDATE - (1/24/60/60)*10,	--[2022-11-16 11:21:48.000] : 현재에서 10초 빼기
	SYSDATE - (1/24/60/60)*30	--[2022-11-16 11:21:28.000] : 현재에서 30초 빼기
FROM DUAL;
```
4. 변환형 함수 : 문자, 숫자, 날짜형의 데이터형을 다른 데이터형으로 형변환한다. <br>
    명시적 형변환 : 변환형 함수를 사용하여 데이터형을 형변환 하는것
    암시적 형변환 : DBMS가 자동으로 데이터형을 변환하는 것 
    예를들어, SQL문 작성시 문자형과 숫자형을 서로 비교할 때 명시적으로 형변환을 하지 않으면 DBMS내부에서 자동으로 2개의 각기 다른 데이터형을
    동일한 데이터형으로 변환 후 연산을 처리하게 되는데, 이러한 상황을 암시적 형변환이 일어났다고 표현한다.
    * TO_NUMBER, TO_CHAR, TO_DATE, CONVERT
```sql
SELECT 
	TO_CHAR(SYSDATE, 'YYYY/MM/DD'),	--[2022/11/16] : 날짜형을 문자형으로 변환
	TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'),	--[2022/11/16 11:32:51] : 날짜형을 문자형으로 변환
	TO_CHAR(10.25, '$999,999,999.99'),	--[          $10.25] : 숫자형을 문자형으로 변환
	TO_CHAR(12500, 'L999,999,999'),	--[             ￦12,500] : 숫자형을 문자형으로 변환
	TO_NUMBER('100') + TO_NUMBER('100'),	--[200] : 문자형을 숫자형으로 변환
	TO_DATE(TO_CHAR(SYSDATE, 'YYYYMMDD'), 'YYYYMMDD')	--[2022-11-16 00:00:00.000] : 날짜형을 문자형으로 변환 후 다시 날짜형으로 변환
FROM DUAL;
```
5. NULL 관련 함수 : NULL을 처리하기 위한 함수이다.
    * NVL, NULLIF, COALESCE
    * NULL과 어떠한 연산을 수행하던 결과는 NULL이기 때문에 NULL에 대한 처리가 중요하다.
```sql
SELECT 
	NULLIF('SQLD', 'SQLP'),	--[SQLD] : 두 문자열이 다르면 첫 번째 문자열 출력
	NULLIF('SQLD', 'SQLD'),	--[NULL] : 두 문자열이 같으면 NULL을 출력
	NVL(NULLIF('SQLD', 'SQLD'), '같음'),	--[같음] : NULL인 경우 '같음'을 출력하도록
	COALESCE(NULL, NULL, 'SQLD'),	--[SQLD] : NULL이 아닌 첫 번째 인자 출력
	COALESCE(NULL, 'SQLP', 'SQLD'),	--[SQLP] : NULL이 아닌 첫 번째 인자 출력
	COALESCE('SQL', 'SQLP', 'SQLD')	--[SQL] : NULL이 아닌 첫 번째 인자 출력
FROM DUAL;
```

6. 단일행 CASE 표현의 종류
    * 특정 값에 대해서 조건에 따라 각기 다른 값을 리턴하도록 하는 것
    * CASE문 또는 DECODE함수를 이용한다. 
```sql
SELECT 
	CASE 
		WHEN A.INDUTY_CL_SE_CD  = 'ICS001' THEN '대' 
		WHEN A.INDUTY_CL_SE_CD  = 'ICS002' THEN '중'
		WHEN A.INDUTY_CL_SE_CD  = 'ICS003' THEN '소'
		ELSE ''
		END AS "업종분류구분명"
FROM SQLD.TB_INDUTY_CL_SE A;


SELECT 
	DECODE (A.INDUTY_CL_SE_CD,
			'ICS001',
			'대',
			'ICS002',
			'중',
			'ICS003',
			'소',
			''
	) AS "업종분류코드명"
FROM SQLD.TB_INDUTY_CL_SE A;
```
    
