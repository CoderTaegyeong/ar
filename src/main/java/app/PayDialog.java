package app;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import app.membership.Membership;
import dao.DAO;
import entity.MembershipDTO;
import entity.Mileage;
import entity.PayDTO;
import gui.Gui;
import gui.InputForm;
import gui.panel.button.ButtonPanel;
import gui.panel.button.input.RadioButtonPanel;
import gui.panel.input.TextFieldPanel;
import gui.panel.layout.BorderLayoutPanel;
import gui.panel.layout.GridBagPanel;
import test.Debug;

public class PayDialog {
	public static final String PAY_OK = "결제완료";
	private JDialog dialog;
	private GridBagPanel gbPanel;
	private BorderLayoutPanel blPanel, passwordPanel;
	private InputForm<PayDTO> payForm;
	private CardLayout cardLayout;
	private JPanel cardPanel;
	private RadioButtonPanel rbPanel;
	private JButton payButton = Gui.createButton("", Color.WHITE, Gui.DARK_GREEN,  
								Gui.font(30), b->cardLayout.next(cardPanel));
	private JTextField mileTextField;
	private JLabel mileLabel = Gui.createLabel(Color.BLACK, Gui.font(13), JLabel.RIGHT);
	private JLabel membershipLabel = Gui.createLabel(Color.BLACK, Gui.font(13), JLabel.LEFT);
	private JLabel starLabel = Gui.createLabel(Color.WHITE, new Font("D2Coding", Font.BOLD, 42), JLabel.CENTER);
	private int width = 400, height = 400, passwordNum;
	private MembershipDTO membershipDTO; 
	private PayDTO pay;
	
	private void initDialog() {
		dialog.setIconImage(Gui.getImage(ArApplication.IMG_PATH+"star.png"));
		gbPanel = new GridBagPanel(width, 200);
		payForm = new InputForm<PayDTO>(23, 100, Gui.font(15));
		payForm.rows = 3;
		blPanel = new BorderLayoutPanel();
		cardLayout = new CardLayout();
		rbPanel = new RadioButtonPanel();
		passwordPanel = new BorderLayoutPanel();
		cardPanel = new JPanel(cardLayout);
		dialog.setTitle("Payment");
		dialog.setResizable(false);
		dialog.setAlwaysOnTop(true);
		blPanel.newScroll(gbPanel, BorderLayout.CENTER);
		blPanel.addSouth(payButton);
		cardPanel.add(blPanel.getPanel(), "pay");
		gbPanel.setBackground(new Color(230,242,215));
		payForm.panelBgColor = gbPanel.getBackground();
		
		gbPanel.addNextRow(payForm.createTFP("id", "  아이디"));
		gbPanel.addNextRow(payForm.createTAP("item", "  상품명"));
		gbPanel.addNextRow(payForm.createTFP("price", "  가격"));
		TextFieldPanel milePanel = payForm.createTFP("mile", "  마일 사용");
		mileTextField = milePanel.getTextField();
		mileTextField.addKeyListener(new MileAdapter());
		gbPanel.addNextRow(milePanel);
		gbPanel.addNextRow(mileLabel);
		gbPanel.addNextRow(membershipLabel);

		rbPanel.addButton("카드 결제    ", new ImageIcon(ArApplication.IMG_PATH+"cardpay.png"));
		rbPanel.addButton("카카오 페이    ", new ImageIcon(ArApplication.IMG_PATH+"kakaopay.png"));
		rbPanel.addButton("네이버 페이", new ImageIcon(ArApplication.IMG_PATH+"naverpay.png"));
		rbPanel.getPanel().setCursor(new Cursor(Cursor.HAND_CURSOR));
		rbPanel.setFonts(Gui.font(12));
		rbPanel.setBackgrounds(gbPanel.getBackground());
		rbPanel.setBackground(gbPanel.getBackground());
		gbPanel.addNextRow(rbPanel);
		
		//라디오 버튼의 Enable 을 설정하는 코드, 0번 버튼을 활성화 하고 클릭할때마다 그 버튼을 활성화 한다
		rbPanel.getButtonList().forEach(b->{
			b.setEnabled(rbPanel.getButtonList().indexOf(b) == 0);
			b.setSelected(rbPanel.getButtonList().indexOf(b) == 0);
			b.addMouseListener(new MouseAdapter() { public void mouseReleased(MouseEvent e) {
					rbPanel.getButtonList().forEach(rb->{
					rb.setEnabled(rb == e.getSource());});
					b.setSelected(true);}});
		});
		
		cardPanel.add(passwordPanel.getPanel(), "password");
		payForm.setEditable(false);
		dialog.setContentPane(cardPanel);
		dialog.setSize(width, height);
	}

