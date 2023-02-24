package gui.panel.input;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JTextField;

import gui.panel.InputPanel;

public class TextFieldPanel extends InputPanel{
	private JLabel label;
	private JTextField textField = new JTextField(10);
	
	{ rootPanel.setLayout(new BorderLayout()) ;}

	public TextFieldPanel(String name) {
		this(name, name);
	}
	
	public TextFieldPanel(String name, String labelText) {
		this(name, labelText, BorderLayout.WEST);
	}
	
	public TextFieldPanel(String name, String labelText, String labelDirection) {
		setName(name);
		label = new JLabel(labelText);
		rootPanel.add(label, labelDirection);
		rootPanel.add(textField, BorderLayout.CENTER);
	}
	
	public void setLabelText(String text) {
		label.setText(text);
	}

	public JLabel getLabel() {
		return label;
	}
	
	public JTextField getTextField() {
		return textField;
	}

	@Override
	public String getValue() {
		return textField.getText();
	}

	@Override
	public void setValue(Object value) {
		textField.setText(value.toString());
	}
	
	public void reset() {
		textField.setText("");
	}
}
