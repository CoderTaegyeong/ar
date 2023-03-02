package app.reserv;

import java.util.Vector;

import app.AppService;
import app.AppView;
import app.SubApp;
import entity.TicketDTO;

public class Reservation extends SubApp {
	private ReservView reservView = new ReservView(this);
	private AirplaneView airplaneView = new AirplaneView(this);  
	private SelectSeat selectSeat = new SelectSeat(this); 
	private CompletePay completepay = new CompletePay(this);
	private ReservData reservData = new ReservData(this);
    
	
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
		selectSeat.setTicket(ticketDTO);
		AppService.instance().openView(selectSeat);
	}
	
	public void openCompletePay(TicketDTO ticketDTO, Vector<String> seatsNumber , int price ) {
		completepay.completep(ticketDTO,seatsNumber,price);
		AppService.instance().openView(completepay);
	}
	
	@Override
	public AppView requestView() {
		return airplaneView;
	}

	public void openReservData() {
		AppService.instance().openView(reservData);
	}
	
	@Override
	public String getTitle() {
		return "예약";
	}
}