package BehaviorPatterns.Strategy;

public class UnvaluedCurrencyTransformer implements CurrencyTransformer{
	
	
	private static final double UNVALUED_CURRENCY = 0d;

	private String moneyUnit;
	
	
	public UnvaluedCurrencyTransformer(String moneyUnit) {
		this.moneyUnit = moneyUnit;
	}

	
	@Override
	public double transform(double price) {
		// TODO Auto-generated method stub
		System.out.println(moneyUnit + "은/는 환전가치가 없습니다.");
		return UNVALUED_CURRENCY;
	}

}
