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


### 3-3 Sorting
```java
Stream<T> sorted();
Stream<T> sorted(Comparator<? super T> comparator);

//1. 인자없이 호출하는 경우 -> 오름차순으로 정렬
List<Integer> list = IntStream.of(14, 11, 20, 39, 23)
	.sorted()
	.boxed()
	.collect(Collectors.toList());
System.out.println(list.toString());	//[11, 14, 20, 23, 39]

//2. 인자를 넘기는 경우
List<String> lang = Arrays.asList("Java", "Scala", "Groovy", "Python", "Go", "Swift");
lang = lang.stream()
	.sorted(Comparator.reverseOrder())
	.collect(Collectors.toList());
System.out.println(lang.toString()); //[Swift, Scala, Python, Java, Groovy, Go]

//3. Comparator의 compare메소드는 두 인자를 비교해서 값을 리턴한다. 
//문자열의 길이를 기준으로 정렬하기 - 오름차순 
lang = lang.stream()
	.sorted(Comparator.comparingInt(String::length))
	.collect(Collectors.toList());
System.out.println(lang.toString()); //[Go, Java, Swift, Scala, Python, Groovy]

//문자열의 길이를 기준으로 정렬하기 - 내림차순
lang = lang.stream()
	.sorted((s1, s2) -> s2.length() - s1.length())
	.collect(Collectors.toList());
System.out.println(lang.toString()); //[Python, Groovy, Swift, Scala, Java, Go]
```


### 3-4. Iterating
    스트림 내 요소들 각각을 대상으로 특정 연산을 수행하는 메서드로는 peek이 있다. 
    특정 결과를 반환하지 않는 함수형 인터페이스 Consumer를 인자로 받는다. 
```java
Stream<T> peek(Consumer<? super T> action);

//결과에 영향을 미치지 않는다. 대부분 작업을 처리하는 중간에 결과를 확인하고자 할때 사용한다.
int sum = IntStream.of(1, 3, 5, 7, 9)
	.peek(System.out::print)
	.sum();
//13579
```


# 4. 결과 만들기
가공한 스트림을 가지고 내가 사용할 결과값으로 만들어내는 단계
스트림을 끝내는 최종작업(terminal operations)

### 4-1. Calculation
최소, 최대, 합, 평균 등 기본형 타입으로 결과를 만들어낼 수 있다. 
```java
long count = IntStream.of(1, 3, 5, 7, 9).count();
long sum = IntStream.of(1, 3, 5, 7, 9).sum();

//빈스트림인 경우 count와 sum은 0을 출력하며 되지만, 
//평균, 최대의 경우에는 표현할 수가 없기 때문에 
//마찬가지로 java 8에 등장한 NULL처리 방법인 optional로 감싸서 리턴한다.
OptionalInt min = IntStream.of(1, 3, 5, 7, 9).min();
OptionalInt max = IntStream.of(1, 3, 5, 7, 9).max();
		
//스트림에서 바로 처리할 수도 있다. 
DoubleStream.of(1.1, 2.2, 3.3, 4.4, 5.5)
	.average()
	.ifPresent(System.out::println);
```

