package app.dash;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import app.AppView;
import dao.DAO;
import gui.panel.button.ButtonPanel;

public class DashBoardWrite extends AppView{
	private DashBoard dash;

	public DashBoardWrite(DashBoard dash) {
		super("데시보드라이트뷰", dash);
		System.out.println(dash);
	}
	
	public boolean validate() {
		return false;
	}

	@Override
	public void initRootPanel() {
		List<String> result = DAO.sql.selectOne("select * from members where id='ydk'");
		JPanel panel = new JPanel();

		panel.add(new JLabel("DASHBOARD VIEW"));
		JTextField textField = new JTextField(10);
		textField.setText(result.get(0));
		panel.add(textField);

		panel.add(new JLabel("DASHBOARD VIEW!!"));
		JPasswordField passwordField = new JPasswordField(10);
		panel.add(passwordField);
		passwordField.setText(result.get(1));
		
		ButtonPanel buttonPanel = new ButtonPanel();
		buttonPanel.addButton("SAVE",b->dash.save());
		buttonPanel.addButton("OPEN LIST", b->dash.openList());

		panel.add(buttonPanel.getPanel());
		rootPanel.add(panel);	
	}
}
