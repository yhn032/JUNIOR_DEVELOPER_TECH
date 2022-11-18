package BehaviorPatterns.Strategy;

public class ValuedCurrencyTransformer implements CurrencyTransformer{
	
	private String moneyUnit;
	
	public double currencyRate;
	
	public ValuedCurrencyTransformer(String moneyUnit, double currencyRate) {
		this.moneyUnit = moneyUnit;
		this.currencyRate = currencyRate;
	}




	@Override
	public double transform(double price) {
		// TODO Auto-generated method stub
		System.out.println(moneyUnit + "통화를 환전합니다.");
		return price * currencyRate;
	}

}
