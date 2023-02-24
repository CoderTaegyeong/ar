package app.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import app.AppView;
import app.ArApplication;
import entity.PackageDTO;
import gui.Gui;
import gui.InputForm;
import gui.panel.ImagePanel;
import gui.panel.button.ButtonPanel;
import gui.panel.button.input.CheckBoxPanel;
import gui.panel.input.InputComponent;
import gui.panel.input.TextFieldPanel;
import gui.panel.layout.BorderLayoutPanel;
import gui.panel.layout.GridBagPanel;

public class AddPackage extends AppView{
	private AdminApp adminApp;
	
	public AddPackage(AdminApp adminApp) {
		this.adminApp = adminApp;
		initRootPanel();
	}
//	private int id;
//	private String imagePath;
//	private String title;
//	private String travelLoc;
//	private int travelDays; 
//	private int price;
//	private String detailText;
	
	private InputForm<PackageDTO> packageInput = new InputForm<>();
	private GridBagPanel formPanel;
	private ImagePanel imagePanel;
		
	public void initRootPanel() {
		BorderLayoutPanel blPanel = new BorderLayoutPanel();
		rootPanel = blPanel.getPanel();
		imagePanel = new ImagePanel();
		imagePanel.setImage(Gui.getImage(ArApplication.IMG_PATH+"n1.png"), 300, 300);
		imagePanel.setText("N1.png");
		imagePanel.setFont(Gui.font(20));
		JPanel panel = blPanel.newPanel(BorderLayout.EAST);
		panel.add(imagePanel.getPanel());
		panel.setBorder(new LineBorder(Color.RED, 1));
		
		formPanel = new GridBagPanel(500, 400);
		blPanel.addCenter(formPanel);
		formPanel.setSize(400, 400);
		
		addComponent(new TextFieldPanel("id","패키지 번호"));
		addComponent(new TextFieldPanel("imagePath","이미지 경로"));
	
		CheckBoxPanel cbPanel = new CheckBoxPanel();
		cbPanel.setName("title");
		cbPanel.addButton("check1");
		cbPanel.addButton("check2");
		addComponent(cbPanel);
		
		ButtonPanel buttonPanel = new ButtonPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		buttonPanel.addButton("이미지로드",b->loadImage());
		buttonPanel.addButton("패키지등록",b->uploadPackage());
		buttonPanel.addButton("리셋",b->packageInput.resetForm());
		formPanel.addNextRow(buttonPanel);
	}
	
	private void loadImage() {
		File file = Gui.getFile(null,new File(ArApplication.IMG_PATH),"png","jpg");
		if(file == null) return;
		Image image = Gui.getImage(file);
		System.out.println(file + " image:" + image);
		if(image != null) {
			imagePanel.setImage(Gui.scaleDown(image, 400, 300));
			imagePanel.setText(file.getName());
		}
	}

	public void addComponent(InputComponent comp) {
		packageInput.addInputComp(comp);
		formPanel.addNextRow(comp.getPanel());
	}
	
	public void uploadPackage() {
//		adminApp.addPackage(packageInput.saveTo(new PackageDTO()));
		System.out.println(packageInput.saveTo(new PackageDTO()));
	}
	
	public static void main(String[] args) {
		Gui.createFrame(new AddPackage(null).rootPanel);
	}
}
