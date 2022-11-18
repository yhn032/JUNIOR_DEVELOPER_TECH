# 빌더 패턴(Builder Pattern)

## 정의 
* 도시에 빌딩을 지을때 지반부터 시작해서 전체를 구성하는 각 부분을 만들면서 단계를 밟아가며 만든다. 
* 위처럼 구조를 가진 인스턴스를 쌓아 올리는 방식의 패턴
* 복잡한 객체를 생성하는 방법을 별도의 클래스로 분리하는 패턴
* 서로 다른 구현이라도 동일한 구축 공정을 사용한다. 

## 예시 - 문제를 이해하고 해결해보자 
    Problem
    식당을 운영하는 A시는 매일 식단을 바꾸고 이를 출력하는 프로그램을 만들었다. 
    최근 웹 페이지를 통해 식단을 확인하려는 사람들이 많아져서 식단을 텍스트 포맷으로 
    뿐만 아니라 HTML포맷으로도 만들기로 하고 현재 프로그램을 확장하려고 한다.
    
    현재 Model 
    식단의 구조는 제목, 소제목, 식단 아이템으로 구성된다. 
    TodayMenu는 이를 private 메소드로 나눠서 구성한다. 

### 클래스 다이어그램 
![image](https://user-images.githubusercontent.com/87313203/202651518-fbc22829-720a-4023-9465-18a97f0f5faa.png)

### 소스 코드

#### Client
```java
public class Client {
	public static void main(String[] args) {
		TodayMenu todayMenu = new TodayMenu();
		String result = todayMenu.construct();
		
		System.out.println(result);
	}
}
```
#### TodayMenu
```java
public class TodayMenu {
	private StringBuffer sb = new StringBuffer();
	
	public String construct() {
		makeTitle("11월 27일 식단");
		makeString("아침식단");
		makeItems(new String[] {"토스트", "우유", "달걀"});
		
		makeString("점심식단");
		makeItems(new String[] {
				"된장찌게",
				"김치",
				"고등어 조림"
		});
		
		sb.append("==========================\n");
		return sb.toString();
	}
	
	// 식단 제목
	private void makeTitle(String title) {
		sb.append("==========================\n");
		sb.append("『" + title + "』");
		sb.append("\n");
	}
	
	// 소제목 생성
	private void makeString(String str) {
		sb.append("\n");
		sb.append("■ " + str + "\n");
	}

	// 식단 아이템 생성
	private void makeItems(String[] items) {
		for (int i = 0; i < items.length; i++) {
			sb.append("-" + items[i] + "\n");
		}
	}
}


//실행 결과 
==========================
『11월 27일 식단』

■ 아침식단
-토스트
-우유
-달걀

■ 점심식단
-된장찌게
-김치
-고등어 조림
==========================
```

    Solution 
    결과 객체 생성 패턴 찾기 
    - 모든 출력이 양식에 독립적이려면 무엇을 해야 하는가? 
    - 식단을 출력하는 보편적인 패턴을 추출하자.
    추상화하기
    - 출력양식에 따라 변하지 않는 공통점을 추상화하고 ERD로 표현하기 
    - 공통점 중 구현이 동일한 것과 동일하지 않은 것을 분리하자. 
    - 위 두가지를 정의할 방법을 결정하자.

## 복잡한 객체 생성 패턴 찾아내기 -> (이 문제에선 계층 구조)
- 식단은 식단제목, 소제목, 메뉴로 구성되어 있으며 그 양식에 상관없이 제공되어야 할 기능은 아래와 같다. 
![image](https://user-images.githubusercontent.com/87313203/202653748-62af72b0-48a2-4f76-b44b-c9b07de2b78b.png)

## 생성 패턴 추상화 
- 공통점 중 동일한 행위```제목, 소제목, 아이템 생성```를 하는 것과 동일하지 않은 부분```출력 양식에 따른 출력 방법```을 분리한다. 
- 추상화된 행위와 구체화된 구현을 분리한다.

출력 양식에 상관없이 메뉴를 구성한다. <br>
양식에 따라 결과를 생성하는 것은 빌더에게 위임한다. <br>
추상화 대상(동일한 행위) -> 제목, 소제목, 아이템 생성 <br>
구현 대상(동이하지 않은 행위) -> 출력 양식에 따른 표현법

![image](https://user-images.githubusercontent.com/87313203/202654245-e2c7bafa-cb38-4992-88bb-9ba4d7fbad5e.png)


### 수정된 클래스 다이어 그램 - 기존 코드와 위의 설명, 아래의 클래스 다이어그램을 참고해서 시스템을 수정해보자.... 전체 소스코드는 아래 링크를 확인하자
[소스코드](./src/Builder/)

![image](https://user-images.githubusercontent.com/87313203/202658818-9779c762-bab3-4891-8e43-3137194f26ad.png)

## 참고 
    객체 지향 프로그래밍에서 "누가 무엇을 알고 있을까?"라는 주제는 아주 중요한 주제이다. 
    즉, 어떤 클래스가 어떤 메소드를 사용할 수 있을까?에 주의해서 프로그래밍을 할 필요가 있다. 
    이렇게 말하면 무슨 말인지 와닿지 않지만 결합도에 관련된 이야기다. 
    
    위에서부터 작성한 예제 코드와 시퀀스 다이어그램을 참고해보자. 
    우선 Main클래스 역할을 하는 Client클래스는 실제로 객체를 생성하는 Builder클래스의 메서드를 모르고 호출하지도 않는다. 
    단지, TodayMenu 클래스의 construct() 메서드만을 호출할 뿐이다. 그러면 TodayMenu 클래스 안에서 비즈니스 로직이 수행되고 문서가 완성된다.
    
    다음으로, TodayMenu 클래스는 Builder클래스를 알고 있고 Builder 클래스의 메서드를 사용해서 문서를 구축하지만, 
    자신이 실제로 이용하는 클래스가 Builder 클래스를 구현하는 하위 클래스 중 어떤 것인지 정확하게 모른다. 알 수 없다. 
    
    이처럼 TodayMenu 클래스가 자신이 이용하는 Builder 클래스의 하위 클래스를 모르기 때문에 하위클래스는 교체가 가능하다. 
    이러한 결합을 결합도가 낮다고 표현하는것이다. 
    반대로 TodayMenu 클래스 안에서 Builder가 아닌 하위 클래스 Text~라던지 HTML~을 직접적으로 사용하게 될 경우 강하게 결합이 되어 있기 때문에
    다른 객체로 교체할 수 없게 된다.(아에 못하는 것은 아니지만 기존 코드를 갈아 엎어야 한다는 말이다. 결합도가 낮으면 그럴 필요가 없는데 말이지,,,)
    
    즉, 항상 클래스 간의 결합도를 낮추어서 교환가능성을 높힐 수 있도록 설계를 해야한다.
    

## 적용
* 복합 객체의 생성 알고리즘이 합성하는 요소와 독립적인 경우
    * 합성하는 요소가 무엇인지는 생성 알고리즘이 관여하지 않는다. 
    * 합성하는 요소가 무엇인지는 차후에 동적으로 결정된다. 
* 합성할 객체들의 표현이 다르더라도 동일한 구축 공정을 가져야 하는 경우

## REFERENCES
- https://catsbi.oopy.io/344dbe7b-9774-48fc-9c95-b554e9c1c4bc <br>
- 도서 <[Design Patterns](https://search.shopping.naver.com/book/catalog/33434717659?query=Design%20Patterns&NaPm=ct%3Dlam9r48o%7Cci%3D39231eb0095b6fc421d76bd70d890917f9c5c0a9%7Ctr%3Dboksl%7Csn%3D95694%7Chk%3Defb20863729ae4b5cb88d8c83f3c823572687cea)>
