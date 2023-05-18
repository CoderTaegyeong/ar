package app.admin;

import java.awt.BorderLayout;
import java.util.Optional;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import app.AppView;
import dao.DAO;
import entity.BoardVO;
import entity.CommentVO;
import gui.Gui;
import gui.InputForm;
import gui.WrapFrame;
import gui.panel.layout.BorderLayoutPanel;
import gui.panel.layout.GridBagPanel;
import util.StrUtil;

public class CustomerServiceBoard extends AppView{
	private AdminApp adminApp;
	
	
	public CustomerServiceBoard(AdminApp adminApp) {
		super("고객센터 답변하기", adminApp);
		this.adminApp = adminApp;
		initRootPanel();
	}
	
	InputForm<BoardVO> boardForm = new InputForm<>();
	InputForm<CommentVO> commentForm = new InputForm<>();
	GridBagPanel gbPanel = new GridBagPanel();
	GridBagPanel gb2Panel = new GridBagPanel();
	
	@Override
	public void initRootPanel() {
		BorderLayoutPanel blPanel = new BorderLayoutPanel();
		rootPanel = blPanel.getPanel();
		blPanel.addCenter(gbPanel);
		blPanel.addEast(gb2Panel);
		gbPanel.addNextRow(boardForm.createTFP("num","글번호"));
		gbPanel.addNextRow(boardForm.createTFP("title","제목"));
		gbPanel.addNextRow(boardForm.createTFP("writer","작성자"));
		gbPanel.addNextRow(boardForm.createTAP("content","내용"));
		gbPanel.addNextRow(boardForm.createTFP("regdate","작성일"));
		boardForm.setEditable(false);
		
		gb2Panel.addNextRow(commentForm.createTFP("num","답변번호"));
		commentForm.setEditable(false);
		gb2Panel.addNextRow(commentForm.createTFP("writer","작성자"));
		gb2Panel.addNextRow(commentForm.createTAP("content","답변내용"));
		gb2Panel.addNextRow(Gui.createButton("답변하기", b->comment()));
		
//		blPanel.newPanel(BorderLayout.SOUTH, 700, 300);
		Gui.setMargin(blPanel,0,30,200,30);
	}

	public void set(int num) {
		Optional<BoardVO> ob = DAO.sql.selectOne("SELECT NUM, TITLE, CONTENT, WRITER, REGDATE FROM CUSTOMERCENTER WHERE NUM = ?",
				new BeanPropertyRowMapper<>(BoardVO.class), num);
		if(ob.isPresent()){
			boardForm.setData(ob.get());
			commentForm.set("num", ob.get().getNum());
		}
	}
	
	public void comment() {
		CommentVO commentVO = new CommentVO();
		commentForm.saveTo(commentVO);
		commentVO.setRegdate(StrUtil.now());
		DAO.sql.insert("CUSTOMERCENTER_COMMENT", commentVO);
		WrapFrame.greenAlert(gb2Panel);
	}
}
