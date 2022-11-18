package CreationalPatterns.Builder;

public class TodayMenu {
	private MenuBuilder builder;
	public TodayMenu() {
		
	}
	
	public void setBuilder(MenuBuilder builder) {
		this.builder = builder;
	}
	
	
	public Object construct() {
		
		builder.makeTitle("11월 27일 식단");
		builder.makeString("아침식단");
		builder.makeItems(new String[] {"토스트","우유","달걀"});
		
		builder.makeString("점심식단");
		builder.makeItems(new String[] {"된장찌개","김치","고등어 조림"});
		
		
		return builder.getResult();
	}
}
