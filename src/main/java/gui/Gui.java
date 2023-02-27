package gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;

import app.ArApplication;
import gui.panel.CustomPanel;
import gui.table.DataTable;
import gui.table.ListTable;
import gui.table.StringTable;

public final class Gui {
	public static final String IMG_PATH = ArApplication.IMG_PATH;
	
	public static final String NIMBUS = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
	public static final String WINDOWS = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
    public static final String MOTIF = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
    public static final String METAL = "javax.swing.plaf.metal.MetalLookAndFeel";
//    public static final String MAC = "com.apple.laf.AquaLookAndFeel";
    
    public static final Color DARK_BLUE = new Color(20, 20, 70);
    
	private Gui() {}

	public static JComponent border(JComponent comp, Color color, int t) {
		comp.setBorder(BorderFactory.createLineBorder(color, t));
		return comp;
	}
	
	public static JComponent redBorder(JComponent comp, int t) {
		return border(comp, Color.RED, t);
	}
	
	public static CustomPanel setMargin(CustomPanel customPanel, int top, int left, int bottom, int right) {
		customPanel.getPanel().setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
		return customPanel;
	}
	
	public static JComponent setMargin(JComponent comp, int top, int left, int bottom, int right) {
		comp.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
		return comp;
	}
	
	public static JLabel createLabel(String text, Color fgColor, int size, int alignment) {
		return createLabel(text, fgColor, font(size), alignment);
	}
	
	public static JLabel createLabel(Color fgColor, Font font, int alignment) {
		return createLabel("", fgColor, font, alignment);
	}

	/** 0 : LEFT,  1: CENTER, 2 : RIGHT */
	public static LayoutManager flowLayout(int l) {
		return new FlowLayout(l,0,0);
	}
	
	public static JLabel createLabel(String text, Color fgColor, Font font, int alignment) {
		JLabel label = new JLabel(text);
		label.setHorizontalAlignment(alignment);
		label.setForeground(fgColor);
		label.setFont(font);
		return label;
	}
	
	public static JLabel createIconLabel(String path) {
		return new JLabel(new ImageIcon(path));
	}
	
	public static JLabel createIconLabel(String path, int width, int height) {
		return createIconLabel(path, width, height, null);
	}
	
	public static JLabel createIconLabel(String path, int width, int height, Consumer<?> action) {
		return createIconLabel(getResizedImage(path, width, height), width, height, action);
	}
	
