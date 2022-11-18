# 어댑터 패턴(Adapter Pattern) - ```객체를 위한 인터페이스```

## 정의 
* 110v 플러그를 220v에서 사용하기 위해서 사용하는 중간 단자와 같은 개념이다. 
* 클래스의 인터페이스를 사용자가 기대하는 다른 인터페이스로 변환하는 패턴
* 호환성이 없는 인터페이스 때문에 함께 동작할 수 없는 클래스들이 함께 동작할 수 있도록 해준다. 
* 클라이언트의 요구 타입과 반환 타입이 달라도, 중간에 어댑터를 둠으로써 적절히 가공하여 둘을 연결해준다.

## 예시
### 클래스 다이어그램 
![image](https://user-images.githubusercontent.com/87313203/202624269-056c5058-22c2-41d4-9bc9-707ad8de5705.png)

### 소스 코드 
```java
//기존 코드 
public class Math {
	public static double twoTime(double sum) {
		return sum * 2;
	}
	
	public static double half(double sum) {
		return sum / 2;
	}
}


public interface Adapter {
  //추가적인 요구사항(인자 타입, 개수 등이 바뀔 수 있다.)
	public Float twice(Float f);
	public Float halfof(Float f);
}

//메인 함수를 변경하지 않고 변경된 요구사항 충족
public class AdapterImpl implements Adapter {

	@Override
	public Float twice(Float f) {
		// TODO Auto-generated method stub
		return (float) Math.twoTime(f);
	}

	@Override
	public Float halfof(Float f) {
		// TODO Auto-generated method stub
		return (float)Math.half(f);
	}

}
```

## 장점 
* 연관없는 두 객체를 연결해서 원하는 요구사항을 수용한다면 생산성이 높아질 수 있다.
* 요구사항마다 새로운 알고리즘을 만들거나 인자의 타입별로 비슷한 알고리즘을 새로 구현할 필요없이 
* 기존의 알고리즘을 변형해 재활용할 수 있다.
    * 예를 들어, 리스트 컬렉션을 버블 정렬해야 하는 요구사항이 추가되었는데, 나에게 배열을 기준으로 버블 정렬을 구현한 알고리즘이 존재한다면 
    * 리스트 컬렉션을 이용한 버블정렬을 새로이 구현할 필요없이 리스트를 배열로 만든 후에 기존에 존재하는 알고리즘을 적용시킨 후 
    * 다시 리스트로 변환시켜서 반환하는것이 효율적이다. 
