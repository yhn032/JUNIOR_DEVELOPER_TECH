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
