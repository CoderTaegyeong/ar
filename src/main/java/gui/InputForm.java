package gui;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gui.panel.input.InputComponent;

public class InputForm<T> {
	private Map<String,InputComponent> compMap = new HashMap<>();

	public InputComponent addInputComp(InputComponent inputComp) {
		compMap.put(inputComp.getName(), inputComp);
		return inputComp;
	}
	
	public void resetForm() {
		compMap.forEach((name,comp)->comp.reset());
	}

	public void setData(T dto) {
		for(Field field : dto.getClass().getDeclaredFields()) {
			if(compMap.containsKey(field.getName())) {
				try {
					compMap.get(field.getName()).setValue(field.get(dto));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public T saveTo(T t) {
		if(t == null) return null;
		for(Field field : t.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			try {
				if(field.getType().equals(int.class)) {
					field.set(t, getInt(field.getName()));
				}else if(field.getType().equals(long.class)) {
					field.setLong(t, getLong(field.getName()));
				}else if(field.getType().equals(double.class)) {
					field.setDouble(t, getDouble(field.getName()));
				}else if(field.getType().equals(float.class)) {
					field.setFloat(t, getFloat(field.getName()));
				}else if(field.getType().equals(String.class)){
					field.set(t, getValue(field.getName()));
				}else {
					field.set(t, null);
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		return t;
	}
	
	public String findEmptyFields() {
	    List<String> keys = new ArrayList<>();
	    for (String key : compMap.keySet()) {
	        InputComponent inputComponent = compMap.get(key);
	        String value = inputComponent.getValue().toString();
	        if (value.isEmpty()) keys.add(key);
	    }
	    return String.join(" ", keys);
	}
	
	public Object getValue(String name) {
		if(compMap.containsKey(name))
			return compMap.get(name).getValue();
		else
			return "";
	}
	
	public String getString(String name) {
		return getValue(name).toString();
	}
	
	public int getInt(String name) {
		if(getString(name).isEmpty()) return 0;
		return Integer.parseInt(getString(name));
	}
	
	public long getLong(String name) {
		if(getString(name).isEmpty()) return 0;
		return Long.parseLong(getString(name));
	}
	
	public float getFloat(String name) {
		if(getString(name).isEmpty()) return 0;
		return Float.parseFloat(getString(name));
	}
	
	public double getDouble(String name) {
		if(getString(name).isEmpty()) return 0;
		return Double.parseDouble(getString(name));
	}
}