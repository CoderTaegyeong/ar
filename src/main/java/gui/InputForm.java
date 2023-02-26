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
	
	public void set(String name, Object value) {
		if(compMap.containsKey(name))
			compMap.get(name).setValue(value);
	}
	
	public void resetForm() {
		compMap.forEach((name,comp)->comp.reset());
	}

	public boolean validate() {
		return true;
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
			String fieldName = field.getName();
			if(!compMap.containsKey(fieldName)) continue;
			Class<?> type = field.getType();
			try {
				if(type.equals(int.class) || type.equals(Integer.class)) {
					field.set(t, getInt(fieldName));
				}else if(type.equals(long.class) || type.equals(Long.class)) {
					field.setLong(t, getLong(fieldName));
				}else if(type.equals(double.class) || type.equals(Double.class)) {
					field.setDouble(t, getDouble(fieldName));
				}else if(type.equals(float.class) || type.equals(Float.class)) {
					field.setFloat(t, getFloat(fieldName));
				}else if(type.equals(String.class)){
					field.set(t, getValue(fieldName));
				}
			} catch (Exception e) {
				System.out.println(e);
				return null;
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