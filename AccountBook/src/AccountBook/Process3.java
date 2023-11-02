package AccountBook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Process3 {
	AccountBookDao dao = new AccountBookDao();
	ArrayList<String> availableCategories = dao.getCategories1();//저장된 카테고리 항목들
	ArrayList<AccountBookVO> accountList;
	ArrayList<AccountBookVO> LASTaccountList;
	ArrayList<AccountBookVO> filteredList;
	ArrayList<AccountBookVO> filteredList2;
	String abbreviationMonth;
	String year;
	String month;
	String category;
	boolean categoryIn = false; //카테고리도 입력으로 넣은 경우
	int previousMonth;
	int totalIncome;
	int totalOutflow;
	int total;
	String previous; // 00 이월분 표시의 00
	String inputToString;
	String date;
	String lastDate = "";
	String modifiedDate;
	Scanner sc = new Scanner(System.in);
	boolean addtionalDelete = false;
	public Process3() {
		//기본 카테고리 + 사용자가 등록한 카테고리 "저장된 카테고리 항목들"에 추가

		// Add default categories for "수입" (Income)
		String[] defaultIncomeCategories = {"월급", "부수입", "용돈", "상여", "금융소득"};
		availableCategories.addAll(Arrays.asList(defaultIncomeCategories));

		// Add default categories for "지출" (Expense)
		String[] defaultExpenseCategories = {"식비", "문화생활", "경조사/회비", "주거/통신", "교통/차량"};
		availableCategories.addAll(Arrays.asList(defaultExpenseCategories));

		// Fetch and add user-added categories from the database using dao.getCategories1()
		ArrayList<String> userCategories = dao.getCategories1();
		availableCategories.addAll(userCategories);
		
		

		insertYearMonth();
			while (true) {
				if (addtionalDelete == true) {
					insertYearMonth();
					addtionalDelete = false;
				}
				if (!categoryIn) {//카테고리가 입력되지 않은 경우 
					accountList = dao.getAccountForMonth(date);
					String[] arr = date.split(" ");
					year = arr[0];
					month = arr[1];
					if (month.length() == 1)
						month = "0" + month;
					abbreviationMonth = monthAbbreviation(month); // 09월이면 그냥 9로 바꾸고 12월이면 그냥 12로 유지하는 함수
					if (year.length() == 2) {
						year = "20" + year;
					}
					modifiedDate = year + " " + month;// 반드시 2023 04 형태임
					previousMonth = previousMonthMoney(modifiedDate);
				
					totalIncome = getTotalIncome(modifiedDate);
					totalOutflow = getTotalOutflow(modifiedDate);
					previous = getPrevious(modifiedDate);
					if (accountList.size() != 0) { // 가져온 내역이 있을 때
						showCurrentAccount(modifiedDate);
						
						int input;
						while (true) {
							System.out.print("삭제할 인덱스를 입력해주세요> ");
							inputToString = sc.nextLine();
							if (!isValidIndex(inputToString)) {
								System.out.println("유효하지 않은 인덱스입니다");
								System.out.println("---------------------------------------------------");
							} else {
								break;
							}
						}
						input = Integer.parseInt(inputToString);
						dao.deleteAccount(input);
						System.out.println("---------------------------------------------------");
						System.out.println("삭제가 완료되었습니다");
						System.out.println("---------------------------------------------------");
						
						int temp = 0;
						String tempInput;
						if (accountList.size() != 0) {
							while(true) {
							showCurrentAccount(modifiedDate);
							System.out.println("1) 추가 삭제 ");
							System.out.println("2) 메인화면으로 돌아가기");
							System.out.print("입력> ");
							tempInput = sc.nextLine();
							System.out.println("---------------------------------------------------");
							
							if(validFor1or2(tempInput)) {
								temp = Integer.parseInt(tempInput.trim());
								break;
								}
							
							System.out.println("유효하지 않은 숫자를 입력하셨습니다");
							System.out.println("---------------------------------------------------");
							
							if (temp == 1) {
								//addtionalDelete = true;
								
								break;
							
							} else if (temp == 2)
								break;
						}
						if (temp == 2) // 메인화면으로 돌아가기
							break;

					
					}}
						else { // 아무것도 가져온게 없을 때
						showCurrentAccount(modifiedDate);
						System.out.println("삭제 가능한 항목이 없습니다.");
						System.out.println("---------------------------------------------------");

						int temp2;
						String input;
						while (true) {
							while(true) {
							//System.out.println("---------------------------------------------------");
							System.out.println("1) “년+월” 또는 “년+월+카테고리” 다시 입력하기");
							System.out.println("2) 메인화면으로 돌아가기");
							System.out.print("입력> ");
							input = sc.nextLine();
							System.out.println("---------------------------------------------------");

							//System.out.println("---------------------------------------------------");

							if(validFor1or2(input)) {
								temp2 = Integer.parseInt(input.trim());
								break;
								}
							}
							if (temp2 == 1) {
								addtionalDelete = true;
								System.out.println("---------------------------------------------------");
								break;
							}
							else if (temp2 == 2) {
								System.out.println("---------------------------------------------------");
								break;

							}
							System.out.println("---------------------------------------------------");
						}
						if (temp2 == 2) // 메인화면으로 돌아가기
							break;
					}}
				
				else {//카테고리 입력 받은 경우 (유효한 카테고리 여부와 관계 없음)
					
					accountList = dao.getAccountForMonth(date);
					String[] arr = date.split(" ");
					year = arr[0];
					month = arr[1];
					if (month.length() ==1)
						month = "0"+month;
					category = arr[2];//카테고리 
					abbreviationMonth = monthAbbreviation(month); // 09월이면 그냥 9로 바꾸고 12월이면 그냥 12로 유지하는 함수
					if (year.length() == 2) {
						year = "20" + year;
					}
					String modifiedDate = year + " " + month;// 반드시 2023 04 형태임
					previousMonth = previousMonthMoney(modifiedDate);
				
					totalIncome = getTotalIncome(modifiedDate);
					totalOutflow = getTotalOutflow(modifiedDate);
					previous = getPrevious(modifiedDate);
					int categorysize = 0;
					for (AccountBookVO e : accountList) {
				        if (e.getCategory().equals(category)) {
				        	categorysize++;
				        }
				    }
					if (categorysize != 0) { // 가져온 내역이 있을 때
						showCurrentAccount2(modifiedDate,category);
						
						int input;
						while (true) {
							System.out.print("삭제할 인덱스를 입력해주세요> ");
							inputToString = sc.nextLine();
							if (!isValidIndex(inputToString)) {
								System.out.println("유효하지 않은 인덱스입니다");
								System.out.println("---------------------------------------------------");
							} else {
								break;
							}
						}
						input = Integer.parseInt(inputToString);
						dao.deleteAccount(input);
						System.out.println("---------------------------------------------------");
						System.out.println("삭제가 완료되었습니다");
						System.out.println("---------------------------------------------------");
						
						int temp = 0;
						String tempInput;
						categorysize = 0;
						for (AccountBookVO e : accountList) {
					        if (e.getCategory().equals(category)) {
					        	categorysize++;
					        }
					    }
						if (categorysize != 0) {
							while(true) {
								try {
									showCurrentAccount2(modifiedDate,category);
								}
								catch(Exception e)
								{
									showCurrentAccount(modifiedDate);

								}
							System.out.println("1) 추가 삭제 ");
							System.out.println("2) 메인화면으로 돌아가기");
							System.out.print("입력> ");
							tempInput = sc.nextLine();
							System.out.println("---------------------------------------------------");
							
							if(validFor1or2(tempInput)) {
								temp = Integer.parseInt(tempInput.trim());
								break;
								}
							
							System.out.println("유효하지 않은 숫자를 입력하셨습니다");
							System.out.println("---------------------------------------------------");
							
							if (temp == 1) {
								//addtionalDelete = true;
								
								break;
							
							} else if (temp == 2)
								break;
						}
						if (temp == 2) // 메인화면으로 돌아가기
							break;

					
					}}
						else { // 아무것도 가져온게 없을 때
							try {
								showCurrentAccount2(modifiedDate,category);
							}
							catch(Exception e)
							{
								showCurrentAccount(modifiedDate);

							}
						System.out.println("삭제 가능한 항목이 없습니다.");
						System.out.println("---------------------------------------------------");

						int temp2;
						String input;
						while (true) {
							while(true) {
							//System.out.println("---------------------------------------------------");
							System.out.println("1) “년+월” 또는 “년+월+카테고리” 다시 입력하기");
							System.out.println("2) 메인화면으로 돌아가기");
							System.out.print("입력> ");
							input = sc.nextLine();
							System.out.println("---------------------------------------------------");

							//System.out.println("---------------------------------------------------");

							if(validFor1or2(input)) {
								temp2 = Integer.parseInt(input.trim());
								break;
								}
							}
							if (temp2 == 1) {
								addtionalDelete = true;
								System.out.println("---------------------------------------------------");
								break;
							}
							else if (temp2 == 2) {
								System.out.println("---------------------------------------------------");
								break;

							}
							System.out.println("---------------------------------------------------");
						}
						if (temp2 == 2) // 메인화면으로 돌아가기
							break;
					}}
			}
	}

	public boolean isDateValid(String date) {
		int spaceCount = 0;
		int space = 0;// 공백(년도와 월을 구분해주는)의 인덱스
		for (int i = 0; i < date.length(); i++) {
			if (date.charAt(i) == ' ') {
				space = i;
				spaceCount++;
			}

		}
		
		if (spaceCount == 2){//공백 개수가 2인 경우 = 카테고리도 받음
			String yearAndDay = date.substring(0,space);
			String category = date.substring(space+1, date.length());
			
			//System.out.println(yearAndDay);
			//System.out.println(category);
			
			// 년도 범위 valid 체크
			for (int i = 0; i < yearAndDay.length(); i++) {
				if (date.charAt(i) == ' ') {
					space = i;
					
				}

			}
			String year = yearAndDay.substring(0, space);
			//System.out.println(year);
			if (year.length() == 2)// 보완필요 (1) isDateValid 함수내에서 년도 + 20 혹은 년도 + 19인지 판별 어떻게
									// 이거 2자리일때는 무조건 20을 붙인다고 기획서에 되어있답니다.
			{
				try {
		            int temp = Integer.parseInt(year);
		            
		            temp = 2000 + temp ;
		            year = Integer.toString(temp);
		        } catch (NumberFormatException e) {
		            return false; // Non-numeric year
		        }
				
			} 
			else if (year.length() == 4) {
				try {
		            int temp = Integer.parseInt(year);
		            if (temp < 1902 || temp > 2037) {
		                return false;
		            }
		            year = Integer.toString(temp);

		        } catch (NumberFormatException e) {
		            return false; // Non-numeric year
		        }
			}	
			  else if (year.length() !=4){
				//System.out.println("1902~2037 중에서 년도를 입력하고, 01~12 중에서 월을 입력해주세요");
				//System.out.println("---------------------------------------------------");

				return false;
			}
			// 월 범위 valid 체크
			String month = yearAndDay.substring(space + 1, yearAndDay.length());
			//System.out.println(month);
			char[] monthChar = month.toCharArray();
			if(monthChar.length>2) {
				//System.out.println("1902~2037 중에서 년도를 입력하고, 01~12 중에서 월을 입력해주세요");
				//System.out.println("---------------------------------------------------");

				return false;
			}
			if (monthChar.length == 2 && monthChar[0] == '0')
				month = String.valueOf(monthChar[1]);
			else if (monthChar.length == 2 && monthChar[0] != '0')
				month = "" + month;
			if (!monthIsInRange(month)) {
				//System.out.println("1902~2037 중에서 년도를 입력하고, 01~12 중에서 월을 입력해주세요");
				//System.out.println("---------------------------------------------------");

				return false;
			}
				
			//카테고리 존재여부
			if (!availableCategories.contains(category)) {
			    System.out.println("해당 카테고리가 존재하지 않습니다. 등록된 카테고리명을 입력해주세요.");
			    return false;
			}

			
			//위의 조건들을 다 만족하는 경우, true 반환
			categoryIn = true;
			return true;
			
		}
		if (spaceCount == 1) {//공백 개수가 1인 경우 = 카테고리 받지 않음
			// 년도 범위 valid 체크
			String year = date.substring(0, space);
			if (year.length() == 2)// 보완필요 (1) isDateValid 함수내에서 년도 + 20 혹은 년도 + 19인지 판별 어떻게
									// 이거 2자리일때는 무조건 20을 붙인다고 기획서에 되어있답니다.
			{
				try {
		            int temp = Integer.parseInt(year);
		            
		            temp = 2000 + temp ;
		            year = Integer.toString(temp);

		        } catch (NumberFormatException e) {
		            return false; // Non-numeric year
		        }
			} else if (year.length() == 4) {
				try {
		            int temp = Integer.parseInt(year);
		            if (temp < 1902 || temp > 2037) {
		                return false;
		            }
		            year = Integer.toString(temp);

		        } catch (NumberFormatException e) {
		            return false; // Non-numeric year
		        }
			}	
			  else
				return false;
			// 월 범위 valid 체크
			String month = date.substring(space + 1, date.length());
			char[] monthChar = month.toCharArray();
			if(monthChar.length>2) {
				return false;
			}
			if (monthChar.length == 2 && monthChar[0] == '0')
				month = String.valueOf(monthChar[1]);
			else if (monthChar.length == 2 && monthChar[0] != '0')
				month = "" + month;
			if (!monthIsInRange(month))
				return false;
			else 
				return true;
		}
		else //공백 개수가 1,2도 아닌 경우들
		{
			return false;
		}
	}
	

	public boolean monthIsInRange(String month)// isDateValid함수 내에서 월의 범위가 유효한지 검사하는 함수
	{
		try {
			int m = Integer.parseInt(month);
            if (m < 1 || m > 12)
                return false;
            else if (m >= 1 && m <= 12)
    			return true;
            else
    			return false;
        } catch (NumberFormatException e) {
            return false; // Non-numeric year
        }
		
	}

	public String monthAbbreviation(String input) {
		char[] arr = input.toCharArray();

		if (arr[0] == '0') {
			return Character.toString(arr[1]);
		} else {
			return input;
		}
	}

	public int getTotalIncome(String date) {// date = 2023 03
		// 총계에서 수입 구하는 함수
		// 전월에 대한 월과 년도 정하기
		int currentMonthNum = Integer.parseInt(date.substring(5, date.length()));
		int currentYearNum = Integer.parseInt(date.substring(0, 4));
		int previousMonthNum = 0;
		int previousYearNum = currentYearNum;
		String previousDate = "";
		int result = 0;// return 하는 값

		if (currentMonthNum > 1)
			previousMonthNum = currentMonthNum - 1;
		else// 년도가 바뀌는 경우
		{
			previousMonthNum = 12;
			previousYearNum = currentYearNum - 1;
		}
		// previousDate 정의
		previousDate += Integer.toString(previousYearNum);
		if (previousMonthNum < 10) {
			previousDate += " 0";
		}
		previousDate += Integer.toString(previousMonthNum);
		// previousDate에 대한 총 수입 가져오기
		accountList = dao.getAccountForMonth(previousDate);
		for (AccountBookVO e : accountList) {
			if (e.getInNout().equals("수입")) {
				result += e.getAmount();
			}
		}
		// System.out.println(result);//1번 결과
		// previousDate에 대한 총 지출 가져오기
		for (AccountBookVO e : accountList) {
			if (e.getInNout().equals("지출")) {
				result -= e.getAmount();
			}
		}
		// System.out.println(result);//2번 결과
		// 전월에 대한 것 완료
		// 이제 현월 (date)에 대한 총 수입 가져오기
		accountList = dao.getAccountForMonth(date);
		for (AccountBookVO e : accountList) {
			if (e.getInNout().equals("수입")) {
				result += e.getAmount();
			}
		}
		for (AccountBookVO e : accountList) {
			if (e.getInNout().equals("지출")) {
				result -= e.getAmount();
			}
		}
		// System.out.println(result);//3번 결과
		total = result;
		if (result < 0)
			result = 0;
		return result;

	}

	public int getTotalOutflow(String date) {
		// int result = 0; // 리턴하는 값
		// // 이제 현월 (date)에 대한 총 수입 가져오기
		// accountList = dao.getAccountForMonth(date);
		// for (AccountBookVO e : accountList) {
		// if (e.getInNout().equals("지출")) {
		// result += e.getAmount();
		// }
		// }
		// return result;
		if (getTotalIncome(date) == 0)
			return total * -1;
		else
			return 0;
	}

	public int previousMonthMoney(String date) { // dao에 함수를 만들고 이걸 작성해보세요
		// 총계에서 수입 구하는 함수
		// 전월에 대한 월과 년도 정하기
		int currentMonthNum = Integer.parseInt(date.substring(5, date.length()));
		int currentYearNum = Integer.parseInt(date.substring(0, 4));
		int previousMonthNum = 0;
		int previousYearNum = currentYearNum;
		String previousDate = "";
		int result = 0;// return 하는 값

		if (currentMonthNum > 1)
			previousMonthNum = currentMonthNum - 1;
		else// 년도가 바뀌는 경우
		{
			previousMonthNum = 12;
			previousYearNum = currentYearNum - 1;
		}
		// previousDate 정의
		previousDate += Integer.toString(previousYearNum);
		if (previousMonthNum < 10) {
			previousDate += " 0";
		}
		previousDate += Integer.toString(previousMonthNum);

		// previousDate에 대한 총 수입 가져오기
		accountList = dao.getAccountForMonth(previousDate);
		for (AccountBookVO e : accountList) {
			if (e.getInNout().equals("수입")) {
				result += e.getAmount();
			}
		}
		// previousDate에 대한 총 지출 가져오기
		for (AccountBookVO e : accountList) {
			if (e.getInNout().equals("지출")) {
				result -= e.getAmount();
			}
		}
		return result;
	}

	public String getPrevious(String date) {
		// 전월에 대한 월과 년도 정하기
		int currentMonthNum = Integer.parseInt(date.substring(5, date.length()));
		int currentYearNum = Integer.parseInt(date.substring(0, 4));
		int previousMonthNum = 0;
		int previousYearNum = currentYearNum;
		String previousDate = "";// 월만

		if (currentMonthNum > 1)
			previousMonthNum = currentMonthNum - 1;
		else// 년도가 바뀌는 경우
		{
			previousMonthNum = 12;
			previousYearNum = currentYearNum - 1;
		}
		// previousDate 정의
		previousDate += Integer.toString(previousMonthNum);
		// System.out.println(previousDate);
		return previousDate;

	}

	
	public String dayProcessing(String date) { // DB에 저장된 날짜를 09 25이면 9.25로 바꾸는 함수
		char[] arr = date.toCharArray();
		String result;
		if (arr[8] == '0') {
			result = abbreviationMonth + "." + arr[9];
		} else {
			result = abbreviationMonth + "." + arr[8] + arr[9];
		}
		return result;
	}

	public boolean isValidIndex(String input) {
		String input2 = input.trim();
		boolean flag = false;
		if(input2.length()==0) {
			return false;
		}
		try {
			int inputToInt = Integer.parseInt(input2);
			for (AccountBookVO e : accountList) {
				if (e.getIndexNumber() == inputToInt) {
					flag = true;
					inputToString = Integer.toString(inputToInt);
				}
			}
		}catch (Exception e) {
			return false;
		}	
		return flag;
	}
	public int findIndexOfFirstDigit(String s, char c) {
	    for (int i = 0; i < s.length(); i++) {
	        if (s.charAt(i) == c) {
	            return i; // 문자 c를 찾았을 때 해당 인덱스를 반환
	        }
	    }
	    return -1; // 문자 c를 찾을 수 없는 경우 -1을 반환
	}


	public void showCurrentAccount(String date) {
	    accountList = dao.getAccountForMonth(date);
	    totalIncome = getTotalIncome(date);//총수입
	    totalOutflow = getTotalOutflow(date);//총지출
	    lastDate = getPrevious(date);//지난달 예)2
	    //System.out.println(lastDate);
	    String lastdateyearmonth;
	    if (Integer.parseInt(lastDate) != 12) {
	    	lastdateyearmonth = date.substring(0,4);
	    	if (lastDate.length() == 1) {
	    		lastdateyearmonth += " 0";
	    		lastdateyearmonth += lastDate;
	    	}
	    	else
	    	{
	    		lastdateyearmonth += " "+lastDate;
	    	}
	    		
	    }
	    else
	    {
	    	lastdateyearmonth = Integer.toString(Integer.parseInt(date.substring(0,4))-1);
	    	lastdateyearmonth += " 12";
	    }

	    System.out.println(date + "\t\t수입\t\t지출\t\t내용\t인덱스");
	    
	    System.out.println("총계\t\t" + String.format("%,-10d\t",+totalIncome)+ String.format("%,-10d\t",totalOutflow) + "\t--");
	    
	    if (accountList != null) {
	    	for (int i = 0; i < accountList.size(); i++) {
		        System.out.print(accountList.get(i).getDate().substring(5));
		        System.out.print("\t" + accountList.get(i).getCategory());

		        if (accountList.get(i).getInNout().compareTo("수입") == 0) {
		            System.out.print("\t" +String.format("%,-10d\t\t\t",accountList.get(i).getAmount()));
		        } else {
		        	System.out.print("\t\t\t" +String.format("%,-10d\t",accountList.get(i).getAmount()));
		        }
		        System.out.print(accountList.get(i).getDetails());
		        System.out.print("\t" + accountList.get(i).getIndexNumber());
		        System.out.println();
		    }
	    }
	    
	   
	    	
	    LASTaccountList = dao.getAccountForMonth(lastdateyearmonth);
	    int lastMonthSumIn = 0;
	    int lastMonthSumOut = 0;
	    
	    if(LASTaccountList != null) {
		    for (int i = 0; i < LASTaccountList.size(); i++) {
		        if (LASTaccountList.get(i).getInNout().compareTo("수입") == 0) {
		        	lastMonthSumIn += LASTaccountList.get(i).getAmount();
		        } else {
		        	lastMonthSumOut += LASTaccountList.get(i).getAmount();
		        }
		    }
	    }
	    String print;
	    if (lastdateyearmonth.substring(5,6).equals("0"))
	    {
	    	print = " " + lastdateyearmonth.substring(6,7);
	    }
	    else
	    {
	    	print = lastdateyearmonth.substring(5,7);
	    }
		System.out.println(print+"월 이월분\t" + String.format("%,-10d\t",lastMonthSumIn)+ String.format("%,-10d\t",lastMonthSumOut) + "\t--");
	    System.out.println("---------------------------------------------------");
	}
	
	public void showCurrentAccount2(String date, String category) {
		//System.out.println(date);
		//System.out.println(category);
		accountList = dao.getAccountForMonth(date);
	    filteredList = new ArrayList<>();
	    int totalIncome = 0;
	    int totalOutflow = 0;

	    for (AccountBookVO e : accountList) {
	        if (e.getCategory().equals(category)) {
	            filteredList.add(e);
	            if (e.getInNout().equals("수입")) {
	                totalIncome += e.getAmount();
	            } else {
	                totalOutflow += e.getAmount();
	            }
	        }
	    }
	    lastDate = getPrevious(date);//지난달 예)2
	    //System.out.println(lastDate);
	    String lastdateyearmonth;
	    if (Integer.parseInt(lastDate) != 12) {
	    	lastdateyearmonth = date.substring(0,4);
	    	if (lastDate.length() == 1) {
	    		lastdateyearmonth += " 0";
	    		lastdateyearmonth += lastDate;
	    	}
	    	else
	    	{
	    		lastdateyearmonth += " "+lastDate;
	    	}
	    		
	    }
	    else
	    {
	    	lastdateyearmonth = Integer.toString(Integer.parseInt(date.substring(0,4))-1);
	    	lastdateyearmonth += " 12";
	    }
	    //System.out.println(lastdateyearmonth);
	    System.out.println(date + "\t\t수입\t\t지출\t\t내용\t인덱스");
	    System.out.println("총계\t\t" + String.format("%,-10d\t",+totalIncome)+ String.format("%,-10d\t",totalOutflow) + "\t--");
	    
	    if (filteredList != null) {
	    	for (int i = 0; i < filteredList.size(); i++) {
		        System.out.print(filteredList.get(i).getDate().substring(5));
		        System.out.print("\t" + filteredList.get(i).getCategory());

		        if (accountList.get(i).getInNout().compareTo("수입") == 0) {
		            System.out.print("\t" +String.format("%,-10d\t\t\t",filteredList.get(i).getAmount()));
		        } else {
		        	System.out.print("\t\t\t" +String.format("%,-10d\t",filteredList.get(i).getAmount()));
		        }
		        System.out.print(filteredList.get(i).getDetails());
		        System.out.print("\t" + filteredList.get(i).getIndexNumber());
		        System.out.println();
		    }
	    }
	   
	    LASTaccountList = dao.getAccountForMonth(lastdateyearmonth);//지난달
	    filteredList2 = new ArrayList<>();//지난달+카테고리
	    int lastMonthSumIn = 0;
	    int lastMonthSumOut = 0;
	    for (AccountBookVO e : LASTaccountList) {
	        if (e.getCategory().equals(category)) {
	        	filteredList2.add(e);
	        }
	    }
	    
	    if(filteredList2 != null) {
	    	
		    for (int i = 0; i < filteredList2.size(); i++) {
		        if (filteredList2.get(i).getInNout().compareTo("수입") == 0) {
		        	lastMonthSumIn += filteredList2.get(i).getAmount();
		        } else {
		        	lastMonthSumOut += filteredList2.get(i).getAmount();
		        }
		    }
	    }
	   
	    String print;
	    if (lastdateyearmonth.substring(5,6).equals("0"))
	    {
	    	print = " " + lastdateyearmonth.substring(6,7);
	    }
	    else
	    {
	    	print = lastdateyearmonth.substring(5,7);
	    }
		System.out.println(print+"월 이월분\t" + String.format("%,-10d\t",lastMonthSumIn)+ String.format("%,-10d\t",lastMonthSumOut) + "\t--");
	    System.out.println("---------------------------------------------------");

	}
	

	public int numberOfSpaces(int number) {
		// 정수를 문자열로 변환
		String numberStr = Integer.toString(number);
		// 문자열의 길이를 확인하여 자릿수를 얻음
		int numberOfDigits = numberStr.length();
		int numberOfSpaces = 16 - numberOfDigits;
		return numberOfSpaces;

	}
	public void insertYearMonth() {
	    while (true) {
	        System.out.print("“년+월” 또는 “년+월+카테고리”를 입력하세요 >");
	        Scanner sc2 = new Scanner(System.in);
	        date = sc2.nextLine().trim();
	        
	      
	        if (isDateValid(date)) {
	        	System.out.println("---------------------------------------------------");
	            break;
	        }
	       //if (!isDateValid(date))
	        System.out.println("---------------------------------------------------");
	    }
	}
	public boolean validFor1or2(String input) {
		try {
			String temp = input.trim();
			if(temp.length()==0) {
				return false;
			}
			int temp2 = Integer.parseInt(temp);
			if(temp2!=1 && temp2!=2) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	public void setBasicCategory(ArrayList<CategoryVO> categoryList) {
		

		String arr[] = {"월급","부수입","용돈","상여","금융소득"};
		String arr2[] = {"식비","문화생활","경조사/회비","주거/통신","교통/차량"};
		for (int i = 0 ; i < arr.length; i++)
		{
			CategoryVO category = new CategoryVO("", "");
			category.setInNout("수입");
			
			category.setCategory(arr[i]);
			categoryList.add(category);

		}
		for (int i = 0 ; i < arr2.length; i++)
		{
			CategoryVO category = new CategoryVO("", "");
			category.setInNout("지출");
			
			category.setCategory(arr2[i]);
			categoryList.add(category);

		}
	
	}
}