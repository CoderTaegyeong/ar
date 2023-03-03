package test;

import app.AppService;
import app.SubApp;
import gui.Gui;
import util.FileUtil;

public class AAA {
	public static void main(String[] args) {
		Gui.setLookAndFeel(Gui.NIMBUS);
		SubApp app = (SubApp) FileUtil.getObjectFromJar("file:/C:/Users/GGG/Desktop/AdminApp.jar", "app.admin.AdminApp");
		AppService as = AppService.instance();
		as.addSubApp(app);
		as.start();
	}
}
