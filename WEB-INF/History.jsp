<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="searcher.*, javax.servlet.http.Cookie, javax.servlet.http.HttpServletRequest, java.net.URLEncoder, java.net.URLDecoder"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>

<%request.setCharacterEncoding("UTF-8"); %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" href="img/logo.jpg">
<title>History</title>
<style type="text/css">
	.TM {background:rgba(200, 200, 200, 0.3) none repeat scroll 0 0 !important;
		filter:Alpha(opacity=80);
		font-family: "Trebuchet MS", Sans-serif;
		font-size: 18px;
		color: #f1f1f1;
		position: relative;
		padding-left: 10%;
		padding-right: 10%;
		border: none;
	}
</style>
</head>

<body background="img/bg0.jpg">

<div  align="center">
	<font style="font-family: Palatino Linotype;" color=white><h1>Search in DBworld</h1></font>
</div>
<div  align="center">
	<div align="center">
	<form action="test.jsp" method = "post" accept-charset=“UTF-8” >
		<input type = "text" name = "qWord" size="35" value="" required>
		<input type = "submit" name = "submit" value = "Search">
		<br/>
		<!-- <p><input type = "radio" name= "type" value="Topic" checked><font color=white> Topic</font></p>
		<p><input type = "radio" name= "type" value="Location"><font color=white> Location</font></p>
		<p><input type = "radio" name= "type" value="Deadline"><font color=white> Deadline</font></p>
		<p><input type = "radio" name= "type" value="Date"><font color=white> Date</font></p> -->
		<p><input type = "radio" name= "type" value="title" checked><font color=white> title</font></p>
		<p><input type = "radio" name= "type" value="poetry"><font color=white> poetry</font></p>
	</form>
	<form action="History.jsp">
		<input type = "submit" name = "hs" value = "View History">
	</form>
</div>
</div>

<hr style="background-color:white; height: 3px;">
	
<%
	String result = "";
	
	Cookie[] cookies = request.getCookies();
	result += "<div class=\"TM\" align=center>";
	if(cookies.length > 0){
		result += "<table border = 1 cellpadding=\"8\" class=\"TM\"><thead>\r\n" + 
				"<tr>\r\n" + 
				"<th>No.</th>\r\n" + 
				"<th>Option</th>\r\n" + 
				"<th>Search</th>\r\n" + 
				"<th>Match</th>\r\n" +
				"</tr>\r\n" + 
				"</thead>\r\n";
	}
	else{
		result += "No History Found!";
	}
	
	for (int i = 0, no = 0; i < cookies.length; i++){
		if(cookies[i].getName().contains("OPT")){
			no++;
			String [] spl = cookies[i].getName().split("OPT");
			String option = spl[0];
			String qWord = cookies[i].getValue();
			String cnt = spl[1];
			result += "<tbody><tr valign=TOP>\r\n" + 
          	"<td>" + no + "</td>\r\n" +  
      		"<td>" + option + "</td>\r\n" + 
      		"<td>" + qWord + "</td>\r\n";
			result += "<td>" + cnt + "</td>\r\n" + 
						"</tr></tbody>\r\n";
		}
		/*if((cookies[i].getName( )).contains("OPT")){		//clear cookies
		            cookies[i].setMaxAge(0);
		            response.addCookie(cookies[i]);
		         }*/
	}
	if(cookies.length > 0)
		result += "</table>";
	result += "</div>";
	out.print(result);
%>
</body>
</html>
