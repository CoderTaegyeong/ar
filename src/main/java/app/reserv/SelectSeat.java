package app.reserv;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import app.AppService;
import app.AppView;
import app.login.LoginApp;
import dao.DAO;
import dao.SeatDAO;
import dao.TicketDAO;
import entity.MemberDTO;
import entity.PayDTO;
import entity.SeatDTO;
import entity.TicketDTO;
import util.StrUtil;

   public class SelectSeat extends AppView implements ActionListener{

      // DTO
      private TicketDTO ticket;

      private ArrayList<String> reservedSeat;
      
      // DAO
      private SeatDAO sDao = new SeatDAO();
      private TicketDAO ticketDao = new TicketDAO(); // 결제하면 ticketdb에 등록
      
      private int person = 0;      // 좌석에 등록할 인원수.
      private int personCheck = 0; // 좌석 선택시 인원수

      // 좌석을 미리 배열로 만들어 놓아야 한다.
      // 행, 열
      private int row;
      private int col;
      private JCheckBox[][] seats;
      private Vector<String> seatsNumber = new Vector<String>(); // 여기에 check한 좌석이 저장된다.
      private Vector<JLabel> seatDetails = new Vector<>();      // 선택된 좌석 check


      private JPanel titlePanel = new JPanel();
      private JPanel leftPanel = new JPanel();
      private JPanel seatPanel = new JPanel();
      private JPanel panel_1 = new JPanel();
      private JPanel panel_2 = new JPanel();
      private JPanel leftBottomPanel = new JPanel();
      private JLabel lblNewLabel_1_1 = new JLabel("비행기:");
      private JLabel lblNewLabel_3_1 = new JLabel("날짜:");
      private JLabel lblNewLabel_5 = new JLabel("인원:");
      private JLabel lblNewLabel_7 = new JLabel("금액:");

      private JLabel titleLabel = new JLabel("-");
      private JLabel airplaneLabel = new JLabel("-");
      private JLabel dateLabel = new JLabel("-");
      private JLabel personLabel = new JLabel("-");
      private JLabel costLabel = new JLabel("-");

      private final JLabel checkSeatLabel = new JLabel("선택한 좌석 번호");
      private JButton ticketingButton = new JButton("좌석선택");
      private JButton reCheckSeatButton = new JButton("다시 선택");
      private JLabel seatInfoLabel = new JLabel("좌석 현황");
      private JLabel seatInfoDetailLabel = new JLabel(""+row*col+ "/"+ row*col);
      private Reservation reserve;
      private int price;
      String SeatNum;

      
      public SelectSeat(Reservation reserve) {
    	 super("티켓 결제 완료",reserve);
         this.reserve = reserve;
      }

      public void setTicket(TicketDTO ticket) {
         this.ticket = ticket;
         MemberDTO member = AppService.instance().getSubApp(LoginApp.class).getMember();
         reservedSeat = new ArrayList<String>();
         
     	List<String> sl = DAO.sql.selectOne
			("select Price from seat where seatGrade = ? and airnum = ? and depdate = ?",
				ticket.getSeatGrade(), ticket.getAirNum(), ticket.getDepDate());
         price = Integer.parseInt(sl.get(0));
         
         ticket.setCost(ticket.getAdultCnt() * price +  (int)(ticket.getKidCnt() * 0.5 * price));
         
         ticket.setCustomerName(member.getName());
         ticket.setCustomerId(member.getId());
         
         getRowAndCol();
         setInfotoLabel();
         buildGUI();
         createLabel();
         setEvent();//
         checkInfo();
      }

      public void setInfotoLabel() {
         titleLabel.setText(ticket.getCustomerName() + " 님");
         titleLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 15))   ;
         airplaneLabel.setText(ticket.getAirNum());
         dateLabel.setText(ticket.getDepDate());
         personLabel.setText(Integer.toString(ticket.getKidCnt() + ticket.getAdultCnt()) +"명");
         costLabel.setText(Integer.toString(ticket.getCost()));

         person = ticket.getKidCnt() + ticket.getAdultCnt();
         // 좌석 하나 눌렀을때 Label 이 찍은 좌석으로 변경.
         // 인원수에 맞게 좌석을 다 눌렀을 경우, 모든 좌석 체크 못하도록 변경 한다.

      }

      public void createLabel() {
         for (int i = 0; i < 5; i++) {
//            JLabel jLabel = new JLabel("좌석 " + (i + 1));
            JLabel jLabel = new JLabel();
            jLabel.setBounds(11, (35 + (i * 25)), 57, 15);
            jLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 10));
            jLabel.setForeground(Color.GRAY);

            // Vector에 넣어서 관리가능하도록 한다.
            seatDetails.add(jLabel);
            panel_1.add(jLabel);
         }
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         // 좌석 다시선택.
         if (e.getSource() == reCheckSeatButton) {
            seatAllchecked();
            for(int r = 0; r< row; r++) {
               for(int c = 0; c< col; c++) {
                  if(seats[r][c].isSelected()) {
                     seats[r][c].setSelected(false);
                  }
               }
            }

            
            for (int i = 0; i < reservedSeat.size(); i++) {
               //System.out.println(reservedSeat.get(i) + " 좌석이 예약되어 있음. ");
               String text = reservedSeat.get(i);
               int row = text.charAt(0); // 행값
               String colStr = text.substring(1);// 1뒤에 있는 값 가져와야됨.
               row = row - 65;
               int col = Integer.parseInt(colStr) - 1;
               seats[row][col].setEnabled(false);
            }
            reCheckSeatButton.setEnabled(false);
            ticketingButton.setEnabled(false);
         }

         if (e.getSource() == ticketingButton) {
            // 좌석 예매 하기.
            reserve();
         }
      }

      public void checkInfo() {
         // ticketDTO의 정보를 가지고 예약된 좌석을 예약 불가능하게.
         String airnum    = ticket.getAirNum();
         String depDate   = ticket.getDepDate();
         String seatGrade = ticket.getSeatGrade();
         reservedSeat = sDao.getSeatList(airnum, depDate,seatGrade);
         for (int i = 0; i < reservedSeat.size(); i++) {
            //System.out.println(reservedSeat.get(i) + " 좌석이 예약되어 있음. ");
            String text = reservedSeat.get(i);
            int row = text.charAt(0); // 행값
            String colStr = text.substring(1);// 1뒤에 있는 값 가져와야됨.
            row = row - 65;
            int col = Integer.parseInt(colStr) - 1;
            seats[row][col].setEnabled(false);
         }
         
         int leftSeat = (row*col - reservedSeat.size());
         seatInfoDetailLabel.setText(Integer.toString(leftSeat) + " / " + row * col );
      }

      // 좌석을 완벽히 선택하면 실행되는 메서드
      public void reserve() {
         PayDTO pay = new PayDTO();
         // setReserveDate
         SeatNum = "";
         // set SeatDTO reserved
         for (int i = 0; i < person; i++) {   
            //seatNumber만들기
            if((person-1) == i ) {
               SeatNum += seatsNumber.get(i);
            } else {
               SeatNum += (seatsNumber.get(i)+"/");
            }
         }

         SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
         ticket.setReserveDate(sdf.format(StrUtil.now()));
         
         pay.setId(AppService.instance().getAttr("id"));
         pay.setItem("좌석 등급 : "+ticket.getSeatGrade() + 
        		 	"\n좌석번호 : " + SeatNum + 
        		    "\n성인 : " + ticket.getAdultCnt()+" 명 " +
        		 	" 소아 : " + ticket.getKidCnt()+" 명 ");
         pay.setPrice(ticket.getCost());
         price = pay.getPrice();
         
         System.out.println("결제창 열기");
         AppService.instance().openPayDialog(pay); //pay 창 엶.
         
         if(pay.ok()) {
            nextpage();
         }else {
            
         }
      }


      private void nextpage()  {
         // 결제 완료 후 실행되어야 될 것들. (DB에 값 넣어주기, 다음 페이지로 이동)
         
         // set SeatDTO reserved
         int person = ticket.getAdultCnt() + ticket.getKidCnt();
         for (int i = 0; i < person; i++) {   
            // 인원수만큼 티켓 DTO를 만들어준다.
            // SeatDTO ~ reserved 되게 변경
            SeatDTO seatDTO = new SeatDTO();
            seatDTO.setAirnum(ticket.getAirNum());
            seatDTO.setSeatGrade(ticket.getSeatGrade());
            seatDTO.setDepDate(ticket.getDepDate());
            seatDTO.setReserved("Y");
            seatDTO.setSeatNumber(seatsNumber.get(i)); // 좌석 번호 넣기
            //System.out.println(seatDTO.toString());
            sDao.setSeatReserved(seatDTO);
         }
            
         // 티켓 도 만들어 줘야함.
         ticket.setSeatNumber(SeatNum);
         
         // ticket insert
         
         DAO.sql.insert("TICKET", ticket, "num", "ticket_seq");
         
         reserve.openCompletePay(ticket, SeatNum, price );
      }

      // 모든 좌석 못누르게 하기.
      public void seatAlldisabledchecked() {
         for (int j = 0; j < row; j++) {
            for (int i = 0; i < col; i++) {
               seats[j][i].setEnabled(false);
            }
         }
         ticketingButton.setEnabled(true);
      }

      // 모든 좌석 누를 수있게 변경.
      public void seatAllchecked() {
         for (int j = 0; j < row; j++) {
            for (int i = 0; i < col; i++) {
               seats[j][i].setEnabled(true);
            }
         }
      }

      // 좌석에 리스너 달기.
      class SeatChangeListener implements ItemListener {

         @Override           //좌석 클릭시
         public void itemStateChanged(ItemEvent e) {

               
            seatDetails.forEach(label->label.setText(""));
            seatsNumber.clear();
            int a=0;
            for(int r = 0; r< row; r++) {
               for(int c = 0; c< col; c++) {
                  if(seats[r][c].isSelected()) {
                     seatDetails.get(a++).setText(seats[r][c].getText().toString());
                     seatsNumber.add(seats[r][c].getText().toString());    //seatsNumber 은 check 된 seats[r][c]의 text를 넣어 줄 배열이다.
                  }
               }
            }
            for (int i = 0; i < seatsNumber.size(); i++) {                       // seatsDetails에는 5개의 JLabel 이 담겨있다. 104번째줄.
               seatDetails.get(i).setText(seatsNumber.get(i));
            }
            
            
            if(person == seatsNumber.size()) {
               reCheckSeatButton.setEnabled(true);
               seatAlldisabledchecked();
            } 
         }
      }

      public void setEvent() {
         for (int j = 0; j < row; j++) {
            for (int i = 0; i < col; i++) {
               seats[j][i].addItemListener(new SeatChangeListener());
            }
         }
         reCheckSeatButton.addActionListener(this);
         ticketingButton.addActionListener(this);
      }

      public void buildGUI() {
         rootPanel.removeAll();
         panel_2.removeAll();
         panel_1.removeAll();
         seatsNumber.clear();
         rootPanel.setLayout(null);
         rootPanel.setBounds(0,0, 1000,1000);
         rootPanel.setBackground(Color.red);
         seats = new JCheckBox[row][col];
         rootPanel.setBackground(Color.yellow);
         titlePanel.setBounds(0, 0, 812, 31);
         rootPanel.add(titlePanel);
         titlePanel.setLayout(null);

         titleLabel.setBounds(553, 10, 100, 21);
         titlePanel.add(titleLabel);

         // 이미지 판넬이 들어감.

         seatPanel.setBounds(10, 41, 664, 499);
         rootPanel.add(seatPanel);
         seatPanel.setLayout(null);

         panel_2.setBackground(Color.WHITE);
         panel_2.setBounds(12, 10, 447, 480);
         seatPanel.add(panel_2);
         panel_2.setLayout(null);
         JLabel screenLabel = new JLabel("\t\t\t\t\t\t\t\tScreen");
         screenLabel.setBackground(Color.WHITE);
         screenLabel.setForeground(Color.BLACK);
         screenLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
         screenLabel.setBounds(198, 10, 76, 15);
         panel_2.add(screenLabel);
         panel_2.setForeground(Color.WHITE);
         seatInfoLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
         seatInfoLabel.setBounds(195, 426, 59, 21);
         
         panel_2.add(seatInfoLabel);
         seatInfoDetailLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
         seatInfoDetailLabel.setBounds(195, 449, 68, 21);
         
         panel_2.add(seatInfoDetailLabel);
         leftPanel.setBounds(459, 10, 192, 480);
         seatPanel.add(leftPanel);
         leftPanel.setLayout(null);
         
         leftBottomPanel.setBounds(12, 323, 180, 142);
         leftPanel.add(leftBottomPanel);
         leftBottomPanel.setLayout(null);

         lblNewLabel_1_1.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
         lblNewLabel_1_1.setBounds(12, 10, 63, 25);
         leftBottomPanel.add(lblNewLabel_1_1);

         lblNewLabel_3_1.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
         lblNewLabel_3_1.setBounds(12, 35, 63, 25);
         leftBottomPanel.add(lblNewLabel_3_1);

         lblNewLabel_5.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
         lblNewLabel_5.setBounds(12, 60, 63, 25);
         leftBottomPanel.add(lblNewLabel_5);

         lblNewLabel_7.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
         lblNewLabel_7.setBounds(12, 85, 63, 25);
         leftBottomPanel.add(lblNewLabel_7);

         ticketingButton.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
         ticketingButton.setBounds(74, 113, 94, 25);
         leftBottomPanel.add(ticketingButton);

         airplaneLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 10));
         airplaneLabel.setBounds(74, 10, 85, 25);
         leftBottomPanel.add(airplaneLabel);

         dateLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 10));
         dateLabel.setBounds(74, 35, 85, 25);
         leftBottomPanel.add(dateLabel);

         personLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 10));
         personLabel.setBounds(74, 61, 85, 25);
         leftBottomPanel.add(personLabel);

         costLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 10));
         costLabel.setBounds(74, 86, 85, 25);
         leftBottomPanel.add(costLabel);
         ticketingButton.setEnabled(false);
         panel_1.setBounds(12, 10, 168, 287);
         leftPanel.add(panel_1);
         panel_1.setBackground(Color.WHITE);
         panel_1.setLayout(null);
         checkSeatLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
         checkSeatLabel.setBounds(11, 10, 103, 15);
         panel_1.add(checkSeatLabel);
         
         reCheckSeatButton.setBounds(11, 252, 139, 23);
         panel_1.add(reCheckSeatButton);
         reCheckSeatButton.setEnabled(false);
         //repaint();
         
