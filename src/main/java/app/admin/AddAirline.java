package app.admin;

import app.AppService;
import app.AppView;
import dao.DAO;
import entity.SeatDTO;
import gui.Gui;
import gui.InputForm;
import gui.panel.input.TextFieldPanel;
import gui.panel.layout.GridBagPanel;
import gui.wiget.SimpleCalendar;

public class AddAirline extends AppView{
	private InputForm<SeatDTO> seatForm = new InputForm<>();
	private GridBagPanel gbPanel = new GridBagPanel();
	
	public AddAirline(AdminApp adminApp) {
		super("비행정보 등록",adminApp);
		initRootPanel();
	}
	
	private TextFieldPanel depDatePanel = seatForm.createTFP("depDate","depDate");
	@Override
	public void initRootPanel() {
		rootPanel.add(Gui.createPanel(gbPanel.getPanel()));
		gbPanel.addNextRow(seatForm.createTFP("airnum","airnum"));
		depDatePanel.setEditable(false);
		gbPanel.addNextRow(depDatePanel);
		gbPanel.addNextRow(Gui.createPanel(Gui.createButton("insert", b->insert()),
				Gui.createButton("날짜선택", b->selectDate())));
	}

	private void selectDate() {
		String date = SimpleCalendar.getDate("YYYY-MM-dd");
		depDatePanel.setValue(date);
	}

	private void insert() {
		insertSeat(10,8,"이코노미",seatForm.getString("airnum"),seatForm.getString("depDate"));
		insertSeat(10,5,"비즈니스",seatForm.getString("airnum"),seatForm.getString("depDate"));
		insertSeat(10,2,"퍼스트클래스",seatForm.getString("airnum"),seatForm.getString("depDate"));
	}
	
    public void insertSeat(int rows, int cols, String grade, String airnum, String depDate) {
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
            SeatDTO seat = new SeatDTO();
            seat.setAirnum(airnum);
            seat.setSeatGrade(grade);
            seat.setReserved("N");
            seat.setDepDate(depDate);
            seat.setSeatNumber(((char) (r + 65)) + String.valueOf(c));
            DAO.sql.insert("SEAT", seat);
         }
      }
   }
}