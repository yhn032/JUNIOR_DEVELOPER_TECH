package BehaviorPatterns.Strategy;

import java.text.NumberFormat;

public class TaskController {
	public static void main(String[] args) {
		TaskController controller = new TaskController();
		controller.process();
	}

//	public void process() {
//		Product product = getProduct();
//		
//		System.out.println("[제품명] " + product.getName());
//		System.out.println("[가격] " + NumberFormat.getInstance().format(product.getPrice()));
//		
//		System.out.println();
//		ProductOrder usProductOrder = new USProductOrder(product);
//		System.out.println("[주무국가] " + usProductOrder.getOrderCountry());
//		System.out.println("[관세] " + NumberFormat.getInstance().format(usProductOrder.calculateTax()));
//		System.out.println("[환율 적용 가격] " + NumberFormat.getInstance().format(usProductOrder.getCurrency()));
//		
//		System.out.println();
//		ProductOrder jpProductOrder = new JPProductOrder(product);
//		System.out.println("[주무국가] " + jpProductOrder.getOrderCountry());
//		System.out.println("[관세] " + NumberFormat.getInstance().format(jpProductOrder.calculateTax()));
//		System.out.println("[환율 적용 가격] " + NumberFormat.getInstance().format(jpProductOrder.getCurrency()));
//		
//		System.out.println();
//		ProductOrder canProductOrder = new CanProductOrder(product);
//		System.out.println("[주무국가] " + canProductOrder.getOrderCountry());
//		System.out.println("[관세] " + NumberFormat.getInstance().format(canProductOrder.calculateTax()));
//		System.out.println("[환율 적용 가격] " + NumberFormat.getInstance().format(canProductOrder.getCurrency()));
//	}
	
	public void process() {
		Product product = getProduct();
		
		System.out.println("[제품명] " + product.getName());
		System.out.println("[가격] " + NumberFormat.getInstance().format(product.getPrice()));
		
		CurrencyTransformer dollarCurrency = new ValuedCurrencyTransformer("Dollar", 0.94d);
		CurrencyTransformer yenCurrency = new ValuedCurrencyTransformer("Yen", 1.34);
		
		TaxCalculator freeTaxCalc = new FreeTaxCalculator();
		
		System.out.println();
		ProductOrder usProductOrder = new ProductOrder(product, "UNITED STATE");
		usProductOrder.setTaxCalculator(new BaseTaxCalculator(0.35d));
		usProductOrder.setCurrencyTransformer(dollarCurrency);
		System.out.println("[주무국가] " + usProductOrder.getOrderCountry());
		System.out.println("[관세] " + NumberFormat.getInstance().format(usProductOrder.calculateTax()));
		System.out.println("[환율 적용 가격] " + NumberFormat.getInstance().format(usProductOrder.getCurrency()));
		
		System.out.println();
		ProductOrder jpProductOrder = new ProductOrder(product, "JAPAN");
		jpProductOrder.setTaxCalculator(new BaseTaxCalculator(0.15d));
		jpProductOrder.setCurrencyTransformer(yenCurrency);
		System.out.println("[주무국가] " + jpProductOrder.getOrderCountry());
		System.out.println("[관세] " + NumberFormat.getInstance().format(jpProductOrder.calculateTax()));
		System.out.println("[환율 적용 가격] " + NumberFormat.getInstance().format(jpProductOrder.getCurrency()));
		
		System.out.println();
		ProductOrder canProductOrder = new ProductOrder(product, "CANADA");
		canProductOrder.setTaxCalculator(new BaseTaxCalculator(0.3d));
		canProductOrder.setCurrencyTransformer(dollarCurrency);
		System.out.println("[주무국가] " + canProductOrder.getOrderCountry());
		System.out.println("[관세] " + NumberFormat.getInstance().format(canProductOrder.calculateTax()));
		System.out.println("[환율 적용 가격] " + NumberFormat.getInstance().format(canProductOrder.getCurrency()));
	
		System.out.println();
		System.out.println("한미FTA로 인해 자유무역이 확산되어 관세가 없어졌다.");
		usProductOrder.setTaxCalculator(freeTaxCalc);
		System.out.println("[주무국가] " + usProductOrder.getOrderCountry());
		System.out.println("[관세] " + NumberFormat.getInstance().format(usProductOrder.calculateTax()));
		System.out.println("[환율 적용 가격] " + NumberFormat.getInstance().format(usProductOrder.getCurrency()));
	}
	
	private Product getProduct() {
		Product product = new Product("넥스브로커", 15000000);
		return product;
	}
}
