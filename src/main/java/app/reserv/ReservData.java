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

import app.AppView;
import dao.DAO;
import gui.Gui;
import gui.table.StringTable;

public class ReservData extends AppView {
	
	JLabel airPlanLbl;
	StringTable jtable = new StringTable();
	JFrame frame;
	JScrollPane sp;
	JButton cancelBtn;
	DefaultTableModel model;
	Reservation reserv;
	List<List<String>> list;
	
	// 작성자: 김태경(CoderTaegyeong)
	
	public ReservData(Reservation r) {
		this.reserv = r;
		initRootPanel();
	}

	@Override
	public void initRootPanel() {
		rootPanel.setBackground(new Color(240, 248, 255));
		rootPanel.setBounds(0, 0, 678, 584);
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
				int choice = JOptionPane.showConfirmDialog(null, "예약 환불/취소는 만료일 30일 이내에만 가능합니다."
						+ "\n 항공권 예약을 취소하시겠습니까?", "예약 취소 안내",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if(choice == JOptionPane.YES_OPTION) {
					 if (jtable.getSelectedRow() != -1) {
				           //DAO.sql.getJdbcTemplate().update("UPDATE TICKET SET RESERVEDATE = '예약 취소' WHERE customerID = ? ", AppService.instance().getAttr("id"));
							DAO.sql.getJdbcTemplate().update("UPDATE TICKET SET RESERVEDATE = '예약 취소' WHERE customerID = ? ", "123");
				        }
					JOptionPane.showMessageDialog(null, "항공권 예약이 취소되었습니다", "항공권 예약 취소", JOptionPane.INFORMATION_MESSAGE);
					// 테이블 갱신
					makeTable();
				} else if(choice == JOptionPane.NO_OPTION) {
					//아니오를 선택 시 그냥 이 페이지에 머무름
				}
			}
		});
		cancelBtn.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		cancelBtn.setBackground(new Color(175, 238, 238));
		rootPanel.add(cancelBtn);
	}
	
	public void makeTable() {
		// 테이블 삭제 후
		if(jtable != null)
		  rootPanel.remove(jtable);
		// 테이블 갱신
		list = DAO.sql.select(
        		"select AIRNUM, CUSTOMERNAME, DEPPLACE, ARRPLACE, DEPDATE, ARRDATE, RESERVEDATE from ticket");	
				
		jtable = new StringTable(list, "항공편", "이름", "출발지", "도착지", "출발일", "도착일", "예약일"
				);
		sp = new JScrollPane(jtable);
		sp.setBounds(79, 93, 568, 436);
		jtable.setBackground(new Color(175, 238, 238));
		jtable.setBorder(new LineBorder(new Color(0, 0, 0)));
		rootPanel.add(sp);
	}
	
// 	public static void main(String[] args) {
// 		Gui.createFrame(new ReservData(null).rootPanel).setSize(700,700);
// 	}

}
