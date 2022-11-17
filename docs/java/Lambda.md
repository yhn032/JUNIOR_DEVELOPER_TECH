# 람다 표현식(Lambda Expression)

## 람다식이란? 
람다식은 메서드를 하나의 식으로 표현한 것이다. 
    * 객체 지향 언어보다는 함수 지향 언어에 가깝다. 
    * 함수를 간략하면서도 명확한 식으로 표현할 수 있도록 해준다. 
    * 메서드를 람다식으로 표현하면 메서드의 이름 및 반환 값이 없어지므로 익명 함수라고도 한다. 
    * 다른 함수에 함수를 인자로 전달할 때 유용하다.
    * 람다식의 형태는 매개변수를 가진 코드 블록이지만, 런타임 시에는 익명 구현 객체를 생성한다. 

### 람다 함수 VS 익명 클래스 
* 익명 클래스는 객체를 생성해야 하지만, 함수는 평가될 때마다 새로 생성되지 않는다. 함수를 위한 메모리 할당은 자바 힙의 Permanent영역에 한 번 저장된다.
* 객체는 데이터와 밀접하게 연관해서 동작하지만, 함수는 데이터와 분리되어 있다. 
* 클래스의 스태틱 메소드가 함수의 개념과 가장 유사하다. 

예시 코드와 함께 차이점을 살펴보자. 
예시1
```java
void anonymousClass() {
	final Server server = new HTTPServer();
	waitFor(new Condition() {
		public Boolean isSatisfied(){
			return !server.isRunning();
		}
	})
}

void closure() {
	Server server = new HTTPServer();
	waitFor(() -> !server.isRunning());
}
```

    변수 server는 Condition 클래스의 익면 객체로 복사되어야 한다. 
    반면, 람다에서는 실행환경이나 다른 조건들이 복사되지 않는다. 
    
예시2
    익명클래스의 메소드에서 this예약어는 익명클래스의 인스턴스를 참조한다. 
    람다에서 this예약어는 람다를 둘러썬 범위를 참조한다. 
```java
//익명클래스
public class Example{
		
		private String firstName = "Charlie";
		
		public void anonymousexample() {
			Function<String, String> addSurName = new Function<String, String>(){
				@Override
				public String apply(String surname) {
					return Example.this.firstName + " " + surname;
				}
			}
		}
}

//람다
private String firstName = "Jack";

public void example() {
	Function<String, String> addSurName = surname -> {
		return firstName + " " + surname;
	}
}
```
    예제에서 익명 클래스의 this키워드는 익명 클래스의 객체를 의미하지만 
    익명 클래스에는 firstName이 없기 때문에 static접근 방법처럼 Example.this.firstName으로 접근해야 한다. 
    반면, 람다에서의 this는 람다를 둘러싼 범위 즉, Example 클래스를 참조하기 때문에
    this는 firstName - Jack를 가리킨다. 

    <b>섀도잉 Shadowing</b>
    섀도잉은 외부, 내부에 동일한 이름의 변수가 존재할 때 내부 범위의 변수가 우선되기 때문에, 
    외부 범위의 변수가 덮어씌워지는 것을 말한다. 
```java
public class ShadowingExample{
	
	private String firstName = "Charlie";
	
	public void ShadowingExample(String firstName) {
		Function<String, String> addSurName = surname -> {
			//firstName : 매개변수
			//this.firstName : "Charlie:
			return this.firstName + " " + surname;
		}
	}
}
```
    this를 사용시에는 람다를 둘러싸고 있는 Charlie를 가리키고
    this를 사용하지 않으면 내부의 매개변수를 가리킨다.
    
## 사용방법
```(매개변수1, 매개변수2, ...) -> {실행문 ... }```
화살표를 기준으로 전자는 실행문 블록을 실행하기 위해 필요한 값을 제공한다. 
화살표는 전자의 매개변수를 이용해서 중괄호{} 실행문을 실행한다는 의미이다. 

```java
//Arrays.sort()와 익명 클래스 
Integer numbers[] = new Integer[] {1,2,3,4,5};
Arrays.sort(numbers, new Comparator<Integer>() {

	@Override
	public int compare(Integer o1, Integer o2) {
		// TODO Auto-generated method stub
		return o1.compareTo(o2);
	}
});

//Arrays.sort()와 람다 
Arrays.sort(numbers, (first, second) -> first.compareTo(second));
```

    람다는 기본적으로 기능을 가지는 익명의 코드 블록이다. 
    예시에서는 람다가 Comparator<Integer> 타입으로 처리된다. 
    Comparator는 하나의 추상 메소드만 가지고 있기 때문에, 컴파일러의 측면에서는 
    람다가 그 추상메서드(compareTo)를 구현한 내용이라고 보는것이다.
    
    따라서 아래와 같이 하나뿐인 추상메소드는 람다로 대체가 가능하다.
    
