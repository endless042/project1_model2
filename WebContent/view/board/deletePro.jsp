<%@page import="board.BoardDBBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
   
	<c:if test="${check==1}">
     if(check==1){
		<meta http-equiv="Refresh" content="0;url=list?pageNum=${pageNum}">
		</c:if>
		<c:if test="${check!=1}">
		
		<script>
alert("비밀번호가 맞지 않습니다");
history.go(-1);		
</script>
</c:if>
