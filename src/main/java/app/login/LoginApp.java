package app.login;

import app.AppService;
import app.AppView;
import app.SubApp;
import dao.DAO;
import entity.MemberDTO;
import entity.mapper.MemberRowMapper;
import gui.WrapFrame;

public class LoginApp extends SubApp{
	private LoginView loginView = new LoginView(this); 
	private SignUp signUp = new SignUp(this); 
	private MemberView memberView = new MemberView(this); 
	private PayInfoView payInfoView = new PayInfoView(this);

	{ index = 10; }
	
	public void openSignUp() {
		AppService.instance().closeView(loginView, memberView);
		AppService.instance().openView(signUp);
	}
	
	public void openLoginView() {
		AppService.instance().closeView(signUp, memberView);
		loginView.reset();
		AppService.instance().openView(loginView);
	}
	
	public boolean isLogin() {
		return AppService.instance().getAttribute("member") != null;
	}
	
	@Override
	public AppView requestView() {
		if(isLogin()) {
			memberView.showMemberInfo();
			return memberView;
		}
		else
			return loginView;
	}
	
	public MemberDTO getMember() {
		MemberDTO member = (MemberDTO) AppService.instance().getAttribute("member");
		return member;
	}

	public void openMemberView() {
		AppService.instance().closeView(signUp, loginView);
		memberView.showMemberInfo();
		AppService.instance().openView(memberView);
	}

	public void login(String id) {
		MemberDTO member = DAO.sql.selectOne("select id, password, name, email, phone from members where id = ?", 
				new MemberRowMapper(), id).get();
		AppService.instance().setAttr("member", member);
		AppService.instance().setAttr("id", member.getId());
		AppService.instance().setAttr("name", member.getName());
		AppService.instance().closeView(loginView, signUp);
	}
	
	public void logout() {
		AppService.instance().remove("member");
		AppService.instance().closeAllViews();
		openLoginView();
	}

	public void openPayinfo() {
		payInfoView.createTable();
		AppService.instance().openView(payInfoView);
	}
	
	@Override
	public String getTitle() {
		return "회원 정보";
	}
}