```java
interface Example {
    R apply(A arg);
}
// 인스턴스 생성 방식 
new Example() {
    @Override
    public R apply(A args) {
      // body
    }
}

위의 추상 메서드를 람다로 바꾸면 아래와 같다. 
(args) -> {
   //body
}
```

### 문법 요약
```java
// 인자 -> 바디 
(int x, int y) -> {return x+y;}

// 인자 타입생략 - 컴파일러가 추론 가능 
(x, y) -> {return x+y;}

//return 및 중괄호 생략 
(x, y) -> x + y

//인자가 하나인 경우 인자 괄호 생략 
x -> x * 2

//인자가 없으면 빈 괄호로 표시
() -> System.out.println("Lambda")

//메소드 참조 Method Reference
//value -> System.out.println(value)의 축약형
System.out::println
```

## 람다의 타입 추론

    Type Inference
    타입이 정해지지 않은 변수의 타입을 컴파일러가 유추하느 기능이다. 
    일반 변수에 대해 타입 추론을 지원하는 python이나 scala는 타입 생략(var 키워드 사용)이 가능하지만 
    자바에서는 일반 변수에 대해선 타입추론을 지원하지 않는다. 
    제네릭과 람다에 대한 타입추론이라고 생각하자.
    
    자바의 컴파일러는 Type Erasure를 사용하는데, 이는 컴파일시 타입 정보를 제거한다. 
    https://docs.oracle.com/javase/tutorial/java/generics/erasure.html
    공문을 확인해보면, 컴파일시에 모든 타입의 제네릭을 계층 구조의 가장 상위 또는 계층이 없다면 Object로 대체한다
    라고 소개한다. 즉, 아무리 원시코드의 제네릭에 타입을 지정하더라도 컴파일 후에 생성되는 바이트 코드에는 
    타입이 삭제되어 기본 클래스, 인터페이스, 메서드가 포함된다는 것이다. 
    
    Type Eraser에 의해서 컴파일시에 제네릭이 삭제된다고 보는게 편할 것 같다. 
    이는 메서드 시그니쳐에도 동일하게 적용이 된다. 메서드 시그니쳐란 메서드 명, 파라미터의 순서, 타입, 개수를 총괄하는 의미로
    리턴 타입과 Exception은 메서드 시그니처가 아니다. 
    아무튼 제네릭 구문이 지워진 List는 List<?>와 같은 의미가 될 것이고, 이말은 곧 List<? extends Object>를 의미한다. 
    Object가 모든 객체의 부모 클래스라는 점을 봤을때 모든 타입이 들어올 수 있다는 의미다. 
    이와 같은 한계로 인해 런타임 시에는 타입 증거가 어렵다는 점이 있다. 
### 1. 메소드 호출 시 인자의 타입 추론 
```java
@SuppressWarnings("rawtypes")
public static final List EMPTY_LIST = new EmptyList<>();
@SuppressWarnings("unchecked")
public static final <T> List<T> emptyList() {
    return (List<T>) EMPTY_LIST;
}


static void processNames(List<String> names) {
	for(String name : names) {
		System.out.println("Hello " + name);
	}
}

List<String> name = Collections.emptyList();		//타입  추론 가능
processNames(Collections.emptyList());				//타입 추론 불가능
```
    Collections.emptyList()는 제네릭 타입을 알 수 없기 때문에 List<Object>로 결과를 반환한다. 
    첫 번째는 컴파일러가 제네릭 타입이 String인 것을 유추할 수 있지만, 
    processNames메서드의 인자List<String>과는 타입이 맞지도 않고 추론하지 못해 컴파일 에러가 발생한다. 
    하지만 자바8에서는 이부분이 개선되어 타입 증거 없이도 인자의 타입이 유추 가능해졌다.


### 2. 연쇄 메서드 호출 시 인자의 타입 추론
```java
static class List<E> {
  static <T> List<T> emptyList() {
    return new List<T>();
  }
    
  List<E> add(E e) {
    // 요소 추가
    return this;
  }
}

List<String> list = List.emptyList(); // OK
List<String> list = List.emptyList().add(":("); // error
List<String> list = List.<String>emptyList().add(":("); // OK
```
    emptyList() 메소드를 호출하면서 타입이 제거되기 때문에 연쇄적으로 호출되는 부분에서 인자의 타입을 알 수 없다. 
    자바 8에서 수정될 예정이었으나,,, 취소되어 여전히 컴파일러에서 명시적으로 타입을 알려줘야 한다.

