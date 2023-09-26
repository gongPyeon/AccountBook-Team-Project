package AccountBook;

public class Process2 {
	AccountBookDao dao = new AccountBookDao();
	
	public Process2() {
		dao.delete_schedule(1);    // 이런 식으로 데이터베이스에서 사용합니다.
	}
}
