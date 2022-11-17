# 메소드 참조 
    메소드 참조는 메소드를 간결하게 지칭할 수 있는 방법으로
    람다가 사용되는 곳 어디에서나 사용할 수 있다. 
    이미 존재하는 이름을 가진 메서드를 람다로써 사용할 수 있도록 가리키는 역할을 한다. 
    
    즉, 일반 함수를 람다 형태로 사용할 수 있도록 해주는 기능이다. 
    또한 메소드를 호출하는 것이 아닌 참조하는 것이기 때문에 소괄호는 생략한다. 

```java
//원래 함수 
public static String valueOf(Object obj) { ... }

//Class::method명 형태로 사용
String::valueOf

//소괄호를 사용하면 에러 발생
String::valueOf()
```

### 1. 기본 사용법
```java
@FucntionalInterface 
interface Conversion {
  abstract String convert(Integer number);
}

public static String convert(Conversion function, Integer Number) {
  return function.convert(number);
}

//convert 메서드를 호출할 때 람다를 인자로 넘겨줄 수 있다. 
convert(100, (number) -> String.valueOf(number));

//valueOf 메서드가 Integer를 받고 String을 반환하는 조건이 일치하기에 
//람다 표현식을  메소드 참조로 대체할 수 있다. 
convert(100, String::valueOf);
```

### 2. 생성자 참조 
생성자 메서드를 호출하는 것이 아닌 참조하는 것뿐이다. 
```java
String::new == () -> new String();

//EX 1. 인자가 없는 생성자 
//Factory는 임의의 객체를 반환하는 create메서드를 가진 함수형 인터페이스
@FunctionalInterface 
interface Factory<T> {
  T create();
}

public void usage() {
  List<Object> list = new ArrayList<>();
  for(int i=0; i<10 ;i++) {
    list.add(new Object());
  }
}

//위의 코드에서 객체를 생성하는 부분을 메소드로 뽑아내보자. 
public void usage() {
  List<Object> list = new ArrayList<>();
  init(list, ... );
}

public void init(List<Object> list, Factory<Object> factory) {
  for(int i=0; i<10 ;i++) {
    list.add(factory.create());
  }
}

//메서드를 메서드 참조방식으로 수정하면 아래와 같다. 
public void usage() {
  List<Object> list = new ArrayList<>();
  init(list, () -> new Object()); -- init(list, Object::new);
}

//EX 2. 인자가 있는 생성자 - 컴파일러가 함수형 인터페이스를 통해서 어떤 생성자를 사용할지 판단한다. 
class Person {
  public Person(String forename, 
                String surname, 
                LocalDate birthday, 
                Gender gender, 
                String emailAddress, 
                int age) {
     //init 
  }
}

@FunctionalInterface
interface PersonFactory {
  Person create(String forename, String surname, LocalDate birthday, Gender gender, String emailAddress, int age);
}

public void Example() {
  List<Person> list = new ArrayList<>();
  
  // 이렇게 사용할 수도 있지만 
  PersonFactory factory = (a, b, c, d, e, f) -> new Person(a, b, c, d, e, f);
  
  // 이렇게 사용해도 컴파일러가 적합한 생성자를 유추한다. 
  PersonFactory facory = Person::new;
  
  // 람다를 넘겨주어 생성하기 
  init(list, factory, a, b, c, d, e, f);
  
  // 인라인으로 처리가능
  init(list, Person::new, a, b, c, d, e, f); 
}

private void init(List<Person> list, 
                  PersonFactory factory, 
                  String forename, 
                  String surname, 
                  LocalDate birthday, 
                  Gender gender, 
                  String emailAddress, 
                  int age) {
  for(int i=0; i<10; i++) {
    list.add(factory.create(forename, surname, birthday, gender, emailAddress, age))
  }
}
```

### 3. 스태틱 메서드 참조
메서드 참조는 스태틱 메서드를 직접적으로 가리킬 수 있다. 
```java
public static class Comparators {
  public static Integer asc(Integer first, Integer second){
    return first.compareTo(second);
  }
}

//스태틱 메서드를 람다로 사용하는 경우
Collections.sort(Arrays.asList(5, 12, 4), (a, b) -> Comparators.asc(a, b));
Collections.sort(Arrays.asList(5, 12, 4), (a, b) -> Comparators::asc);
```


### 4. 인스턴스 메서드 참조(1)
특정 객체의 메서드를 참조할 수 있는데, 이때는 클래스 이름이 아닌 객체의 이름을 적어준다. <br>
이미 정의된 메소드를 람다로 재사용할 수 있다. <br>
함수형 인터페이스 간의 전환도 가능하다.(다른 함수형 인터페이스를 사용할 수 있다.)<br>
```java
x::toString //x는 접근하고자 하는 인스턴스 명


Callable<String> c = () -> "Hello"; //메소드 이름은 call이라고 하자. 
Factory<String> f = c::call; //다른 함수형 인터페이스의 메서드를 사용할 수 있다.


public void example () {
  String x = "hello";
  // 함수형 인터페이스 시그니처에 맞는 인스턴스 메소드를 전달할 수 있다
  function(x::toString); // 내부에는 x 가 없고 외부 범위의 x 에 접근하는 클로저
}

public static String function(Supplier<String> supplier) {
  return supplier.get();
}
```

### 5. 인스턴스 메서드 참조(2)
```java
Object::toString
//위의코드를 보면 Object는 class라는것을 알 수 있지만 위에서 언급한 것과 다르게 toString은
//정적 메서드가 아니라 일반적인 인스턴스 메서드다. 

//예시를 통해 차이점을 살펴보자
public void lambdaExample() {
  function("value", x -> x.toString()); // 넘겨 받은 x 를 사용
  function("value", String::toString); // 메소드 참조
}

public static String function(String value, Function<String, String> function) {
  return function.apply(value); // 클로저 아님
}
```
    내부적으로는 인스턴트 메소드 참조(1)은 클로저이고, (2)가 람다이다. 
    클로저는 미리 알 수 있는 객체의 인스턴스 메서드이고, 람다는 나중에 전달받는 임의의 객체의 인스턴스다.

## 요약
```java
//생성자 참조 
String::new == () -> new String();

//정적 메서드 참조(static) 
//ClassName::staticMethodName
String::valueOf == (s) -> String.valueOf(s)

//인스턴스 메서드 참조(1) 클로저
x::toString //instanceName::instanceMethodName
() -> "hello".toString();

//인스턴스 메서드 참조(2) 람다
String::toString //className::instanceMethodName
(s) -> s.toString();
```
## REFERENCES 
https://futurecreator.github.io/2018/08/02/java-lambda-method-references/ <br>
