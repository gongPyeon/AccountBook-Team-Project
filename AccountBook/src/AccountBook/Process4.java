package AccountBook;

import java.util.ArrayList;
import java.util.Scanner;

public class Process4 {
    private AccountBookDao dao = new AccountBookDao();
    private ArrayList<String> income = new ArrayList<>();
    private ArrayList<String> consumption = new ArrayList<>();

    Scanner scanner = new Scanner(System.in);

    public Process4() {
        select_options();
    }

    private void select_options() {
        while (true) {
            try {
                System.out.println("1) 카테고리 추가");
                System.out.println("2) 카테고리 수정");
                System.out.println("3) 카테고리 삭제");
                System.out.println("4) 메인화면으로 돌아가기");
                System.out.print("입력하세요 > ");
                String input = scanner.nextLine();
				System.out.println("------------------------------------------------------------");

                if (input.trim().length() != 1) {
                    System.out.println("잘못 입력했습니다. 범위(1~4) 안에서 다시 선택해주세요");
    				System.out.println("------------------------------------------------------------");
                } else if (Integer.parseInt(input.trim()) == 1) {
                    String inNout = input_inNout();
                    add_category(inNout);
                    return;
                } else if (Integer.parseInt(input.trim()) == 2) {
                    String inNout = input_inNout();
                    fix_category(inNout);
                    return;
                } else if (Integer.parseInt(input.trim()) == 3) {
                    String inNout = input_inNout();
                    delete_category(inNout);
                    return;
                } else if (Integer.parseInt(input.trim()) == 4) {
                    return;
                } else {
                    System.out.println("잘못 입력했습니다. 범위(1~4) 안에서 다시 선택해주세요");
    				System.out.println("------------------------------------------------------------");
                }

            } catch (Exception e) {
                System.out.println("잘못 입력했습니다. 범위(1~4) 안에서 다시 선택해주세요");
				System.out.println("------------------------------------------------------------");
            }
        }

    }

    private void add_category(String inNout) {
        System.out.println(" - 카테고리 추가 - ");
        show_categoryList(inNout);


        while (true) {
            System.out.print("추가할 카테고리를 입력하세요 > ");
            String input = scanner.nextLine();
            String stringWithoutSpaces = input.trim().replace(" ", "");

            if (inNout.equals("수입")) {
                if (!input.trim().isEmpty() && input.length() <= 10 && stringWithoutSpaces.equals(input.trim()) && !income.contains(input.trim())) {
                    CategoryVO categoryVO = new CategoryVO(inNout, input.trim());

                    dao.InsertCategoryInCategorytable(categoryVO);

                    System.out.println("추가가 완료되었습니다.");
    				System.out.println("------------------------------------------------------------");
                    return;

                } else {
                    System.out.println("다시 입력해 주세요");
    				System.out.println("------------------------------------------------------------");
                }
            } else { // inNout가 지출인 경우
                if (!input.trim().isEmpty() && input.length() <= 10 && stringWithoutSpaces.equals(input.trim()) && !consumption.contains(input.trim())) {

                    CategoryVO categoryVO = new CategoryVO(inNout, input.trim());

                    dao.InsertCategoryInCategorytable(categoryVO);

                    System.out.println("추가가 완료되었습니다.");
    				System.out.println("------------------------------------------------------------");
                    return;
                } else {
                    System.out.println("다시 입력해 주세요");
    				System.out.println("------------------------------------------------------------");
                }
            }
        }
    }

    private void fix_category(String inNout) {
        System.out.println(" - 카테고리 수정 - ");

        int value = show_categoryList(inNout);

        if (value == 1) {
            System.out.println("등록된 카테고리가 없습니다.");
			System.out.println("------------------------------------------------------------");
            return;
        }

        while (true) {
            System.out.print("수정할 카테고리를 입력하세요 > ");
            String input = scanner.nextLine();

            if (input.length() > 10 || !input.trim().contains(":")) {
                System.out.println("다시 입력해 주세요");
				System.out.println("------------------------------------------------------------");
            } else {

                String[] parts = input.trim().split(":");

                if (parts.length == 2) {
                    String old_C = parts[0].trim();
                    String new_C = parts[1].trim();
                    if (inNout.equals("수입")) {
                        if (!old_C.equals(new_C) && !income.contains(new_C)) {

                            CategoryVO oldOne = new CategoryVO(inNout, old_C);
                            CategoryVO newOne = new CategoryVO(inNout, new_C);

                            dao.updateCategoryInCategorytable(oldOne, newOne);

                            System.out.println("수정이 완료되었습니다.");
            				System.out.println("------------------------------------------------------------");

                            return;
                        }
                        else{
                            System.out.println("다시 입력해 주세요");
            				System.out.println("------------------------------------------------------------");
                        }
                    } else if (inNout.equals("지출")) {
                        if (!old_C.equals(new_C) && !consumption.contains(new_C)) {

                            CategoryVO oldOne = new CategoryVO(inNout, old_C);
                            CategoryVO newOne = new CategoryVO(inNout, new_C);

                            dao.updateCategoryInCategorytable(oldOne, newOne);

                            System.out.println("수정이 완료되었습니다.");
            				System.out.println("------------------------------------------------------------");

                            return;
                        }
                        else{
                            System.out.println("다시 입력해 주세요");
            				System.out.println("------------------------------------------------------------");
                        }
                    } else {
                        System.out.println("다시 입력해 주세요");
        				System.out.println("------------------------------------------------------------");
                    }
                } else {
    				System.out.println("------------------------------------------------------------");
                }
            }
        }
    }

