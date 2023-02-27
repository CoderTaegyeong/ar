package app.travel;

import java.awt.Color;

import javax.swing.border.LineBorder;

import app.AppService;
import app.AppView;
import app.reserv.Reservation;
import entity.PackageDTO;
import gui.Gui;
import gui.panel.ImagePanel;
import gui.panel.button.ButtonPanel;
import gui.panel.input.TextAreaPanel;
import gui.panel.layout.BorderLayoutPanel;

public class DetailView extends AppView{
	private Travel travel;
	private BorderLayoutPanel blPanel = new BorderLayoutPanel();
	private ImagePanel imagePanel = new ImagePanel();
	private TextAreaPanel detailTextPanel = new TextAreaPanel();
	
	public DetailView(Travel travel) {
		super("여행 패키지 상세정보", travel);
		this.travel = travel;
		initRootPanel();
	}
	
	public void initRootPanel() {
		rootPanel = blPanel.getPanel();
		detailTextPanel.setEditable(false);
		blPanel.addCenter(imagePanel);

		imagePanel.setBorder(new LineBorder(Color.BLACK, 2));
		imagePanel.setBackground(Color.WHITE);
		BorderLayoutPanel botPanel = new BorderLayoutPanel();
		botPanel.setBorder(new LineBorder(Color.BLACK, 2));
		botPanel.addCenter(detailTextPanel);
		ButtonPanel buttonPanel = new ButtonPanel();
		botPanel.addSouth(buttonPanel);
		blPanel.addSouth(botPanel);
		buttonPanel.addButton("예약하기", b->AppService.instance().getSubApp(Reservation.class).openReservView());
		buttonPanel.addButton("뒤로가기", b->AppService.instance().openView(travel.requestView()));
	}

	public void setPackage(PackageDTO pack) {
		imagePanel.setImage(Gui.scaleDown(Gui.getImage(pack.getImage()), 650, 450));
		detailTextPanel.setValue(
				"\n패키지명: "		+ pack.getTitle() +
				"\n여행지: "		+ pack.getTravelLoc() +
				"\n여행기간: " 	+ pack.getTravelDays() +
				"\n가격: " 		+ String.format("%,d 원", pack.getPrice()) +
				"\n상세정보: " 	+ pack.getDetailText());
	}
}
