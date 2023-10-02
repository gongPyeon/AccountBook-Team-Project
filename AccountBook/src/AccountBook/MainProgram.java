package AccountBook;

import java.util.ArrayList;
import java.util.Scanner;

public class MainProgram {

	int menu;
	private Process1 process1;
	private Process2 process2;
	private Process3 process3;
	private Process4 process4;
	private Process5 process5;
	private String process_input;

	public MainProgram() {
		while (true) {
			System.out.println("> A11 Account book");
			System.out.println("1. Account book View (가계부보기)");
			System.out.println("2. Registration (항목 등록)");
			System.out.println("3. Remove (항목 지우기)"); // 추후 구현 예정
			System.out.println("4. Update(항목 수정) "); // 추후 구현 예정
			System.out.println("5. Help(도움말)");
			System.out.println("6. Quit (종료)");
			System.out.println("---------------------------------------------------");
			System.out.print("A11 Account book : menu > ");
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(System.in);

			try {
				process_input = sc.nextLine();
				System.out.println("---------------------------------------------------");
				if (Is_valid(process_input)) {
					menu = Integer.parseInt(process_input);
					if (menu == 1) 
						process1 = new Process1();
					else if (menu == 2) 
						process2 = new Process2();
					else if (menu == 3) 
						process3 = new Process3();
					else if (menu == 4) 
						process4 = new Process4();
					else if (menu == 5) { 
						process5 = new Process5();
					}
					else if (menu == 6) {
						System.out.println("A11 Account book를 종료합니다.");
						break;
					}else {
						System.out.println("잘못 입력했습니다. 범위(1~6) 안에서 다시 선택해주세요");
						System.out.println("---------------------------------------------------");
					}
				}else {
					System.out.println("잘못 입력했습니다. 범위(1~6) 안에서 다시 선택해주세요");
					System.out.println("---------------------------------------------------");
				}
			} catch (Exception e) {
				System.out.println("잘못 입력했습니다. 범위(1~6) 안에서 다시 선택해주세요");
				System.out.println("---------------------------------------------------");

			}

		}

	}

	public boolean Is_valid(String e) {
		if (e.length() != 1) {
			e = e.trim();
			if(e.length()!=1)
				return false;
			else {
				int analysis = Integer.parseInt(e);
				if (analysis < 1 || analysis > 6)
					return false;
				else
					this.process_input = Integer.toString(analysis);
					return true;
			}
			}
		int analysis = Integer.parseInt(e);
		if (analysis < 1 || analysis > 6)
			return false;
		else
			return true;
	}

}