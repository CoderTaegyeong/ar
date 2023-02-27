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
	
	public void openSignUp() {
		AppService.instance().closeView(loginView, memberView);
		AppService.instance().openView(signUp);
	}
	
	public void openLoginView() {
		AppService.instance().closeView(signUp, memberView);
		loginView.reset();
		AppService.instance().openView(loginView);
	}
	
	@Override
	public AppView requestView() {
		boolean isLogin = AppService.instance().getAttribute("Member") != null;
		if(isLogin) {
			memberView.showMemberInfo();
			return memberView;
		}
		else
			return loginView;
	}
	
	public MemberDTO getMember() {
		MemberDTO member = (MemberDTO) AppService.instance().getAttribute("Member");
		return member;
	}

	public void openMemberView() {
		AppService.instance().closeView(signUp, loginView);
		memberView.showMemberInfo();
		AppService.instance().openView(memberView);
	}

	public void login(String id) {
		AppService.instance().setAttr("Member",
			DAO.sql.selectOne("select id, password, name, email, phone from members where id = ?", 
			new MemberRowMapper(), id).get());
		AppService.instance().closeView(loginView);
	}
	
	public void logout() {
		AppService.instance().remove("Member");
		openLoginView();
	}
}
