package app.membership;

import java.awt.Color;

import javax.swing.JButton;

import app.AppView;
import app.ArApplication;
import gui.Gui;
import gui.panel.ImagePanel;
import gui.panel.layout.BorderLayoutPanel;

public class JoinView extends AppView{
	private Membership memberShip;
	private ImagePanel imagePanel = new ImagePanel();
	
	public JoinView(Membership memberShip) {
		super("멤버십 가입 혜택", memberShip);
		this.memberShip = memberShip;
		initRootPanel();
	}
	
	@Override
	public void initRootPanel() {
		BorderLayoutPanel blPanel = new BorderLayoutPanel();
		rootPanel = blPanel.getPanel();
		imagePanel.setImage(Gui.scaleDown(ArApplication.IMG_PATH+"membershipjoin.png", 700,700));
		imagePanel.setBackground(Color.WHITE);
		blPanel.addCenter(imagePanel);
		JButton button = Gui.createButton("멤버십 가입하기", new Color(11,11,22), new Color(213,255,190), 
											Gui.font(30), b->memberShip.openTerms());
		blPanel.addSouth(button);
	}
}