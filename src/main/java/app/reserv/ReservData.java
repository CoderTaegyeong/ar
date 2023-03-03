package app.reserv;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import app.AppService;
import app.AppView;
import dao.DAO;
import entity.TicketDTO;
import gui.table.DataTable;

public class ReservData extends AppView {

	JLabel airPlanLbl;
	DataTable jtable;
	JFrame frame;
	JScrollPane sp;
	JButton cancelBtn;
	DefaultTableModel model;
	Reservation reserv;
	List<TicketDTO> list;
	TicketDTO ticket;
	
	// 작성자: 김태경(CoderTaegyeong)

	public ReservData(Reservation r) {
		super("예약 일정", r);
		this.reserv = r;
		initRootPanel();
	}

	@Override
	public void initRootPanel() {
		rootPanel.setBackground(new Color(240, 248, 255));
		rootPanel.setBounds(0, 0, 750, 584);
		rootPanel.setLayout(null);

		// 페이지 제목
		airPlanLbl = new JLabel("예약 일정");
		airPlanLbl.setBounds(305, 30, 96, 30);
		airPlanLbl.setFont(new Font("맑은 고딕", Font.BOLD, 22));
		rootPanel.add(airPlanLbl);

		// 데이터 표
		makeTable();

		// 예약 취소 버튼: 예약 취소 시 테이블 예약일 자리에 '예약 취소' 라고 표시되도록(데이터 삭제 X)
		cancelBtn = new JButton("예약 취소");
		cancelBtn.setBounds(280, 558, 136, 44);
		cancelBtn.setForeground(new Color(255, 0, 0));
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int choice = JOptionPane.showConfirmDialog(null,
						"예약 환불/취소는 만료일 30일 이내에만 가능합니다." + "\n 항공권 예약을 취소하시겠습니까?", "예약 취소 안내", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if (choice == JOptionPane.YES_OPTION) {
					if (jtable.getSelectedRow() != -1) {
						DAO.sql.getJdbcTemplate().update("UPDATE TICKET SET RESERVEDATE = '예약 취소' WHERE num = ? ",
								ticket.getNum());
						for(String seat : ticket.getSeatNumber().split("/")) {
							reserv.cancel(ticket.getAirNum(),seat,ticket.getSeatGrade(),ticket.getDepDate());
						}
					}
					JOptionPane.showMessageDialog(null, "항공권 예약이 취소되었습니다", "항공권 예약 취소",
							JOptionPane.INFORMATION_MESSAGE);
					// 테이블 갱신
					makeTable();
				} else if (choice == JOptionPane.NO_OPTION) {
					// 아니오를 선택 시 그냥 이 페이지에 머무름
				}
			}
		});
		cancelBtn.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		cancelBtn.setBackground(new Color(175, 238, 238));
		rootPanel.add(cancelBtn);
	}
//private String customerName; // 고객 이름
//private String customerId; // 고객 아이디
//private String seatNumber; // seatRow + SeatCol
//private String seatGrade; // 좌석등급
//private String airNum; // 비행기 번호
//private String depPlace; // 출발지
//private String arrPlace; // 도착지
//private String depDate; // 출발날짜
//private String arrDate; // 도착날짜
//private String reserveDate; // 예약날짜
//private int cost; // 티켓 가격
//private int kidCnt;
//private int adultCnt;
	public void makeTable() {
		// 테이블 삭제 후
		if (sp != null)
			rootPanel.remove(sp);
		// 테이블 갱신
		list = DAO.sql.select
				("select num, customerName, customerId, seatNumber, "
	+ "seatGrade, airNum, depPlace, arrPlace, depDate, arrDate,reserveDate, cost, adultCnt,  kidCnt from ticket where customerID = ?"
						,new BeanPropertyRowMapper<>(TicketDTO.class) ,AppService.instance().getAttr("id"));
		jtable = new DataTable(TicketDTO.class, list);
		jtable.setColumnNames("번호","이름","아이디","좌석번호","좌석등급","항공편","출발지","도착지","출발일","도착일","예약일","금액","성인","소아");
		jtable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				ticket = list.get(jtable.getSelectedRow());
			}
		});
		sp = new JScrollPane(jtable);
		sp.setBounds(10, 93, 700, 436);
//		AppService.instance().getContainer().getFrame().setSize(1300, 750);
		jtable.setBackground(new Color(175, 238, 238));
		jtable.setBorder(new LineBorder(new Color(0, 0, 0)));
		rootPanel.add(sp);
	}

//	public static void main(String[] args) {
//		Gui.createFrame(new ReservData(null).rootPanel).setSize(700, 700);
//	}
}
