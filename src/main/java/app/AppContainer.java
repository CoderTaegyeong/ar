package app;

import static app.ArApplication.IMG_PATH;
import static gui.Gui.addBorderOnEnterMouse;
import static gui.Gui.createFont;
import static gui.Gui.createIconLabel;
import static gui.Gui.font;
import static gui.Gui.getResizedImage;
import static gui.Gui.moveToCenter;
import static gui.Gui.setMargin;
import static test.Debug.sysout;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import app.admin.AdminApp;
import app.view.TextView;
import entity.MemberDTO;
import gui.WrapFrame;
import gui.panel.ImagePanel;
import gui.panel.button.ButtonPanel;
import gui.panel.layout.BorderLayoutPanel;
import gui.wiget.ZonedClock;
import util.StrUtil;
import util.Style;

public class AppContainer {
	private JFrame frame;
	private BorderLayoutPanel rootPanel = new BorderLayoutPanel();
	private ConfigDialog config = new ConfigDialog(this);
	
	private CardLayout cardLayout = new CardLayout();
	private int cardIndex;
	private JPanel cardPanel, container;
	private BorderLayoutPanel topPanel, botPanel;

	private final int rows = 3, cols = 3;
	private List<AppView> viewList = new Vector<>();
	private AppView currentView;
	
	private JLabel viewIconLabel = new JLabel();
	private JLabel viewTitleLabel = new JLabel();
	private JLabel viewInfoLabel = new JLabel();
	private JLabel timeLabel = new JLabel();

	//+++++++++++++++++++++++++++++++++++Style+++++++++++++++++++++++++++++++
	public Style style;
	private String timeFormat = "YYYY-MM-dd EEE HH:mm:ss";
	private Dimension botBothSide = new Dimension(200,51);
	private ImageIcon contIcon = new ImageIcon(IMG_PATH+"conticon.png");
	private Font subAppTitleFont = createFont("맑은 고딕", 28);
	private List<ImagePanel> iconPanelList;
	
	public void setStyle() {
		timeLabel.setFont(createFont(17));
		viewTitleLabel.setFont(createFont("맑은 고딕", 28));
		cardPanel.setBorder(BorderFactory.createLineBorder(style.getColor("contBorder"), 20));
		viewTitleLabel.setForeground(style.getColor("fontColor"));
		viewInfoLabel.setForeground(style.getColor("fontColor"));
		timeLabel.setForeground(style.getColor("fontColor"));
		topPanel.setBackgrounds(style.getColor("topBotColor"));
		botPanel.setBackgrounds(style.getColor("topBotColor"));
		container.setBackground(style.getColor("contBg"));
		iconPanelList.forEach(ip->{ ip.getPanel().setBackground(style.getColor("contBg")); 
			if(ip.getLabel() != null) ip.getLabel().setForeground(style.getColor("subTitle"));
		});
	}
	//-----------------------------------Style---------------------------------
	public AppContainer() {
		config.loadStyle();
	}
	
