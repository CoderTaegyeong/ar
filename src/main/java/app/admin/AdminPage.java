package app.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import app.AppView;
import dao.DAO;
import gui.Gui;
import gui.panel.button.ButtonPanel;
import gui.panel.layout.BorderLayoutPanel;
import gui.table.StringTable;

public class AdminPage extends AppView{
	private AdminApp adminApp;
	private int tableIndex = 1;
	private JLabel titleLabel = Gui.createLabel(Color.darkGray, new Font("맑은 고딕", Font.BOLD, 20), JLabel.CENTER);
	private JPanel tablePanel = new JPanel(new BorderLayout());
	private JComboBox<String> searchType = new JComboBox<String>();
	private JTextField searchWord = new JTextField(15);
	private JButton csButton = Gui.createButton("고객센터 답변", b->action(4));
	private JPanel csPanel = new JPanel(Gui.flowLayout(0));
	private StringTable table;
	private List<List<String>> list;
	
	public AdminPage(String title, AdminApp adminApp) {
		super(title, adminApp);
		this.adminApp = adminApp;
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
		botBtnPanel.add(searchType);
		botBtnPanel.add(searchWord);
		Gui.addKey(searchWord, KeyEvent.VK_ENTER, b->action(1));
		botBtnPanel.addButton("Search", b->action(1));
		bottomPanel.addWest(botBtnPanel);
		ButtonPanel btnPanel = new ButtonPanel();
		btnPanel.setLayout(Gui.flowLayout(2));
		btnPanel.add(csPanel);
		btnPanel.addButton("여행 패키지 등록", b->action(2));
		btnPanel.addButton("좌석 정보 등록", b->action(3));
		bottomPanel.add(btnPanel);
		rootblPanel.addSouth(bottomPanel);
		createTable(0);
	}
	
	private void action(int i) {
		switch (i) {
			case 1: createTable(0, searchType.getSelectedItem().toString(), searchWord.getText()); break;
			case 2: adminApp.openPackage(); break;
			case 3: adminApp.openAddAirline(); break;
			case 4: 
				int row = table.getSelectedRow();
				if(row != -1)
					adminApp.openCS(Integer.parseInt(list.get(row).get(0))); 
			break;
		}
	}

	public void createTable(int i) {
		createTable(i, "", "");
	}
	
	private String makeQuery(String tableName, String[] columns) {
		return "select " + String.join(",", columns) + " from " + tableName;
	}
	
	public void createTable(int i, String ColumnName, String word) {
		final int tableCount = 3;
		tableIndex += i;
		if(tableIndex <= 0) tableIndex = tableCount;
		if(tableIndex > tableCount) tableIndex = 1;
		String title = "# ";
		String tableName = "";
		String[] columns = null;
		switch (tableIndex) {
			case 1: title += "고객센터 문의글 보기";
					tableName = "customercenter";
					columns = new String[] {"NUM","TITLE","CONTENT","WRITER","REGDATE"};
					break;
			case 2: title += "멤버십 가입자 조회";
					tableName = "membership";
					columns = new String[] {"ID","MILEAGE","INDATE","ENDDATE"};
					break;
			case 3: title += "회원 정보 조회";
					tableName = "members";
					columns = new String[] {"id","phone","name","email"};
					break;
		}
		String query = makeQuery(tableName, columns); 
		
		titleLabel.setText(title);
		
		if(!ColumnName.isBlank() && !word.isBlank()) {
			query += " WHERE " + ColumnName + " LIKE ?";
			list = DAO.sql.select(query, "%"+word+"%");
		}else {
			list = DAO.sql.select(query);
		}
		table = new StringTable(list, columns);
	
		if(tableIndex == 1) {
			csPanel.add(csButton);
		}else {
			csPanel.remove(csButton);
		}
		rootPanel.revalidate();
		searchType.removeAllItems();
		table.getColumnNames().forEach(s->searchType.addItem(s));;
//		table.setColumnsSize(200,400,200);
		tablePanel.removeAll();
		tablePanel.add(new JScrollPane(table));
	}
}