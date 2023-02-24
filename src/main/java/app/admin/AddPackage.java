package app.admin;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.io.File;

import javax.swing.JPanel;

import app.AppView;
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
		imagePanel.setFont(Gui.font(20));
		JPanel panel = blPanel.newPanel(BorderLayout.EAST, 350, 600);
		panel.add(imagePanel.getPanel());
		
		formPanel = new GridBagPanel(600, 400);
		blPanel.addCenter(formPanel);
		
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
		buttonPanel.addButton("리셋",b->reset());
		formPanel.addNextRow(buttonPanel);
	}
	
	private void reset() {
		packageInput.resetForm();
		imagePanel.removeImage();
	}

	private void loadImage() {
		File file = Gui.getFile(null,new File("C:/Users/GGG/Desktop"),"png","jpg");
		if(file == null) return;
		Image image = Gui.getImage(file);
		System.out.println(file + " image:" + image);
		if(image != null) {
			imagePanel.setText(file.getName());
			imagePanel.setImage(Gui.scaleDown(image, 400, 300));
		}
	}

	public void addComponent(InputComponent comp) {
		packageInput.addInputComp(comp);
		formPanel.addNextRow(comp.getPanel());
	}
	
	public void uploadPackage() {
		adminApp.addPackage(packageInput.saveTo(new PackageDTO()));
		System.out.println(packageInput.saveTo(new PackageDTO()));
	}
	
	public static void main(String[] args) {
		Gui.createFrame(new AddPackage(null).rootPanel);
	}
}
