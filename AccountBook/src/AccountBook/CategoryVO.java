package AccountBook;

public class CategoryVO {
	private String inNout;
	private String category;
	
	public CategoryVO(String inNout, String category) {
		super();
		this.inNout = inNout;
		this.category = category;
	}
	public String getInNout() {
		return inNout;
	}
	public void setInNout(String inNout) {
		this.inNout = inNout;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	


}
