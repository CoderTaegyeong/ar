package app.dash;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import app.AppService;
import app.AppView;
import entity.BoardVO;
import entity.CommentVO;
import entity.PayDTO;
import entity.TicketDTO;
import gui.Gui;

public class BoardUpdate extends AppView {
	private JTextField title;
    private JTextField writer;
    private JTextField commentWriter;
    JTextArea   content = new JTextArea();
    JTextArea   comment = new JTextArea();
    JScrollPane pane;
    JTable      commentTable = new JTable();
    DashBoard dash;
    BoardVO vo;
    CommentVO cvo;
    
    List<BoardVO> voList;
    
    public BoardUpdate(DashBoard dash) {
    	super("후기 보기",dash);
        this.dash = dash;
        
        // 게시글 수정
        initRootPanel();
    }
    
    public void setData(BoardVO vo) {
    	commentWriter.setText(AppService.instance().getAttr("id"));
    	
    	this.vo = vo;
    	title.setText(vo.getTitle());
    	writer.setText(vo.writer);
    	content.setText(vo.getContent());
    	createComment();
    }
    
    public void getFormData() {
    	vo.setTitle(title.getText());
    	vo.setWriter(writer.getText());
    	vo.setContent(content.getText());
    }
    
	@Override
	public void initRootPanel() {
		rootPanel.setLayout(null);
		 
		JLabel lblNewLabel = new JLabel("글제목");
        lblNewLabel.setBounds(12, 25, 57, 15);
        rootPanel.add(lblNewLabel);
 
        title = new JTextField();
        title.setBounds(81, 22, 600, 25);
        rootPanel.add(title);
        title.setColumns(10);
 
        JLabel lblNewLabel_1 = new JLabel("글내용");
        lblNewLabel_1.setBounds(12, 59, 57, 15);
        rootPanel.add(lblNewLabel_1);
 
        content.setLineWrap(true);
        content.setRows(5);
        content.setBounds(81, 53, 600, 250);
        rootPanel.add(content);
 
        JLabel lblNewLabel_2 = new JLabel("작성자");
        lblNewLabel_2.setBounds(12, 320, 57, 15);
        rootPanel.add(lblNewLabel_2);
 
        writer = new JTextField();
        writer.setBounds(81, 315, 116, 25);
        rootPanel.add(writer);
        writer.setColumns(10);
        writer.setEditable(false);
        
        JButton btnWrite = new JButton("글수정");
        btnWrite.setBounds(380, 320, 97, 23);
        btnWrite.addActionListener(new ActionListener() {
 
            @Override
            public void actionPerformed(ActionEvent e) {
            	getFormData();
                dash.dao().update(vo);
                dash.openList();
            }
        });
        rootPanel.add(btnWrite);
 
        JButton btnDel = new JButton("글삭제");
        btnDel.setBounds(480, 320, 97, 23);
        btnDel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dash.dao().delete(vo);
                dash.openList();
            }
        });
        rootPanel.add(btnDel);
 
        JButton btnClose = Gui.createButton("닫기", b->dash.openList());
        btnClose.setBounds(580, 320, 97, 23);
        rootPanel.add(btnClose);
        //------------------------------------------------------------------------------
        //------------------- 댓글 부분 ------------------------------------------------
        //------------------------------------------------------------------------------

        commentWriter = new JTextField();
        commentWriter.setEditable(false);
        commentWriter.setBounds(81, 580, 116, 25);
        rootPanel.add(commentWriter);
        commentWriter.setColumns(10);

        comment.setLineWrap(true);
        comment.setEnabled(false);
        comment.setRows(5);
        comment.setBounds(81, 520, 600, 50);
        rootPanel.add(comment);

        JButton btnComWri = new JButton("댓글 쓰기");
        btnComWri.addActionListener(new ActionListener() {

        	@Override
        	public void actionPerformed(ActionEvent e) {
        		System.out.println("댓글 쓰기");
        		comment.setText("댓글을 입력하세요");
        		comment.setEnabled(true);
        	}
        });
        rootPanel.add(btnComWri);

        JButton btnComSave = new JButton("댓글 저장");

        btnComSave.addActionListener(new ActionListener() {

        	@Override
        	public void actionPerformed(ActionEvent e) {
        		System.out.println("댓글 저장 클릭");
        		BoardDao dao = new BoardDao();
        		CommentVO cvo = new CommentVO();

        		String txtarea = comment.getText();
        		String name = commentWriter.getText();

        		cvo.setNum(vo.getNum());
        		cvo.setContent(txtarea);
        		cvo.setWriter(name);
        		cvo.setRegdate(new Date());
        		
        		dao.insertComment(cvo);

        		createComment();
        		//	dash.openCommentList(voList);
        	}
        });
        rootPanel.add(btnComSave);

        JButton btnComDel = new JButton("댓글 삭제");
        btnComDel.addActionListener(new ActionListener() {

        	@Override
        	public void actionPerformed(ActionEvent e) {
        		System.out.println("댓글삭제");
        		dash.dao().commentDelete(cvo);
        		dash.openList();
        	}
        });
//        rootPanel.add(btnComDel);

        btnComWri.setBounds(580, 490, 100, 25);   // 댓글쓰기
        btnComSave.setBounds(580, 580, 100, 25);  // 댓글저장
        btnComDel.setBounds(460, 490, 100, 25);   // 댓글삭제

        //------------------------------------------------------------------------------
        //----------------댓글 테이블---------------------------------------------------
        //------------------------------------------------------------------------------
//        createComment();
	}

	void createComment(){
       commentTable = new JTable();
       commentTable.setModel(
        		new DefaultTableModel( getCommentList() , getColumnList() ) {            
        			@Override
        			public boolean isCellEditable(int row, int column) {
        				return false;   // 모든 cell 편집불가능
        			}            
        		}   
        	);
       if(pane!=null)rootPanel.remove(pane);
       pane = new JScrollPane(commentTable);
       pane.setBounds(81, 360, 600, 120);
       rootPanel.add(pane);
	}
	
	
	// 테이블 입력되야됨
	private Vector< Vector> getCommentList() {
		BoardDao       dao   =  new BoardDao();
		Vector<Vector>  list =  dao.getCommentList(vo.num);
		return  list;
	}

	private Vector<String> getColumnList() {
		Vector<String>  cols = new Vector<>();  // 문자배열 대신 사용
		cols.add("번호");
		cols.add("내용");
		cols.add("작성자");
		cols.add("작성일");
		return  cols;
	}
	
//	public static void main(String[] args) {
//		Gui.createFrame(new BoardUpdate(new DashBoard()).rootPanel).setSize(1000, 700);;
//	}
}
