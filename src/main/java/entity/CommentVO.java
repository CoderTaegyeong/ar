package entity;

import java.util.Date;

public class CommentVO {
	public int    num;
	public String content;
	public String writer;
	public Date   regdate;
	
	public int getNum() {
		return num;
	}
	public String getContent() {
		return content;
	}
	public String getWriter() {
		return writer;
	}
	public Date getRegdate() {
		return regdate;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}
	@Override
	public String toString() {
		return "CommentVO [num=" + num + ", content=" + content + ", writer=" + writer + ", regdate=" + regdate + "]";
	}
	
	
}
