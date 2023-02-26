package test;

import app.AppView;

public class TEST3 {
	public static void main(String[] args) {
		AppView a = new AppView() {
			
			@Override
			public void initRootPanel() {
				// TODO Auto-generated method stub
				
			}
		};
		System.out.println(a.getClass().getName());
		System.out.println(a.getClass().getSimpleName());
		System.out.println(a);
	}
}
