package BehaviorPatterns.Strategy;

public class BaseTaxCalculator implements TaxCalculator{
	
	private double taxRate;
	
	public BaseTaxCalculator(double taxRate) {
		this.taxRate = taxRate;
	}

	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
	}

	@Override
	public double calculateTax(double price) {
		// TODO Auto-generated method stub
		return price * taxRate;
	}

}
