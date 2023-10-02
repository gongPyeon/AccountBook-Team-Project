package AccountBook;
import java.util.ArrayList;
import java.text.DecimalFormat;
import java.util.Scanner;

//전월 밀린거 계산하는부분 + 단위 밀렸을때 다같이 밀리게
public class Process1 {
	AccountBookDao dao = new AccountBookDao();
	Scanner scanner = new Scanner(System.in);
	String date = "";
	String lastDate = "";
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
			if(str.equals("1"))
				inputDate();
			else if(str.equals("2"))
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
		ArrayList<AccountBookVO> thisMonthArray = dao.getAccountList(date);
		ArrayList<AccountBookVO> lastMonthArray = null;
		String last = lastMonth(date);
		if (last != "")
			lastMonthArray = dao.getAccountList(lastDate);
		printAccountBook(thisMonthArray, lastMonthArray);
	}
	private String lastMonth(String thisMonth) {
		String[] parts = thisMonth.split(" ");
		int num1 = 0;
		int num2 = 0;
		try {
			num1 = Integer.parseInt(parts[0]);
			num2 = Integer.parseInt(parts[1]);
			if(num2==1) {
				if(num1 == 1902) {
					return "";
				}
				else {
					num1 = num1 - 1;
					num2 = 12;
					this.lastDate = String.valueOf(num1) + " " + String.valueOf(num2);
					return this.lastDate;
				}
				
			}
			else {
				num2 = num2-1;
				if(num2<10)
					this.lastDate = String.valueOf(num1) + " 0" + String.valueOf(num2);
				else
					this.lastDate = String.valueOf(num1) + " " + String.valueOf(num2);
				return this.lastDate;
				
			}

			
		}
		catch(NumberFormatException e){
			return "";				
		}
		
	}
	private boolean checkDate(String date) {
		String[] parts = date.split(" ");
		int num1 = 0;
		int num2 = 0;
		if (parts.length == 2) {
			try {
				num1 = Integer.parseInt(parts[0]);
				if(num1>=10) {
					
					if (!parts[0].equals(Integer.toString(num1))) {
						return false;
					}
				}
				else {
					if (!parts[0].equals("0"+Integer.toString(num1))) {
						return false;
					}
				}
				num2 = Integer.parseInt(parts[1]);
				if(num2>=10) {
					if (!parts[1].equals(Integer.toString(num2))){
						return false;
					}
				}
				else {
					if (!parts[1].equals("0" + Integer.toString(num2))){
						return false;
					}
				}
					
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
	private void printAccountBook(ArrayList<AccountBookVO> array, ArrayList<AccountBookVO> lastArray) {
	    int thisMonthSumIn = 0;
	    int thisMonthSumOut = 0;
	    DecimalFormat decimalFormat = new DecimalFormat("#,###");

	    System.out.println("------------------------------------------------------------");
	    System.out.println(date + "\t\t수입\t\t수출\t\t내용\t인덱스");
	    
	    for (int i = 0; i < array.size(); i++) {
	        if (array.get(i).getInNout().compareTo("수입") == 0) {
	        	thisMonthSumIn += array.get(i).getAmount();
	        } else {
	        	thisMonthSumOut += array.get(i).getAmount();
	        }
	    }

	    System.out.println("총계\t\t" + decimalFormat.format(thisMonthSumIn) + "\t\t" + decimalFormat.format(thisMonthSumOut) + "\t\t\t--\n");

	    for (int i = 0; i < array.size(); i++) {
	        System.out.print(array.get(i).getDate().substring(5));
	        System.out.print("\t" + array.get(i).getCategory());

	        if (array.get(i).getInNout().compareTo("수입") == 0) {
	            System.out.print("\t" + decimalFormat.format(array.get(i).getAmount()) + "\t\t");
	        } else {
	            System.out.print("\t\t\t" + decimalFormat.format(array.get(i).getAmount()));
	        }
	        System.out.print("\t\t" + array.get(i).getDetails());
	        System.out.print("\t" + array.get(i).getIndexNumber());
	        System.out.println();
	    }
	    if(lastArray != null) {
	    	int lastMonthSumIn = 0;
		    int lastMonthSumOut = 0;

		    for (int i = 0; i < lastArray.size(); i++) {
		        if (lastArray.get(i).getInNout().compareTo("수입") == 0) {
		        	lastMonthSumIn += array.get(i).getAmount();
		        } else {
		        	lastMonthSumOut += array.get(i).getAmount();
		        }
		    }
		    
		    System.out.println(lastDate.substring(5,7)+"월 이월분\t" + decimalFormat.format(lastMonthSumIn) + "\t\t" + decimalFormat.format(lastMonthSumOut) + "\t\t\t--\n");
	    }
	    System.out.println("------------------------------------------------------------");

	}
}
