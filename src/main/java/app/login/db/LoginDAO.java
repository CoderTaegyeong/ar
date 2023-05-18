package app.login.db;

import java.util.ArrayList;
import java.util.List;

import entity.MemberDTO;

public class LoginDAO extends DBConn {

	public LoginDAO() {}

	public ArrayList<MemberDTO> LoginAllSelect() {
		
		ArrayList<MemberDTO> AVO = new ArrayList<MemberDTO>();
		try {
			getConn();
			sql = "select id, password from Members";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				MemberDTO vo = new MemberDTO();
				vo.setId(rs.getString("id"));
				vo.setPassword("password");
				AVO.add(vo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
		return AVO;
	}

	public int getLogin(String userid, String userpwd) {
		List<MemberDTO> AVO = new ArrayList<MemberDTO>();

		int state = 0;
		try {
			getConn();
			sql = "select id, password from members where id = ? and password = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			pstmt.setString(2, userpwd);
			rs = pstmt.executeQuery();
			if (rs.next())
				state = 1;
		} catch (Exception e) {
			System.out.println("DB 아이디 또는 패스워드 에러" + e.getMessage());
		} finally {
			dbClose();
		}
		return state;
	}

}