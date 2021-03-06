<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <%--<link rel="stylesheet" href="../../resources/css1/home.css">--%>
    <link rel="stylesheet" href="../../resources/css1/admin.css">
    <link rel="shortcut icon" href="../../resources/images/icons/favicon.ico" type="image/x-icon">
    <title>Адміністрація |Блог не блогера</title>
</head>
<body class="unselectable">
<a href="/" class="href">На головну</a>
<div class="container">

    <div class="div">
        <h2>Користувачі</h2>
        <hr>
        <c:forEach var="users" items="${users}">
        <img src="/images/${users.photoUrl}" alt="">
        <h4>${users.firstName} ${users.secondName}</h4>
        <br>
            <form:form action="/delete/${users.id}" method="post">
            <button class="de" style="margin-left: -100px;">Видалити</button>
            </form:form>
            <form:form action="/getAdmin/${users.id}" method="post">
            <button class="gA" style="margin-left: 40px;">Зробити Адміном</button>
            </form:form>
            <form:form action="/getUser/${users.id}" method="post">
                <button class="gA" style="margin-left: 110px;">Зробити Юзером</button>
            </form:form>
        <hr>
        </c:forEach>
    </div>

    <div class="div">
        <h2>Пости</h2>
        <hr>
        <c:forEach var="posts" items="${posts}">
        <img src="/images/${posts.user.photoUrl}" alt="">
        <h4>${posts.user}</h4>
        <br>
        <p>${posts.text}</p>
        <form:form action="/postDelete/${posts.id}" method="post">
        <button class="de dee" >Видалити</button>
        </form:form>
        <a href="/editPost/${posts.id}">
        <button class="gA" style="margin-left: 80px;">Редагувати</button>
        </a>
        <hr>
        </c:forEach>
    </div>

    <div class="div">
        <h2>Коментарі</h2>
        <hr>
        <c:forEach var="comments" items="${comments}">
        <img src="/images/${comments.user.photoUrl}" alt="">
        <h4>${comments.user}</h4>
        <br>
        <p>${comments.text}</p>
        <form:form action="/commentDelete/${comments.id}" method="post">
        <button class="de dee">Видалити</button>
        </form:form>
        <a href="/editComment/${comments.id}">
        <button class="gA" style="margin-left: 80px;">Редагувати</button>
        </a>
            <hr>
        </c:forEach>
    </div>
</div>



</body>
</html>