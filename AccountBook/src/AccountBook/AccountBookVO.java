package AccountBook;

public class AccountBookVO {
	private int indexNumber;
	private String date;
	private String inNout;
	private String category;
	private int amount;
	private String details;
	
	public AccountBookVO(String date, String inNout, String category, int amount, String details) {
		super();
		this.date = date;
		this.inNout = inNout;
		this.category = category;
		this.amount = amount;
		this.details = details;
	}
	
	public AccountBookVO(int indexNumber, String date, String inNout, String category, int amount, String details) {
		super();
		this.indexNumber = indexNumber;
		this.date = date;
		this.inNout = inNout;
		this.category = category;
		this.amount = amount;
		this.details = details;
	}

	public int getIndexNumber() {
		return indexNumber;
	}

	public void setIndexNumber(int indexNumber) {
		this.indexNumber = indexNumber;
	}

	public AccountBookVO() {
		
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
	
	
	
}
