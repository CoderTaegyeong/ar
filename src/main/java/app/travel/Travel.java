package app.travel;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import app.AppView;
import app.SubApp;
import dao.DAO;
import entity.PackageDTO;

public class Travel extends SubApp{
	private TravelPackage travelPackage = new TravelPackage(this);
	
	@Override
	public AppView requestView() {
		travelPackage.showPackageList();
		return travelPackage;
	}

	public List<PackageDTO> getPackageList() {
		String query = "SELECT * FROM ( SELECT * FROM TRAVEL_PACKAGE ORDER BY id DESC ) WHERE ROWNUM <= 20";
		return DAO.sql.select(query, new BeanPropertyRowMapper<>(PackageDTO.class));
	}

	public void openDetail(PackageDTO pack) {
		
	}
}
