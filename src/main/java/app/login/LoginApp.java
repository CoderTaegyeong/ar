package app.login;

import app.AppService;
import app.AppView;
import app.SubApp;

public class LoginApp extends SubApp{
	private AirLineMain airLineMain = new AirLineMain(this); 
	private AirLineSignUp airLineSignUp = new AirLineSignUp(this); 
	
	public void openSignUp() {
		AppService.instance().closeView(airLineSignUp);
		AppService.instance().openView(airLineSignUp);
	}
	
	public void openMain() {
		AppService.instance().closeView(airLineSignUp);
		AppService.instance().openView(airLineMain);
	}
	
	@Override
	public AppView requestView() {
		return airLineMain;
	}
}
