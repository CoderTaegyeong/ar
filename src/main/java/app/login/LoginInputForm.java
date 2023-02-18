package app.login;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import app.AppService;
import app.AppView;
import gui.panel.button.ButtonPanel;

public class LoginInputForm extends AppView{
	private LoginApp login;

	public LoginInputForm(LoginApp login) {
		this.login = login;
		List<String> result = AppService.getInstance().sql().selectOne("select id, password from members where id = 'ydk'");
		JPanel panel = new JPanel();

		panel.add(new JLabel("Login ID"));
		JTextField textField = new JTextField(10);
		textField.setText(result.get(0));
		panel.add(textField);

		panel.add(new JLabel("PW"));
		JPasswordField passwordField = new JPasswordField(10);
		panel.add(passwordField);
		passwordField.setText(result.get(1));
		
		ButtonPanel buttonPanel = new ButtonPanel();
		buttonPanel.addButton("Login",b->login.login());
		buttonPanel.addButton("Join", b->login.join());

		panel.add(buttonPanel);
		rootPanel.add(panel);
	}
	
	public boolean validate() {
		return false;
	}
}