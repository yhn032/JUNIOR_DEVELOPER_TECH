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
