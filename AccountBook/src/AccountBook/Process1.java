package AccountBook;
import java.util.ArrayList;
import java.text.DecimalFormat;
import java.util.Scanner;

public class Process1 {
	AccountBookDao dao = new AccountBookDao();
	Scanner scanner = new Scanner(System.in);
	String date = "";
	public Process1() {
		run();
	}
	public void run() {
		inputDate();
		while(true) {
			System.out.println("1) \"년+월\"다시 입력하기");
			System.out.println("2)  메인화면으로 돌아가기");
			System.out.print("입력> ");
			String str = scanner.nextLine();
			str = str.trim().replaceAll("\\s+", " ");
			int num= checkNum(str);
			if(num==1)
				inputDate();
			else if(num==2)
				break;
//			else
//				System.out.println("");
		}
	}
	private void inputDate() {
		while(true) {
			System.out.print("\"년+월\"을 입력하세요. > ");
			date = scanner.nextLine();
			date = date.trim().replaceAll("\\s+", " ");
			
			if (checkDate(date) == true) {
				break;
			}
			else {
				System.out.println("1902~2037 사이에서 년도를 입력하고, 01~12 사이에서 월을 입력해주세요.");
			}
		}
		ArrayList<AccountBookVO> array = dao.getAccountList(date);
		printAccountBook(array);
	}
	private boolean checkDate(String date) {
		String[] parts = date.split(" ");
		int num1 = 0;
		int num2 = 0;
		if (parts.length == 2) {
			try {
				num1 = Integer.parseInt(parts[0]);
				num2 = Integer.parseInt(parts[1]);
				if((num1 > 37&&num1 < 1902)||(num1 < 1)||(num1 > 2037))
					return false;
				if(num1<37)
					num1 = num1+2000;
				if(num2 < 1||num2 > 12)
					return false;
			}
			catch(NumberFormatException e){
				return false;				
			}
		}
		else {
			return false;
		}
		if(num2<10)
			this.date = String.valueOf(num1) + " 0" + String.valueOf(num2);
		else
			this.date = String.valueOf(num1) + " " + String.valueOf(num2);
		return true;
	}
	private void printAccountBook(ArrayList<AccountBookVO> array) {
	    int sumIn = 0;
	    int sumOut = 0;
	    DecimalFormat decimalFormat = new DecimalFormat("#,###");

	    System.out.println("--------------------------------------------");
	    System.out.println(date + "\t\t수입\t수출\t내용\t인덱스");
	    
	    for (int i = 0; i < array.size(); i++) {
	        if (array.get(i).getInNout().compareTo("수입") == 0) {
	            sumIn += array.get(i).getAmount();
	        } else {
	            sumOut += array.get(i).getAmount();
	        }
	    }

	    System.out.println("총계\t\t" + decimalFormat.format(sumIn) + "\t" + decimalFormat.format(sumOut) + "\t\t--\n");

	    for (int i = 0; i < array.size(); i++) {
	        System.out.print(array.get(i).getDate().substring(5));
	        System.out.print("\t" + array.get(i).getCategory());

	        if (array.get(i).getInNout().compareTo("수입") == 0) {
	            System.out.print("\t" + decimalFormat.format(array.get(i).getAmount()) + "\t");
	            sumIn += array.get(i).getAmount();
	        } else {
	            System.out.print("\t\t" + decimalFormat.format(array.get(i).getAmount()));
	            sumOut += array.get(i).getAmount();
	        }
	        System.out.print("\t" + array.get(i).getDetails());
	        System.out.print("\t" + array.get(i).getIndexNumber());
	        System.out.println();
	    }

	    System.out.println("--------------------------------------------");
	    System.out.println("총계\t\t" + decimalFormat.format(sumIn) + "\t" + decimalFormat.format(sumOut) + "\t\t--\n");
	}
	private int checkNum(String str) {
		try {
			int num = Integer.parseInt(str);
			return num;
		}
		catch(NumberFormatException e){
			return 0;				
		}
	}
}
