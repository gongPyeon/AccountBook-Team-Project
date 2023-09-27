package AccountBook;
import java.util.Scanner;

public class Process3 {
	AccountBookDao dao = new AccountBookDao();
	
	public Process3() {
		//dao.delete_schedule(1);    // 이런 식으로 데이터베이스에서 사용합니다.
		Scanner sc = new Scanner(System.in);
		System.out.println("“년+월”을 입력하세요 >");
		String date = sc.nextLine();
		if(isDateValid(date)) {
			
		}
		System.out.println("--------------------------------------------------------------------------");
		System.out.println("2023 9                수입                지출              내용          인덱스");
		System.out.println("총계                  900,000            300,000                           --");
		
		
	}
	
	
	public boolean isDateValid(String date) {
		
		return true;
	}
}
