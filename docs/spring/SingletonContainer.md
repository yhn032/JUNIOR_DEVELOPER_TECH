# 웹 애플리케이션과 싱글톤  
- 스프링은 태생이 기업용 온라인 서비스 기술을 지원하기 위해 탄생했다. 
- 대부분의 스프링 애플리케이션은 웹 애플리케이션이고, 웹 애플리케이션은 보통 여러 고객이 동시에 요청을 한다. 
- 요청이 들어올 때마다 동일한 객체를 매번 만드는 것은 메모리 낭비가 극심하다. 
  - 예를 들어, 고객 트래픽이 초당 100(1초당 100번의 요청)이 나오면 초당 100개의 객체가 생성되는 것이다. 
- 이러한 메모리 낭비를 방지하기 위해서 해당 객체가 첫 호출시 한 번만 생성되고, 이 객체가 공유되도록 설계하는 것이 싱글톤 패턴이다. 

## 싱글톤 패턴 
- 클래스의 인스턴스가 딱 1개만 생성되는 것을 보장하는 패턴 
- 객체의 인스턴스가 2개 이상 생성되지 못하도록 막아야 한다
  - private 생성자를 사용해서 외부에서 임의로 new 키워드를 사용하지 못하도록 한다.

```java
public class SingleTonService {
    //1. 자기 자신을 static 영역에 객체를 딱 1개만 생성해둔다.
    //실행 시점에 자기 자신의 인스턴스를 생성한다.
    private static final SingleTonService instance = new SingleTonService();

    //2. public으로 열어서 객체 인스턴스가 필요하다면 이 static 메서드를 통해서만 조회하도록 허용한다.
    public static SingleTonService getInstance(){
        return instance;
    }

    //3. 생성자를 private으로 선언해서 외부에서 new 키워드를 ㅅ ㅏ용한 객체 생성을 못하게 막는다.
    private SingleTonService(){

    }

    public void logic(){
        System.out.println("싱글톤 객체 로직 호출");
    }

}
```
싱글톤 패턴을 구현하는 방법은 여러가지가 있다. 여기서는 객체를 미리 생성하고 공유하는 가장 단순하고 안전한 방식을 사용했다.
1. static 영역에 객체 instance를 실행 시점에 미리 생성해서 올려둔다. 
2. 이 인스턴스가 필요할 때는 무조건 getInstance()메서드를 통해서만 조회할 수 있다.
3. 이를 호출하면 항상 초기에 생성한 인스턴스를 반환한다. 
4. 생성자를 private으로 생성하여 혹시라도 외부에서 추가로 인스턴스를 생성하는 것을 막는다 .

## 싱글톤 패턴의 문제점
- 구현하는 코드 자체가 많이 들어간다. static, private 생성자 ...etc
- 클라이언트가 구체 클래스에 의존한다. -> MemberServiceImple.getInstace() 등 DIP, OCP 위반
- 유연한 테스트가 어렵다.
- 생성자가 private이기 때문에 자식 클래스를 생성하기 어렵다 
- 유연성이 떨어져 안티 패턴이라고도 불린다. 

## 싱글톤 컨테이너 
- 스프링 컨테이너는 위의 싱글톤 패턴의 문제점을 해결하면서, 객체 인스턴스를 싱글톤으로 관리한다.
  - 싱글톤 패턴을 위한 지저분한 코드가 필요없다.(static private ...etc)
  - DIP, OCP, 테스트, private 생성자로부터 발생되는 제한사항에 관계없이 자유롭게 싱글톤을 사용할 수 있다. 
- 스프링 컨테이너는 싱글톤 패턴을 적용하지 않아도, 객체 인스턴스를 싱글톤으로 관리한다.
  - 스프링 컨테이너를 처음 생성할 때, 설정 정보를 확인하여 @Bean 어노테이션이 붙어있는 모든 메소드를 호출하여 컨테이너 안에 인스턴스를 생성해두었었다.
  - 이후 ac.getBean() 메서드를 통해 컨테이너가 생성되면서 만들어진 Bean을 반환하는 형식이다. 
- 스프링 컨테이너는 싱글톤 컨테이너의 역할을 하고, 이렇게 싱글톤 객체를 생성하고 관리하는 기능을 <b>싱글톤 레지스트리</b>라고 한다.     

