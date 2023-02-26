package app.schedule;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import app.AppView;
import dao.DAO;
import dao.SqlUtil;
import entity.MemberDTO;
import entity.mapper.MemberRowMapper;
import gui.Gui;
import gui.table.DataTable;

public class ScheduleView extends AppView{
	public ScheduleView(Schedule schedule) {
		super(schedule);
		initRootPanel();
	}
	@Override
	public void initRootPanel() {
		rootPanel.setLayout(new BorderLayout());
		rootPanel.setSize(new Dimension(300, 300));

		DataTable s2 = new DataTable(MemberDTO.class, "aa22a", "bbb22", "c22cc");
//		s2.setColumnsSize(30,100,20);
//		s2.setDataList(DAO.sql.select("select * from Members", new MemberRowMapper()));
//		rootPanel.add(new JScrollPane(s2));
//		
//		rootPanel.add(Gui.createButton("a", b->{System.out.println(s2.getModel().getColumnName(1));
//		s2.getTableHeader().repaint();
//		}), "East");
		
//		rootPanel.add(Gui.createButton("a", b->{s2.setColumnNames(new String[] {"123","32","23"});}), "West");;
	}
	
	public void redrawColumnHeaders(JTable table, String[] columnNames) {
	    TableColumnModel columnModel = table.getColumnModel();
	    for (int i = 0; i < columnNames.length; i++) {
	        TableColumn column = columnModel.getColumn(i);
	        column.setHeaderValue(columnNames[i]);
	    }
	    table.getTableHeader().repaint(); // 테이블 헤더 다시 그리기
	}
	
	public static void main(String[] args) {
		Gui.createFrame(new ScheduleView(null).rootPanel);
	}
}