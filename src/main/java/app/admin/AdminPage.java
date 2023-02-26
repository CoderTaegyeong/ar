package app.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

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
		botBtnPanel.addButton("Search", b->action(1));
		bottomPanel.addWest(botBtnPanel);
		bottomPanel.newPanel(BorderLayout.EAST, 2).add(Gui.createButton("여행 패키지 등록", b->action(2)));
		rootblPanel.addSouth(bottomPanel);
		createTable(0);
	}
	
	private void action(int i) {
		switch (i) {
			case 1: createTable(0, searchType.getSelectedItem().toString(), searchWord.getText()); break;
			case 2: adminApp.openPackage(); break;
		}
	}

	public void createTable(int i) {
		createTable(i, "", "");
	}
	
	public void createTable(int i, String ColumnName, String word) {
		final int tableCount = 3;
		tableIndex += i;
		if(tableIndex <= 0) tableIndex = tableCount;
		if(tableIndex > tableCount) tableIndex = 1;
		String query = "", title = "# ";
		switch (tableIndex) {
			case 1: title += "회원 목록"; 
					query = "select * from members"; 
					break;
			case 2: title += "보드 이름 제목"; 
					query = "select id, name from members"; 
					break;
			case 3: title += "회원 아이디 패스워드"; 
					query = "select id, password from members"; 
					break;
		}
		titleLabel.setText(title);
		
		StringTable table;
		if(!ColumnName.isBlank() && !word.isBlank()) {
			query += " WHERE " + ColumnName + " LIKE ?";
			table = new StringTable(DAO.sql.selectWithColumnName(query, "%"+word+"%"));
		}else {
			table = new StringTable(DAO.sql.selectWithColumnName(query));
		}
		
		searchType.removeAllItems();
		table.getColumnNames().forEach(s->searchType.addItem(s));;
//		table.setColumnsSize(200,400,200);
		tablePanel.removeAll();
		tablePanel.add(new JScrollPane(table));
	}
}