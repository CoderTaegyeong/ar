package app.membership;

import java.io.File;
import java.nio.file.Files;
import java.util.Optional;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import app.AppService;
import app.AppView;
import app.ArApplication;
import app.SubApp;
import app.login.LoginApp;
import app.view.TermsView;
import app.view.TextView;
import dao.DAO;
import entity.MemberDTO;
import entity.MemberShipDTO;
import gui.Gui;
import util.FileUtil;

public class MemberShip extends SubApp{
	private JoinView joinView = new JoinView(this);
	private InfoView infoView = new InfoView(this);
	private InputView inputView = new InputView(this);
	@Override
	public AppView requestView() {
		MemberDTO member = AppService.instance().getSubApp(LoginApp.class).getMember();
		Optional<MemberShipDTO> ship = DAO.sql.selectOne("select * from membership where id = ?", 
								new BeanPropertyRowMapper<>(MemberShipDTO.class), member.getId());
		if(ship.isEmpty())
			openJoin();
		else
			openInfo();
		return null;
	}

	public void openJoin() {
		AppService.instance().openView(joinView);
	}
	public void openInfo() {
		AppService.instance().openView(infoView);
	}

	public void openTerms() {
		TermsView termsView = new TermsView("멤버십 이용 약관",
				FileUtil.readString(ArApplication.RES_PATH+"membershipterms.txt"),
				"Next", Gui.font(10),
				b->openInputPage());
		AppService.instance().openView(termsView);
		termsView.getScroll().getVerticalScrollBar().setValue(10);

		termsView.getScroll().revalidate();
	}

	private void openInputPage() {
		AppService.instance().openView(inputView);
	}
}
