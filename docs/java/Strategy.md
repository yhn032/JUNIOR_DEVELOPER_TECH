# 전략패턴 Strategy Pattern

## 정의 
* 실행 중에 <b>알고리즘을 선택</b>할 수 있게 하는 행위 소프트웨어 디자인 패턴
* 여러 알고리즘을 하나의 추상적인 접근점(```Interface```)을 만들어 접근점에서 서로 교환 가능(```Deligate```)하도록 하는 패턴
* 특정 계열의 알고리즘들을 정의하고 각 알고리즘을 캡슐화하여 이 알고리즘들을 해당 계열 안에서 상호 교체가 가능하도록 만든 패턴
* 특정 컨텍스트에서 알고리즘을 별도로 분리하는 설계방법 
    * 특정 기능 수행을 위해 다양한 알고리즘이 적용될 수 있는 경우, 이 다양한 알고리즘을 별도로 분리하는 설계방법 

## 예시

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

## 장점 
* 요구사항(전략)이 변경되는 경우 기존 코드를 수정할 필요가 없다. 
* 요구사항이 추가되는 경우 ```확장성```이 아주 용이하다.
* 새로운 전략에 대해서는 새로운 클래스가 관리하기 때문에 OCP의 원칙을 준수한다. 

## 단점 
* 모든 상황에서 전략 패턴의 사용이 유용하지 않다 
    * 컨텍스트에 적용되는 알고리즘이 하나 또는 두개로 적다면 분기를 통한 구현이 더 유용할 수도 있다. 

## REFERENCES 
- https://catsbi.oopy.io/344dbe7b-9774-48fc-9c95-b554e9c1c4bc <br>
- https://kingname.tistory.com/198 <br>
- https://velog.io/@kyle/%EB%94%94%EC%9E%90%EC%9D%B8-%ED%8C%A8%ED%84%B4-%EC%A0%84%EB%9E%B5%ED%8C%A8%ED%84%B4%EC%9D%B4%EB%9E%80 <br>
