# Optional
    개발자를 가장 괴롭게 하는 예외는 NullPointerException이 아닐까 하는 생각이 든다. 
    마주하면 당황하는 것은 물론이고, 어디에서 NULL이 발생했는지 유지보수 하는것 또한 만만하지 않다.
    
    자바 8에서는 Optional<T> 클래스를 이용해서 NullPointerException을 방지할 수 있다. 
    NULL이 올 수 있는 값을 감싸는 래퍼 클래스로서 참조하더랃호 NULL이 일어나지 않도록 해주는 클래스

```java
public final class Optional<T> {
    /**
     * If non-null, the value; if null, indicates no value is present
     */
    private final T value;
```
    위의 코드처럼 value에 값을 저장하기 때문에, null이더라고 바로 참조 시 NPE가 발생하지 않고, 클래스이기 때문에 
    각종 메소드를 제공해준다. 

## 생성하기 
```java
Optional<String> optional = Optional.empty();
System.out.println(optional);	//Optional.empty
System.out.println(optional.isPresent()); //false


//Optional안에서는 값이 있을 수도 있고 빈 객체 일수도 있다.
Optional<String> optional2 = Optional.ofNullable(getString());
String result = optional2.orElse("other"); //값이 없으면 other를 리턴
System.out.println(result);
```

## 사용하기 
```java
//Java 8 이전의 NULL체크
List<String> list = getList();
List<String> listOpt = list != null ? list : new ArrayList();

//Java 8 이후의 NULL체크
List<String> listOpt = Optional.ofNullable(getList()).orElseGet(() -> new ArrayList<>());
```

## 비교해보기 
```java
//Null로 인해 지저분해진 코드 
User user = getUser();
if(user != null){
  Address address = user.getAddress();
  if(address != null) {
    String stree = address.getStreet();
    if(street != null){
    return street
    }
  }
}
return "주소없음"


//Optional을 적용해보자.
//map메소드는 해당값이 null이 아니면 mapper를 이용해 계산한 값을 저장하는 optional객체를 리턴한다. 
public <U> Optional<U> map(Function<? super T, ? extends U> mapper)
public <U> Optional<U> flatMap(Function<? super T, ? extends Optional<? extends U>> mapper)

Optional<User> user = Optional.ofNullable(getUser());
Optional<Address> address = user.map(User::getAddress);
Optional<String> street = address.map(Address::getStreet);
String result = street.orElse("주소없음");

user.map(User::getAddress)
  .map(Address::getStreet)
  .orElse("주소없음")
```

## NPE 예외처리
```java
String val = null;
String res = "";
try {
	result = val.toUpperCase();
} catch (NullPointerException e) {
	// TODO: handle exception
	e.printStackTrace();
}


String value = null;
Optional<String> valueOpt =  Optional.ofNullable(value);
String res = valueOpt.orElseThrow(CustomException::new).toUpperCase();
```
