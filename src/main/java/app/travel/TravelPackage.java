package app.travel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import app.AppService;
import app.AppView;
import entity.PackageDTO;
import gui.Gui;
import gui.WrapFrame;
import gui.panel.ImagePanel;
import gui.panel.button.ButtonPanel;
import gui.panel.layout.BorderLayoutPanel;
import util.StrUtil;
import util.Style;

public class TravelPackage extends AppView {
	private Travel travel;
	private BorderLayoutPanel blPanel = new BorderLayoutPanel();
	private JPanel centerPanel = new JPanel();
	private JScrollPane scroll = blPanel.newScroll(centerPanel, BorderLayout.CENTER);

	public TravelPackage(Travel travel) {
		super("여행 패키지 상품", travel);
		this.travel = travel;
		initRootPanel();
	}
	
	@Override
	public void initRootPanel() {
		rootPanel = blPanel.getPanel();
		scroll.getVerticalScrollBar().setUnitIncrement(20);
		
		ButtonPanel buttonPanel = new ButtonPanel();
		buttonPanel.addButton("Refresh", b->action(1));
	}
	
	private void action(int i) {
		showPackageList();
	}

	public void showPackageList() {
		List<PackageDTO> packageList = travel.getPackageList();
		System.out.println(packageList.size());
		int rows = (int) Math.ceil((double)packageList.size() / 2);
		int cols = 2;
		centerPanel.removeAll();
		centerPanel.setLayout(new GridLayout(rows, cols, 5, 5));
		
		Style style = AppService.instance().getContainer().style; 
		int width = (style.width - 100) / 2;
		int height = (style.height - 250) / 2;
		for(int i=0; i < packageList.size(); i++) {
			PackageDTO pack = packageList.get(i);
			ImagePanel imagePanel = new ImagePanel();
			imagePanel.setSize(width, height);
			imagePanel.setImage(Gui.scaleDown(Gui.getImage(pack.getImage()), width, height));
			imagePanel.setText(pack.getTitle());
			imagePanel.setFont(Gui.font(17));
			imagePanel.setAlignment(JLabel.CENTER);
			centerPanel.add(imagePanel.getPanel());
			Gui.addBorderOnEnterMouse(imagePanel.getPanel(), b->travel.openDetail(pack));
			WrapFrame.mouseTooltip(imagePanel.getPanel(), StrUtil.addBr(
					"패키지명: " 	+ pack.getTitle(),
					"여행지: "	+ pack.getTravelLoc(),
					"여행기간: " 	+ pack.getTravelDays(),
					"가격: " 		+ String.format("%,d 원", pack.getPrice()),
					"상세정보: " 	+ StrUtil.shorten(pack.getDetailText(), 80)) , 230, 170, Gui.font(12));
		}
	}
}
