## 스프링 컨테이너
```java
AppicationContext ac = new AnnotationConfingApplicationContext(AppConfig.class);
```

- AppicationContext을 스프링 컨테이너라고 하며, 인터페이스이다. 
- 구현하는 구현체로 Annotation~, XML~이 있는데 설정파일의 형식이 자바소스인지, XML파일인지에 따라 다르다. 
- 최근에는 XML로 설정하는 방식을 잘 사용하지 않고 있다.
- 스프링 컨테이너를 생성할 때는 컨테이너에 저장될 스프링 빈의 정보를 전달해주어야 하는데, 
  - 이 정보를 담고 있는 파일 AppConfig를 파라미터로 전달한다. 
- 빈의 이름은 메서드 이름을 사용하며 특별히 지정할 수도 있다. 
  - 빈의 이름이 중복되면, Map의 key처럼 덮어써지거나 무시되는 경우가 있으니 반드시 모든 빈의 이름을 다르게 설정해야 한다. 
```java
@Bean  //빈 이름 == 메서드 명(memberService)
public MemberService memberService(){... return ...};

@Bean(name="member") //빈 이름 == member
public MemberService memberService(){... return ...};
```

### 스프링 컨테이너에 등록된 빈 정보 조회하기 

- [1]. 모든 빈 조회
```java  
String [] beans = ac.getBeanDefinitionNames();
```  
- [2]. 애플리케이션 빈 조회
```java
//ROLE_APPLICATION : AppConfig에서 직접 등록한 Bean의 정보
//ROLE_INFRASTRUCTURE : 스프링이 내부적으로 사용하는 Bean의 정보
String [] beans = ac.getBeanDefinitionNames();
for(String bean : beans){
  BeanDefinition b = ac.getBeanDefinition(bean);
  if(b.getRole() == BeanDefinition.ROLE_APPLICATION){
    ...
  }
}

```
- [3]. 빈 이름으로 조회
```java
MemberService memberService = ac.getBean("memberService", MemberService.class);
```
- [3-1]. 빈 이름으로 조회가 실패하는 경우 
```java
MemberService memberService = ac.getBean("xxxxx", MemberService.class); //존재하지 않는 빈 이름으로 조회하는 경우

NoSuchBeanDefinitionException 발생!
```
- [4]. 이름없이 타입으로 조회(동일한 타입으로 여러개의 빈이 존재하는 경우 에러가 발생할 수 있음)
```java
MemberService memberService = ac.getBean(MemberService.class);
```
- [5]. 구현한 객체 타입으로 조회(역할이 아닌 구현에 의존하는 테스트 코드로 추천하지 않는 방식) 변경시 유연성이 떨어진다.
```java
MemberService memberService = ac.getBean(MemberServiceImpl.class);
```
- [6]. 이름 없이 타입으로 조회시 같은 타입이 둘 이상 있는 경우
```java
//중복 오류가 발생한다. 
NoUniqueBeanDefinitionException

//특정 빈의 이름을 지정해서 조회하자.
MemberService memberService = ac.getBean("memberService1", MemberService.class);
```
- [7]. 특정 타입을 모두 조회하는 경우
```java
Map<String, MemberRepository> beansOfType = ac.getBeansOfType(MemberRepository.class);
for(String key : beansOfType.keySet()){
  key + beansOfType.get(key)
  ...
}
```
- [8]. 상속 관계에 있는 빈을 조회하는 경우 
```java
//부모 타입으로 조회하면 자식 타입도 함께 조회된다. 
void findAllBeanByParentType() {
  Map<String, DiscountPolicy> beansOfType = ac.getBeansOfType(DiscountPolicy.class);
  assertThat(beansOfType.size()).isEqualTo(2);
  for (String key : beansOfType.keySet()) {
    System.out.println("key = " + key + " value=" + beansOfType.get(key));
  }
}
//Object타입으로 조회하면 모든 스프링 빈을 조회할 수 있다.
void findAllBeanByObjectType() {
  Map<String, Object> beansOfType = ac.getBeansOfType(Object.class);
  for (String key : beansOfType.keySet()) {
    System.out.println("key = " + key + " value=" + beansOfType.get(key));
  }
}

```


## BeanFactory
* 스프링 컨테이너의 최상위 인터페이스 
* 스프링 빈을 관리하고 조회하는 역할 
* getBean() 메서드를 제공

### BeanDefinition
* 빈 설정 메타정보
* @Bean, \<bean\> 하나 당 각각 하나씩 메타정보가 생성된다. 
  각각의 형식에 맞는 Reader를 통해 설정정보를 읽고 BeanDefinition을 생성하면
  생성한 메타정보를 읽어서 스프링빈을 생성한다.
* 스프링이 다양한 설정 형식을 지원할 수 있게 도와주는 인터페이스 
* 스프링은 이 인터페이스에만 의존한다. 
* 스프링 컨테이너는 이러한 메타정보를 기반으로 스프링 빈을 생성한다.