```java
    @Test
    @DisplayName("스프링 컨테이너와 싱글톤")
    void SpringContainer(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        //1. 조회 : 호출할 때마다 같은 객체를 반환
        MemberService memberService1 = ac.getBean("memberService", MemberService.class);

        //2. 조회 : 호출할 때마다 같은 객체를 반환
        MemberService memberService2 = ac.getBean("memberService", MemberService.class);

        //참조값이 같은 것을 확인
        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);

        //memberService1 != memberService2
        assertThat(memberService1).isSameAs(memberService2);
    }
```
스프링 컨테이너 덕분에 요청이 들어올 때마다 객체를 생성하는 것이 아니라, 이미 만들어진 객체를 공유해서 효율적으로 재사용할 수 있다. 
참고로 스프링의 기본 빈 등록 방식은 싱글톤이지만, Bean의 생명주기를 조작해서 요청할 때마다 새로운 객체를 생성해서 반환하는 기능도 제공하니
참고하자.

## 싱글톤 방식의 주의점 ★
    객체 인스턴스르 하나만 생성해서 공유하는 싱글톤 방식은 여러 클라이언트가 하나의 동일한 객체를 사용하기 때문에 
    싱글톤 객체는 상태를 유지하지 않도록 무상태(stateless)로 설계해야 한다.
* 특정 클라이언트에 의존적인 필드가 있으면 안 된다.
* 특정 클라이언트가 값을 변경할 수 있는 필드가 있으면 안 된다. 
* 가급적 읽기만 가능해야 한다. 
* 필드 대신에 자바에서 공유되지 않는, 지역변수, 파라미터, ThreadLocal 등 사용하고 소멸되는 변수를 사용해야 한다. 

### 예시 - stateful한 싱글톤 객체로 인한 문제점
```java
public class StatefulService {
    private int price; //상태를 유지하는 필드
    public void order(String name, int price){
        System.out.println("name = " + name + " price = " + price);
        this.price = price; //여기가 문제!
    }

    public int getPrice(){
        return price;
    }
}

////Test Code ======================
    @Test
    void statefulServiceSingleTon(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        //두 개의 요청이 들어왔다고 가정해보자.
        //ThreadA : A사용자가 10000원을 주문
        statefulService1.order("userA", 10000);

        //ThreadB : B사용자가 20000원을 주문
        statefulService1.order("userB", 20000);

        //ThreadA: 사용자 A가 주문 금액을 조회 -> 10000원이 아닌 20000원이 나온다.
        //참조값은 다르지만, 실제 사용하는 인스턴스는 동일하기 때문에 사용자 B의 요청에 덮어씌워졌다.
        int price = statefulService1.getPrice();
        System.out.println("price = " + price);

        assertThat(statefulService1.getPrice()).isEqualTo(20000);
    }

    static class TestConfig{
        @Bean
        public StatefulService statefulService(){
            return new StatefulService();
        }
    }
```

    위의 테스트 코드의 경우 참조값은 다르지만, 싱글톤 객체의 특성상 각 레퍼런스에는 동일한 인스턴스가 할당되기 때문에 
    인스턴스 내부에 상태를 유지하는 price필드를 모든 사용자가 수정을 해버리면, 가장 마지막에 수정한 값만이 
    살아남게 되는 동시성의 오류가 발생하게 된다. 
    
    사용자 A는 10000원의 금액으로 주문을 넣었지만, 이후에 들어온 사용자 B의 요청에 의해서 주문 금액이 20000원으로 변경된 것이다. 
    이러한 오류를 해결하기 위해서는 아에 수정이 안되도록 하는 것이 최선이지만, 불가피하다면
    상태를 유지하는 price변수를 order메서드 내부에서만 사용할 수 있도록 지역변수로 선언하는 방법이 있다. 
    
    예시는 간단하지만, 실무에서는 복잡한 상속관계 등으로 인해 발견하기 쉽지 않고, 발생하게되면 치명적인 장애를 유발하기 때문에 
    싱글톤 객체는 반드시 무상태!!!로 유지해야 한다.
    
    
