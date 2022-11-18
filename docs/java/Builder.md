# 빌더 패턴(Builder Pattern)

## 정의 
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

## 장점

## 단점 

## 적용
* 복합 객체의 생성 알고리즘이 합성하는 요소와 독립적인 경우
    * 합성하는 요소가 무엇인지는 생성 알고리즘이 관여하지 않는다. 
    * 합성하는 요소가 무엇인지는 차후에 동적으로 결정된다. 
* 합성할 객체들의 표현이 다르더라도 동일한 구축 공정을 가져야 하는 경우

## REFERENCES