	public static JLabel createIconLabel(Image image, int width, int height, Consumer<?> action) {
		JLabel label = new JLabel(new ImageIcon(getResizedImage(image, width, height)));
		if(action != null) {
			label.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) { action.accept(null); } });
		}
		return label;
	}
	
	public static ImageIcon getResizedIcon(String path, int width, int height) {
		return new ImageIcon(getResizedImage(path, width, height));
	}
	
	public static Image getResizedImage(String path, String defaultPath, int width, int height) {
		File file = new File(path);
		if(file.exists())
			return getResizedImage(path, width, height);
		else
			return getResizedImage(defaultPath, width, height);
	}

	
	public static ImageIcon getResizedIcon(String path, String defaultPath, int width, int height) {
		return new ImageIcon(getResizedImage(path, defaultPath, width, height));
	}
	
	public static ImageIcon getResizedIcon(ImageIcon icon, int width, int height) {
		return new ImageIcon(getResizedImage(icon.getImage(), width, height));
	}
	
	public static Image getImage(String path) {
		return getImage(new File(path));
	}
	
	public static Image getImage(File file) {
		try {
			return ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Image scaleDown(String imagePath, int maxWidth, int maxHeight) {
		return scaleDown(getImage(imagePath), maxWidth, maxHeight);
	}
	
	public static Image getImage(byte[] bytes) {
		try {
			return ImageIO.read(new ByteArrayInputStream(bytes));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static Image scaleDown(Image originalImage, int maxWidth, int maxHeight) {
	    int originalWidth = originalImage.getWidth(null);
	    int originalHeight = originalImage.getHeight(null);
	    if (originalWidth > maxWidth || originalHeight > maxHeight) {
	        double ratio = (double) originalWidth / (double) originalHeight;
	        if (originalWidth > maxWidth) {
	            originalWidth = maxWidth;
	            originalHeight = (int) (originalWidth / ratio);
	        }
	        if (originalHeight > maxHeight) {
	            originalHeight = maxHeight;
	            originalWidth = (int) (originalHeight * ratio);
	        }
	    }
	    return originalImage.getScaledInstance(originalWidth, originalHeight, Image.SCALE_SMOOTH);
	}
	
	public static Image getResizedImage(String path, int width, int height) {
		return getResizedImage(getImage(path), width, height);
	}
	
	public static Image getResizedImage(Image image, int width, int height) {
		return image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	}
	
	public static void addBorderOnEnterMouse(JComponent comp, Consumer<?> action) {
		addBorderOnEnterMouse(comp, action, Color.RED, Cursor.HAND_CURSOR, 1);
	}

	public static void addBorderOnEnterMouse(JComponent comp, Consumer<?> action, int t) {
		addBorderOnEnterMouse(comp, action, Color.RED, Cursor.HAND_CURSOR, t);
	}
	
	public static void addBorderOnEnterMouse(JComponent comp, Consumer<?> action, Color color, int cursor, int t) {
		Border empty = BorderFactory.createEmptyBorder(t,t,t,t);
		Border line = BorderFactory.createLineBorder(color, t);
		comp.setCursor(new Cursor(cursor));
		comp.setBorder(empty);
		comp.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) { action.accept(null); }
			public void mouseEntered(MouseEvent e) { comp.setBorder(line); }
			public void mouseExited(MouseEvent e) { comp.setBorder(empty); }
		});
	}
	public static void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
	public static StringTable createStringTable() {
		return new StringTable();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> ListTable createTable(List<T> dataList) {
		if(dataList == null || dataList.isEmpty()) 
			return new DataTable(dataList);
		
		if(dataList.get(0) instanceof List) {
			if(((List<?>)dataList.get(0)).get(0) instanceof String) {
				return new StringTable((List<List<String>>) dataList);
			}
		}
		return new DataTable(dataList);
	}
	
	public static JFrame createFrame(CustomPanel panel) {
		return createFrame(panel.getPanel());
	}
	
	public static JFrame createFrame(JComponent comp) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(comp);
		frame.pack();
		frame.setVisible(true);
		return frame;
	}
	
	/**
	 * position 0: Top-left, 1: Top-right, 2: bottom-left, 3: bottom-right, 4: Center
	 */
	public static void placeSubWindow(Window parentWindow, Window subWindow, int position) {
	    int parentX = parentWindow.getX();
	    int parentY = parentWindow.getY();
	    int parentWidth = parentWindow.getWidth();
	    int parentHeight = parentWindow.getHeight();
	    int subWidth = subWindow.getWidth();
	    int subHeight = subWindow.getHeight();
	    
	    switch(position) {
	        case 0: // Top-left
	            subWindow.setLocation(parentX, parentY);
	            break;
	        case 1: // Top-right
	            subWindow.setLocation(parentX + parentWidth - subWidth - 30, parentY + 30);
	            break;
	        case 2: // Bottom-left
	            subWindow.setLocation(parentX, parentY + parentHeight - subHeight);
	            break;
	        case 3: // Bottom-right
	            subWindow.setLocation(parentX + parentWidth - subWidth, parentY + parentHeight - subHeight);
	            break;
	        case 4: // Center
	            subWindow.setLocation(parentX + (parentWidth - subWidth) / 2, parentY + (parentHeight - subHeight) / 2);
	            break;
	        default:
	            throw new IllegalArgumentException("Invalid position: " + position);
	    }
	}
	
	public static void moveToCenter(Window window, int width, int height) {
		window.setSize(width, height);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		window.setLocation((screen.width - width) / 2, (screen.height - height) / 2);
	}
	
	public static void moveToCenter(Window window) {
		int width = window.getWidth();
		int height = window.getHeight();
		moveToCenter(window, width, height);
	}
	
    public static Font createFont(int size) {
        return new Font(Font.SANS_SERIF, Font.PLAIN, size);
    }
    
    public static Font createFont(String font, int size) {
        return new Font(font, Font.PLAIN, size);
    }
    
    public static JButton createButton(String buttonText, Font font, Consumer<?> action) {
		JButton button = new JButton(buttonText);
		button.addActionListener(e->action.accept(null));
		if(font != null) button.setFont(font);
		return button;
    }
    
    public static JButton createButton(String buttonText, Consumer<?> action) {
    	return createButton(buttonText, null, action);
	}
    
    public static JButton createButton(ImageIcon icon, Consumer<?> action) {
		JButton button = new JButton(icon);
		button.addActionListener(e->action.accept(null));
		return button;
	}
    
    public static JButton createButton(String text, Color bgColor, Color fgColor, Font font, Consumer<?> action) {
    	JButton button = new JButton(text);
    	button.setBackground(bgColor);
    	button.setForeground(fgColor);
    	button.setFont(font);
    	button.addActionListener(b->action.accept(null));
    	return button;
    }
    
	public static boolean confirmDialog(JComponent parent, Object message, String title, int type) {
		return JOptionPane.showConfirmDialog(parent, message, title, type) == JOptionPane.OK_OPTION;
	}
	
	public static boolean confirmDialog(String message) {
		return confirmDialog(null, message);
	}
	
	public static boolean confirmDialog(JComponent parent, String message) {
		return JOptionPane.showConfirmDialog(parent, message) == JOptionPane.OK_OPTION;
	}
	
	public static Font font(int size) {
		return new Font("맑은 고딕", Font.PLAIN, size);
	}

	public static void onClick(CustomPanel customPanel, Consumer<?> action) {
		onClick(customPanel.getPanel(), action);
	}

	
	public static void onClick(JComponent comp, Consumer<?> action) {
		comp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				action.accept(null);
			}
		});
	}
	
	public static void addKey(JComponent comp, int keyCode, Consumer<?> action) {
		comp.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == keyCode)
					action.accept(null);
			}
		});
	}
}	
