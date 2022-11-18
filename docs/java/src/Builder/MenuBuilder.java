package CreationalPatterns.Builder;

public abstract class MenuBuilder {
	public abstract void makeTitle(String title);
	public abstract void makeString(String str);
	public abstract void makeItems(String[] items);
	public abstract Object getResult();
}
