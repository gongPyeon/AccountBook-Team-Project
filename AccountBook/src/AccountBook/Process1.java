package AccountBook;
import java.util.ArrayList;
import java.util.Scanner;

//전월 밀린거 계산하는부분 + 단위 밀렸을때 다같이 밀리게
public class Process1 {
	private AccountBookDao dao = new AccountBookDao();
	private Scanner scanner = new Scanner(System.in);
	String date = "";
	String lastDate = "";
	String category = "";
	public Process1() {
		run();
	}
	public void run() {
		inputDate();
		isEnd();
	}
	private void isEnd() {
		while(true) {
			System.out.println("1) \"년+월\" 또는 \"년+월+카테고리\"다시 입력하기");
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
		int checkDate = 0;
		while(true) {
			System.out.print("\"년+월\" 또는 \"년+월+카테고리\"를 입력하세요. > ");
			date = scanner.nextLine();
			date = date.trim().replaceAll("\\s+", " ");
			
			checkDate = checkDate(date);
			if (checkDate == 1) {
				break;
			}
			else if (checkDate == 2) {
				ArrayList<CategoryVO> categoryArray = dao.getCategories();
				boolean isIn = false;
				for(int i=0; i<categoryArray.size();i++) {
					if(categoryArray.get(i).getCategory().compareTo(category)==0) {
						isIn = true;
						break;
					}
				}
				if(isIn == true)
					break;
				else
					System.out.println("해당 카테고리가 존재하지 않습니다. 카테고리 목록을 확인해주세요.");
			}
			else {
				System.out.println("1902~2037 사이에서 년도를 입력하고, 01~12 사이에서 월을 입력해주세요.");
			}
		}
		if (checkDate == 1) {
			ArrayList<AccountBookVO> thisMonthArray = dao.getAccountMonthList(date);
			ArrayList<AccountBookVO> lastMonthArray = null;
			String last = lastMonth(date);
			if (last != "")
				lastMonthArray = dao.getAccountMonthList(lastDate);
			printAccountBook(thisMonthArray, lastMonthArray);
		}
		else if(checkDate == 2) {
			ArrayList<AccountBookVO> thisMonthArray = dao.getAccountCategoryList(date, category);
			ArrayList<AccountBookVO> lastMonthArray = null;
			String last = lastMonth(date);
			if (last != "")
				lastMonthArray = dao.getAccountCategoryList(lastDate, category);
			printAccountBook(thisMonthArray, lastMonthArray);
		}
	}
	private String lastMonth(String thisMonth) {
		this.lastDate = "";
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
	private int checkDate(String date) {
		this.date = "";
		String[] parts = date.split(" ");
		int num1 = 0;
		int num2 = 0;
		if (parts.length == 2 || parts.length == 3) {
			try {
				num1 = Integer.parseInt(parts[0]);
				if(num1>=10) {
					
					if (!parts[0].equals(Integer.toString(num1))) {
						return 0;
					}
				}
				else {
					if (!parts[0].equals("0"+Integer.toString(num1))) {
						return 0;
					}
				}
				num2 = Integer.parseInt(parts[1]);
				if(num2>=10) {
					if (!parts[1].equals(Integer.toString(num2))){
						return 0;
					}
				}
				else {
					if (!parts[1].equals("0" + Integer.toString(num2))){
						return 0;
					}
				}
					
				if((num1 > 37&&num1 < 1902)||(num1 < 1)||(num1 > 2037))
					return 0;
				if(num1<37)
					num1 = num1+2000;
				if(num2 < 1||num2 > 12)
					return 0;
			}
			catch(NumberFormatException e){
				return 0;				
			}
		}
		else {
			return 0;
		}
		if(num2<10)
			this.date = String.valueOf(num1) + " 0" + String.valueOf(num2);
		else
			this.date = String.valueOf(num1) + " " + String.valueOf(num2);
		
		if(parts.length == 2)
			return 1;
		else if(parts.length == 3) {
			category = parts[2];
			return 2;
		}
		else
			return 0;
	}
	private void printAccountBook(ArrayList<AccountBookVO> array, ArrayList<AccountBookVO> lastArray) {
	    int thisMonthSumIn = 0;
	    int thisMonthSumOut = 0;

	    System.out.println("------------------------------------------------------------");
	    System.out.println(date + "\t\t수입\t\t수출\t\t내용\t인덱스");
	    
	    for (int i = 0; i < array.size(); i++) {
	        if (array.get(i).getInNout().compareTo("수입") == 0) {
	        	thisMonthSumIn += array.get(i).getAmount();
	        } else {
	        	thisMonthSumOut += array.get(i).getAmount();
	        }
	    }
	    int lastMonthSumIn = 0;
	    int lastMonthSumOut = 0;
	    if(lastArray != null) {
		    for (int i = 0; i < lastArray.size(); i++) {
		        if (lastArray.get(i).getInNout().compareTo("수입") == 0) {
		        	lastMonthSumIn += lastArray.get(i).getAmount();
		        } else {
		        	lastMonthSumOut += lastArray.get(i).getAmount();
		        }
		    }
	    }
	    System.out.println("총계\t\t" + String.format("%,-10d\t",thisMonthSumIn+lastMonthSumIn)+ String.format("%,-10d\t",thisMonthSumOut+lastMonthSumOut) + "\t--");
	    
	    for (int i = 0; i < array.size(); i++) {
	        System.out.print(array.get(i).getDate().substring(5));
	        System.out.print("\t" + array.get(i).getCategory());

	        if (array.get(i).getInNout().compareTo("수입") == 0) {
	            System.out.print("\t" +String.format("%,-10d\t\t\t",array.get(i).getAmount()));
	        } else {
	        	System.out.print("\t\t\t" +String.format("%,-10d\t",array.get(i).getAmount()));
	        }
	        System.out.print(array.get(i).getDetails());
	        System.out.print("\t" + array.get(i).getIndexNumber());
	        System.out.println();
	    }
	    if(lastArray != null) {
	    	
		    System.out.println(lastDate.substring(5,7)+"월 이월분\t" + String.format("%,-10d\t",lastMonthSumIn)+ String.format("%,-10d\t",lastMonthSumOut) + "\t--\n");
		    
	    }
	    System.out.println("------------------------------------------------------------");

	}
}