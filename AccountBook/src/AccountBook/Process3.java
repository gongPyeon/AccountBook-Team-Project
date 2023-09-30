package AccountBook;

import java.util.ArrayList;
import java.util.Scanner;

public class Process3 {
	AccountBookDao dao = new AccountBookDao();
	ArrayList<AccountBookVO> accountList;
	String abbreviationMonth;
	String year;
	String month;
	int previousMonth;
	int totalIncome;
	int totalOutflow;
	int total;
	String previous; // 00 이월분 표시의 00

	public Process3() {
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.print("“년+월”을 입력하세요 > ");
			String date = sc.nextLine().trim();
			System.out.println("---------------------------------------------------");
			if (isDateValid(date)) {
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
				/*
				 * int income = 0; int outflow =0; for(AccountBookVO e : accountList) {
				 * if(e.getInNout().equals("수입")) income+=e.getAmount(); else
				 * outflow+=e.getAmount(); } income+=previousMonth;
				 */
				totalIncome = getTotalIncome(modifiedDate);
				totalOutflow = getTotalOutflow(modifiedDate);
				previous = getPrevious(modifiedDate);
				if (accountList.size() != 0) { // 가져온 내역이 있을 때
					// System.out.println(year + " " + abbreviationMonth + " 수입 지출 내용 인덱스");
					// System.out.println("총계 " + totalIncome + " " + totalOutflow
					// + " -- ");
					// //
					// System.out.println("--------------------------------------------------------------------------");
					// int numberOfDigits = 0;
					// int numberOfSpaces = 0;
					// String numberStr;
					// for (AccountBookVO e : accountList) {
					// // 정수를 문자열로 변환
					// numberStr = Integer.toString(e.getAmount());
					// // 문자열의 길이를 확인하여 자릿수를 얻음
					// numberOfDigits = numberStr.length();
					// numberOfSpaces = 32 - numberOfDigits;
					// if (e.getInNout().equals("수입")) {
					// System.out.print(
					// dayProcessing(e.getDate()) + " " + e.getCategory() + " " + e.getAmount());
					// for (int i = 0; i < numberOfSpaces; i++) {
					// System.out.print(" ");
					// }
					// System.out.println(e.getDetails() + " " + e.getIndexNumber());
					// } else {
					// System.out.print(
					// dayProcessing(e.getDate()) + " " + e.getCategory() + " " + e.getAmount());
					// for (int i = 0; i < numberOfSpaces; i++) {
					// System.out.print(" ");
					// }
					// System.out.println(e.getDetails() + " " + e.getIndexNumber());
					// }
					// }
					// System.out.println(previous + "월 이월분" + " " + previousMonth);
					// System.out.println("---------------------------------------------------");
					showCurrentAccount(modifiedDate);
					int input;
					while (true) {
						System.out.print("삭제할 인덱스를 입력해주세요> ");
						input = sc.nextInt();
						if (!isValidIndex(input)) {
							System.out.println("유효하지 않은 인덱스입니다");
							System.out.println("---------------------------------------------------");
						} else {
							break;
						}
					}
					dao.deleteAccount(input);
					System.out.println("---------------------------------------------------");
					System.out.println("삭제가 완료되었습니다");
					System.out.println("---------------------------------------------------");
					showCurrentAccount(date);
					int temp;
					while (true) {
						System.out.println("1) 추가 삭제 ");
						System.out.println("2) 메인화면으로 돌아가기");
						System.out.print("입력> ");
						temp = sc.nextInt();
						if (!(temp == 1 || temp == 2)) {
							System.out.println("---------------------------------------------------");
							System.out.println("유효하지 않은 숫자를 입력하셨습니다");
							continue;
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
					System.out.println(year + " " + abbreviationMonth + "		수입		지출		내용		인덱스");
					System.out.println("총계		0		0		--		--");
					System.out.println("---------------------------------------------------");
					System.out.println("삭제 가능한 항목이 없습니다.");
					int temp;
					while (true) {
						System.out.println("1) “년+월” 다시 입력하기");
						System.out.println("2) 메인화면으로 돌아가기");
						System.out.print("입력> ");
						temp = sc.nextInt();
						if (temp == 1)
							break;
						else if (temp == 2)
							break;

						System.out.println("---------------------------------------------------");
					}
					if (temp == 2) // 메인화면으로 돌아가기
						break;
				}
			}

		}
		sc.close();
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
		if (spaceCount != 1) // 공백이 두 개 이상 있는 경우 or 공백이 0개 있는 경우
			return false;
		// 년도 범위 valid 체크
		String year = date.substring(0, space);
		if (year.length() == 2)// 보완필요 (1) isDateValid 함수내에서 년도 + 20 혹은 년도 + 19인지 판별 어떻게
		{
			year = "20" + year;
		} else if (year.length() == 4)
			year = "" + year;
		else
			return false;
		// 월 범위 valid 체크
		String month = date.substring(space + 1, date.length());
		char[] monthChar = month.toCharArray();
		if (monthChar.length == 2 && monthChar[0] == '0')
			month = String.valueOf(monthChar[1]);
		else if (monthChar.length == 2 && monthChar[0] != '0')
			month = "" + month;
		if (!monthIsInRange(month))
			return false;
		else
			return true;
	}

	public boolean monthIsInRange(String month)// isDateValid함수 내에서 월의 범위가 유효한지 검사하는 함수
	{
		int m = Integer.parseInt(month);
		if (m >= 1 && m <= 12)
			return true;
		else
			return false;
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

	public boolean isValidIndex(int input) {
		boolean flag = false;
		for (AccountBookVO e : accountList) {
			if (e.getIndexNumber() == input)
				flag = true;
		}
		return flag;
	}

	public void showCurrentAccount(String date) {
		accountList = dao.getAccountForMonth(date);
		totalIncome = getTotalIncome(date);
		totalOutflow = getTotalOutflow(date);
		System.out.println(year + " " + abbreviationMonth + "		수입		지출		내용		인덱스");
		System.out.print("총계  		" + totalIncome);
		for (int i = 0; i < numberOfSpaces(totalIncome); i++) {
			System.out.print(" ");
		}
		System.out.print(totalOutflow);
		for (int i = 0; i < numberOfSpaces(totalOutflow); i++) {
			System.out.print(" ");
		}
		System.out.println("                 -- ");
		// System.out.println("--------------------------------------------------------------------------");
		for (AccountBookVO e : accountList) {
			if (e.getInNout().equals("수입")) {
				System.out.print(dayProcessing(e.getDate()) + " " + e.getCategory() + "        " + e.getAmount());
				for (int i = 0; i < numberOfSpaces(e.getAmount()); i++) {
					System.out.print(" ");
				}
				System.out.println("                " + e.getDetails() + "             " + e.getIndexNumber());
			} else {
				System.out.print(dayProcessing(e.getDate()) + " " + e.getCategory() + "                        "
						+ e.getAmount());
				for (int i = 0; i < numberOfSpaces(e.getAmount()); i++) {
					System.out.print(" ");
				}
				System.out.println(e.getDetails() + "             " + e.getIndexNumber());
			}
		}
		System.out.println(previous + "월 이월분" + " 	" + previousMonth);
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
}
