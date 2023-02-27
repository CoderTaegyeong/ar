package app.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

import app.AppService;
import app.ArApplication;
import dao.DAO;
import entity.PayDTO;
import gui.Gui;
import gui.InputForm;
import gui.panel.button.input.RadioButtonPanel;
import gui.panel.input.InputComponent;
import gui.panel.input.TextFieldPanel;
import gui.panel.layout.BorderLayoutPanel;
import gui.panel.layout.GridBagPanel;

public class PayDialog {
	private JDialog dialog = new JDialog(AppService.instance().getContainer().getFrame());
	private GridBagPanel gbPanel = new GridBagPanel();
	private BorderLayoutPanel blPanel = new BorderLayoutPanel();
	private JButton payButton = Gui.createButton("", new Color(80, 130, 80), Color.WHITE, Gui.font(30), b->pay());
	private PayDTO pay;
	private InputForm<PayDTO> payForm = new InputForm<PayDTO>();
	
	private void addInputComp(InputComponent inputComp) {
		gbPanel.addNextRow(inputComp.getPanel());
		payForm.addInputComp(inputComp);
	}
	
	public PayDialog() {
//		dialog.setModal(true);
		dialog.setAlwaysOnTop(true);
		blPanel.newScroll(gbPanel, BorderLayout.CENTER);
		blPanel.addSouth(payButton);
		addInputComp(new TextFieldPanel("item", 30, "상품명"));
		addInputComp(new TextFieldPanel("price", 30, "가격"));
		RadioButtonPanel rbPanel = new RadioButtonPanel();
		rbPanel.addButton("카드 결제", new ImageIcon(ArApplication.IMG_PATH+"cardpay.png"));
		rbPanel.addButton("카카오 페이", new ImageIcon(ArApplication.IMG_PATH+"kakaopay.png"));
		rbPanel.addButton("네이버 페이", new ImageIcon(ArApplication.IMG_PATH+"naverpay.png"));
		rbPanel.getPanel().setCursor(new Cursor(Cursor.HAND_CURSOR));
		rbPanel.setFonts(Gui.font(12));
		gbPanel.addNextRow(rbPanel);
		//대충 라디오 버튼의 Enable 을 설정하는 코드 0번 버튼을 활성화 하고 클릭할때마다 그 버튼을 활성화 한다
		rbPanel.getButtonList().forEach(b->{
			b.setEnabled(rbPanel.getButtonList().indexOf(b) == 0);
			b.addMouseListener(new MouseAdapter() { public void mouseReleased(MouseEvent e) {
			rbPanel.getButtonList().forEach(rb->rb.setEnabled(rb == e.getSource()));}});
		});
		
		payForm.setEditable(false);
		dialog.setContentPane(blPanel.getPanel());
		dialog.setSize(500, 500);
	}
	
	public PayDTO openPay(PayDTO pay) {
		this.pay = pay;
		payButton.setText(String.format("%,d 원 결제하기",pay.getPrice()));
		payForm.setData(pay);
		payForm.set("price", String.format("%,d 원",pay.getPrice()));
		dialog.setVisible(true);
		return this.pay;
	}
	
	private void pay() {
		pay.setPayDate(new Date());
		int i = DAO.sql.insert("payment", pay, "id", "pay_seq");
		if(i == 1)
			pay.setStatus("결제 완료");
		dialog.setVisible(false);
	}
	
	public static void main(String[] args) {
		PayDTO p = new PayDTO();
		p.setItem("aaaaaaaa상품");
		p.setPrice(123123);
		p = new PayDialog().openPay(p);
	}
}