//         JButton test;
//         rootPanel.add(test = Gui.createButton("test", b->setTicket(new TicketDTO())));
//         rootPanel.add(test = Gui.createButton("test", b->setTicket(new TicketDTO())));
//         test.setBounds(500, 500, 50, 50);
         
         // 행   
         for (int i = 0; i < row; i++) {
            JLabel seatColLabel = new JLabel();
            char input = (char) ('A' + i);
            seatColLabel.setText(Character.toString(input));
            seatColLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
            seatColLabel.setBounds(25, (72 + (i * 20)), 22, 15);
            panel_2.add(seatColLabel);

         }

         // 열
         for (int i = 0; i < col; i++) {
            JLabel seatRowLabel = new JLabel();
            String input = Integer.toString(i + 1);
            seatRowLabel.setText(input);
            seatRowLabel.setBounds(61 + (i * 22), 55, 22, 15);
            seatRowLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
            panel_2.add(seatRowLabel);
         }

         // 좌석 checkBox
         for (int j = 0; j < row; j++) {
            for (int i = 0; i < col; i++) {
               JCheckBox chkBox = new JCheckBox("");
               chkBox.setBackground(Color.WHITE);
               chkBox.setForeground(Color.WHITE);
               chkBox.setBounds(61 + (i * 22), 72 + (j * 20), 22, 15);
               seats[j][i] = chkBox; // 좌석을 seats배열에 넣어줌. 2차원배열로 생성.
               char input = (char) (j + 65);
               chkBox.setText(input + "" + Integer.toString(i + 1)); // 좌석의 값들을 얻을 수 잇음.
               panel_2.add(chkBox);
            }
         }
      }

      private void getRowAndCol() {
         if(ticket.getSeatGrade() == "이코노미") {
            this.row = 10;
            this.col = 8;
         }else if(ticket.getSeatGrade() == "비즈니스") {
            this.row = 10;
            this.col = 5;
         }else if(ticket.getSeatGrade() == "퍼스트클래스") {
            this.row = 10;
            this.col = 2;
         }
      }
      
      public void initRootPanel() {}
      
//      public static void main(String[] args) {
//         AppService a = AppService.instance();
//         Reservation r=  new Reservation();
//         MemberDTO m = new MemberDTO("aa1","aa2","aa3","aa4","aa5");
//         a.setAttr("member", m);
//         a.setAttr("id", m.getId());
//         a.addSubApp(r);
//         a.addSubApp(new LoginApp());
//         a.start();
//         a.addSubApp(new Membership());
//         
//         TicketDTO t = new TicketDTO();
//         t.setAirNum("OZ1075");
//         t.setAdultCnt(3);
//         t.setDepDate("2023-03-04");
//         t.setSeatGrade("이코노미");
//         r.openSeatView(t);
////         r.openReservView();
////         Gui.createFrame(s.getPanel()).setSize(1000,800);
//      }
   }   