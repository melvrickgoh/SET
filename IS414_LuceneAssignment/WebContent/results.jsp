<%@page import="java.util.*" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

	<% 
	List<String> list = (List<String>) session.getAttribute("list");
	for (int i=0; i<list.size(); i++) {
		String s = list.get(i);
		out.println(s + "<br />");
		if (i==0) {
			out.println("<br />");
		}
	}
	%>
</body>
</html>