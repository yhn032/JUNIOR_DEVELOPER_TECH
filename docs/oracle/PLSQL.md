# PL/SQL
   Oracle's Procedual Language extension to SQL의 약자
   프로그램을 논리적인 블록으로 나누는 구조화 된 블록 언어
   SQL문장에서 변수 정의, 조건문, 반복문 등을 지원한다. 
   오라클 자체에 내장되어 있다. 
   블록 구조이며, 자신이 컴파일 엔진을 가지고 있다.
   
### 장점
  1. 블록 구조로 다수의 SQL문을 DB에 전송하므로 수행속도를 향상시킬 수 있다. 
  2. 모든 요소는 하나 or 두 개 이상의 블록으로 구성하여 모듈화 가능하다.
  3. 큰 블록안에 소블럭을 위치할 수 있다. 
  4. 단순, 복잡한 데이터 형태의 변수를 선언한다. 
  5. 테이블의 데이터 구조와 컬럼명에 기반하여 동적으로 변수 선언 가능하다. 
  

### 블록 구조의 형태 
  Declare(선언부)
    - Optional, Variables(변수, 상수), cursors, user-defined exceptions(사용자 정의 예외 처리)<br>
  Begin(실행부, <b>必</b>)
    - SQL(쿼리, 반복문, 조건문 등), PL/SQL(소블록도 가능하다 했다)<br>
  Exception(예외처리)
    - Optional, 오류 정의 및 처리<br>
  End(마침부, <b>必</b>)
  


### 블록의 유형
![image](https://user-images.githubusercontent.com/87313203/200479864-6610ae18-187f-4d82-a5d3-4e3102eaa2dc.png)

  * Anonymous
    * 이름이 없는 블록. 1회성
    * 프로그램 안에서 선언되고, 실행시에 PL/SQL 엔진으로 전달된다.
  * Procedure
    * 이름이 있는 블록
    * 매개변수를 받을 수 있고, 반복 사용 가능 
  * Function
    * 보통 값을 계산하고 결과값을 반환하기 위해 사용
    * IN 파라미터만 사용할 수 있다.
    * 반환될 값의 데이터 타입을 Return문 안에 선언해야 한다. 
    * 블록내에서 반드시 값을 반환해야 한다. 
   