## @Configuration과 싱글톤
아래의 AppConfig.java 코드를 보자 
```java
@Configuration
public class AppConfig {
    @Bean
    public MemberService memberService(){
        System.out.println("call AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }
    @Bean
    public MemberRepository memberRepository() {
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }
    @Bean
    public OrderService orderService(){
        System.out.println("call AppConfig.orderService");
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }
    @Bean
    public DisCountPolicy discountPolicy() {

        return new RateDiscountPolicy();
    }
}
```
    memberService와 orderService를 만드는 부분을 봄녀 동일하게 memberReposiroty()를 호출하고 
    두 번의 new를 통해 MemoryMemberRepository()를 호출한다. 
    결과적으로 각각 달느 2개의 MemoryMemberRepository가 생성되면서 싱글톤이 깨지는 것처럼 보이니 
    임시로 위의 예시처럼 로그를 찍어서 살펴보자 
    예상(호출 순서는 보장X) -> 
    call AppConfig.memberService
    call AppConfig.memberRepository
    call AppConfig.memberRepository
    call AppConfig.memberRepository
    call AppConfig.orderService
    
    현실 -> 
    call AppConfig.memberService
    call AppConfig.memberRepository
    call AppConfig.orderService
    
    싱글톤이 깨지는 것처럼 보이지만 실제로는 아주 잘 유지되고 있는 것을 확인할 수 있다. 
    어떻게 이것이 가능한 것일까?

## @Configuration과 바이트코드 조작의 마법 ★★★★★★★★★★★★★
    스프링 컨테이너는 싱글톤 레지스트리로 스프링 빈이 싱글톤이 되도록 보장해주어야 한다. 
    하지만 스프링이 자바 코드 내부에까지 어떻게 하기는 어렵기 때문에 우리가 찍은 로그를 보면 3번 호출되어야 하는것이 맞다. 
    -> 왜냐하면 스프링 컨테이너가 생성될 때 파라미터로 받은 설정정보 내부에 @Bean이 있는 메소드를 무조건 1번은 호출하기 때문이다. 
    
```java
    @Test
    void configurationDeep(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        AppConfig bean = ac.getBean(AppConfig.class);

        System.out.println("bean = " + bean.getClass());
    }
```
위의 테스트를 실행해보면 우리가 평소에 알던 순수 클래스가 아니라 이상한 모양의 클래스가 조회된다.
- 순수 : class hello.core.AppConfig
- 조작 : class hello.core.AppConfig$$EnhancerBySpringCGLIB$$48c26325

이는 내가 파라미터로 넘겨준 AppConfig가 아니라 스프링이 CGLIB이라는 바이트 코드 조작 라이브러리를 사용해서 
AppConfig클래스를 상속받은 임의의 <b>신규 다른 클래스</b>를 만들고, 이 클래스를 스프링 빈으로 등록한 것이다. 
이렇게 새롭게 만든 임의의 클래스가 싱글톤을 보장해준다. 

아마 AppConfig@CGLIB의 로직을 예상해서 코드를 예상해보면 아래와 같지 않을까 싶다. 
실제로는 굉장히 복잡하겠지,,,
```java
@Bean 
public MemberRepository memberRepository(){
  if(이미 컨테이너에 등록되어 있으면... ){
    return 등록된 빈을 찾아서 반환;
  }else {
    기존 로직(AppConfig 내부 @Bean이 달린 메서드)을 호출해서 생성하고 스프링 컨테이너에 등록한다.
  }
}
```

즉, @Bean이 붙은 메서드마다 이미 스프링 빈이 존재하면 존재하는 빈을 반환하고, 없으면 생성해서 스프링 빈으로 등록하고 반환하는 코드가 
동적으로 만들어진다.

### @Configuration 없이 @Bean만 적용하면?
    이전과 같이 스프링 컨테이너에 스프링 빈이 등록은 된다. 
    하지만 우리가 처음 예상한 대로 3번의 호출을 거치면서 싱글톤이 보장되지 않는다. 
    또한 CGLIB을 사용한 신규 조작 클래스가 아니라 내가 파라미터로 전달한 순수 AppConfig가 스프링 빈으로 등록된다.
    
    스프링 설정정보는 항상 @Configuration을 사용하여 싱글톤을 보장하도록 하자.
