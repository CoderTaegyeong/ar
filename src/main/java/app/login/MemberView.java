package app.login;

import java.awt.Color;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import app.AppService;
import app.AppView;
import dao.DAO;
import entity.MemberDTO;
import entity.mapper.MemberRowMapper;
import gui.Gui;
import gui.InputForm;
import gui.panel.input.InputComponent;
import gui.panel.input.PasswordPanel;
import gui.panel.input.TextFieldPanel;
import gui.panel.layout.GridBagPanel;

public class MemberView extends AppView{

	private InputForm<MemberDTO> memberForm = new InputForm<>();
	private GridBagPanel gbPanel = new GridBagPanel(600,400);
	private LoginApp loginApp;
	private JButton updateBtn = Gui.createButton("완료", b->updateBtnAction());
	public MemberView(LoginApp loginApp) {
		super(loginApp);
		this.loginApp = loginApp;
		initRootPanel();
	}
	
	@Override
	public void initRootPanel() {
		rootPanel.removeAll();
		rootPanel.add(gbPanel.getPanel());
		Gui.setMargin(gbPanel, 0, 30, 0, 0);
		gbPanel.addNextRow(Gui.createLabel("회원 정보 보기", Color.BLACK, Gui.font(30), JLabel.LEFT));
		addInputComp(new TextFieldPanel("id", "아이디") {{getTextField().setEnabled(false);}});
		addInputComp(new PasswordPanel("password", "비밀번호"));
		addInputComp(new TextFieldPanel("name", "이름"));
		addInputComp(new TextFieldPanel("email", "이메일"));
		addInputComp(new TextFieldPanel("phone", "전화번호"));
		JPanel panel = new JPanel();
		panel.add(updateBtn);
		panel.add(Gui.createButton("Logout", b->loginApp.logout()));
		gbPanel.addNextRow(panel);
	}
	
	private void addInputComp(InputComponent inputComp) {
		memberForm.addInputComp(inputComp);
		gbPanel.addNextRow(inputComp.getPanel());
	}
	
	public void showMemberInfo() {
		Optional<MemberDTO> member = 
				DAO.sql.selectOne("select id, password, name, email, phone from members where id = ?",
				new MemberRowMapper(),loginApp.getMember().getId());
		System.out.println(member);
		if(member.isPresent()){
			memberForm.setData(member.get());
		}
		updateBtn.setText("수정");
		memberForm.setEditable(false);
	}
	
	public void updateBtnAction() {
		boolean editable = false;
		if(updateBtn.getText().equals("수정")) { 
			updateBtn.setText("완료");
			editable = true;
		} else {
			updateBtn.setText("수정");
			editable = false;
			if(memberForm.findEmptyFields().length() == 0) 
				DAO.sql.update("members", memberForm.saveTo(new MemberDTO()), "id");
		}
		memberForm.setEditable(editable);
	}
	
	public static void main(String[] args) {
		Gui.createFrame(new MemberView(new LoginApp()).rootPanel);
	}
}
