package test;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.time.LocalDateTime;

import javax.swing.JPanel;
import javax.swing.Timer;

import gui.Gui;

public class JarClassLoader {
    public static void main(String[] args) throws Exception {
        URL jarUrl = new URL("file:/D:/ws/java/ar/src/main/resources/clock.jar");
        URLClassLoader classLoader = new URLClassLoader(new URL[] { jarUrl });
        Class<?> clazz = classLoader.loadClass("gui.wiget.AnalogClock");
        Object instance = clazz.getConstructor().newInstance();
        System.out.println(instance);
        Field rootPanelField = instance.getClass().getDeclaredField("wrapPanel");
        rootPanelField.setAccessible(true); // private 필드에 접근하기 위해 설정
        JPanel rootPanel = (JPanel) rootPanelField.get(instance);
        Gui.createFrame(rootPanel).setSize(450,450);
        Method m = instance.getClass().getDeclaredMethod("update", LocalDateTime.class);
        javax.swing.Timer timer = new Timer(1000, e->{
        	try {
				m.invoke(instance, LocalDateTime.now());
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvocationTargetException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        });
        timer.start();
        classLoader.close();
    }
    
}