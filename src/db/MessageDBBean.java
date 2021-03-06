package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jdbc.JdbcUtil;

public class MessageDBBean {
	private static MessageDBBean messageDao = new MessageDBBean();

	public static MessageDBBean getInstance() {
		return messageDao;
	}

	private MessageDBBean() {
	}

	public int selectCount(Connection conn, String pronum) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement("select count(*) from reply where pronum=?");
			pstmt.setString(1, pronum);
			rs = pstmt.executeQuery();
			rs.next();
			return rs.getInt(1);
		} finally {

			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}

	}

	public MessageDataBean select(Connection conn, int num) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt=conn.prepareStatement("select * from reply where num=?");
			
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return makeMessageFromResultSet(rs);

			} else {
				return null;
			}
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}

	}
	
	public List<MessageDataBean> selectList(Connection conn,int firstRow, int endRow, String pronum) throws SQLException{
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql="";
		try {
			sql = "select * from (select rownum rnum , a.* from (select num, userid, rdate, pronum, password, content from "
					+"reply where pronum=? order by rdate desc) a) where rnum between ? and ?";
			
			System.out.println(sql);
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, pronum);
			pstmt.setInt(2, firstRow);
			pstmt.setInt(3, endRow);
			rs=pstmt.executeQuery();
			
		if(rs.next()) {
			List<MessageDataBean> messageList=new ArrayList<MessageDataBean>();
			do {messageList.add(makeMessageFromResultSet(rs));
			
			}while(rs.next());
			return messageList;
			
		}else {return Collections.emptyList();
		
		}}finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
			
		}
		
	}
	
	private MessageDataBean makeMessageFromResultSet(ResultSet rs) throws SQLException {
		
		MessageDataBean message=new MessageDataBean();
		message.setUserid(rs.getString("userid"));
		message.setPassword(rs.getString("password"));
		message.setContent(rs.getString("content"));
		message.setRdate(rs.getTimestamp("rdate"));
		message.setPronum(rs.getString("pronum"));
		message.setNum(rs.getInt("num"));
		
		return message;
	}
	
	public int insert(Connection conn,MessageDataBean message) throws SQLException{
		
		PreparedStatement pstmt=null;
		ResultSet rs;
		try {
			
			pstmt=conn.prepareStatement("select nvl(max(num),0) from reply");
			rs=pstmt.executeQuery();
			int max=0;
			if(rs.next()) {
				max=rs.getInt(1);
				
			}
			
			pstmt=conn.prepareStatement("insert into reply (userid,rdate,password,pronum,content,num) values(?,sysdate,?,?,?,?)");
			
			pstmt.setString(1, message.getUserid());
			pstmt.setString(2, message.getPassword());
			pstmt.setString(3, message.getPronum());
			pstmt.setString(4, message.getContent());
			pstmt.setInt(5, max+1);
			return pstmt.executeUpdate();
		
		}finally {
			JdbcUtil.close(pstmt);
		}
	}
	
	public int delete(Connection conn, int num) throws SQLException{
		PreparedStatement pstmt=null;
		try {
			pstmt=conn.prepareStatement("delete from reply where num=?");
			pstmt.setInt(1, num);
			return pstmt.executeUpdate();
		}finally {
		
			JdbcUtil.close(pstmt);
		}
		
	}

}