### 4-2. Reduction
```java
//reduce()
@Params
accumulator : 각 요소를 처리하는 계산 로직. 각 요소가 올 때마다 중간 결과를 생성하는 로직 
identity : 계산을 위한 초기값으로 스트림이 비어서 계산할 내용이 없더라도 이 값은 리턴
combiner : 병렬 스트림에서 나눠 계산한 결과를 하나로 합치는 로직

Optional<T> reduce(BinaryOperator<T> accumulator);
T reduce(T identity, BinaryOperator<T> accumulator);
<U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner);

/* 인자가 1개인 경우 
BinaryOperator<T>는 같은 타입의 인자 두개를 받아 같은 타입의 결과를 반환하는 함수형 인터페이스다. 
*/
OptionalInt reduced = IntStream.range(1, 4)
	.reduce((a,b) -> {
		return Integer.sum(a, b);
	});

/* 인자가 2개인 경우 
여기서 람다는 ★메소드 참조★를 이용해서 넘길수 있다.
*/
int reduce = IntStream.range(1, 4)
	.reduce(10, Integer::sum);


/* 인자가 3개인 경우
세번째 인자인 combiner는 병렬 스트림에서만 실행된다.

초기값 10에 각 스트림 값을 더한 세 개의 값을 각 스레드가 계산한다. 
스레드 1 : 10 + 1 = 11
스레드 2 : 10 + 2 = 12 
스레드 3 : 10 + 3 = 13
combiner호출 -> 11 + 12 = 23
combiner호출 -> 23 + 13 = 36
*/
Integer reduceParallel = Arrays.asList(1, 2, 3)
	.parallelStream()
	.reduce(10, Integer::sum, (a,b) ->{
		System.out.println("combiner was called");
		return a+b;
	});
```

