package app.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import app.AppView;
import app.SubApp;
import gui.Gui;
import gui.panel.button.input.RadioButtonPanel;
import gui.panel.layout.BorderLayoutPanel;

public class TermsView extends AppView{
	private BorderLayoutPanel blPanel = new BorderLayoutPanel();
	private JTextArea textArea;
	private BorderLayoutPanel botPanel = new BorderLayoutPanel();
	private JButton nextButton;
	private JScrollPane scroll = new JScrollPane();
	public TermsView(String title, String text, Consumer<?> action) {
		this(title, text, null, null, action);
	}
	
	public TermsView(String title, String text, String buttonText, Font font, Consumer<?> action) {
		this(title, text, buttonText, font, action, null);
	}
	
	public TermsView(String title, String text, String buttonText, Font font, Consumer<?> action, SubApp subApp) {
		super(title, subApp);
		initRootPanel();
		textArea = new JTextArea(text);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setFont(font == null ? Gui.font(30) : font);
		RadioButtonPanel rbPanel = new RadioButtonPanel();
		rbPanel.setLayout(new GridLayout(2,1));
		rbPanel.addButton("동의", b->nextButton.setEnabled(true));
		rbPanel.addButton("동의 안함", b->nextButton.setEnabled(false));
		rbPanel.setSelected(1);
		botPanel.addCenter(rbPanel);
		nextButton = Gui.createButton(buttonText == null ? "Next" : buttonText, b->action.accept(null));
		nextButton.setEnabled(false);
		botPanel.addEast(nextButton);
		scroll.setViewportView(textArea);
	}
	
	@Override
	public void initRootPanel() {
		rootPanel = blPanel.getPanel();
		blPanel.addCenter(scroll);
		blPanel.addSouth(botPanel);
	}
	
	public JScrollPane getScroll() {
		return scroll;
	}
	public JTextArea getTextArea() {
		return textArea;
	}
}
