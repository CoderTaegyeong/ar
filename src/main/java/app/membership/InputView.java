package app.membership;

import app.AppView;
import gui.panel.ImagePanel;
import gui.panel.layout.BorderLayoutPanel;

public class InputView extends AppView{
	private MemberShip memberShip;
	private ImagePanel imagePanel = new ImagePanel();
	
	public InputView(MemberShip memberShip) {
		super("멤버십 가입", memberShip);
		this.memberShip = memberShip;
		initRootPanel();
	}
	
	@Override
	public void initRootPanel() {
		BorderLayoutPanel blPanel = new BorderLayoutPanel();
		
	}
}