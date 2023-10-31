package AccountBook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Process3 {
	AccountBookDao dao = new AccountBookDao();
	ArrayList<String> availableCategories = dao.getCategories1();//저장된 카테고리 항목들
	ArrayList<AccountBookVO> accountList;
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
				if(addtionalDelete==true) {
					insertYearMonth();
					addtionalDelete = false;
				}
				if (!categoryIn) {//카테고리가 입력되지 않은 경우 
					accountList = dao.getAccountForMonth(date);
					String[] arr = date.split(" ");
					year = arr[0];
					month = arr[1];
					abbreviationMonth = monthAbbreviation(month); // 09월이면 그냥 9로 바꾸고 12월이면 그냥 12로 유지하는 함수
					if (year.length() == 2) {
						year = "20" + year;
					}
					String modifiedDate = year + " " + month;// 반드시 2023 04 형태임
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
						showCurrentAccount(modifiedDate);
						int temp;
						String tempInput;
						while (true) {
							while(true) {
							System.out.println("1) 추가 삭제 ");
							System.out.println("2) 메인화면으로 돌아가기");
							System.out.print("입력> ");
							tempInput = sc.nextLine();
							if(validFor1or2(tempInput)) {
								temp = Integer.parseInt(tempInput.trim());
								break;
								}
							System.out.println("---------------------------------------------------");
							System.out.println("유효하지 않은 숫자를 입력하셨습니다");
							}
							if (temp == 1) {
								sc.nextLine();
								break;
							} else if (temp == 2)
								break;
						}
						if (temp == 2) // 메인화면으로 돌아가기
							break;

					} else { // 아무것도 가져온게 없을 때
						showCurrentAccount(modifiedDate);
						System.out.println("삭제 가능한 항목이 없습니다.");
						int temp;
						String input;
						while (true) {
							while(true) {
							System.out.println("---------------------------------------------------");
							System.out.println("1) “년+월” 또는 “년+월+카테고리” 다시 입력하기");
							System.out.println("2) 메인화면으로 돌아가기");
							System.out.print("입력> ");
							input = sc.nextLine();
							if(validFor1or2(input)) {
								temp = Integer.parseInt(input.trim());
								break;
								}
							}
							if (temp == 1) {
								addtionalDelete = true;
								System.out.println("---------------------------------------------------");
								break;
							}
							else if (temp == 2)
								break;

							System.out.println("---------------------------------------------------");
						}
						if (temp == 2) // 메인화면으로 돌아가기
							break;
					}
				

				}
				
				else {//카테고리 입력 받은 경우 (유효한 카테고리 여부와 관계 없음)
					
					accountList = dao.getAccountForMonth(date);
					String[] arr = date.split(" ");
					year = arr[0];
					month = arr[1];
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
					if (categorysize != 0) { // 특정 카테고리에서 가져온 내역이 있을 때
						//특정 카테고리만 가져오도록 수정해야함
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
						showCurrentAccount2(modifiedDate,category);
						int temp;
						String tempInput;
						while (true) {
							while(true) {
							System.out.println("1) 추가 삭제 ");
							System.out.println("2) 메인화면으로 돌아가기");
							System.out.print("입력> ");
							tempInput = sc.nextLine();
							if(validFor1or2(tempInput)) {
								temp = Integer.parseInt(tempInput.trim());
								break;
								}
							System.out.println("---------------------------------------------------");
							System.out.println("유효하지 않은 숫자를 입력하셨습니다");
							}
							if (temp == 1) {
								sc.nextLine();
								break;
							} else if (temp == 2)
								break;
						}
						if (temp == 2) // 메인화면으로 돌아가기
							break;

					} else { // 특정 카테고리에서 아무것도 가져온게 없을 때
						showCurrentAccount(modifiedDate);
						System.out.println("삭제 가능한 항목이 없습니다.");
						int temp;
						String input;
						while (true) {
							while(true) {
							System.out.println("---------------------------------------------------");
							System.out.println("1) “년+월” 또는 “년+월+카테고리” 다시 입력하기");
							System.out.println("2) 메인화면으로 돌아가기");
							System.out.print("입력> ");
							input = sc.nextLine();
							if(validFor1or2(input)) {
								temp = Integer.parseInt(input.trim());
								break;
								}
							}
							if (temp == 1) {
								addtionalDelete = true;
								System.out.println("---------------------------------------------------");
								break;
							}
							else if (temp == 2)
								break;

							System.out.println("---------------------------------------------------");
						}
						if (temp == 2) // 메인화면으로 돌아가기
							break;
					}
				
				}
			

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
		        } catch (NumberFormatException e) {
		            return false; // Non-numeric year
		        }
			} else if (year.length() == 4) {
				try {
		            int temp = Integer.parseInt(year);
		            if (temp < 1902 || temp > 2037) {
		                return false;
		            }
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
	    totalIncome = getTotalIncome(date);
	    totalOutflow = getTotalOutflow(date);

	    // Print the header
	    System.out.println(year + " " + abbreviationMonth + "\t\t수입\t\t지출\t\t내용\t\t인덱스");

	    // Calculate the spaces needed for alignment
	    int spacesForTotalIncome = numberOfSpaces2(totalIncome);
	    int spacesForTotalOutflow = numberOfSpaces2(totalOutflow);

	    // Print the total row
	    System.out.print("총계         " + formatWithSpaces(totalIncome, spacesForTotalIncome));
	    int kspace = 14 - Integer.toString(totalIncome).length();
	    for (int i = 0 ; i < kspace; i++)
	    	System.out.print(" ");
	    System.out.print(totalOutflow);
	    int qspace = 32 - Integer.toString(totalOutflow).length();
	    for (int i = 0 ; i < qspace; i++)
	    	System.out.print(" ");
	    System.out.println("--");

	    int index = 1;

	    // Print the account entries
	    for (AccountBookVO e : accountList) {
	        System.out.print(abbreviationMonth + "." + (index < 10 ? "0" + index : index) + "  " + e.getCategory());

	        if (e.getInNout().equals("수입")) {
	        	int gspace = 15 - e.getCategory().length() - 6;
			    for (int i = 0 ; i < gspace; i++)
			    	System.out.print(" ");
			    System.out.print(formatWithSpaces(e.getAmount(), 0));
	            
	        } else {
	        	int gspace = 30 - e.getCategory().length();
			    for (int i = 0 ; i < gspace; i++)
			    	System.out.print(" ");
			    System.out.print(e.getAmount());
	        }

	        int pspace = 46 - Integer.toString(e.getAmount()).length();
		    for (int i = 0 ; i < pspace; i++)
		    	System.out.print(" ");
	        System.out.println(e.getIndexNumber());

	        index++;
	    }

	    // Print the previous month's entry
	      
	    if (previousMonth == 0 && accountList.size() == 0) {
	        System.out.println(previous + "월 이월분\t\t" + previousMonth + "\t\t0\t\t  \t\t--");
	    } else {
	    	System.out.print(previous + "월 이월분");
	    	int hspace = 12 - Integer.toString(previousMonth).length();
		    for (int i = 0 ; i < hspace; i++)
		    	System.out.print(" ");
		    System.out.print(formatWithSpaces(previousMonth, 0));
		    int zspace = 48 - Integer.toString(previousMonth).length();
		    for (int i = 0 ; i < zspace; i++)
		    	System.out.print(" ");
		    System.out.println("--");
	    }

	    System.out.println("---------------------------------------------------");
	}

	// Helper method to calculate the number of spaces needed for alignment
	private int numberOfSpaces2(int value) {
	    return 11 - String.valueOf(value).length();
	}

	// Helper method to format a value with the specified number of spaces
	private String formatWithSpaces(int value, int spaces) {
	    String formattedValue = String.format("%,d", value);
	    for (int i = 0; i < spaces; i++) {
	        formattedValue = " " + formattedValue;
	    }
	    return formattedValue;
	}
	/*public void showCurrentAccount2(String date, String category) {
		accountList = dao.getAccountForMonth(date);
	    List<AccountBookVO> filteredList = new ArrayList<>();
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
	   
	    totalIncome = getTotalIncome(date);
	    totalOutflow = getTotalOutflow(date);

	    // Print the header
	    System.out.println(year + " " + abbreviationMonth + "\t\t수입\t\t지출\t\t내용\t\t인덱스");

	    // Calculate the spaces needed for alignment
	    int spacesForTotalIncome = numberOfSpaces2(totalIncome);
	    int spacesForTotalOutflow = numberOfSpaces2(totalOutflow);

	    // Print the total row
	    System.out.print("총계         " + formatWithSpaces(totalIncome, spacesForTotalIncome));
	    int kspace = 14 - Integer.toString(totalIncome).length();
	    for (int i = 0 ; i < kspace; i++)
	    	System.out.print(" ");
	    System.out.print(totalOutflow);
	    int qspace = 32 - Integer.toString(totalOutflow).length();
	    for (int i = 0 ; i < qspace; i++)
	    	System.out.print(" ");
	    System.out.println("--");

	    int index = 1;

	    // Print the account entries
	    for (AccountBookVO e : accountList) {
	        System.out.print(abbreviationMonth + "." + (index < 10 ? "0" + index : index) + "  " + e.getCategory());

	        if (e.getInNout().equals("수입")) {
	        	int gspace = 15 - e.getCategory().length() - 6;
			    for (int i = 0 ; i < gspace; i++)
			    	System.out.print(" ");
			    System.out.print(formatWithSpaces(e.getAmount(), 0));
	            
	        } else {
	        	int gspace = 30 - e.getCategory().length();
			    for (int i = 0 ; i < gspace; i++)
			    	System.out.print(" ");
			    System.out.print(e.getAmount());
	        }

	        int pspace = 46 - Integer.toString(e.getAmount()).length();
		    for (int i = 0 ; i < pspace; i++)
		    	System.out.print(" ");
	        System.out.println(e.getIndexNumber());

	        index++;
	    }

	    // Print the previous month's entry
	      
	    if (previousMonth == 0 && accountList.size() == 0) {
	        System.out.println(previous + "월 이월분\t\t" + previousMonth + "\t\t0\t\t  \t\t--");
	    } else {
	    	System.out.print(previous + "월 이월분");
	    	int hspace = 12 - Integer.toString(previousMonth).length();
		    for (int i = 0 ; i < hspace; i++)
		    	System.out.print(" ");
		    System.out.print(formatWithSpaces(previousMonth, 0));
		    int zspace = 48 - Integer.toString(previousMonth).length();
		    for (int i = 0 ; i < zspace; i++)
		    	System.out.print(" ");
		    System.out.println("--");
	    }

	    System.out.println("---------------------------------------------------");
	}*/
	
	public void showCurrentAccount2(String date, String category) {
	    accountList = dao.getAccountForMonth(date);
	    List<AccountBookVO> filteredList = new ArrayList<>();
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

	    System.out.println(date + "\t\t수입\t\t지출\t\t내용\t인덱스");

	    // Calculate the spaces needed for alignment
	    int spacesForTotalIncome = 11; // You can choose the desired width for total income
	    int spacesForTotalOutflow = 11; // You can choose the desired width for total outflow

	    System.out.print("총계");
	    System.out.print(String.format("%,11d", totalIncome));
	    System.out.print(String.format("%,11d", totalOutflow));
	    System.out.print("\t--");
	    System.out.println();

	    int index = 1;

	    for (AccountBookVO e : filteredList) {
	        String monthAndDay = e.getDate().substring(5);
	        String categoryName = e.getCategory();
	        String incomeAmount = e.getInNout().equals("수입") ? String.format("%,11d", e.getAmount()) : "";
	        String outflowAmount = e.getInNout().equals("지출") ? String.format("%,11d", e.getAmount()) : "";
	        String indexNumber = String.valueOf(e.getIndexNumber());

	        System.out.print(monthAndDay + "\t" + categoryName + "\t");
	        System.out.print(incomeAmount);
	        System.out.print("\t" + outflowAmount);
	        System.out.print("\t" + indexNumber);
	        System.out.println();

	        index++;
	    }

	    if (filteredList.isEmpty()) {
	        System.out.print(previous + "월 이월분\t" + previousMonth + "\t\t");
	        System.out.print(String.format("%,11d", 0));
	        System.out.print(String.format("%,11d", 0));
	        System.out.print("\t--");
	    } else {
	        System.out.print(previous + "월 이월분\t" + previousMonth + "\t\t");
	        System.out.print(String.format("%,11d", previousMonth));
	        System.out.print(String.format("%,11d", 0));
	        System.out.print("\t--");
	    }
	    System.out.println();

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