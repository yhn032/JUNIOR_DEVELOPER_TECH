# 1. Stream이란? 
    자바 8에서 추가된 스트림은 람다를 활용할 수 있는 기술 중 하나이다. 
    자바 8이전에는 배열 또는 컬렉션 객체를 다루기 위해서는 for or foreach문을 
    돌면서 요소 하나씩을 꺼내 다루는 방법을 사용했다. 
    
    스트림은 말 그대로 '데이터의 흐름'이다. 
    배열 또는 컬렉션 객체에 함수 여러 개를 조합하여 원하는 결과를 필터링하고 가공된 결과를 얻을 수 있다.
    또한 람다를 이용해 코드의 양을 줄이고, 가독성있게 표현할 수 있다. 
    즉, 배열과 컬렉션을 함수형으로 처리할 수 있다.
    
    
    스트림을사용하면 하나의 작업을 둘 이상의 작업으로 잘게 나눠서 동시에 진행하는 병렬 처리를 쓰레드를 이용하여 
    많은 요소들을 빠르게 처리할 수 있다.

```
스트림의 과정
전체 -> 맵핑 -> 필터링 1 -> 필터링 2 -> 결과 만들기 -> 결과물
```

# 2. 생성하기 (스트림 인스턴스 생성)

### 2-1. 배열 스트림 ```Arrays.stream()```
```java
String[] arr = new String[] {"A", "V", "D"};
Stream<String> stream = Arrays.stream(arr);					//배열을 스트림으로 만들기
Stream<String> streamOfArrayPart = Arrays.stream(arr, 1, 3);//배열의 일부분을 스트림으로 만들기

```

### 2-2. 컬렉션 스트림 ```인터페이스에 추가된 디폴트 메소드```
```java
//실제 인터페이스
public interface Collection<E> extends Iterable<E>{
    default Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
    
    default Stream<E> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }
}

//사용방법
List<String> list = Arrays.asList("a", "b", "c");
Stream<String> stream = list.stream();
Stream<String> parallelStream = list.parallelStream();//병렬처리 스트림
```

### 2-3. 비어있는 스트림
요소가 없을때 null대신 빈 스트림을 만들어서 사용한다. 
```java
public Stream<String> streamOf(List<String> list){
		return (list == null || list.isEmpty()) ? Stream.empty() : list.stream();
}
```

### 2-4. builder 패턴
스트림에 직접 원하는 값을 넣을 수 있다.
```java
Stream<String> builderStream = 
				Stream.<String>builder()
				.add("java").add("is").add("fun")
				.build();
```

### 2-5. generate
람다로 값을 넣을 수 있다. Supplier<T>는 인자는 없고 리턴값만 있는 함수형 인터페이스다.
단, 이렇게 생성하는 스트림은 크기가 정해져있지 않고 무한하기 때문에 특정 사이즈로 최대 크기를 제한해야 한다. 
```java
//실제 인터페이스 메서드
public static<T> Stream<T> generate(Supplier<? extends T> s) { ... }
//5개의 "java"가 들어간 스트림 생성
Stream<String> generatedStream = Stream.generate(() -> "java").limit(5);
```

### 2-6. iterate()
초기값과 해당 값을 다루는 람다를 이용해서 스트림에 들어갈 요소를 만든다.
이 방법또한 특정 사이즈로 스트림의 크기를 제한해야 한다.
```java
//[30, 32, 34, 36, 38]
Stream<Integer> iteratedStream = Stream.iterate(30, n -> n+2).limit(5);
```

### 2-7 기본 타입형 스트림 
```java
IntStream intStream = IntStream.range(1, 5);	//[1, 2, 3, 4] : 종료지점 포함x
LongStream longStream = LongStream.rangeClosed(1, 5);	//[1, 2, 3, 4, 5] : 종료지점 포함o

//제네릭을 사용하는 경우에는 boxed메서드를 사용해 박싱한다.
Stream<Integer> boxedIntStream = IntStream.range(1, 5).boxed();

//Random클래스를 사용해 도출한 난수로 세 가지 타입의 난수 스트림을 만들 수 있다. 
IntStream ints = new Random().ints(3);
LongStream longs = new Random().longs(3);
DoubleStream doubles = new Random().doubles(3);
```
