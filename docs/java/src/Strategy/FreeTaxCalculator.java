package BehaviorPatterns.Strategy;

public class FreeTaxCalculator implements TaxCalculator{
	
	//자유 무역으로 관세가 없는 경우에 대한 구현
	
	
	private static final double TAX_FREE = 0d; 
	@Override
	public double calculateTax(double price) {
		
		System.out.println("Tax is Free");
		return TAX_FREE;
	}

}
