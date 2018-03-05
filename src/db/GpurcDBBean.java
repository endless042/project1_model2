package db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GpurcDBBean {
private static GpurcDBBean gpurchase=new GpurcDBBean();
	
	private GpurcDBBean() {
		
	}
	
	public static GpurcDBBean getInstance() {
		
		return gpurchase;
	}
	
	public static Connection getConnection(){
		
		Connection con=null;
		try {
			String jdbcUrl="jdbc:oracle:thin:@localhost:1521:orcl";
			String dbId="scott";
			String dbPass="tiger";
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con=DriverManager.getConnection(jdbcUrl,dbId,dbPass);
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return con;
	}
	
	public void close(Connection con, ResultSet rs,PreparedStatement pstmt) {
		if(rs!=null)
			try {
			rs.close();}catch(SQLException ex) {
				
			}
		if(pstmt!=null)
			try {
			pstmt.close();}catch(SQLException ex) {
				
			}
		if(con!=null)
			try {
			con.close();}catch(SQLException ex) {}
		
	}
	
	public void addGproduct(GpurcDataBean gproduct) {
		
		String sql="";
		Connection con=getConnection();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		int number=0;
		Date bd=null;
	
	try {
		pstmt=con.prepareStatement("select pSer.nextval from dual");
		rs=pstmt.executeQuery();
		if(rs.next()) number=rs.getInt(1);
	
		
	
		
			sql="insert into gproduct (num,state,origin,title,name,"
					+ "category,height,sdate,edate,price,goal,deliv,content,process,imgs,rdate,imgsize) "
					+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate,?)";
			pstmt=con.prepareStatement(sql);
			
			pstmt.setInt(1, number);
			pstmt.setString(2, gproduct.getState());
			pstmt.setString(3,gproduct.getOrigin());
			pstmt.setString(4, gproduct.getTitle());
			pstmt.setString(5, gproduct.getName());
			pstmt.setString(6, gproduct.getCategory());
			pstmt.setString(7,gproduct.getHeight());
			pstmt.setString(8, gproduct.getSdate());
			pstmt.setString(9, gproduct.getEdate());
			pstmt.setString(10, gproduct.getPrice());
			pstmt.setInt(11, gproduct.getGoal());
			pstmt.setString(12, gproduct.getDeliv());
			pstmt.setString(13, gproduct.getContent());
			pstmt.setString(14, gproduct.getProcess());
			pstmt.setString(15, gproduct.getImgs());
			pstmt.setInt(16, gproduct.getImgsize());
			
			pstmt.executeUpdate();
			
		
		
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			close(con,rs,pstmt);
		}
	
	}
	

public int getGproductCount (){
	String sql="select count(*) from gproduct";
	Connection con=getConnection();
	PreparedStatement pstmt=null;
	ResultSet rs=null;
	int x=0;
	
	try{ pstmt=con.prepareStatement(sql);
	
	
	rs=pstmt.executeQuery();
	
	if(rs.next()) {
		x=rs.getInt(1);
	}
	}catch(Exception e) {
		e.printStackTrace();
	}
	finally {
		close(con, rs, pstmt);
	}
	return x;
}


public List getProducts(int startRow, int endRow) {
		
		
		Connection con=getConnection();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		List productList=null;
		String sql="";
		
		try {
			con=getConnection();
			sql="select * from (" + 
					"select rownum rum , b.* from (" + 
					"select a.* from gproduct a  ORDER BY rdate DESC) b)" + 
					"where rum between ? and ? ORDER BY  num desc";
			
			
				pstmt=con.prepareStatement(sql);
				
				pstmt.setInt(1, startRow);
				pstmt.setInt(2, endRow);
				
				rs=pstmt.executeQuery();
			
				if(rs.next()) {
					productList=new ArrayList();
					do {
						
						GpurcDataBean gproduct=new GpurcDataBean();
						
						gproduct.setNum(rs.getInt("num"));
						gproduct.setState(rs.getString("state"));
						gproduct.setOrigin(rs.getString("origin"));
						gproduct.setTitle(rs.getString("title"));
						gproduct.setName(rs.getString("name"));
						gproduct.setCategory(rs.getString("category"));
						gproduct.setHeight(rs.getString("height"));
						gproduct.setSdate(rs.getString("sdate"));
						gproduct.setEdate(rs.getString("edate"));
						gproduct.setProcess(rs.getString("process"));
						gproduct.setPrice(rs.getString("price"));
						gproduct.setRe(rs.getInt("re"));
						gproduct.setRdate(rs.getDate("rdate"));
						gproduct.setDeliv(rs.getString("deliv"));
						gproduct.setImgs(rs.getString("imgs"));
						gproduct.setContent(rs.getString("content"));
						gproduct.setGoal(rs.getInt("goal"));
						
						
						productList.add(gproduct);
					
					}while(rs.next());
				}
			}catch(Exception ex) {
					ex.printStackTrace();
			}finally {close(con, rs, pstmt);}
		
		return productList;
		
	}

public GpurcDataBean getProduct(int num,  String chk) {
	
	
	Connection con=getConnection();
	PreparedStatement pstmt=null;
	ResultSet rs=null;
	String sql="";
	GpurcDataBean gproduct=null;
	
	try {
		con=getConnection();
		
		if(chk.equals("content")) {
		sql="Update gproduct set readcount=readcount+1 where num=?";
		//조회수 1 증가
		
		pstmt=con.prepareStatement(sql);
		
		pstmt.setInt(1, num);
		
		
		pstmt.executeUpdate();
		}
		
		sql="Select * from gproduct where num=?";
		
		pstmt=con.prepareStatement(sql);	//pstmt에 sql문을 새로 수정해서 넣어야 함. 
		
		pstmt.setInt(1, num);
		
		rs=pstmt.executeQuery();
		
		if(rs.next()) {
			gproduct=new GpurcDataBean();
			
			gproduct.setNum(rs.getInt("num"));
			gproduct.setState(rs.getString("state"));
			gproduct.setOrigin(rs.getString("origin"));
			gproduct.setTitle(rs.getString("title"));
			gproduct.setName(rs.getString("name"));
			gproduct.setCategory(rs.getString("category"));
			gproduct.setHeight(rs.getString("height"));
			gproduct.setSdate(rs.getString("sdate"));
			gproduct.setEdate(rs.getString("edate"));
			gproduct.setPrice(rs.getString("price"));
			gproduct.setProcess(rs.getString("process"));
			gproduct.setRdate(rs.getTimestamp("rdate"));
			gproduct.setDeliv(rs.getString("deliv"));
			gproduct.setRe(rs.getInt("re"));
			gproduct.setImgs(rs.getString("imgs"));
			gproduct.setContent(rs.getString("content"));
			gproduct.setCount(rs.getInt("count"));
			gproduct.setGoal(rs.getInt("goal"));
			
			
	
			
		}
		
		
	}catch(Exception e) {
		e.printStackTrace();
	}finally {
		close(con, rs, pstmt);
	}
		
	return gproduct;

}

}
