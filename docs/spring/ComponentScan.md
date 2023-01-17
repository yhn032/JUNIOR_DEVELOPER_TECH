# 컴포넌트 스캔 

## 컴포넌트 스캔과 의존관계 자동 주입 시작하기 

- 지금까지는 @Bean or <bean>사용해서 직접 스프링 빈으로 등록할 내용을 지정해주었다.
- 하지만 실무에서는 등록되어야 할 빈이 수십 수백개가 될 터인데, 이를 일일히 등록하다 보면 설정 정보의 크기가 커질뿐만 아니라 
  분명히 누락되는 부분이 생기게 될 것이다. 
- 그리하여 스프링에서는 AppConfig같은 설정 정보가 없어도 자동으로 스프링 빈을 등록하는 기능을 제공하는 데 이것이 컴포넌트 스캔이다. 
- 또한 자동으로 스프링 빈을 등록하는 과정에서 의존관계를 자동으로 주입하도록 지원하는 @Autowired가 있다. 
  
[1]. @ComponentScan
* 컴포넌트 스캔을 사용하기 위해선 먼저 설정 정보로 사용할 클래스 파일을 생성하고, 클래스에 @Component를 붙여준다. 
* 이렇게 하면 @Component가 붙어있는 클래스 파일을 스프링이 찾아서 해당 클래스를 스프링 빈으로 등록해준다.
* 단, @Configuration내부에 들어가면 @Component가 있기 때문에 기존에 진행하던 예제 파일에 영향을 주지 않기 위해서 제외 조건을 필터링으로 추가해주자.
    * @ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class))
    * @Component가 붙은 클래스를 자동 스캔하다가 클래스가 Configuration인 어노테이션 타입은 필터링을 통해 제외하겠다는 뜻이다.
  
[2]. @Autowired
* 이전에는 AppConfig에 설정정보를 명시하여 의존관계를 주입해주었다면, 이번에는 스프링이 자동으로 찾아서 등록하기 때문에 의존관계에 대한 처리를 해주어야 하는데
* @Component가 붙어서 스캔되는 클래스 내부에 생성자(생성자 주입) or Setter함수(세터 주입)에 @Autowired를 붙여주면 스프링이 빈으로 등록할 때 자동으로 의존관계를 만들어 준다.
  
[3]. Test 로그 확인 
```java
    @Test
    void basicScan(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);

        MemberService bean = ac.getBean(MemberService.class);
        Assertions.assertThat(bean).isInstanceOf(MemberService.class);
    }
```
  위와 같은 테스트 코그의 결과로 나타나는 로그를 살펴보자.
  ```xml
  14:09:01.208 [main] DEBUG org.springframework.context.annotation.ClassPathBeanDefinitionScanner - Identified candidate component class: file [C:\Users\qqwee2\OneDrive\김병국\인프런\스프링 핵심 원리 기본편\SpringStudy\core\out\production\classes\hello\core\discount\RateDiscountPolicy.class]
14:09:01.212 [main] DEBUG org.springframework.context.annotation.ClassPathBeanDefinitionScanner - Identified candidate component class: file [C:\Users\qqwee2\OneDrive\김병국\인프런\스프링 핵심 원리 기본편\SpringStudy\core\out\production\classes\hello\core\member\MemberServiceImpl.class]
14:09:01.213 [main] DEBUG org.springframework.context.annotation.ClassPathBeanDefinitionScanner - Identified candidate component class: file [C:\Users\qqwee2\OneDrive\김병국\인프런\스프링 핵심 원리 기본편\SpringStudy\core\out\production\classes\hello\core\member\MemoryMemberRepository.class]
14:09:01.216 [main] DEBUG org.springframework.context.annotation.ClassPathBeanDefinitionScanner - Identified candidate component class: file [C:\Users\qqwee2\OneDrive\김병국\인프런\스프링 핵심 원리 기본편\SpringStudy\core\out\production\classes\hello\core\order\OrderServiceImpl.class]
  ```
ClassPathBeanDefinitionScanner라는 스캐너를 통해서 @Component를 붙여주었던 클래스를 자동으로 스캔하여 스프링 빈으로 등록된 것을 확인할 수 있다. 
<br>
  
