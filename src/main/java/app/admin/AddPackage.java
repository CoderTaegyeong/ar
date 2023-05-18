package app.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import app.AppView;
import app.ArApplication;
import entity.PackageDTO;
import gui.Gui;
import gui.InputForm;
import gui.panel.ImagePanel;
import gui.panel.button.ButtonPanel;
import gui.panel.input.InputComponent;
import gui.panel.input.TextAreaPanel;
import gui.panel.input.TextFieldPanel;
import gui.panel.layout.BorderLayoutPanel;
import gui.panel.layout.GridBagPanel;
import util.FileUtil;
//	private int id;
//	private String imagePath;
//	private String title;
//	private String travelLoc;
//	private int travelDays; 
//	private int price;
//	private String detailText;
	
public class AddPackage extends AppView {
	private InputForm<PackageDTO> packageInput = new InputForm<>();
	private GridBagPanel formPanel;
	private ImagePanel imagePanel;
	private AdminApp adminApp;
	
	public AddPackage(AdminApp adminApp) {
		this.adminApp = adminApp;
		initRootPanel();
	}
	
	public void initRootPanel() {
		BorderLayoutPanel blPanel = new BorderLayoutPanel();
		rootPanel = blPanel.getPanel();
		imagePanel = new ImagePanel();
		imagePanel.setFont(Gui.font(20));
		imagePanel.setBackground(Color.cyan);
		blPanel.newPanel(BorderLayout.CENTER, 400, 300).add(imagePanel.getPanel());
		
		formPanel = new GridBagPanel(280, 600);
		Gui.setMargin(formPanel, 0, 10, 0, 0);
		blPanel.addWest(formPanel.getPanel());
		
		addInputComp(new TextFieldPanel("title","제목"));
		addInputComp(new TextFieldPanel("imagePath","이미지 경로") {{
			getTextField().setEditable(false);
			getPanel().add(Gui.createButton("Load",b->loadImage()),BorderLayout.EAST);
		}});
		
		addInputComp(new TextFieldPanel("travelLoc","여행지"));
		addInputComp(new TextFieldPanel("travelDays","여행일정"));
		addInputComp(new TextFieldPanel("price","가격"));
		addInputComp(new TextAreaPanel("detailText", 15, 15, "상세정보"));
	
		ButtonPanel buttonPanel = new ButtonPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.addButton("패키지 등록",b->uploadPackage());
		buttonPanel.addButton("리셋",b->reset());
		formPanel.addNextRow(buttonPanel);
	}
	
	public void reset() {
		packageInput.resetForm();
		imagePanel.removeImage();
	}

	private void loadImage() {
		File file = FileUtil.getFile(formPanel,new File(ArApplication.IMG_PATH + "/packImages"),"png","jpg");
		if(file == null) return;
		Image image = Gui.getImage(file);
		System.out.println(file + " image:" + image);
		if(image != null) {
			imagePanel.setText(file.getName());
			imagePanel.setImage(Gui.scaleDown(image, 400, 300));
			packageInput.set("imagePath", file.getAbsolutePath());
		}
	}

	public InputComponent addInputComp(InputComponent comp) {
		packageInput.addInputComp(comp);
		formPanel.addNextRow(comp.getPanel());
		return comp;
	}
	
	public void uploadPackage() {
		PackageDTO pack = new PackageDTO();
		pack = packageInput.saveTo(pack);
		File file = new File(packageInput.getString("imagePath"));
        try {
			pack.setImage(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
        adminApp.addPackage(pack);
	}
	
	public static void main(String[] args) {
		Gui.createFrame(new AddPackage(new AdminApp()).rootPanel);
	}
}
