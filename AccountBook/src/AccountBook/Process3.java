package AccountBook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class Process3 {
	AccountBookDao dao = new AccountBookDao();
	ArrayList<String> availableCategories = dao.getCategories1();// 저장된 카테고리 항목들
	ArrayList<AccountBookVO> accountList;
	ArrayList<AccountBookVO> accountList2;
	ArrayList<AccountBookVO> LASTaccountList;
	ArrayList<AccountBookVO> filteredList;
	ArrayList<AccountBookVO> filteredList2;
	ArrayList<String> categoryList = new ArrayList<>();
	String abbreviationMonth;
	String year;
	String month;
	String category = "";
	boolean categoryIn = false; // 카테고리도 입력으로 넣은 경우
	int previousMonth;
	long totalIncome;
	long totalOutflow;
	String previous; // 00 이월분 표시의 00
	String inputToString;
	String date;
	String lastDate = "";
	String modifiedDate;
	Scanner sc = new Scanner(System.in);
	boolean addtionalDelete = false;

	public Process3() {
		// 기본 카테고리 + 사용자가 등록한 카테고리 "저장된 카테고리 항목들"에 추가

		/*
		 * // Add default categories for "수입" (Income) String[] defaultIncomeCategories
		 * = { "월급", "부수입", "용돈", "상여", "금융소득" };
		 * availableCategories.addAll(Arrays.asList(defaultIncomeCategories));
		 * 
		 * // Add default categories for "지출" (Expense) String[]
		 * defaultExpenseCategories = { "식비", "문화생활", "경조사/회비", "주거/통신", "교통/차량" };
		 * availableCategories.addAll(Arrays.asList(defaultExpenseCategories));
		 * 
		 * // Fetch and add user-added categories from the database using //
		 * dao.getCategories1()
		 */
		ArrayList<String> userCategories = dao.getCategories1();
		availableCategories.addAll(userCategories);
		availableCategories.add("수입");
		availableCategories.add("지출");
		categoryList.clear();

		insertYearMonth();
		while (true) {

			if (addtionalDelete == true) {
				insertYearMonth();
				addtionalDelete = false;
			}
			if (!categoryIn) {// 카테고리가 입력되지 않은 경우
				
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
							System.out.println("------------------------------------------------------------");
						} else {
							break;
						}
					}
					input = Integer.parseInt(inputToString);
					dao.deleteAccount(input);
					System.out.println("------------------------------------------------------------");
					System.out.println("삭제가 완료되었습니다");
					System.out.println("------------------------------------------------------------");
					
					int temp = 0;
					String tempInput;
					if (accountList.size() != 0) {
						while (true) {
							showCurrentAccount(modifiedDate);
							System.out.println("1) 추가 삭제 ");
							System.out.println("2) 메인화면으로 돌아가기");
							System.out.print("입력> ");
							tempInput = sc.nextLine();
							System.out.println("------------------------------------------------------------");

							if (validFor1or2(tempInput)) {
								temp = Integer.parseInt(tempInput.trim());
								break;
							}

							System.out.println("유효하지 않은 숫자를 입력하셨습니다");
							System.out.println("------------------------------------------------------------");

							if (temp == 1) {
								// addtionalDelete = true;

								break;

							} else if (temp == 2)
								break;
						}
						if (temp == 2) // 메인화면으로 돌아가기
							break;

					}
				} else { // 아무것도 가져온게 없을 때
					showCurrentAccount(modifiedDate);
					System.out.println("삭제 가능한 항목이 없습니다.");
					System.out.println("------------------------------------------------------------");

					int temp2;
					String input;
					while (true) {
						while (true) {
							// System.out.println("---------------------------------------------------");
							System.out.println("1) “년+월” 또는 “년+월+카테고리”를 입력하세요");
							System.out.println("2) 메인화면으로 돌아가기");
							System.out.print("입력> ");
							input = sc.nextLine();
							System.out.println("------------------------------------------------------------");

							// System.out.println("---------------------------------------------------");

							if (validFor1or2(input)) {
								temp2 = Integer.parseInt(input.trim());
								break;
							}
						}
						if (temp2 == 1) {
							addtionalDelete = true;
							// System.out.println("------------------------------------------------------------");
							break;
						} else if (temp2 == 2) {
							// System.out.println("------------------------------------------------------------");
							break;

						}
						System.out.println("------------------------------------------------------------");
					}
					if (temp2 == 2) // 메인화면으로 돌아가기
						break;
				}
			}

			else {// 카테고리 입력 받은 경우 (카테고리 유효성 이미 검사함)

				
				String[] arr = date.split(" ");
				year = arr[0];
				month = arr[1];
				if (month.length() == 1)
					month = "0" + month;
				abbreviationMonth = monthAbbreviation(month); // 09월이면 그냥 9로 바꾸고 12월이면 그냥 12로 유지하는 함수
				if (year.length() == 2) {
					year = "20" + year;
				}
				String modifiedDate = year + " " + month;// 반드시 2023 04 형태임
				previousMonth = previousMonthMoney(modifiedDate);
				accountList = dao.getAccountForMonth(modifiedDate);
				totalIncome = getTotalIncome(modifiedDate);
				totalOutflow = getTotalOutflow(modifiedDate);
				previous = getPrevious(modifiedDate);
				// 카테고리
				ArrayList<String> arr2n = new ArrayList<>(Arrays.asList(arr).subList(2, arr.length));
				int categorysize = 0;
				if (arr2n.size() > 1) {
					ConsiderCategoryAndOrNot(arr2n, modifiedDate);
					categorysize = filteredList.size();
				} else if (arr2n.size() == 1) {
					for (int i = 2; i < arr.length; i++) {
						category += arr[i];
						category += " ";
					}
					category = category.trim();
					// System.out.println(category);
					for (AccountBookVO e : accountList) {
						if (e.getCategory().equals(category) || e.getCategory().contains(category)) {
							categorysize++;
						}else if(e.getInNout().equals(category)) {
							categorysize++;
						}
					}
				} else
					break;

				arr2n = new ArrayList<>(Arrays.asList(arr).subList(2, arr.length));
				boolean a = !arr2n.contains("Or") && !arr2n.contains("And") && !arr2n.contains("Not");

				if (categorysize != 0 && a) { // 가져온 내역이 있을 때 + "Or, Not, And" 포함x
					showCurrentAccount2(modifiedDate, category);

					int input;
					while (true) {
						System.out.print("삭제할 인덱스를 입력해주세요> ");
						inputToString = sc.nextLine();
						if (!isValidIndex(inputToString)) {
							System.out.println("유효하지 않은 인덱스입니다");
							System.out.println("------------------------------------------------------------");
						} else {
							break;
						}
					}
					input = Integer.parseInt(inputToString);
					dao.deleteAccount(input);
					System.out.println("------------------------------------------------------------");
					System.out.println("삭제가 완료되었습니다");
					System.out.println("------------------------------------------------------------");
					for(AccountBookVO e : filteredList) {
						if(e.getIndexNumber()==input)
							filteredList.remove(e);
					}
					int temp = 0;
					String tempInput;
					categorysize = 0;
					for (AccountBookVO e : filteredList) {
						if (e.getCategory().equals(category) || e.getCategory().contains(category)
								|| e.getInNout().equals(category)) {
							categorysize++;
						}
					}
					if (categorysize != 0) {
						while (true) {
							try {
								showCurrentAccount2(modifiedDate, category);
							} catch (Exception e) {
								showCurrentAccount(modifiedDate);

							}
							System.out.println("1) 추가 삭제 ");
							System.out.println("2) 메인화면으로 돌아가기");
							System.out.print("입력> ");
							tempInput = sc.nextLine();
							System.out.println("------------------------------------------------------------");

							if (validFor1or2(tempInput)) {
								temp = Integer.parseInt(tempInput.trim());
								break;
							}

							System.out.println("유효하지 않은 숫자를 입력하셨습니다");
							System.out.println("------------------------------------------------------------");

							if (temp == 1) {
								addtionalDelete = true;

								break;

							} else if (temp == 2)
								break;
						}
						if (temp == 1)
							break;
						if (temp == 2) // 메인화면으로 돌아가기
							break;

					}

				} else if (categorysize == 0 && a) {
					showCurrentAccount2(modifiedDate,category);
					System.out.println("삭제 가능한 항목이 없습니다.");
					System.out.println("------------------------------------------------------------");

					int temp2;
					String input1;
					while (true) {
						while (true) {
							// System.out.println("---------------------------------------------------");
							System.out.println("1) “년+월” 또는 “년+월+카테고리”를 입력하세요");
							System.out.println("2) 메인화면으로 돌아가기");
							System.out.print("입력> ");
							input1 = sc.nextLine();
							System.out.println("------------------------------------------------------------");

							// System.out.println("---------------------------------------------------");

							if (validFor1or2(input1)) {
								temp2 = Integer.parseInt(input1.trim());
								break;
							}
						}
						if (temp2 == 1) {
							addtionalDelete = true;
							// System.out.println("------------------------------------------------------------");
							break;
						} else if (temp2 == 2) {
							// System.out.println("------------------------------------------------------------");
							break;

						}
						System.out.println("------------------------------------------------------------");
					}
					if (temp2 == 2) // 메인화면으로 돌아가기
						break;
				}

				else if (categorysize != 0 && !a)// "Not, and , or"입력받고 가져온게 있을떄
				{
					arr2n = new ArrayList<>(Arrays.asList(arr).subList(2, arr.length));
					showCurrentAccount3(modifiedDate, arr2n);

					int input3;
					while (true) {
						System.out.print("삭제할 인덱스를 입력해주세요> ");
						inputToString = sc.nextLine();
						// System.out.println("------------------------------------------------------------");

						if (!isValidIndex2(inputToString)) {
							System.out.println("유효하지 않은 인덱스입니다");
							System.out.println("------------------------------------------------------------");
						} else {
							break;
						}
					}

					input3 = Integer.parseInt(inputToString);
					dao.deleteAccount(input3);
					System.out.println("------------------------------------------------------------");
					System.out.println("삭제가 완료되었습니다");
					System.out.println("------------------------------------------------------------");

					int temp3 = 0;
					String tempInput3;
					categorysize = 0;

					arr2n = new ArrayList<>(Arrays.asList(arr).subList(2, arr.length));
					ConsiderCategoryAndOrNot(arr2n, modifiedDate);
					categorysize = filteredList.size();
					arr2n = new ArrayList<>(Arrays.asList(arr).subList(2, arr.length));

					if (categorysize != 0) {
						while (true) {

							try {
								arr2n = new ArrayList<>(Arrays.asList(arr).subList(2, arr.length));
								showCurrentAccount3(modifiedDate, arr2n);
							} catch (Exception e) {
								showCurrentAccount(modifiedDate);

							}
							System.out.println("1) 추가 삭제 ");
							System.out.println("2) 메인화면으로 돌아가기");
							System.out.print("입력> ");
							tempInput3 = sc.nextLine();
							System.out.println("------------------------------------------------------------");

							if (validFor1or2(tempInput3)) {
								temp3 = Integer.parseInt(tempInput3.trim());
								break;
							}

							System.out.println("유효하지 않은 숫자를 입력하셨습니다");
							System.out.println("------------------------------------------------------------");

							if (temp3 == 1) {
								addtionalDelete = true;

								break;

							} else if (temp3 == 2)
								break;
						}

						if (temp3 == 2) // 메인화면으로 돌아가기
							break;

					}

				} else if (categorysize == 0 && !a) { // 아무것도 가져온게 없을 때

					try {
						arr2n = new ArrayList<>(Arrays.asList(arr).subList(2, arr.length));
						showCurrentAccount3(modifiedDate, arr2n);
					} catch (Exception e) {
						showCurrentAccount(modifiedDate);

					}
					System.out.println("삭제 가능한 항목이 없습니다.");
					System.out.println("------------------------------------------------------------");

					int temp2;
					String input3;
					while (true) {
						while (true) {
							// System.out.println("---------------------------------------------------");
							System.out.println("1) “년+월” 또는 “년+월+카테고리”를 입력하세요");
							System.out.println("2) 메인화면으로 돌아가기");
							System.out.print("입력> ");
							input3 = sc.nextLine();
							System.out.println("------------------------------------------------------------");

							// System.out.println("---------------------------------------------------");

							if (validFor1or2(input3)) {
								temp2 = Integer.parseInt(input3.trim());
								break;
							}
						}
						if (temp2 == 1) {
							addtionalDelete = true;
							// System.out.println("------------------------------------------------------------");
							break;
						} else if (temp2 == 2) {
							// System.out.println("------------------------------------------------------------");
							break;

						}
						System.out.println("------------------------------------------------------------");
					}

					if (temp2 == 2) // 메인화면으로 돌아가기
						break;
				}
			}
		}
	}

	private void ConsiderCategoryAndOrNot(ArrayList<String> arr2n, String modifiedDate) {
		// 불리기
		if (!arr2n.contains("Not")) {
			int i = 0;
			for (; i < arr2n.size();) {

				if (!arr2n.get(i).equals("Or") && arr2n.get(i).equals("And")) {

					//arr2n.add(i, arr2n.get(i));
					i += 1;
				} else if (!arr2n.get(i).equals("And") && arr2n.get(i).equals("Or")) {
					//arr2n.add(i, arr2n.get(i));
					i += 1;
				} else if (!arr2n.get(i).equals("And") && !arr2n.get(i).equals("Or")) {
					arr2n.add(i, arr2n.get(i));
					i += 2;
				}

			}

			arr2n.remove(0);
			arr2n.remove(arr2n.size() - 1);

		} else if (arr2n.contains("Not")) {
			int i = 0;
			for (; i < arr2n.size();) {

				if (!arr2n.get(i).equals("Or") && arr2n.get(i).equals("And")) {

					// arr2n.add(i, arr2n.get(i));
					i += 1;
				} else if (!arr2n.get(i).equals("And") && arr2n.get(i).equals("Or")) {
					// arr2n.add(i, arr2n.get(i));
					i += 1;
				} else if (arr2n.get(i).equals("Not")) {
					arr2n.add(i + 2, arr2n.get(i));
					arr2n.add(i + 3, arr2n.get(i + 1));
					i += 4;
				} else if (!arr2n.get(i).equals("And") && !arr2n.get(i).equals("Or")) {
					arr2n.add(i, arr2n.get(i));
					i += 2;
				}

			}
			if (arr2n.get(0).equals("Not"))
				;
			else
				arr2n.remove(0);
			arr2n.remove(arr2n.size() - 1);

		}
		if(arr2n.get(arr2n.size()-1).equals("And")
				||arr2n.get(arr2n.size()-1).equals("Or")
				||arr2n.get(arr2n.size()-1).equals("Not"))
			arr2n.remove(arr2n.size() - 1);

		lastDate = getPrevious(modifiedDate);// 지난달 예)2
		String lastdateyearmonth;
		if (Integer.parseInt(lastDate) != 12) {
			lastdateyearmonth = modifiedDate.substring(0, 4);
			if (lastDate.length() == 1) {
				lastdateyearmonth += " 0";
				lastdateyearmonth += lastDate;
			} else {
				lastdateyearmonth += " " + lastDate;
			}

		} else {
			lastdateyearmonth = Integer.toString(Integer.parseInt(modifiedDate.substring(0, 4)) - 1);
			lastdateyearmonth += " 12";
		}
		accountList = dao.getAccountForMonth(modifiedDate);// 요번달 모든 항목들
		LASTaccountList = dao.getAccountForMonth(lastdateyearmonth);// 지난달 모든 항목들
		filteredList = new ArrayList<>();
		filteredList2 = new ArrayList<>();
		

		if (!arr2n.contains("Not")) {// Not이 포함되지 않은 arr2n일때
			while (arr2n.contains("And") && !arr2n.contains("Or")) {// and가 있을때만
				//And 개수 알아내기
				// "And"의 개수 세기
		        int andCount = 0;
				for (String item : arr2n) {
		            if (item.equals("And")) {
		                andCount++;
		            }
			    }
		        // "And"가 아닌 인덱스를 저장할 배열의 크기 계산
		        int nonAndIndexesSize = arr2n.size() - andCount;

		        // "And"가 아닌 인덱스를 저장할 배열
		        int[] nonAndIndexes = new int[nonAndIndexesSize];
		        
				if (arr2n.contains("And")) {

					for (AccountBookVO e : accountList)// 요번달
					{
						ArrayList<String> categoryList = new ArrayList<>(Arrays.asList(e.getCategory().split(" ")));
					
						int putIn = 0;
						for (int i = 0 ; i < arr2n.size(); i++)
						{
							if (categoryList.contains(arr2n.get(i)))
								putIn += 1;
						}
						if (putIn == nonAndIndexesSize)
							filteredList.add(e);

					}

					for (AccountBookVO e : LASTaccountList)// 저번달
					{
						ArrayList<String> categoryList = new ArrayList<>(Arrays.asList(e.getCategory().split(" ")));
						
						int putIn = 0;
						for (int i = 0 ; i < arr2n.size(); i++)
						{
							if (categoryList.contains(arr2n.get(i)))
								putIn += 1;
						}
						if (putIn == nonAndIndexesSize)
							filteredList2.add(e);
					}
					
					// "And"에 해당하는 문자열 제거
			        removeAndStrings(arr2n);
				}

			}

			while (arr2n.contains("Or") && !arr2n.contains("And")) {// Or 가 있을때만
				if (arr2n.contains("Or")) {
					int indexOfOr = arr2n.indexOf("Or");

					if (indexOfOr - 1 >= 0 && indexOfOr + 1 <= arr2n.size() - 1) {// ㅁ Or ㄹ
						for (AccountBookVO e : accountList)// 요번달
						{
							ArrayList<String> categoryList = new ArrayList<>(Arrays.asList(e.getCategory().split(" ")));
							
							if (categoryList.contains(arr2n.get(indexOfOr - 1))//하위 카테고리 Or 하위 카테고리
									|| categoryList.contains(arr2n.get(indexOfOr + 1))) {
								filteredList.add(e);
							}else if(arr2n.get(indexOfOr - 1).equals(e.getInNout())//상위 카테고리 Or 하위 카테고리
									|| categoryList.contains(arr2n.get(indexOfOr + 1))) {
								filteredList.add(e);
							}else if(categoryList.contains(arr2n.get(indexOfOr - 1))//하위 카테고리 Or 상위 카테고리
									|| arr2n.get(indexOfOr + 1).equals(e.getInNout())) {
								filteredList.add(e);
							}else if (arr2n.get(indexOfOr-1).equals(e.getInNout()) || //상위 카테고리 Or 상위 카테고리
									arr2n.get(indexOfOr-1).equals(e.getInNout()) ) {
								filteredList.add(e);

							}
								
						}
						for (AccountBookVO e : LASTaccountList)// 저번달
						{
							ArrayList<String> categoryList = new ArrayList<>(Arrays.asList(e.getCategory().split(" ")));
							if (categoryList.contains(arr2n.get(indexOfOr - 1))
									|| categoryList.contains(arr2n.get(indexOfOr + 1))) {
								filteredList2.add(e);
							}else if(arr2n.get(indexOfOr - 1).equals(e.getInNout())
									|| categoryList.contains(arr2n.get(indexOfOr + 1))) {
								filteredList2.add(e);
							}else if(categoryList.contains(arr2n.get(indexOfOr - 1))
									|| arr2n.get(indexOfOr + 1).equals(e.getInNout())) {
								filteredList2.add(e);
							}
						}
						arr2n.remove(indexOfOr - 1);
						arr2n.remove(indexOfOr - 1);
						arr2n.remove(indexOfOr - 1);

					}

				}
			}
			
			while(arr2n.contains("Or") && arr2n.contains("And")) {//Or 와 And 모두 있을때---------------------
				// "And"의 개수 세기
		        int andCount = 0;
		        for (String item : arr2n) {
		            if (item.equals("And")) {
		                andCount++;
		            }
		        }

		        // "And"가 존재하는 위치 저장
		        ArrayList<int[]> andExist = new ArrayList<>();
		        for (int i = 0; i < arr2n.size(); i++) {
		            if (arr2n.get(i).equals("And")) {
		                andExist.add(new int[]{i - 1, i + 1});
		            }
		        }
		       
		        // And 연속된 부분의 수를 합치기
		        ArrayList<int[]> mergedList = mergeConsecutiveParts(andExist);
		        
		      
	        	for (AccountBookVO e : accountList)// 요번달 And 처리
				{
					ArrayList<String> categoryList = new ArrayList<>(Arrays.asList(e.getCategory().split(" ")));
					
					for (int k = 0; k <mergedList.size(); k++)
					{
						int[] here = mergedList.get(k);
			        	int SIZE = ((here[1] - here[0] + 1) / 3)*2; 
			        	
			        	int putIn = 0;
						for (int i = here[0] ; i <= here[1]; i++)
						{	
							
							if (categoryList.contains(arr2n.get(i)))
							{
								putIn += 1;
							}
								
						}
						if ( putIn == SIZE )
							filteredList.add(e);
					}
				}
	        	for (AccountBookVO e : LASTaccountList)// 지난달 And 처리
				{
					ArrayList<String> categoryList = new ArrayList<>(Arrays.asList(e.getCategory().split(" ")));
					
					for (int k = 0; k <mergedList.size(); k++)
					{
						int[] here = mergedList.get(k);
			        	int SIZE = ((here[1] - here[0] + 1) / 3)*2; 
			        	
			        	int putIn = 0;
						for (int i = here[0] ; i <= here[1]; i++)
						{	
							
							if (categoryList.contains(arr2n.get(i)))
							{
								putIn += 1;
							}
								
						}
						if ( putIn == SIZE )
							filteredList2.add(e);
					}
				}
	        	// "And"에 해당하는 문자열 제거
		        removeAndStrings(arr2n);
		        
					// "Or"의 개수 세기
			        int orCount = 0;
			        for (String item : arr2n) {
			            if (item.equals("Or")) {
			                andCount++;
			            }
			        }

			        // "Or"가 존재하는 위치 저장
			        ArrayList<int[]> orExist = new ArrayList<>();
			        for (int i = 0; i < arr2n.size(); i++) {
			            if (arr2n.get(i).equals("Or")) {
			            	orExist.add(new int[]{i - 1, i + 1});
			            }
			        }
			       
			        // Or 연속된 부분의 수를 합치기
			       mergedList = mergeConsecutiveParts(orExist);
			        
					for (AccountBookVO t : accountList)// 요번달 Or 처리
					{
						ArrayList<String> categoryList = new ArrayList<>(Arrays.asList(t.getCategory().split(" ")));
						
						for (int k = 0; k <mergedList.size(); k++)
						{
							int[] here = mergedList.get(k); 
				        	
				        	//int putIn = 0;
							for (int i = here[0] ; i <= here[1]; i++)
							{	
								
								if (categoryList.contains(arr2n.get(i)))
								{
									filteredList.add(t);
								}
									
							}
					
						}
					
						
					}
					for (AccountBookVO t : LASTaccountList)// 지난달 Or 처리
					{
						ArrayList<String> categoryList = new ArrayList<>(Arrays.asList(t.getCategory().split(" ")));
						
						for (int k = 0; k <mergedList.size(); k++)
						{
							int[] here = mergedList.get(k);
				        	
				        	//int putIn = 0;
							for (int i = here[0] ; i <= here[1]; i++)
							{	
								
								if (categoryList.contains(arr2n.get(i)))
								{
									filteredList2.add(t);
								}
									
							}
					
						}
					
						
					}
					// "Or"에 해당하는 문자열 제거
			        removeAndStrings2(arr2n);
						

					
		        }//--------------------------
			
		      
			
		} else // Not이 포함될때.
		{
			while (arr2n.contains("And")) {// and가 있을때만
				if (arr2n.contains("And")) {
					int indexOfAnd = arr2n.indexOf("And");

					for (AccountBookVO e : accountList)// 요번달
					{
						ArrayList<String> categoryList = new ArrayList<>(Arrays.asList(e.getCategory().split(" ")));
						if (indexOfAnd - 2 >= 0) {
							if (arr2n.get(indexOfAnd - 2).equals("Not")) {
								if (arr2n.get(indexOfAnd + 1).equals("Not")) {// Not ㅁ And Not ㄹ
									if (categoryList.contains(arr2n.get(indexOfAnd - 1)))
										filteredList.remove(e);
									else if(arr2n.get(indexOfAnd - 1).equals(e.getInNout()))
										filteredList.remove(e);
									if (categoryList.contains(arr2n.get(indexOfAnd + 2)))
										filteredList.remove(e);
									else if(arr2n.get(indexOfAnd + 2).equals(e.getInNout()))
										filteredList.remove(e);
								} else if (!arr2n.get(indexOfAnd + 1).equals("Not")) {// Not ㅁ And ㄹ
									if (categoryList.contains(arr2n.get(indexOfAnd - 1)))
										filteredList.remove(e);
									else if(arr2n.get(indexOfAnd - 1).equals(e.getInNout()))
										filteredList.remove(e);
									if (categoryList.contains(arr2n.get(indexOfAnd + 1))
											&& !categoryList.contains(arr2n.get(indexOfAnd - 1)))
										filteredList.add(e);
									else if(categoryList.contains(arr2n.get(indexOfAnd + 1))
											&& !arr2n.get(indexOfAnd - 1).equals(e.getInNout()))
										filteredList.add(e);
									else if(arr2n.get(indexOfAnd + 1).equals(e.getInNout())
											&& !categoryList.contains(arr2n.get(indexOfAnd - 1)))
										filteredList.add(e);
								}
							}
						} else {// ㅁ And Not ㄹ
							if (arr2n.get(indexOfAnd + 1).equals("Not")) {
								if (categoryList.contains(arr2n.get(indexOfAnd + 2))) {
									filteredList.remove(e);
								}else if(arr2n.get(indexOfAnd + 2).equals(e.getInNout()))
									filteredList.remove(e);
								if (categoryList.contains(arr2n.get(indexOfAnd - 1))
										&& !categoryList.contains(arr2n.get(indexOfAnd + 2))) {
									filteredList.add(e);
								}else if(arr2n.get(indexOfAnd - 1).equals(e.getInNout())
										&& !categoryList.contains(arr2n.get(indexOfAnd + 2))) {
									filteredList.add(e);
								}else if(categoryList.contains(arr2n.get(indexOfAnd - 1))
										|| !arr2n.get(indexOfAnd + 2).equals(e.getInNout()))
									filteredList.add(e);
							}
						}
					}

					for (AccountBookVO e : LASTaccountList)// 저번달
					{
						ArrayList<String> categoryList = new ArrayList<>(Arrays.asList(e.getCategory().split(" ")));
						if (indexOfAnd - 2 >= 0) {
							if (arr2n.get(indexOfAnd - 2).equals("Not")) {
								if (arr2n.get(indexOfAnd + 1).equals("Not")) {// Not ㅁ And Not ㄹ
									if (categoryList.contains(arr2n.get(indexOfAnd - 1)))
										filteredList2.remove(e);
									else if(arr2n.get(indexOfAnd - 1).equals(e.getInNout()))
										filteredList2.remove(e);
									if (categoryList.contains(arr2n.get(indexOfAnd + 2)))
										filteredList2.remove(e);
									else if(arr2n.get(indexOfAnd + 2).equals(e.getInNout()))
										filteredList2.remove(e);
								} else if (!arr2n.get(indexOfAnd -2).equals("Not")) {// Not ㅁ And ㄹ
									if (categoryList.contains(arr2n.get(indexOfAnd - 1)))
										filteredList2.remove(e);
									else if(arr2n.get(indexOfAnd - 1).equals(e.getInNout()))
										filteredList2.remove(e);
									if (categoryList.contains(arr2n.get(indexOfAnd + 1))
											&& !categoryList.contains(arr2n.get(indexOfAnd - 1)))
										filteredList2.add(e);
									else if(categoryList.contains(arr2n.get(indexOfAnd + 1))
											&&!arr2n.get(indexOfAnd - 1).equals(e.getInNout()))
										filteredList2.add(e);
									else if(arr2n.get(indexOfAnd - 1).equals(e.getInNout())
											&&!categoryList.contains(arr2n.get(indexOfAnd - 1)))
										filteredList2.add(e);
								}

							} else {// ㅁ And Not ㄹ
								if (arr2n.get(indexOfAnd + 1).equals("Not")) {
									if (categoryList.contains(arr2n.get(indexOfAnd + 2)))
										filteredList2.remove(e);
									else if(arr2n.get(indexOfAnd + 2).equals(e.getInNout()))
										filteredList2.remove(e);
									if (categoryList.contains(arr2n.get(indexOfAnd - 1))
											&& !categoryList.contains(arr2n.get(indexOfAnd + 2)))
										filteredList2.add(e);
									else if(arr2n.get(indexOfAnd - 1).equals(e.getInNout())
											&& !categoryList.contains(arr2n.get(indexOfAnd + 2)))
										filteredList2.add(e);
									else if(categoryList.contains(arr2n.get(indexOfAnd - 1))
											&& !arr2n.get(indexOfAnd + 2).equals(e.getInNout()))
										filteredList2.add(e);
								}

							}

						}
					}
					if (indexOfAnd - 2 >= 0) {// 왼쪽에 Not이 들어간 경우

						if (arr2n.get(indexOfAnd - 2).equals("Not")) {
							arr2n.remove(indexOfAnd - 2);
							arr2n.remove(indexOfAnd - 2);
						}

						arr2n.remove(indexOfAnd - 2);// 연산자 제거

						if (arr2n.get(indexOfAnd - 2).equals("Not")) {
							arr2n.remove(indexOfAnd - 2);
							arr2n.remove(indexOfAnd - 2);
						} else {
							arr2n.remove(indexOfAnd - 2);

						}

					} else// 처음에 Not이 들어가지 않은 경우
					{
						arr2n.remove(indexOfAnd - 1);
						arr2n.remove(indexOfAnd - 1);// 연산자 제거

						if (arr2n.get(indexOfAnd - 1).equals("Not")) {
							arr2n.remove(indexOfAnd - 1);
							arr2n.remove(indexOfAnd - 1);
						} else {
							arr2n.remove(indexOfAnd - 1);

						}
					}

				}

			}

			while (arr2n.contains("Or")) {
				if (arr2n.contains("Or")) {
					int indexOfOr = arr2n.indexOf("Or");

					for (AccountBookVO e : accountList)// 요번달
					{
						ArrayList<String> categoryList = new ArrayList<>(Arrays.asList(e.getCategory().split(" ")));
						if (indexOfOr - 2 >= 0) {
							if (arr2n.get(indexOfOr - 2).equals("Not"))// Not ㅁ Or Not ㄹ
							{
								if (arr2n.get(indexOfOr + 1).equals("Not")) {
									if (categoryList.contains(arr2n.get(indexOfOr - 1))
											|| categoryList.contains(arr2n.get(indexOfOr + 2))
											|| arr2n.get(indexOfOr - 1).equals(e.getInNout())
											|| arr2n.get(indexOfOr + 2).equals(e.getInNout()))
										filteredList.remove(e);
								} else if (!arr2n.get(indexOfOr + 1).equals("Not")) {// Not ㅁ or ㅎ
									if (categoryList.contains(arr2n.get(indexOfOr - 1)))
										filteredList.remove(e);
									else if(arr2n.get(indexOfOr - 1).equals(e.getInNout()))
										filteredList.remove(e);
									if (categoryList.contains(arr2n.get(indexOfOr + 1)))
										filteredList.add(e);
									else if(arr2n.get(indexOfOr + 1).equals(e.getInNout()))
										filteredList.add(e);
								}
							}
							}
							else {
								if (arr2n.get(indexOfOr + 1).equals("Not")) {// ㅁ Or Not ㄹ
									if (categoryList.contains(arr2n.get(indexOfOr - 1)))
										filteredList.add(e);
									else if(arr2n.get(indexOfOr - 1).equals(e.getInNout()))
										filteredList.add(e);
									if (categoryList.contains(arr2n.get(indexOfOr + 2)))
										filteredList.remove(e);
									else if(arr2n.get(indexOfOr + 2).equals(e.getInNout()))
										filteredList.remove(e);
									
								}

							}

						

					}

					for (AccountBookVO e : LASTaccountList)// 저번달
					{
						ArrayList<String> categoryList = new ArrayList<>(Arrays.asList(e.getCategory().split(" ")));
						if (indexOfOr - 2 >= 0) {
							if (arr2n.get(indexOfOr - 2).equals("Not"))// Not ㅁ Or Not ㄹ
							{
								if (arr2n.get(indexOfOr + 1).equals("Not")) {
									if (categoryList.contains(arr2n.get(indexOfOr - 1))
											|| categoryList.contains(arr2n.get(indexOfOr + 2)))
										filteredList2.remove(e);
									else if(arr2n.get(indexOfOr - 1).equals(e.getInNout())
											|| arr2n.get(indexOfOr + 2).equals(e.getInNout()))
										filteredList2.remove(e);
								} else if (!arr2n.get(indexOfOr + 1).equals("Not")) {// not ㅁ or ㅎ
									if (categoryList.contains(arr2n.get(indexOfOr - 1)))
										filteredList2.remove(e);
									else if(arr2n.get(indexOfOr - 1).equals(e.getInNout()))
										filteredList2.remove(e);
									if (categoryList.contains(arr2n.get(indexOfOr + 1)))
										filteredList2.add(e);
									else if(arr2n.get(indexOfOr + 1).equals(e.getInNout()))
										filteredList2.add(e);
								}

							} else {// ㅁ Or Not ㄹ
								if (arr2n.get(indexOfOr + 1).equals("Not")) {
									if (categoryList.contains(arr2n.get(indexOfOr + 2)))
										filteredList2.remove(e);
									else if(arr2n.get(indexOfOr + 2).equals(e.getInNout()))
										filteredList2.remove(e);
									if (categoryList.contains(arr2n.get(indexOfOr - 1)))
										filteredList2.add(e);
									else if(arr2n.get(indexOfOr - 1).equals(e.getInNout()))
										filteredList2.add(e);
								}

							}

						}

					}
					if (indexOfOr - 2 >= 0) {// 왼쪽에 Not이 들어간 경우

						if (arr2n.get(indexOfOr - 2).equals("Not")) {
							arr2n.remove(indexOfOr - 2);
							arr2n.remove(indexOfOr - 2);
						}

						arr2n.remove(indexOfOr - 2);// 연산자 제거

						if (arr2n.get(indexOfOr - 2).equals("Not")) {
							arr2n.remove(indexOfOr - 2);
							arr2n.remove(indexOfOr - 2);
						} else {
							arr2n.remove(indexOfOr - 2);

						}

					} else// 처음에 Not이 들어가지 않은 경우
					{
						arr2n.remove(indexOfOr - 1);
						arr2n.remove(indexOfOr - 1);// 연산자 제거

						if (arr2n.get(indexOfOr - 1).equals("Not")) {
							arr2n.remove(indexOfOr - 1);
							arr2n.remove(indexOfOr - 1);
						} else {
							arr2n.remove(indexOfOr - 1);

						}
					}
					// Or가 없는 경우
					if (!arr2n.contains(("Or"))) {
						if (arr2n.contains("Not")) {
							int indexOfNot = arr2n.indexOf("Not");
							for (AccountBookVO e : accountList)// 요번달
							{
								ArrayList<String> categoryList = new ArrayList<>(
										Arrays.asList(e.getCategory().split(" ")));
								if (!categoryList.contains(arr2n.get(indexOfNot + 1)))
									filteredList.add(e);
								else if(!arr2n.get(indexOfNot + 1).equals(e.getInNout()))
									filteredList.add(e);
							}

						} else {

							for (AccountBookVO e : accountList)// 요번달
							{
								ArrayList<String> categoryList = new ArrayList<>(
										Arrays.asList(e.getCategory().split(" ")));
								if(!arr2n.isEmpty()) {
									if (categoryList.contains(arr2n.get(0)))
										filteredList.add(e);
									else if(arr2n.get(0).equals(e.getInNout()))
										filteredList.add(e);
								}
		
							}

						}

						if (arr2n.contains("Not")) {
							int indexOfNot = arr2n.indexOf("Not");
							for (AccountBookVO e : LASTaccountList)// 저번달
							{
								ArrayList<String> categoryList = new ArrayList<>(
										Arrays.asList(e.getCategory().split(" ")));
								if (!categoryList.contains(arr2n.get(indexOfNot + 1)))
									filteredList2.add(e);
								else if(!arr2n.get(indexOfNot + 1).equals(e.getInNout()))
									filteredList2.add(e);
							}
							arr2n.remove(0);
							arr2n.remove(0);
						} else {

							for (AccountBookVO e : LASTaccountList)// 요번달
							{
								ArrayList<String> categoryList = new ArrayList<>(
										Arrays.asList(e.getCategory().split(" ")));
								if(!arr2n.isEmpty()) {
									if (categoryList.contains(arr2n.get(0)))
										filteredList2.add(e);
									else if(arr2n.get(0).equals(e.getInNout()))
										filteredList2.add(e);
								}
								
							}
							if(!arr2n.isEmpty())
								arr2n.remove(0);
						}

					}

				}
			}

		}

		Set<Integer> seenIndexes = new HashSet<>();

		// filteredList 중복 제거
		Iterator<AccountBookVO> iterator = filteredList.iterator();
		while (iterator.hasNext()) {
			AccountBookVO item = iterator.next();
			if (!seenIndexes.add(item.getIndexNumber())) {
				iterator.remove(); // 중복된 항목이면 제거
			}
		}

		// filteredList2 중복 제거
		seenIndexes.clear(); // seenIndexes 초기화
		iterator = filteredList2.iterator();
		while (iterator.hasNext()) {
			AccountBookVO item = iterator.next();
			if (!seenIndexes.add(item.getIndexNumber())) {
				iterator.remove(); // 중복된 항목이면 제거
			}
		}

	}

	private static ArrayList<int[]> mergeConsecutiveParts(ArrayList<int[]> andExist) {
	    ArrayList<int[]> mergedList = new ArrayList<>();
	    if (andExist.size() == 0) {
	        return mergedList; // 빈 리스트일 경우 빈 리스트 반환
	    }

	 
	    int size = andExist.size()-1;
	    
	    for (int i = 0; i < size; i++) {
	        int currentStart = andExist.get(i)[0];
	        int currentEnd = andExist.get(i)[1];

	        while (andExist.get(i+1)[0] - currentEnd == 1) {
	        	currentEnd = andExist.get(i+1)[1];
	        	i++;
	        	if (i == size)
	        		break;
	            
	        } 
	        mergedList.add(new int[]{currentStart, currentEnd});
	        if (i == size)
        		break;
	        
	    }


	    return mergedList;
	}


	private void removeAndStrings(ArrayList<String> arr2n) {
		Iterator<String> iterator = arr2n.iterator();
        while (iterator.hasNext()) {
            String item = iterator.next();
            if (item.equals("And")) {
                iterator.remove();
            }
        }	
	}
	private void removeAndStrings2(ArrayList<String> arr2n) {
		Iterator<String> iterator = arr2n.iterator();
        while (iterator.hasNext()) {
            String item = iterator.next();
            if (item.equals("Or")) {
                iterator.remove();
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
		if(!detectNotNot(date)) {
			System.out.println("올바른 논리연산자가 아닙니다. 알맞은 And, Or, Not을 사용해주세요.");
			return false;
		}

		ArrayList<String> tempList = new ArrayList<>(Arrays.asList(date.split(" ")));
		if (!AndOrNot(tempList))// 유효한 카테고리인지 검사
			return false;


		if (spaceCount == 2) {// 공백 개수가 2인 경우 = 카테고리도 받음
			String yearAndDay = date.substring(0, space);
			String category = date.substring(space + 1, date.length());

			// System.out.println(yearAndDay);
			// System.out.println(category);

			// 년도 범위 valid 체크
			for (int i = 0; i < yearAndDay.length(); i++) {
				if (date.charAt(i) == ' ') {
					space = i;

				}

			}
			String year = yearAndDay.substring(0, space);
			// System.out.println(year);
			if (year.length() == 2)// 보완필요 (1) isDateValid 함수내에서 년도 + 20 혹은 년도 + 19인지 판별 어떻게
									// 이거 2자리일때는 무조건 20을 붙인다고 기획서에 되어있답니다.
			{
				try {
					int temp = Integer.parseInt(year);

					temp = 2000 + temp;
					if (temp < 1902 || temp > 2037) {
						return false;
					}
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
			} else if (year.length() != 4) {
				// System.out.println("1902~2037 중에서 년도를 입력하고, 01~12 중에서 월을 입력해주세요");
				// System.out.println("---------------------------------------------------");

				return false;
			}
			// 월 범위 valid 체크
			String month = yearAndDay.substring(space + 1, yearAndDay.length());
			// System.out.println(month);
			char[] monthChar = month.toCharArray();
			if (monthChar.length > 2) {
				// System.out.println("1902~2037 중에서 년도를 입력하고, 01~12 중에서 월을 입력해주세요");
				// System.out.println("---------------------------------------------------");

				return false;
			}
			if (monthChar.length == 2 && monthChar[0] == '0')
				month = String.valueOf(monthChar[1]);
			else if (monthChar.length == 2 && monthChar[0] != '0')
				month = "" + month;
			if (!monthIsInRange(month)) {
				// System.out.println("1902~2037 중에서 년도를 입력하고, 01~12 중에서 월을 입력해주세요");
				// System.out.println("---------------------------------------------------");

				return false;
			}

			// 카테고리 존재여부
			if (!availableCategories.contains(category)) {
				System.out.println("해당 카테고리가 존재하지 않습니다. 등록된 카테고리명을 입력해주세요.");
				return false;
			}

			// 위의 조건들을 다 만족하는 경우, true 반환
			categoryIn = true;
			return true;

		}
		if (spaceCount == 1) {// 공백 개수가 1인 경우 = 카테고리 받지 않음
			// 년도 범위 valid 체크
			String year = date.substring(0, space);
			if (year.length() == 2)// 보완필요 (1) isDateValid 함수내에서 년도 + 20 혹은 년도 + 19인지 판별 어떻게
									// 이거 2자리일때는 무조건 20을 붙인다고 기획서에 되어있답니다.
			{
				try {
					int temp = Integer.parseInt(year);

					temp = 2000 + temp;
					if (temp < 1902 || temp > 2037) {
						return false;
					}
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
			} else
				return false;
			// 월 범위 valid 체크
			String month = date.substring(space + 1, date.length());
			char[] monthChar = month.toCharArray();
			if (monthChar.length > 2) {
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
		} else if ((date.contains("Or") || date.contains("And") || date.contains("Not"))) {
			ArrayList<String> dateList = new ArrayList<>(Arrays.asList(date.split(" ")));
			// year month
			// 년도 범위 valid 체크
			String year = dateList.get(0);
			if (year.length() == 2) {
				try {
					int temp = Integer.parseInt(year);

					temp = 2000 + temp;
					if (temp < 1902 || temp > 2037) {
						return false;
					}
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
			} else
				return false;
			// 월 범위 valid 체크
			String month = dateList.get(1);
			char[] monthChar = month.toCharArray();
			if (monthChar.length > 2) {
				return false;
			}
			if (monthChar.length == 2 && monthChar[0] == '0')
				month = String.valueOf(monthChar[1]);
			else if (monthChar.length == 2 && monthChar[0] != '0')
				month = "" + month;
			if (!monthIsInRange(month))
				return false;

			// 카테고리 부분
			categoryIn = true;

			if (AndOrNot(dateList))// 유효한 카테고리인지 검사
				return true;

		} else {
			System.out.println("올바른 논리연산자가 아닙니다. 알맞은 And, Or, Not을 사용해주세요.");
			return false;
		}
		return false;

	}

	private boolean isValidOperator(String operator) {
		return operator.equals("Or") || operator.equals("And") || operator.equals("Not");
	}

	private boolean isValidCategory(String category) {
		return availableCategories.contains(category);
	}
	private boolean containsAlphabet(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isLetter(c)) {
                return true; // 알파벳이 하나라도 발견되면 true 반환
            }
        }
        return false; // 문자열에 알파벳이 없으면 false 반환
    }

	private boolean AndOrNot(ArrayList<String> dateList) {
		dateList.remove(0);
		dateList.remove(0);

		for (String element : dateList) {
			if (isValidOperator(element)) {
				// Skip operators
				continue;
			} else if (isValidCategory(element)) {
				// Valid category
				continue;
			}else if(!isValidOperator(element) && containsAlphabet(element)) {
				System.out.println("올바른 논리연산자가 아닙니다. 알맞은 And, Or, Not을 사용해주세요.");
				return false;
			}else {
				// Invalid category
				System.out.println("해당 카테고리가 존재하지 않습니다. 등록된 카테고리명을 입력해주세요.");
				return false;
			}
		}
		return true;
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

	public long getTotalIncome(String date) {// date = 2023 03
		// 총계에서 수입 구하는 함수
		// 전월에 대한 월과 년도 정하기
		int currentMonthNum = Integer.parseInt(date.substring(5, date.length()));
		int currentYearNum = Integer.parseInt(date.substring(0, 4));
		int previousMonthNum = 0;
		int previousYearNum = currentYearNum;
		String previousDate = "";
		long result = 0;// return 하는 값

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
		return result;

	}

	public long getTotalOutflow(String date) {
		long result = 0; // 리턴하는 값
		// 이제 현월 (date)에 대한 총 수입 가져오기
		accountList = dao.getAccountForMonth(date);
		for (AccountBookVO e : accountList) {
			if (e.getInNout().equals("지출")) {
				result += e.getAmount();
			}
		}
		return result;
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
		if (input2.length() == 0) {
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
		} catch (Exception e) {
			return false;
		}
		return flag;
	}

	public boolean isValidIndex2(String input) {
		String input2 = input.trim();
		boolean flag = false;
		if (input2.length() == 0) {
			return false;
		}
		try {
			int inputToInt = Integer.parseInt(input2);
			for (AccountBookVO e : filteredList) {
				if (e.getIndexNumber() == inputToInt) {
					flag = true;
					inputToString = Integer.toString(inputToInt);
				}
			}
		} catch (Exception e) {
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
		totalIncome = getTotalIncome(date);// 총수입
		totalOutflow = getTotalOutflow(date);// 총지출
		lastDate = getPrevious(date);// 지난달 예)2
		// System.out.println(lastDate);
		String lastdateyearmonth;
		if (Integer.parseInt(lastDate) != 12) {
			lastdateyearmonth = date.substring(0, 4);
			if (lastDate.length() == 1) {
				lastdateyearmonth += " 0";
				lastdateyearmonth += lastDate;
			} else {
				lastdateyearmonth += " " + lastDate;
			}

		} else {
			lastdateyearmonth = Integer.toString(Integer.parseInt(date.substring(0, 4)) - 1);
			lastdateyearmonth += " 12";
		}

//	    카테고리 출력 길이에 따른 출력 열 공백 계산 (공백 길이 카운트)
//		String categorySpace = ""; // 카테고리 출력 길이에 해당하는 공백
		int NumofLongest = 0; // 카테고리 갯수가 가장 많은 내역의 출력 순서 번호를 저장하는 변수
		ArrayList<String> over6 = new ArrayList<String>(); // 카테고리 이름이 6을 넘을 떄마다 기록
		for (int i = 0; i < accountList.size(); i++) {
			String categories[] = accountList.get(i).getCategory().split(" "); // 한 내역의 모든 카테고리를 저장한 배열
			if (categories.length > NumofLongest)
				NumofLongest = i; // 내역마다 카테고리 갯수를 비교해 가장 많은 카테고리를 가진 내역의 출력 순서 번호를 저장
			for (String c : categories)
				if (c.length() >= 6 && !over6.contains(c))
					over6.add(c);
		}
		 if(accountList.size()!=0) {
			System.out.println(date + "\t\t수입\t\t지출\t\t내용\t\t인덱스\t카테고리");
			System.out.println("총계\t\t" + String.format("%,-10d\t", +(long) totalIncome)
	
					+ String.format("%,-10d\t", (long) totalOutflow) + "--");
		 }else {
			 System.out.println(date + "\t\t수입\t\t지출\t\t내용\t\t인덱스");
			 System.out.println("총계\t\t" + String.format("%,-10d\t", +(long) totalIncome)
				
				+ String.format("%,-10d\t", (long) totalOutflow) + "--\t\t--");
		 }

		Comparator<AccountBookVO> dateComparator = new Comparator<AccountBookVO>() {
			@Override
			public int compare(AccountBookVO o1, AccountBookVO o2) {
				// date를 기준으로 비교하여 정렬
				try {
					return Integer.parseInt(o2.getDate().substring(8)) - Integer.parseInt(o1.getDate().substring(8));
				} catch (NumberFormatException e) {
					return 0;
				}
			}
		};

		// Comparator를 사용하여 ArrayList 정렬
		Collections.sort(accountList, dateComparator);

		if (accountList != null) {
			for (int i = 0; i < accountList.size(); i++) {
				try {
					System.out.print(Integer.parseInt(accountList.get(i).getDate().substring(5, 7)));
					System.out.print("." + Integer.parseInt(accountList.get(i).getDate().substring(8)));
				} catch (NumberFormatException e) {
					break;
				}
				
				boolean hasOver6 = false;
				String category[] = accountList.get(i).getCategory().split(" ");
				String printCategory = "";
				printCategory += category[0];
				for (String c : category) {
					if (over6.contains(c))
						hasOver6 = true;
					if (c.equals(category[0]))
						continue;
					printCategory += "|" + c;
				}


				if (accountList.get(i).getInNout().compareTo("수입") == 0) {
					System.out.print("\t\t" + String.format("%,-10d\t\t", accountList.get(i).getAmount()));
				} else {
					System.out.print("\t\t\t\t" + String.format("%,-10d", accountList.get(i).getAmount()));
				}
				System.out.print("\t"+accountList.get(i).getDetails()+"\t");
				if(accountList.get(i).getDetails().length()>6) {
					System.out.print(accountList.get(i).getIndexNumber());
				}else {
					System.out.print("\t" + accountList.get(i).getIndexNumber());
				}
				
				if (accountList.get(i).getCategory().contains(" "))
					System.out.print("\t" + accountList.get(i).getCategory().replace(" ", "|"));
				else
					System.out.print("\t" + accountList.get(i).getCategory());

//		    	카테고리 출력 + 공백 계산
				System.out.println();
			}
		}

		LASTaccountList = dao.getAccountForMonth(lastdateyearmonth);
		long lastMonthSumIn = 0;
		long lastMonthSumOut = 0;

		if (LASTaccountList != null) {
			for (int i = 0; i < LASTaccountList.size(); i++) {
				if (LASTaccountList.get(i).getInNout().compareTo("수입") == 0) {
					lastMonthSumIn += LASTaccountList.get(i).getAmount();
				} else {
					lastMonthSumOut += LASTaccountList.get(i).getAmount();
				}
			}
		}
		String print;
		if (lastdateyearmonth.substring(5, 6).equals("0")) {
			print = " " + lastdateyearmonth.substring(6, 7);
		} else {
			print = lastdateyearmonth.substring(5, 7);
		}
		if(accountList.size()!=0) {
			System.out.println(print + "월 이월분\t" + String.format("%,-10d\t", (long) lastMonthSumIn)
			+ String.format("%,-10d\t", (long) lastMonthSumOut) + "--");
		}else {
			System.out.println(print + "월 이월분\t" + String.format("%,-10d\t", (long) lastMonthSumIn)
			+ String.format("%,-10d\t", (long) lastMonthSumOut) + "--\t\t--");
		}
		System.out.println("------------------------------------------------------------");
	}

	public void showCurrentAccount2(String date, String category) {

		accountList = dao.getAccountForMonth(date);
		filteredList = new ArrayList<>();
		long totalIncome = 0;
		long totalOutflow = 0;

		for (AccountBookVO e : accountList) {
			if (e.getCategory().contains(category) || category.equals(e.getInNout())) {
				filteredList.add(e);
				if (e.getInNout().equals("수입")) {
					totalIncome += e.getAmount();
				} else {
					totalOutflow += e.getAmount();
				}
			}
		}

		lastDate = getPrevious(date);// 지난달 예)2
		// System.out.println(lastDate);
		String lastdateyearmonth;
		if (Integer.parseInt(lastDate) != 12) {
			lastdateyearmonth = date.substring(0, 4);
			if (lastDate.length() == 1) {
				lastdateyearmonth += " 0";
				lastdateyearmonth += lastDate;
			} else {
				lastdateyearmonth += " " + lastDate;
			}

		} else {
			lastdateyearmonth = Integer.toString(Integer.parseInt(date.substring(0, 4)) - 1);
			lastdateyearmonth += " 12";
		}

	    int NumofLongest = 0;	// 카테고리 갯수가 가장 많은 내역의 출력 순서 번호를 저장하는 변수
	    ArrayList<String> over6 = new ArrayList<String>(); //카테고리 이름이 6을 넘을 떄마다 기록
	    for (int i = 0; i < filteredList.size(); i++) {
	        String categories[] = filteredList.get(i).getCategory().split(" "); // 한 내역의 모든 카테고리를 저장한 배열
	        if (categories.length > NumofLongest)
	        	NumofLongest = i; // 내역마다 카테고리 갯수를 비교해 가장 많은 카테고리를 가진 내역의 출력 순서 번호를 저장
	        for (String c : categories)
	        	if (c.length() >= 6 && !over6.contains(c))
	        		over6.add(c);
	    }
	    if(filteredList.size()!=0) {
	    	System.out.println(date + "\t\t수입\t\t지출\t\t내용\t\t인덱스\t카테고리");
		    System.out.println("총계\t\t" + String.format("%,-10d\t", +(long) totalIncome)
	
			+ String.format("%,-10d\t", (long) totalOutflow) + "--");
	    }
	    else {
	    	System.out.println(date + "\t\t수입\t\t지출\t\t내용\t\t인덱스");
	    	System.out.println("총계\t\t" + String.format("%,-10d\t", +(long) totalIncome)
	    	
			+ String.format("%,-10d\t", (long) totalOutflow) + "--\t\t--");
	    }

		if (filteredList != null) {
			Collections.sort(filteredList, new Comparator<AccountBookVO>() {
				@Override
				public int compare(AccountBookVO o1, AccountBookVO o2) {
					try {
						return Integer.parseInt(o2.getDate().substring(8))
								- Integer.parseInt(o1.getDate().substring(8));
					} catch (NumberFormatException e) {
						return 0;
					}
				}
			});
			for (int i = 0; i < filteredList.size(); i++) {
				System.out.print(filteredList.get(i).getDate().substring(5));

//		    	카테고리 출력 + 공백 계산
		    	boolean hasOver6 = false;
		        String categories[] = filteredList.get(i).getCategory().split(" ");
		        String printCategory = "";
		        printCategory += categories[0];
		        for (String c : categories) {
		        	if (over6.contains(c))	hasOver6 = true;
		        	if (c.equals(categories[0])) continue;
		        	printCategory += "|" + c;
		        }
		        
				
		        if (filteredList.get(i).getInNout().compareTo("수입") == 0) {
					System.out.print("\t\t" + String.format("%,-10d\t\t", filteredList.get(i).getAmount()));
				} else {
					System.out.print("\t\t\t\t" + String.format("%,-10d", filteredList.get(i).getAmount()));
				}
		        System.out.print("\t"+filteredList.get(i).getDetails()+"\t");
		        if(filteredList.get(i).getDetails().length()>6) {
					System.out.print(filteredList.get(i).getIndexNumber());
				}else {
					System.out.print("\t" + filteredList.get(i).getIndexNumber());
				}
		        
		        if (filteredList.get(i).getCategory().contains(" "))
					System.out.print("\t" + filteredList.get(i).getCategory().replace(" ", "|"));
				else
					System.out.print("\t" + filteredList.get(i).getCategory());
				System.out.println();
			}
		}

		LASTaccountList = dao.getAccountForMonth(lastdateyearmonth);// 지난달
		filteredList2 = new ArrayList<>();// 지난달+카테고리
		long lastMonthSumIn = 0;
		long lastMonthSumOut = 0;
		for (AccountBookVO e : LASTaccountList) {
			if (e.getCategory().contains(category)) {
				filteredList2.add(e);
			}
		}

		if (filteredList2 != null) {

			for (int i = 0; i < filteredList2.size(); i++) {
				if (filteredList2.get(i).getInNout().compareTo("수입") == 0) {
					lastMonthSumIn += filteredList2.get(i).getAmount();
				} else {
					lastMonthSumOut += filteredList2.get(i).getAmount();
				}
			}
		}

		String print;
		if (lastdateyearmonth.substring(5, 6).equals("0")) {
			print = " " + lastdateyearmonth.substring(6, 7);
		} else {
			print = lastdateyearmonth.substring(5, 7);
		}
		if(accountList.size()!=0) {
			System.out.println(print + "월 이월분\t" + String.format("%,-10d\t", (long) lastMonthSumIn)
			+ String.format("%,-10d\t", (long) lastMonthSumOut) + "--");
		}else {
			System.out.println(print + "월 이월분\t" + String.format("%,-10d\t", (long) lastMonthSumIn)
			+ String.format("%,-10d\t", (long) lastMonthSumOut) + "\t--\t\t--");
		}
		
		System.out.println("------------------------------------------------------------");

	}

	private void showCurrentAccount3(String modifiedDate, ArrayList<String> arr2n) {

		long totalIncome = 0;
		long totalOutflow = 0;
		long lastMonthSumIn = 0;
		long lastMonthSumOut = 0;
		// arr2n "AND / NOT / OR"고려해서 filteredList,filteredList2 채우기
		ConsiderCategoryAndOrNot(arr2n, modifiedDate);

		if (filteredList2 != null) {

			for (int i = 0; i < filteredList2.size(); i++) {
				if (filteredList2.get(i).getInNout().compareTo("수입") == 0) {
					lastMonthSumIn += filteredList2.get(i).getAmount();
				} else {
					lastMonthSumOut += filteredList2.get(i).getAmount();
				}
			}
		}
		
		for (AccountBookVO e : filteredList) {

			if (e.getInNout().equals("수입")) {
				totalIncome += e.getAmount();
			} else {
				totalOutflow += e.getAmount();
			}

		}
		totalIncome += lastMonthSumIn;
		totalOutflow += lastMonthSumOut;
		
		if(filteredList.size()!=0) {
			System.out.println(modifiedDate + "\t\t수입\t\t지출\t\t내용\t\t인덱스\t카테고리");
			System.out.println("총계\t\t" + String.format("%,-10d\t", +(long) totalIncome)

			+ String.format("%,-10d\t", (long) totalOutflow) + "--");
		}else {
			System.out.println(modifiedDate + "\t\t수입\t\t지출\t\t내용\t\t인덱스");
			System.out.println("총계\t\t" + String.format("%,-10d\t", +(long) totalIncome)

			+ String.format("%,-10d\t", (long) totalOutflow) + "--\t\t--");
		}
		

		if (filteredList != null) {
			Collections.sort(filteredList, new Comparator<AccountBookVO>() {
				@Override
				public int compare(AccountBookVO o1, AccountBookVO o2) {
					try {
						return Integer.parseInt(o2.getDate().substring(8))
								- Integer.parseInt(o1.getDate().substring(8));
					} catch (NumberFormatException e) {
						return 0;
					}
				}
			});
			for (int i = 0; i < filteredList.size(); i++) {
				System.out.print(filteredList.get(i).getDate().substring(5));

				if (filteredList.get(i).getInNout().compareTo("수입") == 0) {
					System.out.print("\t\t" + String.format("%,-10d\t\t", filteredList.get(i).getAmount()));
				} else {
					System.out.print("\t\t\t\t" + String.format("%,-10d", filteredList.get(i).getAmount()));
				}
				System.out.print("\t"+filteredList.get(i).getDetails()+"\t");
				if(filteredList.get(i).getDetails().length()>6) {
					System.out.print(filteredList.get(i).getIndexNumber());
				}else {
					System.out.print("\t" + filteredList.get(i).getIndexNumber());
				}
				if (filteredList.get(i).getCategory().contains(" "))
					System.out.print("\t" + filteredList.get(i).getCategory().replace(" ", "|"));
				else
					System.out.print("\t" + filteredList.get(i).getCategory());
				System.out.println();
			}
		}

		
		// 다음 부분을 위한 부분
		lastDate = getPrevious(modifiedDate);// 지난달 예)2
		String lastdateyearmonth;
		if (Integer.parseInt(lastDate) != 12) {
			lastdateyearmonth = modifiedDate.substring(0, 4);
			if (lastDate.length() == 1) {
				lastdateyearmonth += " 0";
				lastdateyearmonth += lastDate;
			} else {
				lastdateyearmonth += " " + lastDate;
			}

		} else {
			lastdateyearmonth = Integer.toString(Integer.parseInt(modifiedDate.substring(0, 4)) - 1);
			lastdateyearmonth += " 12";
		}

		String print;
		if (lastdateyearmonth.substring(5, 6).equals("0")) {
			print = " " + lastdateyearmonth.substring(6, 7);
		} else {
			print = lastdateyearmonth.substring(5, 7);
		}
		if(filteredList.size()!=0) {
			System.out.println(print + "월 이월분\t" + String.format("%,-10d\t", (long) lastMonthSumIn)
			+ String.format("%,-10d\t", (long) lastMonthSumOut) + "--");
		}else {
			System.out.println(print + "월 이월분\t" + String.format("%,-10d\t", (long) lastMonthSumIn)
			+ String.format("%,-10d\t", (long) lastMonthSumOut) + "--\t\t--");
		}
		
		System.out.println("------------------------------------------------------------");

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
			date = sc2.nextLine().trim().replaceAll("\\s+", " ");

			if (isDateValid(date)) {
				System.out.println("------------------------------------------------------------");
				break;
			}
			// if (!isDateValid(date))
			System.out.println("------------------------------------------------------------");
		}
	}

	public boolean validFor1or2(String input) {
		try {
			String temp = input.trim();
			if (temp.length() == 0) {
				return false;
			}
			int temp2 = Integer.parseInt(temp);
			if (temp2 != 1 && temp2 != 2) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public void setBasicCategory(ArrayList<CategoryVO> categoryList) {

		String arr[] = { "월급", "부수입", "용돈", "상여", "금융소득" };
		String arr2[] = { "식비", "문화생활", "경조사/회비", "주거/통신", "교통/차량" };
		for (int i = 0; i < arr.length; i++) {
			CategoryVO category = new CategoryVO("", "");
			category.setInNout("수입");

			category.setCategory(arr[i]);
			categoryList.add(category);

		}
		for (int i = 0; i < arr2.length; i++) {
			CategoryVO category = new CategoryVO("", "");
			category.setInNout("지출");

			category.setCategory(arr2[i]);
			categoryList.add(category);

		}

	}
	public boolean detectNotNot(String input) {
	      
        int index = input.indexOf("Not Not");

        if (index != -1) {
            return false;
        }
        // "Not Not" 패턴이 발견되지 않으면 true 반환
        return true;
    }

}