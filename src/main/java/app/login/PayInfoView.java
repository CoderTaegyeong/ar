package app.login;

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

public class PayInfoView extends AppView{
	private JLabel titleLabel = Gui.createLabel(Color.darkGray, new Font("맑은 고딕", Font.BOLD, 20), JLabel.CENTER);
	private JPanel tablePanel = new JPanel(new BorderLayout());
	public PayInfoView(LoginApp loginApp) {
		super("결제 정보", loginApp);
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
		rootblPanel.addCenter(tablePanel);
		
		BorderLayoutPanel bottomPanel = new BorderLayoutPanel();
		ButtonPanel botBtnPanel = new ButtonPanel();
		rootblPanel.addSouth(botBtnPanel);
		botBtnPanel.setLayout(Gui.flowLayout(FlowLayout.LEFT));
		bottomPanel.addWest(botBtnPanel);
		rootblPanel.addSouth(bottomPanel);
	}
	
	public void createTable() {
		String title = "# 결제 내역 보기 ";
		String tableName = "PAYMENT";
		String[] columns = {"NUM","ID","PAYDATE","ITEM","PRICE","PAY","MILE","PAYWITH","STATUS"};
		String query = StrUtil.selectQuery(tableName, columns, "WHERE ID = ?"); 
		
		titleLabel.setText(title);
		StringTable table = new StringTable(DAO.sql.select(query, AppService.instance().getAttr("id")), columns);
		
		tablePanel.removeAll();
		table.setColumnsSize(50,50,100,100,50,50,50,50);
		tablePanel.add(new JScrollPane(table));
	}
}