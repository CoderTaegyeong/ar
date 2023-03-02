package app.membership;

import java.util.Optional;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import app.AppService;
import app.AppView;
import app.ArApplication;
import app.SubApp;
import app.login.LoginApp;
import app.view.PayView;
import app.view.TermsView;
import dao.DAO;
import entity.MembershipDTO;
import entity.Mileage;
import entity.PayDTO;
import gui.Gui;
import util.FileUtil;
import util.StrUtil;

public class Membership extends SubApp{
	private JoinView joinView = new JoinView(this);
	private InfoView infoView = new InfoView(this);
	private MembershipPay membershipPay = new MembershipPay(this);
	private TermsView termsView;
	
	public Membership() {
		termsView = new TermsView("멤버십 이용 약관",
				FileUtil.readString(ArApplication.RES_PATH+"membershipterms.txt"),
				"Next", Gui.font(15),b->openInputPage());
	}
	
	@Override
	public AppView requestView() {
		if(isVaild(AppService.instance().getAttr("id"))) {
			infoView.createTable(0);
			return infoView;
		} else
			return joinView;
	}

	private void openInputPage() {
		membershipPay.openInputPage(AppService.instance().getSubApp(LoginApp.class).getMember());
		AppService.instance().openView(membershipPay);
	}

	/**
	 * 멤버십 유효기간 안인지 확인한다
	 */
	public boolean isVaild(String id) {
		return DAO.sql.selectOne("select * from membership where id = ? AND SYSDATE BETWEEN indate AND enddate", 
			new BeanPropertyRowMapper<>(MembershipDTO.class), id).isPresent();
	}

	/**
	 * 멤버십에 가입한적이 있는지 확인한다
	 */
	public boolean isAlreadyJoin(String id) {
		return getMembership(id).isPresent();
	}
	
	public Optional<MembershipDTO> getMembership(String id) {
		return DAO.sql.selectOne("select * from membership where id = ?", 
				new BeanPropertyRowMapper<>(MembershipDTO.class), id);
	}
	
	public void joinMembership(PayDTO pay) {
		int result = 0;
		AppService.instance().openPayDialog(pay);
		if(pay.ok()) {
			result = DAO.sql.getJdbcTemplate().update
			("insert into Membership (id,mileage,indate,enddate) VALUES "
			+ "(?, 50000, SYSDATE, ADD_MONTHS(SYSDATE, 12))" , pay.getId());
			if(result == 1) {
				AppService.instance().closeView(membershipPay, joinView);
				AppService.instance().openView(new PayView("맴버십 가입 결제정보", pay));				
			}
		}
	}
	
	public void updateMileage(Mileage mile) {
		//마일리지 사용 로그를 추가한다
		//멤버쉽 테이블에서 마일리지를 추가 or 삭감한다
		if(!isAlreadyJoin(mile.getId())) return;
		mile.setTimestamp(StrUtil.now());
		DAO.sql.insert("MILEAGE", mile, "num", "mile_seq");
		DAO.sql.getJdbcTemplate().update
		("update membership set mileage = mileage + ? where id = ? " , mile.getMileChange(), mile.getId());
	}
	
	public void openJoin() {
		AppService.instance().openView(joinView);
	}
	
	public void openInfo() {
		AppService.instance().openView(infoView);
	}

	public void openTerms() {
		AppService.instance().openView(termsView);
	}
//
//	
//	
//	public static void updateMileage2(Mileage mile) {
//		//마일리지 사용 로그를 추가한다
//		//멤버쉽 테이블에서 마일리지를 추가 or 삭감한다
//		mile.setTimestamp(StrUtil.now());
//		DAO.sql.insert("MILEAGE", mile, "num", "mile_seq");
//		DAO.sql.getJdbcTemplate().update
//		("update membership set mileage = mileage + ? where id = ? " , mile.getMileChange(), mile.getId());
//	}
	
	public static void main(String[] args) {
		Mileage mile = new Mileage();
		mile.setId("dummy");
		mile.setDetail("마일리지 증가");
		mile.setMileChange(+310000);
//		updateMileage2(mile);
	}
	
	@Override
	public String getTitle() {
		return "멤버십";
	}
}
