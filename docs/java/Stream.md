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


### 2-8. 문자열 스트림
문자열 String을 이용해서 스트림을 생성할 수 있다. 
```java
//스트링의 각 문자를 IntStream으로 변환하는 예제이다.
IntStream charStream = "Stream".chars(); //[83, 116, 114, 101, 97, 109]

//정규식을 이용해 문자열을 자르고, 분리된 요소들로 스트림을 만들기 
Stream<String> stringStream = Pattern.compile(", ").splitAsStream("Java, Is, Fun"); //[Java, Is, Fun]
```

### 2-9. 파일 스트림
```java
//Files클래스의 lines메소드는 해당 파일의 각 line을 String타입의 스트림으로 만들어준다.
try {
	Stream<String> lineStream = Files.lines(Paths.get("file.txt"), Charset.forName("UTF-8"));
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
```

### 2-10. 병렬 스트림
```java
List<Product> productList = new ArrayList<>();
		
//병렬 스트림 생성 
Stream<Product> parallelStream = productList.parallelStream();
		
//병렬 여부 확인 
boolean isParallel = parallelStream.isParallel();
		
//각 작업을 스레드를 이용해 병렬 처리 
//스트림에서 amout값을 꺼내서 10을 곱한 후 
//결과값이 200보다 큰 값을 모두 찾는다.
boolean isMany = parallelStream
		.map(product -> product.getAmount()*10)
		.anyMatch(amount -> amount > 200);
}
	
class Product {
	int amount = 0;
		
	public int getAmount() {
		return this.amount;
	}
}

//배열을 이용해서 병렬 스트림 만들기 
Arrays.stream(arr).parallel();
		
//스트림 연결하기 
Stream<String> stream1 = Stream.of("Java", "Scala", "C");
Stream<String> stream2 = Stream.of("Python", "Go", "Swift");
Stream<String> concat = Stream.concat(stream1, stream2);
```

# 3. 가공하기
    전체 요소 중에서 API를 이용해 내가 원하는 것만 뽑아낼 수 있다.
    이러한 가공 단계를 중간작업(Intermediate operations)이라고 한다. 
    이러한 작업은 스트림을 리턴하기 때문에 여러 작업을 이어서 메서드 체이닝 방식을 사용한다.
```java
List<String> codes = Arrays.asList("Java", "C", "Python", "Javascript", "Go");
```

### 3-1. Filtering
필터는 스트림 내 요소들을 하나씩 평가해서 걸러내는 작업이다. 
인자로 받는 Predicate는 boolean을 리턴하는 함수형 인터페이스로 평가식이 들어간다.
```java
Stream<T> filter(Predicate<? super T> predicate);

//스트림의 각 요소에 대해 평가식 (contains("a"))을 실행하게 되고, true값을 반환하는 경우만 스트림으로 구성해서 리턴한다. 
Stream<String> stream = codes.stream()
	.filter(c -> c.contains("a"));	//[Java, Javascript]
```


### 3-2. Mapping
스트림내 요소들을 하나씩 특정 값으로 변환해준다. 
이때 값을 변환하기 위한 람다를 인자로 받는다. 
스트림에 들어가 있는 값이 input이 되어서 특정 로직을 거친 후 output이 되어 <b>새로운 스트림</b>에 담는 작업
```java
<R> Stream<R> map(Function<? super T, ? extends R> mapper);

//스트림내 String의 메서드를 실행해서 대문자로 변환한 값들이 담긴 """새로운 스트림"""을 리턴한다.
Stream<String> stream = codes.stream()
	.map(String::toUpperCase);

------------------------------------------------------------------------------------------------------------------------------

//중첩구조를 한 단계 제거하고 단일 컬렉션으로 만들어주는 역할을 하는 flatMap, map메소드 자체만으로는 한 번에 할 수 없는 기능을 수행가능
//실무 데이터처럼 중첩을 감싸져 있는 형태를 이런식으로 중첩을 제거한다.
//인자는 mapper, 반환값은 스트림이다.
<R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper);

//중첩 리스트 예제 [[a], [b]]
List<List<String>> list = Arrays.asList(Arrays.asList("a"), Arrays.asList("b"));

//중첩을 제거한 후 작업 [a, b]
List<String> flatList = list.stream()
	.flatMap(Collection::stream)
	.collect(Collectors.toList());
----------------------------------------------------------------------------------------------------------------------------
//객체로부터 뽑아내기
//학생 객체를 가진 스트림에서 학생의 국영수 점수를 뽑아 새로운 스트림을 만들어 평균을 구한다.
//평균값이 존재하는 경우에만 출력한다.
student.stream()
	.flatMapToInt(student -> IntStream.of(student.getKor(),
						studnet.getEng(),
						student.getMat()))
	.average().ifPresent(avg -> System.out.println(Math.round(avg*10)/10.0));
```