    public void initRootPanel() {
    	viewList.clear();
    	rootPanel.removeAll();
    	
    	cardPanel = new JPanel(cardLayout);
    	container = new JPanel(new GridLayout(rows,cols,20,20));
    	
    	cardPanel.setPreferredSize(new Dimension(style.width, style.height-80));
		container.setPreferredSize(cardPanel.getPreferredSize());

		topPanel = new BorderLayoutPanel();
		JPanel topLeftPan = topPanel.newPanel(BorderLayout.WEST, 450, 45);
		topLeftPan.setLayout(new FlowLayout(FlowLayout.LEFT));
		topLeftPan.add(viewIconLabel);
		topLeftPan.add(viewTitleLabel);

		ButtonPanel topBtnPanel = new ButtonPanel();
		topBtnPanel.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		topBtnPanel.setButtonSize(22, 22);
		for(int i=1; i<=8; i++) {
			final int a = i;
			topBtnPanel.addButton(new ImageIcon(IMG_PATH+"n"+i+".PNG"), b->action(a));
		}
		topPanel.addCenter(topBtnPanel);
		
		topPanel.newPanel(BorderLayout.EAST)
		.add(setMargin(createIconLabel(IMG_PATH+"config.png", 33, 33, b->config.open()), 5, 0, 0, 5));
		
		botPanel = new BorderLayoutPanel();
		botPanel.newPanel(BorderLayout.WEST, botBothSide, FlowLayout.RIGHT)
			.add(setMargin(viewInfoLabel, 6, 0, 0, 0));
		viewInfoLabel.setPreferredSize(new Dimension(botBothSide.width - 20, botBothSide.height));
		
		ButtonPanel botBtnPan = new ButtonPanel();
		botBtnPan.addButton(new ImageIcon(IMG_PATH+"leftarrow.png"), b->moveClick(-1));
		botBtnPan.addButton(new ImageIcon(IMG_PATH+"home.png"), b->moveClick(-100));
		botBtnPan.addButton(new ImageIcon(IMG_PATH+"rightarrow.png"), b->moveClick(1));
		botBtnPan.addButton(new ImageIcon(IMG_PATH+"close.png"), b->moveClick(-200));
		
		timeLabel.setHorizontalAlignment(JLabel.CENTER);
		timeLabel.setPreferredSize(botBothSide);
		
		botPanel.addCenter(botBtnPan);
		botPanel.addEast(timeLabel);
		
		rootPanel.addNorth(topPanel);
		rootPanel.addCenter(cardPanel);
		rootPanel.addSouth(botPanel);
		
		container.setName("Container");
		cardPanel.add(container, container.getName());
		
		AppService.instance().updateSubAppIcons();

		move(-100);
		updateViewCount();
		setStyle();
	}
    
    public void moveClick(int d) {
    	if(AppService.instance().getAttribute("Member") == null) {
    		WrapFrame.alert("로그인 해주세요.", cardPanel);
    	}else {
    		move(d);
    	}
    }
    
    /**
     * @param d == -100 : "Home",<br>
     * @param d == -200 : removeView()
     */
	public void move(int d) {
		currentView = null;
		if(d == -100 || viewList.isEmpty()) {
			cardLayout.show(cardPanel, container.getName());
			viewTitleLabel.setText("Home");
			viewIconLabel.setIcon(contIcon);
			cardIndex = -1;
		} else if(d == -200 && cardIndex >= 0 && cardIndex < viewList.size()) {
			removeView(viewList.get(cardIndex));
		} else {
			cardIndex += d;
			if(cardIndex < 0) cardIndex = viewList.size() -1;
			else if(cardIndex >= viewList.size()) cardIndex = 0;
			attachView(viewList.get(cardIndex));
		}
		updateViewCount();
	}

    public void removeViews(SubApp subApp) {
    	int viewCount = viewList.size();
    	viewList.removeIf(view -> subApp != null && view.parentApp() != null && view.parentApp().equals(subApp));
    	int removeCount = viewCount - viewList.size();
    	if(cardIndex != -1) move(-removeCount);
    	sysout("Remove Views -- View Count : "+ viewCount, "removeCount : "+ removeCount);
    }
    
	public void removeView(AppView appView) {
		if(appView != null && appView.close() && viewList.contains(appView)) {
			cardPanel.remove(appView.getPanel());
			cardLayout.removeLayoutComponent(appView.getPanel());
			viewList.remove(appView);
			move(-1);
		}
		updateViewCount();
		sysout("Remove View : " + appView, "View List:" +viewList);
	}
	
	public void addView(AppView appView) {
		if(appView == null) return;
		if(viewList.size() >= 25) {
			WrapFrame.alert("Max View Count is 25", font(50), cardPanel);
			return;
		}
		if(!viewList.contains(appView)) {
			viewList.add(appView);
			cardPanel.add(appView.getPanel(), appView.getClass().getName());
			cardIndex = viewList.size() -1;
		}else {
			cardIndex = viewList.indexOf(appView);
		}
		attachView(appView);
		sysout("Add View : " + appView, " View List:" + viewList);
	}
	
	private void attachView(AppView appView) {
		cardLayout.show(cardPanel, appView.getClass().getName());
		viewTitleLabel.setText(appView.getTitle());
		ImageIcon icon = appView.getImageIcon();
		if(icon == null) icon = contIcon;
		viewIconLabel.setIcon(icon);
		currentView = appView;
		updateViewCount();
	}
	
