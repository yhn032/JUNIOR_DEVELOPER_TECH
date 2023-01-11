# 1.LSP(LisKov's Substitution Principle)

## 정의 
- 단순 기존 타입의 하위 타임을 만드는 가이드 
- S 타입의 객체 o1에 대해 T타입의 객체 o2가 있을 경우, T로 정의된 모든 프로그램 P에 대해 o1이 o2로 대체되어도 P는 변경되지 않는다면, S가 T의 하위타입이다.
![image](https://user-images.githubusercontent.com/87313203/206625289-9f0bd33e-1dcb-4c3f-a7e4-22660a0767d8.png)
- 프로그램 모듈이 BASE 클래스에 대한 참조를 사용하고 있다면, 프로그램 모듈의 기능에 영향을 주지 않고 Base클래스를 파생 클래스로 대체할 수 있다.
- 상위 타입의 객체를 하위 타입의 객체로 치환해도 상위타입을 사용하던 프로그램은 정상적으로 작동해야 한다.
- 리스코프 치환 원칙이 제대로 지켜지지 않으면 다형성에 기반한 개방 폐쇄 원칙 역시 지켜지지 않기 때문에, 리스코프 치환 원칙을 지키는 것은 매우 중요하다.

### 예시(LSP를 지키지 않은 예시) 
    직사각형과 정사각형을 표현하는 클래스
    정사각형을 직사각형의 특수한 형태로 보고 상속관계를 만들어보자. 
```java
public class Rectangle {
	private int width;
	private int height;
	
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
}

public class Square extends Rectangle{
	@Override
	public void setWidth(int width) {
		// TODO Auto-generated method stub
		super.setWidth(width);
		super.setHeight(width);
	}

	@Override
	public void setHeight(int height) {
		// TODO Auto-generated method stub
		super.setWidth(height);
		super.setHeight(height);
	}
	
}
```

이제 높이와 폭을 비교해서 높이를 더 길게 만들어 주는 기능을 제공한다고 해보자.
```java
public static void increaseHeight(Rectangle rec) {
		if(rec.getHeight() <= rec.getWidth()) rec.setHeight(rec.getWidth()+10);
}
```
increaseHeight메서드를 사용하는 코드는 실행 후에 너비와 폭이 그대로 거나 조건이 만족하면 결과로 폭이 너비값보다 더 커질 것이라고 가정할 것이다. 
하지만 파라미터로 상위타입인 직사각형이 아니라 하위 타입인 정사각형 Square클래스를 전달한다면 이 가정은 깨진다.
Square 클래스는 너비를 변경하던 폭을 변경하던 상위 타입의 세터를 재정의하여 값이 같은 값으로 셋팅 되기 때문에 크기가 변해도 같은 값으로 변경된다. 
즉, 상위타입의 결과를 하위 타입으로는 이루어 낼 수 없다는 것이다. 
instacneOf를 사용하여 파라미터가 상위타입인 경우에만 실행되도록 조건을 설정할 수 있겠지만 
이미 리스코프 치환 원칙의 기본을 어긴 셈이다. 

결국 비슷해보이지만 직사각형과 정사각형은 별개의 클래스로 구현해야 한다는 것이다.

# 2.SRP(Single Responsibility Principle)
* 한 클래스는 하나의 책임만 가져야 한다. 
	* 하나의 클래스가 여러가지 역할을 수행하면 안 된다.
* 하나의 책임이라는 것은 모호하다. 클 수도, 작을 수도 있으며 문맥과 상황에 따라 다르다. 
* <b>중요한 판단의 기준은 변경</b>이다. 코드의 변경이 필요한 경우 파급효과가 적으면 SRP를 잘 따른것이다. 
	* 예 : UI 변경, 객체의 생성과 사용을 분리	  


# 3.OCP(Open/Closed Principle)
* 소프트웨어의 요소는 확장에는 열려 있으나, 변경에는 닫혀 있어야 한다.
	* 기존 기능을 수정하는 경우 클라이언트 코드를 수정하는 일이 없어야 한다. 
	* 다형성의 원리를 사용하여 클래스(구현)가 아닌 인터페이스(역할)에 의존하도록 설계해야 한다. 	

# 4.ISP(Interface Segregation Principle)
* 특정 클라이언트를 위한 인터페이스 여러 개가 범용 인터페이스 하나보다 낫다. 
	* 하나의 인터페이스로 해결하려고 하지 말고, 역할을 세분화해서 구성하라. 	
* 예
	* 자동차 인터페이스 -> 운전 인터페이스, 정비 인터페이스로 분리
	* 사용자 클라이언트 -> 운전자 클라이언트, 정비사 클라이언트로 분리 
* 분리하면 정비 인터페이스 자체가 변해도, 운전자 클라이언트에 영향을 주지 않는다. 
* 인터페이스의 역할이 명확해지고, 대체 가능성이 높아진다.

# 5.DIP(Dependency Inversion Principle)
* 개발자는 "추상화에 의존해야지, 구체화에 의존하면 안 된다." 의존성 주입은 이 원칙을 따르는 방법 중 하나다. 
* 구현 클래스가 아닌 인터페이스에 의존해라. 
* 클라이언트가 인터페이스에 의존해야 유연하게 구현체를 변경할 수 있다. 
* 반대로 구현체에 의존하면 변경이 아주 어려워진다.
