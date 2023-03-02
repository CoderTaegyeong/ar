package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import entity.SeatDTO;

public class SeatDAO {
	private Connection conn = null;

	// Constructor
	public SeatDAO() {
		conn = DBConn.getInstance();
	}

	public void close() {
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

//--------------------------------------SeatDAO.getSeatList ( seatGrade가 없어서 수정 )  --------------------------------------------------

	public ArrayList<String> getSeatList(String airnum, String depDate, String seatGrade) {

		String sql = "";
		sql += "SELECT S.SEATNUMBER, S.DEPDATE ";
		sql += " FROM SEAT S JOIN AIRPLAN A ON S.AIRNUM = A.AIRNUM";
		sql += " WHERE S.AIRNUM = ? AND S.DEPDATE = ? AND S.SEATGRADE = ? AND S.RESERVED = 'Y'";
		ArrayList<String> list = new ArrayList<>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, airnum);
			pstmt.setString(2, depDate);
			pstmt.setString(3, seatGrade);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String seatNumber = rs.getString("SEATNUM");
				list.add(seatNumber);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
			}
		}
		return list;
	}

//----------------------------- seatDAO.setSeatReserved(); SeatGrade가 없어서수정-----------------------------------
	public void setSeatReserved(SeatDTO seatDTO) {
		String airnum = seatDTO.getAirnum();
		String seatNumber = seatDTO.getSeatNumber();
		String seatGrade = seatDTO.getSeatGrade();
		String depdate = seatDTO.getDepDate();

		String sql = "";
		sql += "INSERT INTO SEAT ";
		sql += " VALUES (?,?,?,?,?) ";

		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, airnum);
			pstmt.setString(2, seatNumber);
			pstmt.setString(3, seatGrade);
			pstmt.setString(4, "Y");
			pstmt.setString(5, depdate);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		}
		try {
			if (pstmt != null)
				pstmt.close();
		} catch (SQLException e) {
		}
	}
}
