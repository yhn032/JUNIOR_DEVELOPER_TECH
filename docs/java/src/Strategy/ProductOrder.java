package BehaviorPatterns.Strategy;

public class ProductOrder {
	
	private TaxCalculator taxCalculator; //관세계산
	private CurrencyTransformer currencyTransformer; //환율변환
	
	private Product product;
	
	private String orderCountry; //주문 국가

	
	public ProductOrder(Product product, String orderCountry) {
		this.product = product;
		this.orderCountry = orderCountry;
	}

	public double getPrice() {
		return product.getPrice();
	}
	
	public void setTaxCalculator(TaxCalculator taxCalculator) {
		this.taxCalculator = taxCalculator;
	}

	public void setCurrencyTransformer(CurrencyTransformer currencyTransformer) {
		this.currencyTransformer = currencyTransformer;
	}
	
	public String getOrderCountry() {
		return orderCountry;
	}

	public double calculateTax() {
		return taxCalculator.calculateTax(getPrice());
	}
	public double getCurrency() {
		return currencyTransformer.transform(getPrice());
	}
}
