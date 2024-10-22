package AccountBook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountBookDao {// DB를 다루는 클래스
	private Connection conn;
	private static final String USERNAME = "root";
	private static final String PASSWORD = "root";

	private static final String URL = "jdbc:mysql://localhost/AccountBook";

	public AccountBookDao() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

		} catch (Exception e) {
			System.out.println("DB연결이 실패했습니다. 오류를 수정 후 다시 시도해주세요" + e);
		}

	}

	public void InsertAccountBook(AccountBookVO accountBookVO) {

		String sql = "insert into AccountBook(date,inNout,category,amount,details) values (?,?,?,?,?);";
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, accountBookVO.getDate());
			pstmt.setString(2, accountBookVO.getInNout());
			pstmt.setString(3, accountBookVO.getCategory());
			pstmt.setInt(4, accountBookVO.getAmount());
			pstmt.setString(5, accountBookVO.getDetails());

			pstmt.executeUpdate();
			System.out.print("");//"항목이 등록되었습니다.
		} catch (Exception e) {
			System.out.println("데이터 삽입 실패");

		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}
	}

	public ArrayList<AccountBookVO> getAccountList(String date) {
		String sql = "select * from AccountBook where date = ?;";
		PreparedStatement pstmt = null;
		ArrayList<AccountBookVO> result = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, date);// 첫번째 물음표
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				AccountBookVO re = new AccountBookVO();
				re.setIndexNumber(rs.getInt("indexNumber"));
				re.setDate(rs.getString("date"));
				re.setInNout(rs.getString("inNout"));
				re.setCategory(rs.getString("category"));
				re.setAmount(rs.getInt("amount"));
				re.setDetails(rs.getString("details"));
				result.add(re);
			}
		} catch (Exception e) {
			System.out.println("데이터베이스 작업 중 에러발생");
		}
		return result;
	}

	public ArrayList<AccountBookVO> getAccountMonthList(String date) {
		String sql = "select * from AccountBook WHERE SUBSTRING(date, 1, 7) = ?;";
		PreparedStatement pstmt = null;
		ArrayList<AccountBookVO> result = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, date);// 첫번째 물음표
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				AccountBookVO re = new AccountBookVO();
				re.setIndexNumber(rs.getInt("indexNumber"));
				re.setDate(rs.getString("date"));
				re.setInNout(rs.getString("inNout"));
				re.setCategory(rs.getString("category"));
				re.setAmount(rs.getInt("amount"));
				re.setDetails(rs.getString("details"));
				result.add(re);
			}
		} catch (Exception e) {
			System.out.println("데이터베이스 작업 중 에러발생");
		}
		return result;
	}
	
	public ArrayList<AccountBookVO> getAccountCategoryList(String date, String category) {
		String sql = "SELECT * FROM AccountBook WHERE SUBSTRING(date, 1, 7) = ? AND CONCAT(' ', category, ' ') Like ?;";
		PreparedStatement pstmt = null;
		ArrayList<AccountBookVO> result = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, date);// 첫번째 물음표
			pstmt.setString(2, "% " +category+" %");// 두번째 물음표
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				AccountBookVO re = new AccountBookVO();
				re.setIndexNumber(rs.getInt("indexNumber"));
				re.setDate(rs.getString("date"));
				re.setInNout(rs.getString("inNout"));
				re.setCategory(rs.getString("category"));
				re.setAmount(rs.getInt("amount"));
				re.setDetails(rs.getString("details"));
				result.add(re);
			}
		} catch (Exception e) {
			System.out.println("데이터베이스 작업 중 에러발생");
		}
		return result;
	}
	public void updateAmount(int amount, int indexNumber) {
		String sql = "update AccountBook set amount=? where indexNumber=?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, amount);
			pstmt.setInt(2, indexNumber);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public void updateDetails(String details, int indexNumber) {
		String sql = "update AccountBook set details=? where indexNumber=?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, details);
			pstmt.setInt(2, indexNumber);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public void updateInNOut(String inNout, int indexNumber) {
		String sql = "update AccountBook set inNout=? where indexNumber=?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, inNout);
			pstmt.setInt(2, indexNumber);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public void deleteAccount(int indexNumber) {

		String sql = "DELETE FROM AccountBook WHERE indexNumber = ?";
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, indexNumber);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public ArrayList<AccountBookVO> getAccountForMonth(String date) {
		String sql = "SELECT * FROM AccountBook WHERE date LIKE ?;";
		PreparedStatement pstmt = null;
		ArrayList<AccountBookVO> result = new ArrayList<>();

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, date + "%");// 첫번째 물음표

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				AccountBookVO re = new AccountBookVO();
				re.setIndexNumber(rs.getInt("indexNumber"));
				re.setDate(rs.getString("date"));
				re.setInNout(rs.getString("inNout"));
				re.setCategory(rs.getString("category"));
				re.setAmount(rs.getInt("amount"));
				re.setDetails(rs.getString("details"));
				result.add(re);
			}
		} catch (Exception e) {
			System.out.println("데이터베이스 작업 중 에러발생");
		}
		return result;
	}

	public void InsertCategoryInCategorytable(CategoryVO categoryVO) {

		String sql = "insert into savedcategory(inNout,category) values (?,?);";
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, categoryVO.getInNout());
			pstmt.setString(2, categoryVO.getCategory());
			pstmt.executeUpdate();
			//System.out.print("성공");// 데이터 삽입 성공
		} catch (Exception e) {
			System.out.println("에러났네...");

		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed())
					pstmt.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}
	}

	public void updateCategoryInCategorytable(CategoryVO oldOne, CategoryVO newOne) { // 기존의 정보를 oldOne에 바꾸고 싶은건 newOne에 넣기
		String categorySql = "update savedcategory set category=? where category=?;";
		PreparedStatement categoryPstmt = null;

		String accountBookSql = "update accountbook set category = replace(category, ?, ?) where category like ? and inNout = ?;";
		PreparedStatement accountBookPstmt = null;

		try {
			categoryPstmt = conn.prepareStatement(categorySql);
			categoryPstmt.setString(1, newOne.getCategory());
			categoryPstmt.setString(2, oldOne.getCategory());
			categoryPstmt.executeUpdate();

			accountBookPstmt = conn.prepareStatement(accountBookSql);
			accountBookPstmt.setString(1, oldOne.getCategory());
			accountBookPstmt.setString(2, newOne.getCategory());
			accountBookPstmt.setString(3, "%" + oldOne.getCategory() + "%"); // 원본 문자열을 포함하는 경우만 대체
			accountBookPstmt.setString(4, oldOne.getInNout());
			accountBookPstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (categoryPstmt != null && !categoryPstmt.isClosed() && accountBookPstmt != null && !accountBookPstmt.isClosed()){
					categoryPstmt.close();
					accountBookPstmt.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public void deleteCategoryInCategorytable(CategoryVO categoryVO) {  //지우고 싶은 category를 categoryVO로 만들어서 전달해야 함

		String categorySql = "DELETE FROM savedcategory WHERE category = ?";
		PreparedStatement categoryPstmt = null;

		String accountBookSql = "UPDATE accountbook " +
				"SET category = " +
				"CASE " +
				"WHEN category = ? THEN '----' " +
				"WHEN category LIKE ? THEN REPLACE(category, ? ' ', '') " +
				"WHEN category LIKE ? THEN REPLACE(category, ? ' ', '') " +
				"WHEN category LIKE ? THEN REPLACE(category, ' ' ?, '') " +
				"ELSE category " +
				"END;";

		PreparedStatement accountBookPstmt = null;

		try {
			categoryPstmt = conn.prepareStatement(categorySql);
			categoryPstmt.setString(1, categoryVO.getCategory());
			categoryPstmt.executeUpdate();

			accountBookPstmt = conn.prepareStatement(accountBookSql);
			accountBookPstmt.setString(1, categoryVO.getCategory());
			accountBookPstmt.setString(2, categoryVO.getCategory() + " %");
			accountBookPstmt.setString(3, categoryVO.getCategory());
			accountBookPstmt.setString(4, "% " + categoryVO.getCategory() + " %");
			accountBookPstmt.setString(5, categoryVO.getCategory());
			accountBookPstmt.setString(6, "% " + categoryVO.getCategory());
			accountBookPstmt.setString(7, categoryVO.getCategory());
			accountBookPstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (categoryPstmt != null && !categoryPstmt.isClosed() && accountBookPstmt != null && !accountBookPstmt.isClosed()){
					categoryPstmt.close();
					accountBookPstmt.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public ArrayList<CategoryVO> getCategoriesWithIn() {
		ArrayList<CategoryVO> categoryList = new ArrayList<>();
		String sql = "SELECT * FROM savedcategory WHERE inNout = '수입';";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				CategoryVO category = new CategoryVO("", "");
				category.setInNout(rs.getString("inNout"));
				category.setCategory(rs.getString("category"));
				categoryList.add(category);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return categoryList;
	}

	public ArrayList<CategoryVO> getCategoriesWithOut() {
		ArrayList<CategoryVO> categoryList = new ArrayList<>();
		String sql = "SELECT * FROM savedcategory WHERE inNout = '지출';";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				CategoryVO category = new CategoryVO("", "");
				category.setInNout(rs.getString("inNout"));
				category.setCategory(rs.getString("category"));
				categoryList.add(category);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return categoryList;
	}
	public ArrayList<String> getCategories1() {//process3에서 카테고리 존재 여부 확인할떄 사용
	    ArrayList<String> categoryList = new ArrayList<>();
	    String sql = "SELECT category FROM savedcategory;"; // Select only the category column
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    try {
	        pstmt = conn.prepareStatement(sql);
	        rs = pstmt.executeQuery();

	        while (rs.next()) {
	            String category = rs.getString("category");
	            categoryList.add(category);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) {
	                rs.close();
	            }
	            if (pstmt != null) {
	                pstmt.close();
	            }
	        } catch (Exception e2) {
	            e2.printStackTrace();
	        }
	    }

	    return categoryList;
	}

	public ArrayList<AccountBookVO> getAccountLogicalCategoryList(String date, String sqlStr) {
		String sql = sqlStr;
		PreparedStatement pstmt = null;
		ArrayList<AccountBookVO> result = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, date);// 첫번째 물음표
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				AccountBookVO re = new AccountBookVO();
				re.setIndexNumber(rs.getInt("indexNumber"));
				re.setDate(rs.getString("date"));
				re.setInNout(rs.getString("inNout"));
				re.setCategory(rs.getString("category"));
				re.setAmount(rs.getInt("amount"));
				re.setDetails(rs.getString("details"));
				result.add(re);
			}
		} catch (Exception e) {
			System.out.println("데이터베이스 작업 중 에러발생");
		}
		return result;
	}
	public int getTotalAmount(String date, String sqlStr) {
	    String sql = "SELECT SUM(amount) AS total_amount\n"
	    		+ "FROM AccountBook\n"
	    		+ "WHERE inNout = ?\n"
	    		+ sqlStr+"\n"
	    		+ "AND STR_TO_DATE(date, '%Y %m %d') < ?;";
	    PreparedStatement pstmt = null;
	    int totalAmount = 0;
	    
	    try {
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, "수입");
	        pstmt.setString(2, date);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            totalAmount = rs.getInt("total_amount");
	        }
	        
	        rs.close();
	    } catch (SQLException e) {
	        System.out.println("데이터베이스 작업 중 에러 발생");
	        e.printStackTrace();
	    }
	    
	    try {
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, "지출");
	        pstmt.setString(2, date);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            totalAmount -= rs.getInt("total_amount");
	        }
	        
	        rs.close();
	    } catch (SQLException e) {
	        System.out.println("데이터베이스 작업 중 에러 발생");
	        e.printStackTrace();
	    }
	    return totalAmount;
	}
}