```xml
14:09:01.454 [main] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Autowiring by type from bean name 'memberServiceImpl' via constructor to bean named 'memoryMemberRepository'
14:09:01.461 [main] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Autowiring by type from bean name 'orderServiceImpl' via constructor to bean named 'memoryMemberRepository'
14:09:01.461 [main] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Autowiring by type from bean name 'orderServiceImpl' via constructor to bean named 'rateDiscountPolicy'
```
또한 위와 같이 의존관계가 자동으로 주입된것을 확인할 수 있다. 
  
** 이전과는 조금 다른 점이 있다면, @Bean태그를 사용하여 스프링 빈을 등록했을 때는 메서드의 이름이 빈의 이릉으로 등록되었지만 
  자동스캔을 통해 빈이 등록될때는 클래스의 이름으로 등록이 된다.(첫글자 대문자만 소문자로 변경되어 빈 이름이 지정된다.)
  
  
## 탐색 위치와 기본 스캔 대상
- 모든 자바 클래스를 전부 스캔하면 시간이 오래 걸리므로, 꼭 필요한 위치부터 시작할 수 있도록 아래와 같이 스캔 위치를 지정할 수 있다.  
```java
  @ComponentScan(
        basePackages = "hello.core",
        basePackageClasses = AutoAppConfig.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
 )
```
- basePakages는 해당 패키지를 스캔 시작 위치로 잡는다. 
- basePaageClasses는 해당 클래스가 포함된 패키지를 스캔 시작 위치로 잡는다. 
- ★ 시작 위치를 잡지 않은 경우 @ComponentScan이 붙어있는 설정 정보의 패키지로 디폴트가 지정된다.
    - 관례상 패키지를 지정하지 않고, 설정 정보의 클래스 위치를 프로젝트 폴더 구조 상 최상단에 두는 것이 좋다.
  
## 컴포넌트 스캔 기본 대상 
1. @Component : 컴포넌트 스캔에 사용
2. @Controller : 스프링 MVC 컨트롤러에서 사용
3. @Service : 스프링 비즈니스 로직에서 사용
4. @Repository : 스프링 데이터 접근 계층에서 사용
5. @Configuration : 스프링 설정 정보에서 사용

컴포넌트 스캔의 용도 뿐만 아니라 아래의 애노테이션이 있으면 스프링이 부가 기능을 수행한다. 
1. @Controller : 스프링 MVC 컨트롤러로 인식
2. @Service : 실제로 특별한 처리를 하지는 않지만, 개발자들이 핵심 비즈니스 로직이 여기에 있겠구나 라고 비즈니스 계층을 인식하는데 도움이 된다.
3. @Repository : 스프링 데이터 접근 계층으로 인식하고, 데이터 계층의 예외를 스프링 예외로 변환해준다. 
4. @Configuration : 스프링 설정 정보로 인식하고, 스프링 빈이 싱글톤을 유지하도록 추가 처리를 한다.(CGLIB)
  
### 참고 
  * 스프링 부트를 사용하면 스프링 부트의 대표 시작 정보인 @SpringBootApplication안에 @ComponentScan이 들어있다.
  * useDefaultFilters라는 옵션은 기본적으로 켜져 있는데, 이 옵션을 끄게 되면 위에서 소개한 기본 스캔 대상들이 제외된다.


## 필터
- includeFilters : 컴포넌트 스캔 대상을 추가로 지정한다. 
- excludeFilters : 컴포넌트 스캔에서 제외할 대상을 지정한다.
  
### FilterType의 옵션 
1. ANNOTATION : 기본값으로 애노테이션을 인식해서 동작한다. (ex> org.example.SomeAnnotation)
2. ASSIGNABLE_TYPE : 지정한 타입과 자식 타입을 인식해서 동작한다. (ex> org.example.SomeClass)
3. ASPECTJ : AspectJ 패턴 사용 (ex> org.example..*Service+)
4. REGEX : 정규 표현식 (ex> org\.example\.Default.*)
5. CUSTOM : TypeFilter라는 인터페이스를 구현해서 처리한다. (ex> org.example.MyTypeFilter)
