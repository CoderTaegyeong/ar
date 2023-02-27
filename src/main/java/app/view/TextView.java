package app.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.function.Consumer;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import app.AppService;
import app.AppView;
import app.SubApp;
import gui.Gui;
import gui.panel.layout.BorderLayoutPanel;

public class TextView extends AppView{
	private BorderLayoutPanel blPanel = new BorderLayoutPanel();
	private JTextArea textArea = new JTextArea();
	private JPanel botPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	
	public TextView(String title, String text) {
		this(title, text, null);
	}
	
	public TextView(String title, String text, Consumer<?> action) {
		this(title, text, null, null, action);
	}
	
	public TextView(String title, String text, String buttonText, Font font, Consumer<?> action) {
		this(title, text, buttonText, action, font, null);
	}
	
	public TextView(String title, String text, String buttonText, Consumer<?> action, Font font, SubApp subApp) {
		super(title, subApp);
		initRootPanel();
		textArea.setText(text);
		textArea.setFont(font);
		if(action != null) 
			botPanel.add(Gui.createButton(buttonText == null ? "Action" : buttonText, b->action.accept(null)));
		else 
			botPanel.add(Gui.createButton("Close", b->AppService.instance().closeView(this)));
	}
	
	@Override
	public void initRootPanel() {
		rootPanel = blPanel.getPanel();
		blPanel.newScroll(textArea, BorderLayout.CENTER);
		blPanel.addSouth(botPanel);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setFont(Gui.font(30));
	}
}
