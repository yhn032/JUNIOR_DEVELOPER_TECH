# 전략패턴 (Strategy Pattern) ```알고리즘의 선택```

## 정의 
* 실행 중에 <b>알고리즘을 선택</b>할 수 있게 하는 행위 소프트웨어 디자인 패턴
* 여러 알고리즘을 하나의 추상적인 접근점(```Interface```)을 만들어 접근점에서 서로 교환 가능(```Deligate```)하도록 하는 패턴
* 특정 계열의 알고리즘들을 정의하고 각 알고리즘을 캡슐화하여 이 알고리즘들을 해당 계열 안에서 상호 교체가 가능하도록 만든 패턴
* 특정 컨텍스트에서 알고리즘을 별도로 분리하는 설계방법 
    * 특정 기능 수행을 위해 다양한 알고리즘이 적용될 수 있는 경우, 이 다양한 알고리즘을 별도로 분리하는 설계방법 

## 예시 1

### 클래스 다이어그램 
![image](https://user-images.githubusercontent.com/87313203/202619090-ad15cb87-1fe4-4f5a-9625-6b464ea3c140.png)

### 소스 코드 
```java
//하나의 추상적인 접근점
public interface Weapon {
	public void attack();
}

//알고리즘 1 
public class Sword implements Weapon{

	@Override
	public void attack() {
		// TODO Auto-generated method stub
		System.out.println("검 공격");
	}
}

//알고리즘 2
public class Arrow implements Weapon{

	@Override
	public void attack() {
		// TODO Auto-generated method stub
		System.out.println("화살 공격");
	}
}

//알고리즘 3
public class Magic implements Weapon {

	@Override
	public void attack() {
		// TODO Auto-generated method stub
		System.out.println("마법 공격");
	}
}

public class User {
	//접근점
	private Weapon weapon;

	//알고리즘을 교환할 수 있게 해주는 세터 함수 
	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}
	
	public void attack() {
		if(weapon == null) System.out.println("맨손 공격");
		else weapon.attack();
	}
}

public static void main(String[] args) {
		
		//전략 패턴
		User user = new User();
		user.attack();
		
		user.setWeapon(new Sword());
		user.attack();
		
		user.setWeapon(new Arrow());
		user.attack();
		
		user.setWeapon(new Magic());
		user.attack();
		
	}
```

### 실행 결과 
맨손 공격 <br>
검 공격 <br>
화살 공격 <br>
마법 공격


## 예시 2 - 문제를 이해하고 해결해보자 
    Problem
    무역회사에서 해외 영업을 담당하고 있는 A씨는 주문관리 시스템이 필요함을 느꼈다. 
    여러 국가에서 제품에 대해 주문을 하면 해당 국가의 관세와 환율을 적용하여 가격을 
    계산해 주는 것이 필요하여 시스템 개발자에게 필요한 내용을 요구하여 아래와 같이 디자인된 시스템이 개발되었다.
![image](https://user-images.githubusercontent.com/87313203/202636086-7fcd76e2-1401-44d2-821f-fca5335cf5c3.png)
    
    Solution 
    Is-A와 Has-A 관계
    - 해외 영업망이 넓어지면서 영국과 프랑스가 주문을 하기 시작했다면, 이들의 관계를 다이어그램에 추가해보자 
    - 추가된 다이어그램을 기준으로 구현하고 제대로 동작하는지 확인해보자
    행위 및 알고리즘군 형성 
    - 상속을 통해서 구현할 경우 나타나는 문제점을 파악하고 상속이 아닌 다른 방법으로 행위 및 알고리즘을 분리하여 구현하도록 해보자 
    - 상속을 통한 구현과 행위 및 알고리즘을 분리해서 구현했을 때의 차이점을 비교해보자.

### TaskController
```java
public class TaskController {
	public static void main(String[] args) {
		TaskController controller = new TaskController();
		controller.process();
	}

	public void process() {
		Product product = getProduct();
		
		System.out.println("[제품명] " + product.getName());
		System.out.println("[가격] " + NumberFormat.getInstance().format(product.getPrice()));
		
		System.out.println();
		ProductOrder usProductOrder = new USProductOrder(product);
		System.out.println("[주무국가] " + usProductOrder.getOrderCountry());
		System.out.println("[관세] " + NumberFormat.getInstance().format(usProductOrder.calculateTax()));
		System.out.println("[환율 적용 가격] " + NumberFormat.getInstance().format(usProductOrder.getCurrency()));
		
		System.out.println();
		ProductOrder jpProductOrder = new JPProductOrder(product);
		System.out.println("[주무국가] " + jpProductOrder.getOrderCountry());
		System.out.println("[관세] " + NumberFormat.getInstance().format(jpProductOrder.calculateTax()));
		System.out.println("[환율 적용 가격] " + NumberFormat.getInstance().format(jpProductOrder.getCurrency()));
		
		System.out.println();
		ProductOrder canProductOrder = new CanProductOrder(product);
		System.out.println("[주무국가] " + canProductOrder.getOrderCountry());
		System.out.println("[관세] " + NumberFormat.getInstance().format(canProductOrder.calculateTax()));
		System.out.println("[환율 적용 가격] " + NumberFormat.getInstance().format(canProductOrder.getCurrency()));
	}
	
	private Product getProduct() {
		Product product = new Product("넥스브로커", 15000000);
		return product;
	}
}
```
### Product
```java
public class Product {
	private String name;
	
	private double price;

	public Product(String name, double price) {
		this.name = name;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}
}
```

### ProductOrder
```java
public abstract class ProductOrder {
	private Product product;

	public ProductOrder(Product product) {
		this.product = product;
	}
	
	public double getPrice() {
		return product.getPrice();
	}
	
	public abstract String getOrderCountry();
	public abstract double calculateTax();
	public abstract double getCurrency();
}

```

### USProductOrder
```java
public class USProductOrder extends ProductOrder{
	private static final String ORDER_COUNTRY = "UNITED STATE";
	
	private static final double TAX_RATE = 0.35;
	
	private static final double CURRENCY_RATE = 0.94;
	
	public USProductOrder(Product product) {
		super(product);
	}
	
	public String getOrderCountry() {
		return ORDER_COUNTRY;
	}
	
	public double calculateTax() {
		return getPrice() * TAX_RATE;
	}

	public double getCurrency() {
		return getPrice() * CURRENCY_RATE;
	}
}

```

### JPProductOrder
```java
public class JPProductOrder extends ProductOrder{
	
	private static final String ORDER_COUNTRY = "JAPAN";
	
	private static final double TAX_RATE = 0.15d;
	
	private static final double CURRENCY_RATE = 1.34;
	
	public JPProductOrder(Product product) {
		super(product);
	}
	
	@Override
	public double calculateTax() {
		return getPrice() * TAX_RATE;
	}

	@Override
	public double getCurrency() {
		return getPrice() * CURRENCY_RATE;
	}

	@Override
	public String getOrderCountry() {
		return ORDER_COUNTRY;
	}
}

```

### CanProductOrder
```java
public class CanProductOrder extends ProductOrder{
	
	private static final String ORDER_COUNTRY = "CANADA";
	
	private static final double TAX_RATE = 0.3d;
	
	private static final double CURRENCY_RATE = 0.94d;
	
	public CanProductOrder(Product product) {
		super(product);
	}
	
	public String getOrderCountry() {
		return ORDER_COUNTRY;
	}
	
	@Override
	public double calculateTax() {
		return getPrice() * TAX_RATE;
	}

	@Override
	public double getCurrency() {
		return getPrice() * CURRENCY_RATE;
	}
}

//실행 결과 
[제품명] 넥스브로커
[가격] 15,000,000

[주무국가] UNITED STATE
[관세] 5,250,000
[환율 적용 가격] 14,100,000

[주무국가] JAPAN
[관세] 2,250,000
[환율 적용 가격] 20,100,000

[주무국가] CANADA
[관세] 4,500,000
[환율 적용 가격] 14,100,000
```

### Is-A (상속 - 밀접한 결합)
* 상속은 다양한 알고리즘과 행위를 지원하는 방법 중 하나이다. 
* 국가별로 서로 다른 행위를 제공하기 위해 ProductOrder 클래스를 직접 상속받아 구현
* 하지만 행위를 처리하는 방법을 직접 코딩(하위 클래스마다 관세 및 환율을 입력하는 것)하게 되면 추후 수정이 어려워지고, 다양하게 알고리즘을 적용할 수 없다. 


![image](https://user-images.githubusercontent.com/87313203/202636430-97020581-72e0-4ac3-ba75-78c7aeaee009.png)
1. 국가별 관세율이나 환율은 하위클래스마다 다르게 적용되어야 하는 부분이다. 하지만, 미국과 캐나다 같은 경우 동일한 US Dollar를 사용하므로 환율이 동일하다. -> 행위를 처리하는 코드가 중복된다. 
2. 미국과 캐나다가 서로 다른 관세를 유지하고 있지만, 차후에 정책이 변경되어 관세마저 동일해진다면, 두 하위 클래스는 이름만 다를뿐 거의 유사해진다.
3. 유럽의 모든 국가가 EU로 통합되면서 FTA를 통해 관세율과 유로화로 화폐를 통합하여 환율이 동일해지면 중복되는 로직을 어떻게 개선할 것인가? 
4. 상품을 주문하는 국가가 늘어날 수록 하위 클래스의 개수가 증가할 것인데 이 부분은 어떻게 관리할 것인가? 

좀 더 효과적으로 시스템을 관리할 수 있는 패턴이 필요하다. 

### Has-A (인터페이스 조립 - 느슨한 결합)
* 행위에 대한 상속 대신 행위 클래스를 생성한 후 ```조합```하는 것이 시스템을 보다 유연하게 만든다.
* ProductOrder클래스의 경우도 행위의 상속으로 인해 코드의 중복이 발생하고 그로 인해 변경에 대한 유연성을 확보하지 못했다.
* Has-A관계는 클래스 내의 알고리즘을 캡슐화할 뿐만 아니라 행위에 대한 인터페이스 구현을 통해서 객테의 행위를 런타임에 동적으로 변경할 수 있다.
* 즉, 상속이 아닌 위임을 통해서 유연성을 확보해야 한다.
* 각각 변화하는 행위 및 알고리즘을 추출하여 인터페이스로 정의 및 구현한 후 객체가 수행중에 그 행위를 변경할 수 있도록 해야 한다.


### 행위 및 알고리즘 분리하기 
Product Order의 가장 핵심 로직은 관세와 환율을 대입하는 것이다. <br>
이를 기준으로 각 로직을 인터페이스로 추출할 수 있다.
![image](https://user-images.githubusercontent.com/87313203/202639799-0b36a716-ff5c-465e-bcb0-f3eb4e8b9bfd.png)
위와 같이 상품 주문 클래스에서 관세 계산과 환율 변환을 인터페이스로 추출하면 상품 주문 클래스와 무관하게 행위 및 알고리즘을 변형시킬 수 있고 <br>
알고리즘을 바꾸거나 이해하거나 확장하기가 쉬워진다.


### 수정된 클래스 다이어 그램 - 기존 코드와 위의 설명, 아래의 클래스 다이어그램을 참고해서 시스템을 수정해보자.... 전체 소스코드는 아래 링크를 확인하자
[소스코드](./src/Strategy/)

![image](https://user-images.githubusercontent.com/87313203/202643416-7da7446e-e514-4286-ae2b-15c67cbda8c1.png)

## 장점 
* 요구사항(전략)이 변경되는 경우 기존 코드를 수정할 필요가 없다. 
* 요구사항이 추가되는 경우 ```확장성```이 아주 용이하다.
* 새로운 전략에 대해서는 새로운 클래스가 관리하기 때문에 OCP의 원칙을 준수한다. 

## 단점 
* 모든 상황에서 전략 패턴의 사용이 유용하지 않다 
    * 컨텍스트에 적용되는 알고리즘이 하나 또는 두개로 적다면 분기를 통한 구현이 더 유용할 수도 있다. 

## 적용 
* 행위들이 조금씩 다를 뿐 개념적으로 관련된 많은 클래스들이 존재하는 경우, 각각의 서로 다른 행위 별로 클래스를 작성
	* 개념에 해당하는 클래스는 하나만 정의 
	* 서로 다른 행위들은 별도의 클래스로 만든다. 
* 알고리즘의 변형이 필요한 경우에 사용할 수 있다. 
	* 기억공간과 처리 속도 간의 절층에 따라 서로 다른 알고리즘을 사용할 수 있다. 
	* 전략 패턴은 이런 문제를 해결하기 위해 알고리즘 자체를 클래스화하고 다양한 알고리즘은 상속 관계로 정의한다. 
* 사용자가 모르는 데이터를 사용해야 하는 알고리즘이 있는 경우 
	* 사용자가 몰라도 되는 복잡한 데이터 구조는 Strategy 클래스에만 둬야 한다.  	 	
* 많은 행위를 정의하기 위해 클래스 안에 복잡한 다중 조건물을 사용해야 하는 경우 
	* 선택문보다 전략 패턴으로 만드는 것이 바람직하다.	 
## REFERENCES 
- https://catsbi.oopy.io/344dbe7b-9774-48fc-9c95-b554e9c1c4bc <br>
- https://kingname.tistory.com/198 <br>
- https://velog.io/@kyle/%EB%94%94%EC%9E%90%EC%9D%B8-%ED%8C%A8%ED%84%B4-%EC%A0%84%EB%9E%B5%ED%8C%A8%ED%84%B4%EC%9D%B4%EB%9E%80 <br>
