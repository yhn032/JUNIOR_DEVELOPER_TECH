package CreationalPatterns.Builder;

public class TextMenuBuilder extends MenuBuilder{
	
	private StringBuilder sb = new StringBuilder();
	
	@Override
	public void makeTitle(String title) {
		// TODO Auto-generated method stub
		sb.append("==========================\n");
		sb.append("『" + title + "』");
		sb.append("\n");
	}

	@Override
	public void makeString(String str) {
		// TODO Auto-generated method stub
		sb.append("\n");
		sb.append("■ " + str + "\n");
	}

	@Override
	public void makeItems(String[] items) {
		// TODO Auto-generated method stub
		for (int i = 0; i < items.length; i++) {
			sb.append("-" + items[i] + "\n");
		}
	}

	@Override
	public Object getResult() {
		// TODO Auto-generated method stub
		sb.append("==========================\n");
		return sb.toString();
	}

}
