package AccountBook;

public abstract class TestMain {

	public static void main(String[] args) {
		AccountBookDao dao  = new AccountBookDao();
		dao.delete_schedule(1);
		MainProgram main = new MainProgram();

	}

}
