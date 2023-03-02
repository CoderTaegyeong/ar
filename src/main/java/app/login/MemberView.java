package app.login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import app.AppService;
import app.AppView;
import app.membership.Membership;
import dao.DAO;
import entity.MemberDTO;
import entity.MembershipDTO;
import entity.mapper.MemberRowMapper;
import gui.Gui;
import gui.InputForm;
import gui.panel.input.InputComponent;
import gui.panel.input.PasswordPanel;
import gui.panel.input.TextFieldPanel;
import gui.panel.layout.BorderLayoutPanel;
import gui.panel.layout.GridBagPanel;
import test.Debug;

public class MemberView extends AppView{
	private InputForm<MemberDTO> memberForm = new InputForm<>();
	private InputForm<MembershipDTO> shipForm = new InputForm<>();
	private BorderLayoutPanel blPanel = new BorderLayoutPanel();
	private GridBagPanel gbPanel = new GridBagPanel(330,400);
	private GridBagPanel shipPanel = new GridBagPanel();
	private LoginApp loginApp;
	private JButton updateBtn = Gui.createButton("완료", b->updateBtnAction());
	public MemberView(LoginApp loginApp) {
		super("회원 정보",loginApp);
		this.loginApp = loginApp;
		initRootPanel();
	}
	
	@Override
	public void initRootPanel() {
		rootPanel = blPanel.getPanel();
		memberForm.font = Gui.font(15);
		blPanel.removeAll();
		blPanel.addCenter(gbPanel);
		gbPanel.addNextRow(Gui.createLabel("회원 정보 보기", Color.BLACK, Gui.font(30), JLabel.LEFT));
		TextFieldPanel idPanel = memberForm.createTFP("id", "아이디");
		idPanel.setEditable(false);
		gbPanel.addNextRow(idPanel);
		gbPanel.addNextRow(memberForm.createPWP("password", "비밀번호"));
		gbPanel.addNextRow(memberForm.createTFP("name", "이름"));
		gbPanel.addNextRow(memberForm.createTFP("email", "이메일"));
		gbPanel.addNextRow(memberForm.createTFP("phone", "전화번호"));
		JButton logoutBtn = Gui.greenButton("로그아웃", b->loginApp.logout());
		JButton payinfoBtn = Gui.createButton("결제내역",b->loginApp.openPayinfo());
		Gui.copyProp(logoutBtn,updateBtn,payinfoBtn);
		gbPanel.addNextRow(Gui.createPanel(updateBtn, logoutBtn,payinfoBtn));
		
		blPanel.newPanel(BorderLayout.EAST).add(shipPanel.getPanel());
		Gui.setMargin(shipPanel,50,0,0,50);
		Gui.setMargin(gbPanel,0,50,100,0);
	}

	public void showMemberInfo() {
		Optional<MemberDTO> member = 
				DAO.sql.selectOne("select id, password, name, email, phone from members where id = ?",
				new MemberRowMapper(),loginApp.getMember().getId());
		if(member.isPresent()){
			memberForm.setData(member.get());
		}
		updateBtn.setText("수정");
		memberForm.setEditable(false);
		
		shipPanel.removeAll();
		shipForm.resetForm();
		Membership membership = AppService.instance().getSubApp(Membership.class);
		Optional<MembershipDTO> membershipDTO = membership.getMembership(memberForm.getString("id"));
		Debug.sysout(membershipDTO);
		if(membershipDTO.isPresent()) {
			shipPanel.addNextRow(Gui.createLabel("멤버십 정보", Color.BLACK, Gui.font(30), JLabel.LEFT));
			shipPanel.addNextRow(shipForm.createTFP("mileage", "마일리지"));
			shipPanel.addNextRow(shipForm.createTFP("inDate", "시작일"));
			shipPanel.addNextRow(shipForm.createTFP("endDate", "종료일"));
			shipForm.setData(membershipDTO.get());
		} else { 
			shipPanel.addNextRow(Gui.createLabel("멤버십에 가입되지 않았습니다.", Color.RED, Gui.font(20), JLabel.LEFT));
		}
		if(!membership.isVaild(memberForm.getString("id"))) {
			shipPanel.addNextRow(Gui.createButton("멤버십 가입하기", b->membership.openJoin()));
		}
		shipForm.setEditable(false);
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
		Gui.createFrame(new MemberView(null).rootPanel);
	}
}
