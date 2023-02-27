package app.membership;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;

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

public class InfoView extends AppView{
	private MemberShip memberShip;
	private int tableIndex = 1;
	private JLabel titleLabel = Gui.createLabel(Color.darkGray, new Font("맑은 고딕", Font.BOLD, 20), JLabel.CENTER);
	private JPanel tablePanel = new JPanel(new BorderLayout());
	private JComboBox<String> searchType = new JComboBox<String>();
	private JTextField searchWord = new JTextField(15);
	
	public InfoView(MemberShip memberShip) {
		super("멤버쉽 정보", memberShip);
		this.memberShip = memberShip;
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
		bottomPanel.newPanel(BorderLayout.EAST, 2).add(Gui.createButton("여행 패키지 등록", b->action(2)));
		rootblPanel.addSouth(bottomPanel);
		createTable(0);
	}
	
	private void action(int i) {
		switch (i) {
			case 1: createTable(0, searchType.getSelectedItem().toString(), searchWord.getText()); break;
		}
	}

	public void createTable(int i) {
		createTable(i, "", "");
	}
	
	private String makeQuery(String tableName, String[] columns) {
		return "select " + String.join(",", columns) + " from " + tableName;
	}
	
	public void createTable(int i, String columnName, String word) {
		final int tableCount = 3;
		tableIndex += i;
		if(tableIndex <= 0) tableIndex = tableCount;
		if(tableIndex > tableCount) tableIndex = 1;
		String title = "# ";
		String tableName = "";
		String[] columns = null;
		switch (tableIndex) {
			case 1: title += "회원 목록";
					tableName = "members";
					columns = new String[] {"id","password"};
					break;
			case 2: title += "보드 이름 제목";
					tableName = "members";
					columns = new String[] {"id","password","name"};
					break;
			case 3: title += "회원 아이디 패스워드";
					tableName = "members";
					columns = new String[] {"id","password","phone","name"};
					break;
		}
		String query = makeQuery(tableName, columns); 
		
		titleLabel.setText(title);
		
		StringTable table;
		if(!columnName.isBlank() && !word.isBlank()) {
			query += " WHERE " + columnName + " LIKE ?";
			table = new StringTable(DAO.sql.select(query, "%"+word+"%"), columns);
		}else {
			table = new StringTable(DAO.sql.select(query), columns);
		}
		
		searchType.removeAllItems();
		table.getColumnNames().forEach(s->searchType.addItem(s));;
//		table.setColumnsSize(200,400,200);
		tablePanel.removeAll();
		tablePanel.add(new JScrollPane(table));
	}
}