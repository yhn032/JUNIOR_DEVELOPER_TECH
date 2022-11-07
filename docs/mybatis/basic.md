  마이바티스는 ORM중 하나로 XML문법으로 작성할 수 있으며, 
  DB Query를 작성할 때 아래와 같이 상황에 맞게 사용한다. 
<hr>

## 기본 문법
  CRUD에 대해서 각각 태그명으로 편하게 사용이 가능하다. 
  엘리먼트의 속성값 중에서 id는 Mapper Class에 매핑된 함수명을 기입한다. 즉, mybatis를 호출한 함수이름
  parameterType에는 파라미터로 넣을 타입을 적어준다. 
  resultType에는 반환받을 타입을 적어준다. 
 
 
```xml
<select></select>
<insert></insert>
<update></update>
<delete></delete>
```
<br>

```xml
<select id="mybatisSample" parameterType="com.exam.mybatis.testVo" resultType="com.exam.mybatis.testVo">
	(DB Query)
</select>
```

<hr>

## 파라미터 사용 

  SQL문에서 WHERE 조건절을 사용하기 위해서는 파라미터를 활용해야 하는데, 
  파라미터를 받는 방식이 여러 가지가 있으므로
  다양한 방식을 알아두어야 상황에 맞게 효과적으로 대응이 가능하다.
<br><br>

### 1. 2개 이상의 파라미터 - param1, param2
  {param'N'}으로 표현이 가능하지만, 파라미터가 의미하는 바를 알기 어려워 많이 사용하진 않음
```xml
<select>
	SELECT 	*
	 FROM 	테이블명
	WHERE 	컬럼=#{param1} AND	컬럼=#{param2}
</select>
```

### 2. 배열 파라미터 - String params(n)
  {'N'}으로 표현이 가능하지만, 의미하는 바를 알기 어려워 많이 사용하지 않음 
```xml
<select>
	SELECT 	*
	FROM 	테이블명
	WHERE 	컬럼=#{0} AND 	컬럼=#{1}
</select>
```

### 3. @Param 애노테이션 사용 - @Param("TestParam")String testColumn
  코드가 비교적 길어지지만, 유지보수 측면에서 파라미터의 이름만으로도 어떤 의미를 가지고 
  있는지 분명하게 파악할 수 있어 자주 사용한다. 
```xml
<select>
	SELECT 	*
	FROM 	테이블명
	WHERE 	컬럼=#{TestParam}
</select>
```
### 4. 해시맵 파라미터 - Map<String, String> 또는 Vo - Web Data Object
  가장 많이 사용한다.
  공공데이터의 구조인 JSON 데이터와 궁합이 좋다. 
  key를 넣으면 매핑되는 value를 얻을 수 있다.

```xml 
<select>
	SELECT 	*
	FROM 	테이블명
	WHERE 	컬럼=#{map의 키}
	  AND 	컬럼=#{map의 키}
</select>
```

<hr>

## 유용한 문법들 
마이바티스에서 유용하게 사용할 수 있는 함수들

### 문자열 비교
  문자열- String 비교방법
```xml
<if test='str == null && str == "" && str.equal("")'>
```
<br>

### 배열 비교 
  배열- List<String> 비교 방법
```xml
<if test='str == null && str.isEmpty()'>
```
<br>

### NULL값 사전 정의
  Query에서 필요한 파라미터가 ParameterType에 정의된 모델에 없거나 
  파라미터의 값이 Null이라면 jdbcType...이라는 데이터 타입을 찾을 수 없는 에러가 발생한다. 
  사전에 방지할 수 있는 함수를 만들어서 사용해보자. 
  Null인 경우 기본 타입을 지정해주는 느낌이다.
```xml
<select id="mybatisSample" parameterType="com.exam.mybatis.testVo" resultType="com.exam.mybatis.testVo">
    SELECT     *
    FROM    TEST
    WHERE    NAME = #{name, jdbcType=VARCHAR}
</select>
```