	private void passwordPanel() {
		passwordPanel.removeAll();
		passwordPanel.setBackground(new Color(40,50,100));
		passwordPanel.setBorder(new LineBorder(Color.BLACK, 2));
		passwordNum = 0;
		JPanel panel = passwordPanel.newPanel(BorderLayout.NORTH);
		panel.setBackground(Color.WHITE);
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 2, 2));
		panel.add(Gui.createLabel("결제 비밀번호 입력   ", Color.BLACK, new Font("맑은 고딕", Font.PLAIN, 27), JLabel.LEFT));
		panel.add(Gui.createButton("뒤로가기", Color.WHITE, Gui.DARK_RED, Gui.font(17),b->cardLayout.previous(cardPanel)));
		
		starLabel.setPreferredSize(new Dimension(width,50));
		Gui.border(starLabel, Gui.DARK_BLUE, 2);
		passwordPanel.addCenter(starLabel);
		List<String> btnStr = new ArrayList<>();
		for(int i=0; i<=9; i++) {
			btnStr.add(String.valueOf(i));
		}
		Collections.shuffle(btnStr);
		ButtonPanel btnPanel = new ButtonPanel();
		btnPanel.setSize(width, (int)(height * 0.5));
		btnPanel.setColor(Gui.DARK_BLUE, Color.WHITE);
		passwordPanel.addSouth(btnPanel);
		btnPanel.setLayout(new GridLayout(3,4));
		btnStr.forEach(s->{btnPanel.addButton(s,a->inputPassword(+1));});
		btnPanel.addButton("←",b->inputPassword(-1));
		btnPanel.addButton("결제",b->doPay());
		btnPanel.setFonts(Gui.font(27));
		inputPassword(0);
	}
	
	private void doPay() {
		if(passwordNum != 6) return;
		pay.setPayDate(new Date());
		pay.setPayWith(rbPanel.getValue());
		pay.setStatus(PAY_OK);
		int payResult = DAO.sql.insert("payment", pay, "num", "pay_seq");
		if(payResult == 1) {
			if(membershipDTO.getId() != null && membershipDTO.getId().equals(pay.getId())) { //멤버십에 가입되있는경우
				Mileage mile = new Mileage();
				mile.setId(membershipDTO.getId());
				if(pay.getMile() > 0) {
					membershipDTO.setMileage(membershipDTO.getMileage() - pay.getMile());
					mile.setMileChange(-pay.getMile());
					mile.setDetail(pay.getItem() + " --마일 사용");
					AppService.instance().getSubApp(Membership.class).updateMileage(mile);
					DAO.sql.update("membership", membershipDTO, "id");
				}
				mile.setMileChange((int)(pay.getPay() * 0.01));
				if(mile.getMileChange() > 0) {
					mile.setDetail(pay.getItem() + "++마일 적립");
					AppService.instance().getSubApp(Membership.class).updateMileage(mile);
				}
			}
			Debug.sysout("결제 정보(PayDialog.doPay()) : ", pay);
			JOptionPane.showMessageDialog(dialog, "[" + pay.getItem() + "] 결제 완료");
			dialog.dispose();
		} else {
			Debug.sysout("error : " + pay);
		}
	}

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
		if(pay == null) return null;
		if(pay.getId() == null || !pay.getId().equals(AppService.instance().getAttribute("id"))) {
			pay.setStatus("결제실패 : 잘못 된 아이디");
			return pay;
		}
		
		JFrame frame = AppService.instance().getContainer().getFrame();
		dialog = new JDialog(frame);
		initDialog();
		passwordPanel();
		Gui.placeSubWindow(frame, dialog, 4);

		this.pay = pay;
		payForm.setData(pay);
		payForm.set("price", String.format("%,d 원",pay.getPrice()));
		
		Optional<MembershipDTO> om =  AppService.instance().getSubApp(Membership.class).getMembership(pay.getId());
		membershipDTO = om.isPresent() ? om.get() : new MembershipDTO();
		if(om.isPresent()) {
			mileTextField.setEditable(true);
			mileLabel.setText(String.valueOf("  사용 가능 마일 : "+String.format("%,d 마일",membershipDTO.getMileage())));
		}
		calcPay(0);
		
		dialog.setModal(true);
		dialog.setVisible(true);
		return this.pay;
	}

	private void calcPay(int mile) {
		if(mile >= 0 && mile <= membershipDTO.getMileage() && mile <= pay.getPrice()) {
			pay.setPay(pay.getPrice() - mile);
			pay.setMile(mile);
			payButton.setText(String.format("%,d 원 결제하기",pay.getPay()));
		}
		if(membershipDTO.getId() != null && membershipDTO.getId().equals(pay.getId())) {
			membershipLabel.setText("  결제시 마일 적립 : " + String.format("%,d 마일", (int)(pay.getPay() * 0.01)));
		}else {
			membershipLabel.setText("  결제시 마일 적립 : 멤버십 회원이 아닙니다.");
		}
	}
	
	private class MileAdapter extends KeyAdapter {
		public void keyTyped(KeyEvent e) {
		    char c = e.getKeyChar();
		    String text = mileTextField.getText();
		    if (membershipDTO == null || !(Character.isDigit(c) || e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK ||
		    	c != KeyEvent.VK_BACK_SPACE || c != KeyEvent.VK_DELETE )) {
		        e.consume();
		    } 
	        try {
	        	int inputValue = Integer.parseInt(mileTextField.getText()+c);
	            if (inputValue > pay.getPrice() || inputValue < 0 || inputValue > membershipDTO.getMileage()) { // 입력된 값이 0 과 membership.getmileage() 사이의 값이 아니면 무시
	                e.consume();
	            }
	            calcPay(inputValue);
	        } catch (NumberFormatException ex) {
	        	if(!text.isBlank())
	        		calcPay(Integer.parseInt(text));
	            e.consume();
	        }
		}
	}
}
