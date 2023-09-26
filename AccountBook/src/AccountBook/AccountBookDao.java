package AccountBook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountBookDao{
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
			pstmt.setString(1, date);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				AccountBookVO re = new AccountBookVO();
				re.setIndexNumber(rs.getInt("indexNumber"));
				re.setDate(rs.getString("dage"));
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
	
	public void updateAmount(int amount,int indexNumber) {
		String sql = "update AccountBook set amount=? where indexNumber=?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, amount);
			pstmt.setInt(2, indexNumber);
			pstmt.executeUpdate();
			} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt !=null && !pstmt.isClosed())
					pstmt.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	public void updateDetails(String details,int indexNumber) {
		String sql = "update AccountBook set details=? where indexNumber=?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, details);
			pstmt.setInt(2, indexNumber);
			pstmt.executeUpdate();
			} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt !=null && !pstmt.isClosed())
					pstmt.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public void updateInNOut(String inNout,int indexNumber) {
		String sql = "update AccountBook set inNout=? where indexNumber=?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, inNout);
			pstmt.setInt(2, indexNumber);
			pstmt.executeUpdate();
			} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt !=null && !pstmt.isClosed())
					pstmt.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	
	public void delete_schedule(int indexNumber) {
			String sql = "delete from AccountBook where indexNumber=?;";
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1,indexNumber);
				pstmt.executeUpdate();
			} catch (Exception e) {
				System.out.println("데이터베이스 에러발생");
			}finally {
				try {
					if(pstmt !=null && !pstmt.isClosed())
						pstmt.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}

	} 

}
