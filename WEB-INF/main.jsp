<%@ page language="java" contentType="text/html; charset=UTF-8"
	 import="searcher.*, javax.servlet.http.Cookie, javax.servlet.http.HttpServletResponse, java.net.URLEncoder, java.net.URLDecoder"  
	 pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" href="img/logo.jpg">
<title>Search in DBworld</title>

</head>
<body background="img/bg0.jpg">
<div  align="center" style="padding-top:10%">
	<font style="font-family: Palatino Linotype;" color=white><h1>Search in DBworld</h1></font>
</div>
<div align="center">
<form action="test.jsp" method = "post" accept-charset=“UTF-8” >
	<input type = "text" name = "qWord" size="35" required>
	<input type = "submit" name = "submit" value = "Search">
	<br/>
		<p><input type = "radio" name= "type" value="Topic" checked><font color=white> Topic</font></p>
		<p><input type = "radio" name= "type" value="Location"><font color=white> Location</font></p>
		<p><input type = "radio" name= "type" value="Deadline"><font color=white> Deadline</font></p>
		<p><input type = "radio" name= "type" value="Date"><font color=white> Date</font></p>
		<!-- <p><input type = "radio" name= "type" value="title" checked><font color=white> title</font></p>
		<p><input type = "radio" name= "type" value="poetry"><font color=white> poetry</font></p> -->
</form>
 <form action="History.jsp">
		<input type = "submit" name = "hs" value = "View History">
	</form> 
</div>
</body>
</html>
