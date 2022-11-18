package BehaviorPatterns.Strategy;

public class ProtectedTaxCalculator implements TaxCalculator{

	//보호 무역 대상국가에 대한 관세 계산 
	private static final double PROTECTED_TAX = 2d;
	private double taxRate;
	
	public ProtectedTaxCalculator(double taxRate) {
		this.taxRate = taxRate;
	}

	@Override
	public double calculateTax(double price) {
		// TODO Auto-generated method stub
		return price * taxRate * PROTECTED_TAX;
	}
	
}
