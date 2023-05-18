package app.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import app.AppService;
import app.AppView;
import entity.PayDTO;
import gui.Gui;
import gui.panel.layout.BorderLayoutPanel;

public class PayView extends AppView{
	private BorderLayoutPanel blPanel = new BorderLayoutPanel();
	private JTextArea textArea = new JTextArea();
	private JPanel botPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	
	public PayView(String title, PayDTO pay) {
		super(title);
		initRootPanel();
		textArea.setText(pay.payInfo());
	}
	
	@Override
	public void initRootPanel() {
		rootPanel = blPanel.getPanel();
		blPanel.newScroll(textArea, BorderLayout.CENTER);
		blPanel.addSouth(botPanel);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setFont(Gui.font(30));
		Gui.setMargin(blPanel, 40, 40, 40, 40);
		botPanel.add(Gui.createButton("Close", Gui.font(20), b->AppService.instance().closeView(this)));
	}
}