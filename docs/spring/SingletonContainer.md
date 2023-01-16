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

