package AccountBook;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;
import java.util.Comparator;

//전월 밀린거 계산하는부분 + 단위 밀렸을때 다같이 밀리게
public class Process1 {
	private AccountBookDao dao = new AccountBookDao();
	private Scanner scanner = new Scanner(System.in);
	private Process2 process2;
	private int deleteAccount;
	private ArrayList<AccountBookVO> temp;
	private String date = "";
	private String lastDate = "";
	private String category = "";
	private String firstSqlStr = "";
	private String sqlString = "";
	private ArrayList<String> categoryList = new ArrayList<String>();
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
			System.out.println("3) 항목 수정하기");
			System.out.print("입력> ");
			String str = scanner.nextLine();
			str = str.trim().replaceAll("\\s+", " ");
			str=isValidFor123(str);
			if(str.equals("1")) {
				date = "";
				lastDate = "";
				category = "";
				firstSqlStr = "";
				sqlString = "";
				System.out.println("------------------------------------------------------------");
				inputDate();
			}
			else if(str.equals("2")) {
				System.out.println("------------------------------------------------------------");
				break;
			}
			else if(str.equals("3")) {
				System.out.println("------------------------------------------------------------");
				updateAccount();
				process2 = new Process2();
				dao.deleteAccount(deleteAccount);
				break;
			}
		}
	}

	 private String isValidFor123(String str) {
		 String temp = str;
	      while(true) {
	         if(temp.equals("1")||temp.equals("2")||temp.equals("3")) {
	            return temp;
	         }
				System.out.println("------------------------------------------------------------");
	          System.out.print("> ");
	          scanner = new Scanner(System.in);
	          temp = scanner.next();
	      }
	   }
	 
	   
	   private void isValidInDex(int input) {
	      int flag=0;
	      int tempNum = input;
	      
	      while(true) {
	         for(AccountBookVO vo : temp) {
	            if(vo.getIndexNumber()==tempNum) {
	               flag = 1;
	               break;
	            }
	         }
	         if(flag==1)
	            break;
				System.out.println("------------------------------------------------------------");
	          System.out.print("> ");
	          scanner = new Scanner(System.in);
	          tempNum = scanner.nextInt();
	      }
	   }
	   
	   private void updateAccount() {
	       System.out.println("> 수정할 항목의 인덱스를 입력해주세요");
	       System.out.println("------------------------------------------------------------");
	       System.out.print("> ");
	       int temp=0;
	       while(true) {
	    	   try {
	    		   Scanner scanner = new Scanner(System.in);
	    		   temp = scanner.nextInt();
	    		   break;
			} catch (Exception e) {
				System.out.println("------------------------------------------------------------");
			    System.out.print("> ");
			}
	    	   
	       }
	       isValidInDex(temp);
	       deleteAccount = temp;
	   }
	private void inputDate() {
		int checkDate = 0;
		while(true) {
			date = "";
			lastDate = "";
			category = "";
			System.out.print("\"년+월\" 또는 \"년+월+카테고리\"를 입력하세요. > ");
			date = scanner.nextLine();
			date = date.trim().replaceAll("\\s+", " ");
			
			checkDate = checkDate(date);
			if (checkDate == 1) {
				break;
			}
			else if (checkDate == 2) {
				ArrayList<String> categoryArray = dao.getCategories1();
				boolean isIn = false;
				for(int i=0; i<categoryArray.size();i++) {
					if(categoryArray.get(i).compareTo(category)==0) {
						isIn = true;
						break;
					}
				}
				if(isIn == true)
					break;
				else {
					System.out.println("해당 카테고리가 존재하지 않습니다. 카테고리 목록을 확인해주세요.");
					System.out.println("------------------------------------------------------------");
				}
			}
			else if(checkDate == 3) {
				System.out.println("올바른 논리연산자가 아닙니다. 알맞은 And, Or, Not을 사용해주세요.");
				System.out.println("------------------------------------------------------------");
			}
			else if(checkDate == 4) {
				System.out.println("해당 카테고리가 존재하지 않습니다. 카테고리 목록을 확인해주세요.");
				System.out.println("------------------------------------------------------------");
			}
			else if(checkDate == 5) {
				ArrayList<String> categoryArray = dao.getCategories1();
				boolean isIn = categoryArray.containsAll(categoryList);
				if(isIn == true)
					break;
				else {
					System.out.println("해당 카테고리가 존재하지 않습니다. 카테고리 목록을 확인해주세요.");
					System.out.println("------------------------------------------------------------");
				}
			}
			else {
				System.out.println("1902~2037중에서 년도를 입력하고, 01~12중에서 월을 입력해주세요.");
				System.out.println("------------------------------------------------------------");
			}
		}
		if (checkDate == 1) {
			ArrayList<AccountBookVO> thisMonthArray = dao.getAccountForMonth(date);
			temp = thisMonthArray;
			ArrayList<AccountBookVO> lastMonthArray = null;
			String last = lastMonth(date);
			if (last != "")
				lastMonthArray = dao.getAccountForMonth(lastDate);
			printAccountBook(thisMonthArray, lastMonthArray);
		}
		else if(checkDate == 2) {
			if(category.compareTo("수입")==0||category.compareTo("지출")==0) {
				sqlString = "AND (inNout = '" + category + "')";
				String sql = "SELECT * FROM AccountBook WHERE SUBSTRING(date, 1, 7) = ? AND ( inNout = '"+category+"')";
				ArrayList<AccountBookVO> thisMonthArray = dao.getAccountLogicalCategoryList(date, sql);
				temp = thisMonthArray;
				ArrayList<AccountBookVO> lastMonthArray = null;
				String last = lastMonth(date);
				if (last != "")
					lastMonthArray = dao.getAccountLogicalCategoryList(lastDate, sql);
				printAccountBook(thisMonthArray, lastMonthArray);
			}
			else {
				sqlString = "AND (CONCAT(' ', category, ' ') Like '% "+category+" %')";
				ArrayList<AccountBookVO> thisMonthArray = dao.getAccountCategoryList(date, category);
				temp = thisMonthArray;
				ArrayList<AccountBookVO> lastMonthArray = null;
				String last = lastMonth(date);
				if (last != "")
					lastMonthArray = dao.getAccountCategoryList(lastDate, category);
				printAccountBook(thisMonthArray, lastMonthArray);
			}
		}
		else if(checkDate == 5) {
			ArrayList<AccountBookVO> thisMonthArray = dao.getAccountLogicalCategoryList(date, firstSqlStr+sqlString);
			temp = thisMonthArray;
			ArrayList<AccountBookVO> lastMonthArray = null;
			sqlString = "AND "+sqlString;
			String last = lastMonth(date);
			if (last != "")
				lastMonthArray = dao.getAccountCategoryList(lastDate, category);
				lastMonthArray = dao.getAccountLogicalCategoryList(lastDate, firstSqlStr+sqlString);
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
		categoryList.clear();
		try {
			num1 = Integer.parseInt(parts[0]);

			if(parts[0].length() != 2&&parts[0].length() != 4) {
				return 0;
			}
			if(parts[1].length() > 2)
				return 0;
			num2 = Integer.parseInt(parts[1]);
			
			if((num1 > 37&&num1 < 1902)||(num1 < 0)||(num1 > 2037))
				return 0;
			if(num1<37)
				num1 = num1+2000;
			if(num2 < 1||num2 > 12)
				return 0;
		}
		catch(NumberFormatException e){
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
		else {
			firstSqlStr ="SELECT * FROM AccountBook WHERE SUBSTRING(date, 1, 7) = ? AND "; 
			sqlString = "( ";
			int index = 2;
			if(parts[index].equals("Not")) {
				if(parts.length<index+1)
					return 4;
				if(parts[index+1].equals("수입")||parts[index+1].equals("지출")) {
					sqlString = sqlString + "inNout " + "<> '"+ parts[index+1] + "'";
					index = index + 2;
				}
				else {
					sqlString = sqlString + "CONCAT(' ', category, ' ') " + "Not Like '% "+ parts[index+1] + " %'";
					categoryList.add(parts[index+1]);
					index = index + 2;
				}
			}
			else {
				if(parts[index].equals("수입")||parts[index].equals("지출")) {
					sqlString = sqlString + "inNout " + "= '"+ parts[index] + "'";
					index = index + 1;
				}
				else {
					sqlString = sqlString + "CONCAT(' ', category, ' ') " + "Like '% "+ parts[index] + " %'";
					categoryList.add(parts[index]);
					index = index + 1;
				}
			}
			while(parts.length>index) {
				if(parts[index].equals("And")) {
					sqlString = sqlString + " And ";
					index = index + 1;
					if(parts.length<index+1)
						return 4;
					if(parts[index].equals("Not")) {
						if(parts.length<index+1)
							return 4;
						if(parts[index+1].equals("수입")||parts[index+1].equals("지출")) {
							sqlString = sqlString + "inNout " + "<> '"+ parts[index+1] + "'";
							index = index + 2;
						}
						else {
							sqlString = sqlString + "CONCAT(' ', category, ' ') " + "Not Like '% "+ parts[index+1] + " %'";
							categoryList.add(parts[index+1]);
							index = index + 2;
						}
					}
					else {
						if(parts[index].equals("수입")||parts[index].equals("지출")) {
							sqlString = sqlString + "inNout " + "= '"+ parts[index] + "'";
							index = index + 1;
						}
						else {
							sqlString = sqlString + "CONCAT(' ', category, ' ') " + "Like '% "+ parts[index] + " %'";
							categoryList.add(parts[index]);
							index = index + 1;
						}
					}
				}
				else if(parts[index].equals("Or")) {
					sqlString = sqlString + " Or ";
					index = index + 1;
					if(parts.length<index+1)
						return 4;
					if(parts[index].equals("Not")) {
						if(parts.length<index+1)
							return 4;
						if(parts[index+1].equals("수입")||parts[index+1].equals("지출")) {
							sqlString = sqlString + "inNout " + "<> '"+ parts[index+1] + "'";
							index = index + 2;
						}
						else {
							sqlString = sqlString + "CONCAT(' ', category, ' ') " + "Not Like '% "+ parts[index+1] + " %'";
							categoryList.add(parts[index+1]);
							index = index + 2;
						}
					}
					else {
						if(parts[index].equals("수입")||parts[index].equals("지출")) {
							sqlString = sqlString + "inNout " + "= '"+ parts[index] + "'";
							index = index + 1;
						}
						else {
							sqlString = sqlString + "CONCAT(' ', category, ' ') " + "Like '% "+ parts[index] + " %'";
							categoryList.add(parts[index]);
							index = index + 1;
						}
					}
				}
				else
					return 3;
			}
			sqlString = sqlString + ")";
			System.out.println(sqlString);
			return 5;
		}
	}
	
	private void printAccountBook(ArrayList<AccountBookVO> array, ArrayList<AccountBookVO> lastArray) {
	    long thisMonthSumIn = 0;
	    long thisMonthSumOut = 0;

		System.out.println("------------------------------------------------------------");
	    try {
	    	System.out.println(date.substring(0,5)+Integer.parseInt(date.substring(5,7)) + "\t\t수입\t\t지출\t\t내용\t인덱스\t카테고리");
		}
		catch(NumberFormatException e){
							
		}
	    
	    for (int i = 0; i < array.size(); i++) {
	        if (array.get(i).getInNout().compareTo("수입") == 0) {
	        	thisMonthSumIn += array.get(i).getAmount();
	        } else {
	        	thisMonthSumOut += array.get(i).getAmount();
	        }
	    }
	    long lastMonthSumIn = 0;
	    long lastMonthSumOut = 0;
	    long allLastSum = dao.getTotalAmount(lastDate.replace(" ","-")+"-01",sqlString);
	    if(allLastSum > 0) {
	    	lastMonthSumIn += allLastSum;
	    }
	    else if(allLastSum < 0) {
	    	lastMonthSumOut -= allLastSum;
	    }
	    if(lastArray != null) {
		    for (int i = 0; i < lastArray.size(); i++) {
		        if (lastArray.get(i).getInNout().compareTo("수입") == 0) {
		        	lastMonthSumIn += lastArray.get(i).getAmount();
		        } else {
		        	lastMonthSumOut += lastArray.get(i).getAmount();
		        }
		    }
	    }
	    
	    Comparator<AccountBookVO> dateComparator = new Comparator<AccountBookVO>() {
            @Override
            public int compare(AccountBookVO o1, AccountBookVO o2) {
                // date를 기준으로 비교하여 정렬
            	try {
            		return Integer.parseInt(o2.getDate().substring(8)) - Integer.parseInt(o1.getDate().substring(8));
    			}
    			catch(NumberFormatException e){
    				return 0;			
    			}
            }
        };
        long lastSum = lastMonthSumIn-lastMonthSumOut;
        if(lastSum < 0) {
        	thisMonthSumOut -=lastSum;
        	
        }
        else {
        	thisMonthSumIn +=lastSum;
        }
        // Comparator를 사용하여 ArrayList 정렬
        Collections.sort(array, dateComparator);
        System.out.println("총계\t\t" +  String.format("%,-10d\t",(long)(thisMonthSumIn))+ String.format("%,-10d\t",(long) (thisMonthSumOut)) + "\t--");
	    
	    for (int i = 0; i < array.size(); i++) {
	    	try {
	    		System.out.print(Integer.parseInt(array.get(i).getDate().substring(5,7)));
		        System.out.print("."+Integer.parseInt(array.get(i).getDate().substring(8)));
			}
			catch(NumberFormatException e){
				break;				
			}
	        if (array.get(i).getInNout().compareTo("수입") == 0) {
	            System.out.print("\t\t" +String.format("%,-10d\t\t\t",array.get(i).getAmount()));
	        } else {
	        	System.out.print("\t\t\t\t" +String.format("%,-10d\t",array.get(i).getAmount()));
	        }
	        System.out.print(array.get(i).getDetails());
	        System.out.print("\t" + array.get(i).getIndexNumber());
	        System.out.print("\t" + array.get(i).getCategory().replaceAll(" ", "|"));
	        System.out.println();
	    }
	    if(lastArray != null) {
	    	try {
	    		System.out.println(String.format("%2d",Integer.parseInt(lastDate.substring(5,7))) + "월 이월분\t" + String.format("%,-10d\t",lastMonthSumIn)+ String.format("%,-10d\t",lastMonthSumOut) + "\t--");
	    	}
			catch(NumberFormatException e){
							
			}
		    
	    }
		System.out.println("------------------------------------------------------------");

	}
}
