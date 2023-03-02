package app;

import javax.swing.Timer;

import app.admin.AdminApp;
import app.center.CustomerService;
import app.dash.DashBoard;
import app.login.LoginApp;
import app.membership.Membership;
import app.reserv.Reservation;
import app.travel.Travel;
import entity.MemberDTO;
import gui.Gui;
import test.Debug;

public class ArApplication {
	public static final String RES_PATH = "src/main/resources/";
	public static final String IMG_PATH = RES_PATH + "images/";
	
	public static void main(String[] args) {
		Gui.setLookAndFeel(Gui.NIMBUS);
		new ArApplication().run();
	}

	//앱 추가부분
	public void run() {
		long start = System.currentTimeMillis();
		Debug.sysout(getClass() +" start...");
		
		AppService service = AppService.instance();
//		service.setAttr("member", new MemberDTO("dummy","1234","name1","",""));
//		service.setAttr("id", "dummy");
		service.addSubApp(new LoginApp());
		service.addSubApp(new Reservation());
		service.addSubApp(new DashBoard());
		service.addSubApp(new AdminApp());
		service.addSubApp(new Travel());
		service.addSubApp(new Membership());
		service.addSubApp(new CustomerService());
		service.start();

		//앱 시작시 로그인 창을 띄운다
		service.openView(service.getSubApp(LoginApp.class).requestView());
		
		//전체 동작 타이머 시작
		new Timer(1000, e->service.update()).start();
	
		long end = System.currentTimeMillis();
		Debug.sysout("Run time: "+ (end-start)+" ms ");
	}
}
