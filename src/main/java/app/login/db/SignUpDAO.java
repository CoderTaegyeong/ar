package app.login.db;

import java.util.ArrayList;
import java.util.List;

import entity.MemberDTO;


public class SignUpDAO extends DBConn{

	public SignUpDAO() {}
	// 회원가입 db insert
	public int SignUpInsert(MemberDTO vo) {
		int result = 0;
		try{
			getConn();
			String sql = "";
			sql += "INSERT INTO Members";
			sql += "  ( ID, NAME, PASSWORD, PHONE, EMAIL)";
			sql += " VALUES ";
			sql += "  ( ?, ?, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getId());
			pstmt.setString(2, vo.getName());
			pstmt.setString(3, vo.getPassword());
			pstmt.setString(4, vo.getPhone());
			pstmt.setString(5, vo.getEmail());
			result = pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally{
			dbClose();
		}
		return result;
	}
	// 회원 아이디 비밀번호 검색, 회원 유무 확인
	public List<MemberDTO> getidCheck(String getID){
		List<MemberDTO> lst = new ArrayList<MemberDTO>();		
		try {
			getConn();
			sql = "select ID from Members where ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, getID);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				MemberDTO vo = new MemberDTO();
				vo.setId(rs.getString(1));
				
				lst.add(vo);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}finally {
			dbClose();
		}
		
		return lst;
	}
}