### 4-3. Collecting
```java
//collect()
//Collector타입의 인자를 받아서 처리한다. 자주 사용하는 작업은 Collectors객체에서 제공한다.
-----------------------------------------------------------------------------------------------------
//Collectors.toList() - 스트림에서 작업한 결과를 담은 리스트로 반환한다. 
List<String> collectorCollection = productList.stream()
		.map(Product::getName)
		.collect(Collectors.toList());
System.out.println(collectorCollection.toString());	//[potatoes, orange, lemon, bread, sugar]
-----------------------------------------------------------------------------------------------------
//Collectors.joining() - 스트림에서 작업한 결과를 하나의 스트링으로 연결한다.
@Params
	delimiter : 각 요소 중간에 들어가 요소를 구분시켜주는 구분자
	prefix : 결과 맨 앞에 붙는 문자
	suffix : 결과 맨 뒤에 붙는 문자

String listToString = productList.stream()
		.map(Product::getName)
		.collect(Collectors.joining());
System.out.println(listToString);	//potatoesorangelemonbreadsugar


String listToString2 = productList.stream()
		.map(Product::getName)
		.collect(Collectors.joining(",", "<", ">"));
System.out.println(listToString2);	//<potatoes,orange,lemon,bread,sugar>
-----------------------------------------------------------------------------------------------------
//Collectors.averageingInt() - 숫자 값의 평균을 나타낸다. 
Double averageAmount = productList.stream()
		.collect(Collectors.averagingInt(Product::getAmount));
System.out.println(averageAmount);
-----------------------------------------------------------------------------------------------------
//Collectors.summingInt() - 숫자 값의 합을 나타낸다.
Integer summingAmount = productList.stream()
		.collect(Collectors.summingInt(Product::getAmount));
System.out.println(summingAmount);


Integer summingAmount = productList.stream()
		.mapToInt(Product::getAmount)
		.sum();
System.out.println(summingAmount);
-----------------------------------------------------------------------------------------------------
//Collectors.summarizingInt() -합계와 평균을 모두 구한다. 이를 사용하면 통계 작업을 위한 mapToInt를 호출할 필요가 없다.
IntSummaryStatistics statistics = productList.stream()
		.collect(Collectors.summarizingInt(Product::getAmount));
System.out.println(statistics);
//-> IntSummaryStatistics{count=5, sum=86, min=13, average=17.200000, max=23}
개수 : statistics.getCount();
합계 : statistics.getSum();
평균 : statistics.getAverage();
최소 : statistics.getMin();
최대 : statistics.getMax();
-----------------------------------------------------------------------------------------------------
//Collectors.groupingBy() - 특정 조건을 기준으로 요소들을 그룹지을 수 있다. 함수형 인터페이스 Function을 인자로 받는다.
Map<Integer, List<Product>> collectMapOfLists = productList.stream()
		.collect(Collectors.groupingBy(Product::getAmount));
System.out.println(collectMapOfLists);
//같은 수량이면 리스트로 묶어서 보여준다.
{23=[Product{amount=23, name='potatoes'}, 
     Product{amount=23, name='bread'}], 
 13=[Product{amount=13, name='lemon'}, 
     Product{amount=13, name='sugar'}], 
 14=[Product{amount=14, name='orange'}]}
-----------------------------------------------------------------------------------------------------
//Collectors.partitioningBy() - 함수형 인터페이스 predicate를 인자로 받아서 boolean값을 리턴한다.
Map<Boolean, List<Product>> mapPartitioned = productList.stream()
		.collect(Collectors.partitioningBy(res -> res.getAmount() > 15));
System.out.println(mapPartitioned.toString());
//평가하는 함수를 통해서 스트림 내 요소들을 true or false로 나눌 수 있다.
{false=[Product{amount=14, name='orange'}, 
        Product{amount=13, name='lemon'}, 
        Product{amount=13, name='sugar'}], 
 true=[Product{amount=23, name='potatoes'}, 
       Product{amount=23, name='bread'}]}
-----------------------------------------------------------------------------------------------------
//Collectors.collectingAndThen() - 특정 타입으로 결과를 collect한 후에 추가 작업이 필요한 경우에 사용한다.
public static<T,A,R,RR> Collector<T,A,RR> collectingAndThen(Collector<T,A,R> downstream, Function<R,RR> finisher) { ... }
finisher => collect후에 실행할 작업


//결과를 set으로 collect한 후에 수정 불간으한 set으로 변환하는 작업
Set<Product> unmodifiableSet = productList.stream()
		.collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));
 -----------------------------------------------------------------------------------------------------
//Collectors.of() - 직접 collector를 만들어서 사용한다.
public static<T, R> Collector<T, R, R> of(Supplier<R> supplier,
                                          BiConsumer<R, T> accumulator,
                                          BinaryOperator<R> combiner,
                                          Characteristics... characteristics) { ... }
/*
컬렉터를  생성하는 supplier에 LinkedList의 생성자를 넘겨준다. 
accumulator에는 생성한 리스트에 추가하는 add메소드를 넘겨준다. 
마지막으로 combiner를 이용해 생성된 리스트들을 하나의 리스트로 합친다.
*/
Collector<Product, ?, LinkedList<Product>> toLinkedList = Collector.of(LinkedList::new,
		LinkedList::add, (first, second) -> {
			first.addAll(second);
			return first;
		});

//위에서 직접 컬렉터를 만들었기 때문에, collect메소드에 우리가 만든 커스텀 컬렉터를 넘겨줄 수 있다.
LinkedList<Product> linkedListOfPersons = productList.stream()
		.collect(toLinkedList);
```

### 4-3. Matching
조건식 람다를 받아서 해당 조건을 만족하는 요소가 있는지 체크한 결과를 리턴한다. 
```java
boolean anyMatch(Predicate<? super T> predicate);	//하나라도 만족하는 요소가 있는지
boolean allMatch(Predicate<? super T> predicate);	//모든 요소가 조건을 만족하는지
boolean noneMatch(Predicate<? super T> predicate);	//모든 요소가 조건을 만족하지 않는지

//결과는 모두 true
List<String> names = Arrays.asList("Eric", "Elena", "Java");

boolean anyMatch = names.stream()
  .anyMatch(name -> name.contains("a"));
boolean allMatch = names.stream()
  .allMatch(name -> name.length() > 3);
boolean noneMatch = names.stream()
  .noneMatch(name -> name.endsWith("s"));
```

### 4-4. Iterating
요소를 돌면서 실행되는 최종작업. 결과를 출력하는 작업
peek는 중간 작업이라는 점에서 차이가 있다.
```java
codes.stream().forEach(System.out::println);
```

# References
https://github.com/eugenp/tutorials/tree/master/core-java-modules/core-java-streams-2 <br>
https://futurecreator.github.io/2018/08/26/java-8-streams/
