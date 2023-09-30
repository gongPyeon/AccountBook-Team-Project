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

public class AccountBookDao {// DB를 다루는 클라스
	private Connection conn;
	private static final String USERNAME = "root";
	private static final String PASSWORD = "root";

	private static final String URL = "jdbc:mysql://localhost/AccountBook";

	public AccountBookDao() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

		} catch (Exception e) {
			System.out.println("DB�뿰寃곗씠 �떎�뙣�뻽�뒿�땲�떎. �삤瑜섎�� �닔�젙 �썑 �떎�떆 �떆�룄�빐二쇱꽭�슂" + e);
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
			System.out.print("");// "�빆紐⑹씠 �벑濡앸릺�뿀�뒿�땲�떎.
		} catch (Exception e) {
			System.out.println("�뜲�씠�꽣 �궫�엯 �떎�뙣");

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
			System.out.println("�뜲�씠�꽣踰좎씠�뒪 �옉�뾽 以� �뿉�윭諛쒖깮");
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

}
