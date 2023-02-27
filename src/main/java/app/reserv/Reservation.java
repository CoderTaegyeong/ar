package app.reserv;

import app.AppService;
import app.AppView;
import app.SubApp;
import entity.TicketDTO;

public class Reservation extends SubApp {
	private ReservView reservView = new ReservView(this);
	private AirplaneView airplaneView = new AirplaneView(this);  
	private SelectSeat selectSeat = new SelectSeat(this);  
    //private Payment payment = new Payment(this);  // 결제페이지 연결
	
//	public void openPayment(TicketDTO ticketDTO) { // 결제페이지 불러오기
//		AppService.getInstance().openView(payment);
//		System.out.println(ticketDTO);
//	}
	public void openReservView() {
		AppService.instance().openView(reservView);
	}
	
	public void openAirplaneView() {
		AppService.instance().openView(airplaneView);
	}
	
	public void openSeatView(TicketDTO ticketDTO) {
		selectSeat.SetTicket(ticketDTO);
		AppService.instance().openView(selectSeat);
	}
	
	@Override
	public AppView requestView() {
		return airplaneView;
	}
}