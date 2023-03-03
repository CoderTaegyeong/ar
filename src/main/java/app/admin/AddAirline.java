package app.admin;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.text.DateFormatter;

import app.AppService;
import app.AppView;
import dao.DAO;
import entity.SeatDTO;
import gui.Gui;
import gui.InputForm;
import gui.panel.input.TextFieldPanel;
import gui.panel.layout.GridBagPanel;
import gui.wiget.SimpleCalendar;

public class AddAirline extends AppView {
	private InputForm<SeatDTO> seatForm = new InputForm<>();
	private GridBagPanel gbPanel = new GridBagPanel();

	public AddAirline(AdminApp adminApp) {
		super("비행정보 등록", adminApp);
		initRootPanel();
	}

	private TextFieldPanel depDatePanel = seatForm.createTFP("depDate", "depDate");

	@Override
	public void initRootPanel() {
		rootPanel.add(Gui.createPanel(gbPanel.getPanel()));
		gbPanel.addNextRow(seatForm.createTFP("airnum", "airnum"));
		depDatePanel.setEditable(false);
		gbPanel.addNextRow(depDatePanel);
		gbPanel.addNextRow(seatForm.createTFP("price", "이코노미가격"));
		gbPanel.addNextRow(Gui.createPanel(Gui.createButton("insert", b -> insert()),
				Gui.createButton("날짜선택", b -> selectDate())));
	}

	private void selectDate() {
		String date = SimpleCalendar.getDate("YYYY-MM-dd");
		depDatePanel.setValue(date);
	}

	private void insert() {
		insertSeat(10, 8, seatForm.getInt("price"), "이코노미", seatForm.getString("airnum"),
				seatForm.getString("depDate"));
		insertSeat(10, 5, seatForm.getInt("price") * 4, "비즈니스", seatForm.getString("airnum"),
				seatForm.getString("depDate"));
		insertSeat(10, 2, seatForm.getInt("price") * 8, "퍼스트클래스", seatForm.getString("airnum"),
				seatForm.getString("depDate"));
	}

	private void insert2(int price, String depDate, String airnum) {
		insertSeat2(10, 8, price, "이코노미", airnum, depDate);
		insertSeat2(10, 5, price * 4, "비즈니스", airnum, depDate);
		insertSeat2(10, 2, price * 8, "퍼스트클래스", airnum, depDate);
	}

	public void insertSeat(int rows, int cols, int price, String grade, String airnum, String depDate) {
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
            SeatDTO seat = new SeatDTO();
            seat.setAirnum(airnum);
            seat.setSeatGrade(grade);
            seat.setReserved("N");
            seat.setDepDate(depDate);
            seat.setPrice(price);
            seat.setSeatNumber(((char) (r + 65)) + String.valueOf(c));
            DAO.sql.insert("SEAT", seat);
            }
         }
      }

	public void insertSeat2(int rows, int cols, int price, String grade, String airnum, String depDate) {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				SeatDTO seat = new SeatDTO();
				seat.setAirnum(airnum);
				seat.setSeatGrade(grade);
				seat.setReserved("N");
				seat.setDepDate(depDate);
				seat.setPrice(price);
				seat.setSeatNumber(((char) (r + 65)) + String.valueOf(c));
				DAO.sql.insert("SEAT", seat);
			}
		}
	}

	public static void main(String[] args) {
		AddAirline f = new AddAirline(null);
		LocalDate d = LocalDate.now();
		for (int i = 0; i < 365; i++) {
			LocalDate d2 = d.plusDays(i);
			System.out.println(d2.format(DateTimeFormatter.ofPattern("YYYY-MM-dd")));
			String s2 = d2.format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
			f.insert2(1750000, s2, "VN423");
		}
//		SimpleDateFormat sdf=  new SimpleDateFormat("YYYY-MM-dd");
	}
}