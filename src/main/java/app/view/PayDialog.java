package app.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import app.AppService;
import app.ArApplication;
import dao.DAO;
import entity.PayDTO;
import gui.Gui;
import gui.InputForm;
import gui.panel.button.ButtonPanel;
import gui.panel.button.input.RadioButtonPanel;
import gui.panel.input.InputComponent;
import gui.panel.input.TextFieldPanel;
import gui.panel.layout.BorderLayoutPanel;
import gui.panel.layout.GridBagPanel;

public class PayDialog {
	private JDialog dialog = new JDialog(AppService.instance().getContainer().getFrame());
	private GridBagPanel gbPanel = new GridBagPanel();
	private BorderLayoutPanel blPanel = new BorderLayoutPanel();
	private InputForm<PayDTO> payForm = new InputForm<PayDTO>();
	private CardLayout cardLayout = new CardLayout();
	private JPanel cardPanel = new JPanel(cardLayout);
	private BorderLayoutPanel passwordPanel = new BorderLayoutPanel();
	private JButton payButton = Gui.createButton("", new Color(80, 130, 80), Color.WHITE, 
			Gui.font(30), b->cardLayout.next(cardPanel));
	private PayDTO pay;
	
	private int width = 400, height = 300;
	private void addInputComp(InputComponent inputComp) {
		gbPanel.addNextRow(inputComp.getPanel());
		payForm.addInputComp(inputComp);
	}
	
	public PayDialog() {
//		dialog.setModal(true);
		dialog.setAlwaysOnTop(true);
		blPanel.newScroll(gbPanel, BorderLayout.CENTER);
		blPanel.addSouth(payButton);
		cardPanel.add(blPanel.getPanel(), "pay");
		
		addInputComp(new TextFieldPanel("item", 20, "     상품명"));
		addInputComp(new TextFieldPanel("price", 20, "     가격"));
		RadioButtonPanel rbPanel = new RadioButtonPanel();
		rbPanel.addButton("카드 결제", new ImageIcon(ArApplication.IMG_PATH+"cardpay.png"));
		rbPanel.addButton("카카오 페이", new ImageIcon(ArApplication.IMG_PATH+"kakaopay.png"));
		rbPanel.addButton("네이버 페이", new ImageIcon(ArApplication.IMG_PATH+"naverpay.png"));
		rbPanel.getPanel().setCursor(new Cursor(Cursor.HAND_CURSOR));
		rbPanel.setFonts(Gui.font(12));
		gbPanel.addNextRow(rbPanel);
		//라디오 버튼의 Enable 을 설정하는 코드, 0번 버튼을 활성화 하고 클릭할때마다 그 버튼을 활성화 한다
		rbPanel.getButtonList().forEach(b->{
			b.setEnabled(rbPanel.getButtonList().indexOf(b) == 0);
			b.addMouseListener(new MouseAdapter() { public void mouseReleased(MouseEvent e) {
			rbPanel.getButtonList().forEach(rb->rb.setEnabled(rb == e.getSource()));}});
		});
		passwordPanel();
		cardPanel.add(passwordPanel.getPanel(), "password");
		
		payForm.setEditable(false);
		dialog.setContentPane(cardPanel);
		dialog.setSize(width, height);
	}

	private JLabel starLabel;
	private void passwordPanel() {
		passwordPanel.removeAll();
		starLabel = Gui.createLabel(Color.BLACK, new Font("D2Coding", Font.BOLD, 40), JLabel.CENTER);
		starLabel.setPreferredSize(new Dimension(width,50));
		passwordPanel.addNorth(starLabel);
		List<String> btnStr = new ArrayList<>();
		for(int i=0; i<=9; i++) {
			btnStr.add(String.valueOf(i));
		}
		Collections.shuffle(btnStr);
		ButtonPanel btnPanel = new ButtonPanel();
		btnPanel.setColor(Gui.DARK_BLUE, Color.WHITE);
		passwordPanel.addCenter(btnPanel);
		btnPanel.setLayout(new GridLayout(3,4));
		btnStr.forEach(s->{btnPanel.addButton(s,a->inputPassword(+1));});
		btnPanel.addButton("←",b->inputPassword(-1));
		btnPanel.addButton("결제",b->pay());
		btnPanel.setFonts(Gui.font(20));
		passwordPanel.newPanel(BorderLayout.SOUTH).add(Gui.createButton
				("뒤로가기",new Color(70,33,33), Color.WHITE, Gui.font(20),b->cardLayout.previous(cardPanel)));
		inputPassword(0);
	}
	
	private void pay() {
		if(passwordNum != 6) return;
		pay.setPayDate(new Date());
		int i = DAO.sql.insert("payment", pay, "id", "pay_seq");
		if(i == 1)
			pay.setStatus("결제 완료");
		dialog.setVisible(false);	
	}

	int passwordNum;
	private void inputPassword(int d) {
		List<String> stars = new ArrayList<>();
		passwordNum += d;
		if(passwordNum < 0) passwordNum = 0;
		else if(passwordNum > 6) passwordNum = 6;
		for(int i=0; i<6; i++) {
			stars.add(i<passwordNum ? "*" : "-");
		}
		starLabel.setText(String.join("  ", stars));
	}
	
	public PayDTO openPay(PayDTO pay) {
		this.pay = pay;
		payButton.setText(String.format("%,d 원 결제하기",pay.getPrice()));
		payForm.setData(pay);
		payForm.set("price", String.format("%,d 원",pay.getPrice()));
		dialog.setVisible(true);
		return this.pay;
	}
	
	public static void main(String[] args) {
		PayDTO p = new PayDTO();
		p.setItem("aaaaaaaa상품");
		p.setPrice(123123);
		p = new PayDialog().openPay(p);
	}
}
