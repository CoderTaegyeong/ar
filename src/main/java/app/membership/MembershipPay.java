package app.membership;

import java.awt.Color;

import app.AppView;
import entity.MemberDTO;
import entity.PayDTO;
import gui.Gui;
import gui.panel.input.TextAreaPanel;
import gui.panel.layout.BorderLayoutPanel;

public class MembershipPay extends AppView{
	private Membership membership;
	private TextAreaPanel textPanel = new TextAreaPanel();
	private PayDTO pay;
	
	public MembershipPay(Membership membership) {
		super("멤버십 가입", membership);
		this.membership = membership;
		initRootPanel();
	}
	
	public void openInputPage(MemberDTO member) {
		pay = new PayDTO();
		pay.setItem("맴버십 가입 (즉시 50,000 마일 적립 + 결제 금액의 1% 마일 적립 혜택)\n유효기간 : 1년");
		pay.setPrice(50000);
		pay.setId(member.getId());
		String text = 	"아이디 : " + pay.getId() + 
//						"\n이름 : " + member.getName() + 
						"\n가입상품 : " + pay.getItem() + 
						"\n가격 : " + String.format("%,d 원",pay.getPrice());
		textPanel.setValue(text);
	}
	
	@Override
	public void initRootPanel() {
		BorderLayoutPanel blPanel = new BorderLayoutPanel();
		Gui.setMargin(blPanel, 40,40,40,40);
		rootPanel = blPanel.getPanel();
		blPanel.addCenter(textPanel);
		textPanel.setFont(Gui.font(30));
		textPanel.setEditable(false);
		blPanel.addSouth(Gui.createButton("결제하기", Color.WHITE, Gui.DARK_GREEN, Gui.font(30),
											b->membership.joinMembership(pay)));
	}
	
//	public static void main(String[] args) {
//		InputView v = new InputView(new MemberShip());
//		MemberDTO m = new MemberDTO();
//		m.setName("aaa");
//		m.setId("idaaa");
//		v.openInputPage(m);
//		Gui.createFrame(v.getPanel());
//	}
}