	public void addAppIcons(List<SubApp> appList) {
		iconPanelList = new Vector<ImagePanel>();
		container.removeAll();
		for(int i=0; i<rows * cols; i++) {
			if(i < appList.size())
				addAppIcon(appList.get(i));
			else
				addAppIcon(null);
		}
		setStyle();
	}
	
	public void addAppIcon(SubApp subApp) {
		ImagePanel iconPanel = new ImagePanel();
		iconPanelList.add(iconPanel);
		container.add(iconPanel.getPanel());
		
		if(subApp == null) return;
		iconPanel.setText(subApp.getTitle());
		iconPanel.setAlignment(JLabel.CENTER);
		iconPanel.setFont(subAppTitleFont);
		iconPanel.setImage(getResizedImage(IMG_PATH+subApp.getClass().getSimpleName()+".PNG", IMG_PATH+"defaultimg.PNG", 100, 100));
		addBorderOnEnterMouse(iconPanel.getPanel(), b->addView(subApp.requestView()), 2);
	}
	
	public void update() {
		LocalDateTime time = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormat);
	    String formattedTime = time.format(formatter);
		timeLabel.setText(formattedTime);
		viewList.forEach(view->view.update(time));
	}
	
    public void showFrame() {
    	if(frame != null) frame.dispose();
		frame = new JFrame("항공권 예약 어플리케이션");
		frame.setIconImage(contIcon.getImage());
		frame.setContentPane(rootPanel.getPanel());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setMinimumSize(new Dimension(style.width, style.height));
		moveToCenter(frame, style.width, style.height+100);
    }
	
	public JFrame getFrame() {
		return frame;
	}
	
	//___________________________________________DEBUG_________________________________________________
	public void updateViewCount() {
		viewInfoLabel.setText(StrUtil.addBr("View Count : "+ viewList.size(), "Card Index : "+ cardIndex
					,"Current View : "+ (currentView == null ? "Home" : 
					currentView.getClass().getSimpleName().isEmpty() ? "Anonymous" : 
					currentView.getClass().getSimpleName())
					));
	}
	
	public void action(int i) {
		sysout("Debug Button :", i);
		
		if(i==1) {
			AppService.instance().addSubApp(new AdminApp());
			AppService.instance().updateSubAppIcons();
		}
		if(i==2) { 
			AppService.instance().removeSubApp(AppService.instance().getSubApp(AdminApp.class));
			AppService.instance().updateSubAppIcons();
		}
		
		if(i==3) {
//			SelectSeat s = new SelectSeat(null, new TicketDTO());
//			addView(s);
		}
		
		if(i==4) {
			addView(
				new AppView() {
					ZonedClock clock = new ZonedClock(110);
					{initRootPanel();}
					@Override
					public void initRootPanel() {
						clock.setColor(Color.yellow, Color.BLACK);
						clock.initRootPanel();
						JPanel panel = new JPanel(new BorderLayout());
						panel.add(clock.getPanel(), "Center");
//						rootPanel.setBackground(Color.black);
//						panel.setBorder(new LineBorder(Color.YELLOW, 2));
						rootPanel.add(panel);
					}
					@Override
					public void update(LocalDateTime time) {
						clock.setTime(time);
					}
					public String getTitle() {
						return "Analog Clock";
					}
				}
			);
		}
		
		if(i == 5) {
//			sysout(AppService.instance().getAttr("Member"));
			topPanel.setBackgrounds(Color.red);
		}
		if( i == 6) {
			WrapFrame.greenAlert("Success !", iconPanelList.get(0).getPanel(), font(25));
			WrapFrame.alert("Alert !", iconPanelList.get(1).getPanel());
		}
		
		if(i == 7) {
			addView(new TextView("title1", "seececesfaefsazc4216542365874463q23743q23684732q86437q2683214563f", b->action(2)));
		}
		if(i == 8) {
			MemberDTO member = new MemberDTO("dummy","1234","name","010-0100-1211","ggg@gmail.com");
			AppService.instance().setAttr("Member", member);
			sysout("로그인 : " + member);
		}
	}
	//___________________________________________DEBUG_________________________________________________
}