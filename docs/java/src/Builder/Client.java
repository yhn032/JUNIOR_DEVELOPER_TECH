package CreationalPatterns.Builder;

public class Client {
	public static void main(String[] args) {
		TodayMenu todayMenu = new TodayMenu();
		
		todayMenu.setBuilder(new TextMenuBuilder());
		String txtResult = (String)todayMenu.construct();
		System.out.println(txtResult);
		
		System.out.println();
		
		todayMenu.setBuilder(new HtmlMenuBuilder());
		String htmlResult = (String)todayMenu.construct();
		System.out.println("메뉴가 작성되었습니다. (" + htmlResult + ")");
	}
}
