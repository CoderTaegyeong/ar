package app.travel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import app.AppService;
import app.AppView;
import app.ArApplication;
import entity.PackageDTO;
import gui.Gui;
import gui.WrapFrame;
import gui.panel.ImagePanel;
import gui.panel.button.ButtonPanel;
import gui.panel.layout.BorderLayoutPanel;
import util.StrUtil;

public class TravelPackage extends AppView {
	private Travel travel;
	
	private JPanel center = new JPanel();

	private BorderLayoutPanel blPanel = new BorderLayoutPanel();
	private JScrollPane scroll = blPanel.newScroll(center, BorderLayout.CENTER);

	public TravelPackage(Travel travel) {
		this.travel = travel;
		initRootPanel();
	}
	
	@Override
	public void initRootPanel() {
		rootPanel = blPanel.getPanel();
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.getVerticalScrollBar().setUnitIncrement(20);
		
		ButtonPanel buttonPanel = new ButtonPanel();
		buttonPanel.addButton("Refresh", b->action(1));
		blPanel.addSouth(buttonPanel);
	}
	
	private void action(int i) {
		showPackageList();
	}

	public void showPackageList() {
		List<PackageDTO> packageList = travel.getPackageList();
		int rows = Math.max(2, packageList.size()/2);
		int cols = 2;
		center.removeAll();
		center.setLayout(new GridLayout(rows, cols, 5, 5));
		
		int width = (AppService.getInstance().getContainer().style.width - 100) / 2;
		int height = (AppService.getInstance().getContainer().style.height - 250) / 2;
		for(int i=0; i < rows * cols; i++) {
			if(i >= packageList.size()) return;
				PackageDTO pack = packageList.get(i);
				ImagePanel imagePanel = new ImagePanel();
				imagePanel.setImage(Gui.scaleDown(ArApplication.IMG_PATH+"packImages/"+pack.getImagePath(), width, height));
				imagePanel.setText(pack.getTitle());
				imagePanel.setFont(Gui.font(17));
				imagePanel.setAlignment(JLabel.CENTER);
				center.add(imagePanel.getPanel());
				Gui.addBorderOnEnterMouse(imagePanel.getPanel(), b->travel.openDetail(pack));
				WrapFrame.mouseTooltip(imagePanel.getPanel(), StrUtil.addBr(
						"패키지명: " + pack.getTitle(),
						"여행지: " + pack.getTravelLoc(),
						"가격: " + String.format("%,d 원", pack.getPrice()),
						"상세정보: " + StrUtil.shorten(pack.getDetailText(), 20)) , 150, 100, Gui.font(12));
		}
	}
}
