package AccountBook;

public class Process5 {
	AccountBookDao dao = new AccountBookDao();
	
	public Process5() {
		dao.delete_schedule(1);    // 이런 식으로 데이터베이스에서 사용합니다.
	}
}
