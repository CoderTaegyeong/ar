package app;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import entity.PayDTO;
import test.Debug;
import util.Style;

public class AppService {
	private static AppService appService;
	private List<SubApp> appList = new Vector<>();
	private AppContainer appContainer = new AppContainer();
	private Map<String, Object> appInfoMap = new HashMap<>();
	
	private AppService() {}

	public static AppService instance() {
		return appService == null ? appService = new AppService() : appService;
	}
	private PayDialog payDialog = new PayDialog();
	
	public void addSubApp(SubApp subApp) {
		Debug.sysout("addSubApp : " +subApp);
		if(subApp != null && getSubApp(subApp.getClass()) == null)
			appList.add(subApp);
	}
	
	public void removeSubApp(SubApp subApp) {
		Debug.sysout("removeSubApp : " +subApp);
		if(subApp != null) {
			appContainer.removeViews(subApp);
			appList.remove(subApp);
			updateSubAppIcons();
		}
	}
	
	public void openView(AppView appView) {
		appContainer.addView(appView);
	}
	
	public void closeView(AppView... appViews) {
		for(AppView view : appViews) appContainer.removeView(view, false);
	}
	
	public void closeAllViews() {
		appContainer.removeAllViews();
	}
	
	public <T extends SubApp> T getSubApp(Class<T> subAppClass) {
		for(SubApp subApp : appList) {
			if(subAppClass.isInstance(subApp)) {
				Debug.sysout("getSubApp : " +subApp);
				return subAppClass.cast(subApp);
			}
		}
		return null;
	}
	
	public void updateSubAppIcons() {
		appContainer.addAppIcons(appList);
	}
	
	public AppContainer getContainer() {
		return appContainer;
	}
	
	public Style getStyle(){
		return appContainer.style;
	}
	
	public void start() {
		appContainer.initRootPanel();
		appContainer.showFrame();
		appContainer.setStyle();
		update();
	}
	
	public void update() {
		appContainer.update();
	}
	
	public void setAttr(String key, Object value) {
		appInfoMap.put(key, value);
	}
	
	public Object getAttribute(String key) {
		if(appInfoMap.containsKey(key))
			return appInfoMap.get(key);
		else
			return null;
	}
	
	public String getAttr(String key) {
		if(appInfoMap.containsKey(key))
			return getAttribute(key).toString();
		else
			return "is Null";
	}
	
	public void remove(String... keys) {
		for(String key : keys)
			appInfoMap.remove(key);
	}

	public PayDTO openPayDialog(PayDTO pay) {
		return payDialog.openPay(pay);
	}
}
