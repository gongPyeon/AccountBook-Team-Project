package AccountBook;

import java.util.ArrayList;
import java.util.Scanner;

public class Process2 {
    private AccountBookDao dao = new AccountBookDao();
    private String DB_date; // DB로 들어갈 날짜
    private String DB_inNout; // DB로 들어갈 상위 카테고리
    private String DB_category; // DB로 들어갈 하위 카테고리
    private int DB_amount;
    private String DB_details;

    Scanner scanner = new Scanner(System.in);


    public Process2() {
        //dao.delete_schedule(1);    // 이런 식으로 데이터베이스에서 사용합니다.
        input_Date();
    }

    private void input_Date() {

        while (true) {
            System.out.print("\"년+월+일\"을 입력하세요 > ");
            String input = scanner.nextLine();
            System.out.println("---------------------------------------------------");
            input = input.trim();
            String validationCheckInput = input.replaceAll("\\s+", "");
//			System.out.println(validationCheckInput);
            if (Is_valid_date(validationCheckInput)) {
                input_inNout();
                break;
            } else {
                System.out.println("입력 가능한 문자열이 아닙니다.");
                System.out.println("---------------------------------------------------");
            }
        }
    }

    private void input_inNout() {
//		System.out.println(date_Input);
        while (true) {
            System.out.print("\"수입\" 혹은 \"지출\"을 입력하세요 > ");
            String input = scanner.nextLine();
            System.out.println("---------------------------------------------------");
            input = input.trim();
            if (Is_valid_inNout(input)) {
                input_category(this.DB_inNout);
                break;
            } else {
                System.out.println("입력 가능한 문자열이 아닙니다.");
                System.out.println("---------------------------------------------------");
            }
        }

    }

    private void input_category(String inNout) {
        while (true) {
            if (inNout.equals("수입")) {
                System.out.print("카테고리를 입력해주세요(월급 | 부수입 | 용돈 | 상여 | 금융소득 | 기타) >");
            } else {// inNout.equals("지출"){
                System.out.print("카테고리를 입력해주세요(식비 | 문화생활 | 경조사/회비 | 주거/통신 | 교통/차량 | 기타) >");
            }
            String input = scanner.nextLine();
            System.out.println("---------------------------------------------------");
            input = input.trim();
            if (Is_valid_category(input, inNout)) {
                input_amount();
                break;
            } else {
                System.out.println("입력 가능한 문자열이 아닙니다.");
                System.out.println("---------------------------------------------------");
            }

        }

    }

    public boolean Is_valid_date(String e) {
        String p1, p2, p3;

        if (e.length() == 8) {
            p1 = e.substring(0, 4);
            p2 = e.substring(4, 6);
            p3 = e.substring(6);
        } else if (e.length() == 6) {
            p1 = e.substring(0, 2);
            p1 = "20" + p1;
            p2 = e.substring(2, 4);
            p3 = e.substring(4);
        } else {
            return false;
        }

        // 유효한 날짜인지 검사
        if (Is_valid_date2(p1, p2, p3)) {

            this.DB_date = p1 + " " + p2 + " " + p3;
//			System.out.println(date_Input);
            return true;
        } else {
            return false;
        }

    }

    public boolean Is_valid_date2(String p1, String p2, String p3) {
        try {
            int year = Integer.parseInt(p1);
            int month = Integer.parseInt(p2);
            int day = Integer.parseInt(p3);

            // 31일까지 있는 달을 ArrayList로 초기화합니다.
            ArrayList<Integer> monthsWith31Days = new ArrayList<>();
            monthsWith31Days.add(1);
            monthsWith31Days.add(3);
            monthsWith31Days.add(5);
            monthsWith31Days.add(7);
            monthsWith31Days.add(8);
            monthsWith31Days.add(10);
            monthsWith31Days.add(12);

            ArrayList<Integer> monthsWith30Days = new ArrayList<>();
            monthsWith30Days.add(4);
            monthsWith30Days.add(6);
            monthsWith30Days.add(9);
            monthsWith30Days.add(11);

            if (year <= 1901 || year >= 2038) {
                return false;
            } else {
                if (year % 4 == 0) { // 윤년
                    if (monthsWith31Days.contains(month)) {
                        if (day >= 0 && day <= 31)
                            return true;
                        else
                            return false;
                    } else if (monthsWith30Days.contains(month)) {
                        if (day >= 0 && day <= 30)
                            return true;
                        else
                            return false;
                    } else if (month == 2) {
                        if (day >= 0 && day <= 29)
                            return true;
                        else
                            return false;
                    } else {
                        return false;
                    }
                } else { // non-윤년
                    if (monthsWith31Days.contains(month)) {
                        if (day >= 0 && day <= 31)
                            return true;
                        else
                            return false;
                    } else if (monthsWith30Days.contains(month)) {
                        if (day >= 0 && day <= 30)
                            return true;
                        else
                            return false;
                    } else if (month == 2) {
                        if (day >= 0 && day <= 28)
                            return true;
                        else
                            return false;
                    } else {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean Is_valid_inNout(String e) {
        if (e.equals("수입") || e.equals("지출")) {
            this.DB_inNout = e;
            return true;
        } else {
            return false;
        }
    }

    public boolean Is_valid_category(String e, String inNout) {
        ArrayList<String> income = new ArrayList<>();
        income.add("월급");
        income.add("부수입");
        income.add("용돈");
        income.add("상여");
        income.add("금융소득");
        income.add("기타");

        ArrayList<String> consumption = new ArrayList<>();
        consumption.add("식비");
        consumption.add("문화생활");
        consumption.add("경조사/회비");
        consumption.add("주거/통신");
        consumption.add("교통/차량");
        consumption.add("기타");

        if (inNout.equals("수입")) {
            if (income.contains(e)) {
                this.DB_category = e;
                return true;
            }
        } else if (inNout.equals("지출")) {
            if (consumption.contains(e)) {
                this.DB_category = e;
                return true;
            }
        }
        return false;
    }
    public boolean Is_vaild_empty(String command) {
		if(command == null || command.trim().isEmpty()) {// string 안에 문자가 있는 경우
			System.out.println("---------------------------------------------------");
			return false;
		}
		return true;
	}
	
	public void input_amount() {
		String command;
		while(true) {
			try {
				System.out.print("금액을 입력하세요 > ");
				command =scanner.nextLine();
				if(Is_vaild_empty(command)) { //enter만을 입력하는 경우, 공백이 포함된 경우
					DB_amount = Integer.parseInt(command); //string 안에 문자가 있거나, float 등 다른 자료형일 경우
					System.out.println("---------------------------------------------------");
					input_details();
					break;
				}
			}catch(Exception e) {
				System.out.println("---------------------------------------------------");
			}
		}
	}
	
	public void input_details() {
		while(true) {
			try {
				System.out.print("세부내역을 입력하세요 > ");
				DB_details =scanner.nextLine();
				if(Is_vaild_empty(DB_details)) { //enter만을 입력하는 경우, 공백이 포함된 경우
					System.out.println("---------------------------------------------------");
					System.out.println("등록이 완료되었습니다");
					System.out.println("DB_date : " + this.DB_date);
	                System.out.println("DB_inNout : " + this.DB_inNout);
	                System.out.println("DB_category : " + this.DB_category);
	                System.out.println("DB_amount : " + this.DB_amount);
	                System.out.println("DB_details : " + this.DB_details);
					System.out.println("---------------------------------------------------");
					break;
				}
			}catch(Exception e) {
				System.out.println("---------------------------------------------------");
			}
		}
	}
    
}
