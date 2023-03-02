package app.travel;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import app.AppService;
import app.AppView;
import app.SubApp;
import dao.DAO;
import entity.PackageDTO;

public class Travel extends SubApp{
	private TravelPackage travelPackage = new TravelPackage(this);
	private DetailView detailView = new DetailView(this);
	
	@Override
	public AppView requestView() {
		openList();
		return travelPackage;
	}

	public List<PackageDTO> getPackageList() {
		String query = "SELECT * FROM ( SELECT * FROM TRAVEL_PACKAGE ORDER BY num DESC ) WHERE ROWNUM <= 20";
		return DAO.sql.select(query, new BeanPropertyRowMapper<>(PackageDTO.class));
	}

	public void openDetail(PackageDTO pack) {
		detailView.setPackage(pack);
		AppService.instance().openView(detailView);
	}

	public void openList() {
		travelPackage.showPackageList();
	}
	
	public String getTitle() {
		return "여행 패키지";
	}
}
