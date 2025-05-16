<%@ page import="OE_RM.models.User" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>User Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">
</head>
<body>
<div class="dashboard-header">
    <div class="container">
        <h2>User Management</h2>
        <div class="dashboard-options-wrapper">
            <div class="dashboard-options">
                <a href="adminDashboard"><button>Back to Dashboard</button></a>
            </div>
        </div>
        <% String successMessage = (String) session.getAttribute("successMessage"); %>
        <% if (successMessage != null) { %>
        <p class="message success"><%= successMessage %></p>
        <% session.removeAttribute("successMessage"); %>
        <% } %>
        <% String error = (String) request.getAttribute("error"); %>
        <% if (error != null) { %>
        <p class="message error"><%= error %></p>
        <% } %>
        <% String message = (String) request.getAttribute("message"); %>
        <% if (message != null) { %>
        <p class="message success"><%= message %></p>
        <% } %>
    </div>
</div>
<div class="container">
   