## 함수형 인터페이스
    자바는 람다를 지원하기 위해서 타입 추론을 강화해야 하는 필요성이 있었는데, 
    이를 충족시키기 위해 함수형 인터페이스가 등장했다. 
    위에서 잠깐 언급한대로 단 하나의 <b>추상 메소드</b>로 이루어진 인터페이스를 람다가 대체할 수 있다고 했는데, 
    이렇게 하나의 추상 메소드로 이루어진 인터페이스를 함수형 인터페이스라고 한다.
    이 안에 함수의 시그니쳐가 정의되어 있기 때문에 컴파일러는 이 정보를 참고해 람다에서 생략된 정보를 추론한다.

    컴파일러가 미리 체크할 수 있도록 @FunctionalInterface 어노테이션으로 표시한다. 
```java
//컴파일은 가능 
public interface Supplier<T> {}

//추상메소드가 하나도 없어서 에러 발생 
@FunctionalInterface
public interface Supplier<T> {
    T get();
}

//추상메소드가 두 개 이상이라 에러 발생 
@FunctionalInterface
public interface Supplier<T> {
    T apply(A args);
    T get();
}

//오류 없이 실행
@FunctionalInterface
public interface Supplier<T> {
    T get();
}
```

### 상속 
함수형 인터페이스를 상속받는 경우도 지금까지의 특성을 그대로 이어받는다. 

```java
@FunctionalInterface
interface A {
   abstract void apply();
}

//상속받은 인터페이스 외 메서드를 가질 수 없음
interface B extends A {

}

//명시적으로 표현가능
interface B extends A {
   @Override
   abstract void apply();
}

//메서드 추가 불가
interface B extends A {
   void Get(); //Error!!
}
```

## 람다의 타입 추론 
람다는 함수형 인터페이스가 타입에 대한 정보를 컴파일러에게 제공하기 때문에 타입추론이 가능하다. 
```java
@FuntionalInterface
interface Calculation {
   Integer apply(Integer x, Integer y);
}

static Integer calculate(Calculation operation, Integer x, Integer y) {
   return operation.apply(x, y);
}

//람다 생성 
Calculation addition = (x, y) -> x + y;
Calculation subtraction = (x, y) -> x - y;

//사용 
calculate(addition, 2, 2);
calculate(subtraction, 5, calculate(addition, 3, 2));
```

## 예외 
마지막으로 기억해야할 것은 함수형 인터페이스는 단 하나의 추상 메소드만 가질 수 있다는 것이다. <br>
추상 메소드의 개수만 하나로 제한한다는 뜻이다. <br>
예외로는 아래의 세개가 대표적이다. <br>
* Object 클래스의 메소드를 오버라이드 하는 경우 
* 디폴트 메소드 
      * 자바 8에 추가된 내용으로 
      * 이미 만들어진 인터페이스에 메서드를 추가하면 해당 인터페이스를 구현한 구현체 또한 오버라이딩을 해주는 등
      * 작업을 해야 한다. 이를 보완하기 위해 등장한 것으로 default 예약어를 사용하며 범위는 public이다. 
      * 인터페이스에 디폴트로 메소드를 추가하면 이를 구현한 구현체에는 자동으로 구현된다.  
* 스태틱 메소드


### 람다식의 장점
1. 코드를 간격하게 만들 수 있다. 
2. 식에 개발자의 의도가 명확히 드러나므로 가독성이 향상된다. 
3. 함수를 만드는 과정없이 한 번에 처리할 수 있기에 코딩 시간이 줄어든다. 
4. 병렬 프로그래밍이 용이하다. 

### 람다식의 단점
1. 람다를 사용하면서 만드는 익명함수는 재사용이 불가능하다. 
2. 디버깅이 까다롭다 
3. 람다를 많이 사용하면 비슷한 기능의 함수를 계속 중복해서 만들기 때문에 코드가 지저분해질 수 있다. 
4. 재귀로 만들 경우에는 다소 부적합한 면이 있다. 


## REFERENCES 
https://futurecreator.github.io/2018/07/19/java-lambda-basics/ <br>
https://futurecreator.github.io/2018/07/20/java-lambda-type-inference-functional-interface/ <br>
https://docs.oracle.com/javase/tutorial/java/IandI/defaultmethods.html <br>
https://medium.com/asuraiv/java-type-erasure%EC%9D%98-%ED%95%A8%EC%A0%95-ba9205e120a3 <br>
airportal.go.kr/knowledge/statsnew/air/airline.jsp <br>
