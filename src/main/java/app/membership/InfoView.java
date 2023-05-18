package app.membership;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import app.AppService;
import app.AppView;
import dao.DAO;
import gui.Gui;
import gui.panel.button.ButtonPanel;
import gui.panel.layout.BorderLayoutPanel;
import gui.table.StringTable;
import util.StrUtil;

public class InfoView extends AppView{
	private int tableIndex = 1;
	private JLabel titleLabel = Gui.createLabel(Color.darkGray, new Font("맑은 고딕", Font.BOLD, 20), JLabel.CENTER);
	private JPanel tablePanel = new JPanel(new BorderLayout());
	
	public InfoView(Membership membership) {
		super("멤버십 정보", membership);
		initRootPanel();
	}
	
	@Override
	public void initRootPanel() {
		BorderLayoutPanel rootblPanel = new BorderLayoutPanel();
		rootPanel = rootblPanel.getPanel();
		
		BorderLayoutPanel northPanel = new BorderLayoutPanel();
		rootblPanel.addNorth(northPanel);
		northPanel.setBackground(Color.LIGHT_GRAY);
		northPanel.addCenter(titleLabel);
		northPanel.addWest(Gui.createButton("◀◀ ", b->createTable(-1)));
		northPanel.addEast(Gui.createButton(" ▶▶", b->createTable(+1)));
		rootblPanel.addCenter(tablePanel);
		
		BorderLayoutPanel bottomPanel = new BorderLayoutPanel();
		ButtonPanel botBtnPanel = new ButtonPanel();
		rootblPanel.addSouth(botBtnPanel);
		botBtnPanel.setLayout(Gui.flowLayout(FlowLayout.LEFT));
		bottomPanel.addWest(botBtnPanel);
		rootblPanel.addSouth(bottomPanel);
	}
	
	public void createTable(int i) {
		final int tableCount = 2;
		tableIndex += i;
		System.out.println(tableIndex);
		if(tableIndex <= 0) tableIndex = tableCount;
		if(tableIndex > tableCount) tableIndex = 1;
		
		String title = "# 마일리지 " + (tableIndex != 1 ? "사용 내역" : "적립 내역") ;
		String tableName = "MILEAGE";
		String[] columns = {"NUM","TIMESTAMP","ID","MILECHANGE","DETAIL"};
		String query = StrUtil.selectQuery(tableName, columns, "WHERE ID = ? AND MILECHANGE " + (tableIndex != 1 ? "< 0" : " > 0") + "ORDER BY TIMESTAMP DESC"); 
		
		titleLabel.setText(title);
		StringTable table = new StringTable(DAO.sql.select(query, AppService.instance().getAttr("id")), columns);
		
		tablePanel.removeAll();
		table.setColumnsSize(50,130,100,100,300);
		tablePanel.add(new JScrollPane(table));
	}
}