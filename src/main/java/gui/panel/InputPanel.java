package gui.panel;

import gui.panel.input.InputComponent;

public abstract class InputPanel extends CustomPanel implements InputComponent{
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValue(Object value) {
		// TODO Auto-generated method stub
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
	}
}
