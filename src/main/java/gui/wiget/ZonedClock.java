package gui.wiget;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.Gui;
import gui.panel.layout.BorderLayoutPanel;
import util.StrUtil;

@SuppressWarnings("serial")
public class ZonedClock {
	private BorderLayoutPanel rootPanel;
	
	private AnalogClock analogClock;
	
	private JLabel timeLabel = new JLabel();
	
	private ZonedDateTime zonedTime;
	
	private JPanel timePanel = new JPanel(Gui.flowLayout(FlowLayout.CENTER));
	
	private LocalDateTime time = LocalDateTime.now();
	private int size = 300, size2 = 40;
	private int r, length;
	
	private int hour = 0;
	private int min = 0;
	private int sec = 0;
	private Graphics2D g2;
	
	private final BasicStroke stroke1 = new BasicStroke(1);
	private final BasicStroke stroke2 = new BasicStroke(2);
	private final BasicStroke stroke3 = new BasicStroke(3);
	
	private Font clockFont;
	private String zoneId;

	private String format = "dd일 HH:mm:ss";
	
	private Color fgColor = Color.BLACK, bgColor = Color.WHITE;
	
	public void setColor(Color fgColor, Color bgColor) {
		this.fgColor = fgColor;
		this.bgColor = bgColor;
		timeLabel.setForeground(fgColor);
		timePanel.setBackground(bgColor);
	}
	
	public void setFont(Font font) {
		size2 = (int) (font.getSize() * 3.2);
		timeLabel.setFont(font);
		timePanel.setPreferredSize(new Dimension(size, size2));
	}
	
	public ZonedClock() {
		this(200,null, 0);
	}
	
	public ZonedClock(int size) {
		this(size, null, 0);
	}
	
	public ZonedClock(int size, String zoneId, int remove) {
		if(zoneId == null) zoneId = "JST";
		this.size = size;
		initRootPanel(remove);
		setZone(zoneId);
		setTime(time);
	}

	public void initRootPanel() {
		initRootPanel(0);
	}
	
	public void initRootPanel(int remove) {
		rootPanel = new BorderLayoutPanel();
		
		if(remove != 1) {
			analogClock = new AnalogClock(); 
			rootPanel.addCenter(analogClock);
		}
		if(remove != 2) {
			timePanel.setPreferredSize(new Dimension(size, size2));
			rootPanel.addSouth(timePanel);
			timePanel.add(timeLabel);
		}
		setFont(Gui.font(size / 7));
		rootPanel.setSize(size, remove == 1 ? size2 : remove == 2 ? size : size + size2);
		clockFont = new Font(Font.SANS_SERIF,Font.PLAIN, Math.max(size / 15, 9));
		r = size / 2;
		length = size / 3 + 5;
	}
	
	private class AnalogClock extends JPanel{
		public void paint(Graphics g) {
			g2 = (Graphics2D) g;
			g2.setFont(clockFont);
			// 시간 정보를 가져온다.
			min = time.getMinute();
			hour = time.getHour();
			sec = time.getSecond();
			if (sec == 60) {
				sec = 0;
				min++;
			}
			if (min == 60) {
				min = 0;
				hour++;
			}
			if (min == 60 && hour == 12) {
				hour = 0;
			}
			g.setColor(bgColor);
			g.fillRect(0, 0, size, size);
			g.setColor(fgColor);
			drawRect();
			g2.setStroke(stroke1);
			drawLine((int)(length * 0.90), sec * 6);
			g2.setStroke(stroke2);
			drawLine((int)(length * 0.75), min * 6);
			g2.setStroke(stroke3);
			drawLine((int)(length * 0.55), hour * 30 + min / 2);
		}
	
		public void drawRect() {
			int j = 1;
			for (int i = 1; i <= 60; i++) {
				if (i % 5 == 0) {
					drawDots(i * 6, 3);
					drawTimeString(i * 6, j + "");
					j++;
				} else {
					drawDots(i * 6, 1);
				}
			}
		}
	
		public void drawDots(int angle, int width) {
			int x = r + (int) (r * 0.9 * Math.sin(angle * Math.PI / 180));
			int y = r - (int) (r * 0.9 * Math.cos(angle * Math.PI / 180));
			g2.fillRect(x, y, width, width);
		}
	
		public void drawTimeString(int angle, String i) {
			int x = r + (int) (r * 0.75 * Math.sin(angle * Math.PI / 180)) - 1;
			int y = r - (int) (r * 0.75 * Math.cos(angle * Math.PI / 180)) + 5;
			g2.drawString(i, x, y);
		}
	
		public void drawLine(int l, int angle) {
			int x = r + (int) (l * Math.sin(angle * Math.PI / 180));
			int y = r - (int) (l * Math.cos(angle * Math.PI / 180));
			g2.drawLine(r, r, x, y);
		}
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
		if(timeLabel != null) {
			ZoneId timeZone = ZoneId.of(zoneId);
			zonedTime = ZonedDateTime.of(time, timeZone);
		}
			timeLabel.setText(StrUtil.addBr("["+zoneId+"]", zonedTime.format(DateTimeFormatter.ofPattern(format))));
		if(analogClock != null)
			analogClock.repaint();
	}
	
	public void setFormat(String format) {
		this.format = format;
	}
	
	public LocalDateTime getTime() {
		return time;
	}
	
	public void setZone(String zoneId) {
		if(ZoneId.SHORT_IDS.containsKey(zoneId)) {
			zoneId = ZoneId.SHORT_IDS.get(zoneId);
		}
		this.zoneId = zoneId;
	}

	public String getZoneId() {
		return zoneId;
	}
	
	public JPanel getPanel() {
		return rootPanel.getPanel();
	}
}