    private void delete_category(String inNout) {
        System.out.println(" - 카테고리 삭제 - ");

        int value = show_categoryList(inNout);

        if (value == 1) {
            System.out.println("등록된 카테고리가 없습니다.");
			System.out.println("------------------------------------------------------------");
            return;
        }

        while (true) {
            System.out.print("삭제할 카테고리를 입력하세요 > ");
            String input = scanner.nextLine();
            String stringWithoutSpaces = input.trim().replace(" ", "");

            if (inNout.equals("수입")) {
                if (!input.trim().isEmpty() && input.length() <= 10 && stringWithoutSpaces.equals(input.trim()) && income.contains(input.trim())) {
                    CategoryVO categoryVO = new CategoryVO(inNout, input.trim());

                    dao.deleteCategoryInCategorytable(categoryVO);

                    System.out.println("삭제가 완료되었습니다.");
    				System.out.println("------------------------------------------------------------");
                    return;

                } else {
                    System.out.println("다시 입력해 주세요");
    				System.out.println("------------------------------------------------------------");
                }
            } else { // inNout가 지출인 경우
                if (!input.trim().isEmpty() && input.length() <= 10 && stringWithoutSpaces.equals(input.trim()) && consumption.contains(input.trim())) {

                    CategoryVO categoryVO = new CategoryVO(inNout, input.trim());

                    dao.deleteCategoryInCategorytable(categoryVO);

                    System.out.println("삭제가 완료되었습니다.");
    				System.out.println("------------------------------------------------------------");
                    return;
                    
                } else {
                    System.out.println("다시 입력해 주세요");
    				System.out.println("------------------------------------------------------------");
                }
            }
        }
    }

    private String input_inNout() {
        String input;
        while (true) {
            System.out.print("\"수입\" 혹은 \"지출\"을 입력하세요 > ");
            input = scanner.nextLine();
			System.out.println("------------------------------------------------------------");
            if (input.length() <= 10) {
                input = input.trim();
                if (Is_valid_inNout(input)) {
                    break;
                } else {
                    System.out.println("입력 가능한 문자열이 아닙니다.");
    				System.out.println("------------------------------------------------------------");
                }
            }
        }
        return input;

    }

    private boolean Is_valid_inNout(String e) {
        return e.equals("수입") || e.equals("지출");
    }

    private int show_categoryList(String inNout) {
        boolean first = true;
        System.out.print(inNout + " 카테고리 목록 (");
        if (inNout.equals("수입")) {
            ArrayList<CategoryVO> categoryInList = dao.getCategoriesWithIn();

            for (CategoryVO categoryVO : categoryInList)
                income.add(categoryVO.getCategory());

            if (income.isEmpty()) {
                System.out.println(")");
                return 1;
            } else {
                for (String s : income) {
                    if (first) {
                        System.out.print(s);
                        first = false;
                    } else {
                        System.out.printf(" | " + s);
                    }
                }
                System.out.println(")");
                return 0;
            }

        } else {// inNout.equals("지출"){
            ArrayList<CategoryVO> categoryOutList = dao.getCategoriesWithOut();

            for (CategoryVO categoryVO : categoryOutList)
                consumption.add(categoryVO.getCategory());

            if (consumption.isEmpty()) {
                System.out.println(")");
                return 1;
            } else {
                for (String s : consumption) {
                    if (first) {
                        System.out.print(s);
                        first = false;
                    } else {
                        System.out.printf(" | " + s);
                    }
                }
                System.out.println(")");
                return 0;
            }
        }
    }

}