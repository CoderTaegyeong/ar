package test;

import dao.DAO;
import dao.SqlUtil;
import entity.TicketDTO;
import gui.Gui;
import gui.wiget.ZonedClock;

public class J {
	public static void main(String[] args) {
		String q = "UPDATE TICKET SET RESERVEDATE = 'aaa123xa' WHERE customerid = 'aaa'";
		System.out.println(DAO.sql.getJdbcTemplate().update(q));
//		SqlUtil sql = new SqlUtil(DAO.getDataSource());
////		sql.getJdbcTemplate().update(q);
		
	}
}
