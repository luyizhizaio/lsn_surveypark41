<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>参与调查</title>
		<link rel="stylesheet" type="text/css" href='<s:url value="/styles.css" />'>
	</head>
	<body>
		<s:include value="/header.jsp" />
		<s:if test="allSurveys.isEmpty()">目前没有可用的调查!</s:if>
		<s:else>
		<table>
			<tr>
				<td colspan="2" style="height: 5px"></td>
			</tr>
			<tr>
				<td colspan="2" class="tdHeader">参与调查:请选择要参与的调查</td>
			</tr>
			<tr>
				<td colspan="2" style="height: 5px"></td>
			</tr>
			<tr>
				<td class="tdListHeader">调查标题</td><td class="tdListHeader">我要参与</td>
			</tr>
			<s:iterator var="s" value="allSurveys">
				<s:set var="sId" value="id" />
				<tr>
					<td><s:property value="title" /></td>
					<td><s:a action="EngageSurveyAction_entry?sid=%{#sId}" namespace="/" cssClass="aList">我要参与</s:a></td>
				</tr>
			</s:iterator>
		</table>
		</s:else>
	</body>
</html>