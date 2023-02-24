package app.admin;

import app.AppService;
import app.AppView;
import app.SubApp;
import dao.DAO;
import entity.PackageDTO;

public class AdminApp extends SubApp{
	private AdminPage adminPage = new AdminPage("AdminApp", this);
	private AddPackage addPackage = new AddPackage(this);
	
	@Override
	public AppView requestView() {
		// TODO Auto-generated method stub
		return adminPage;
	}

	public void openPackage() {
		AppService.getInstance().openView(addPackage);
	}

	public void addPackage(PackageDTO packDTO) {
		DAO.sql.insert("Travel_Package", packDTO);
	}
}
