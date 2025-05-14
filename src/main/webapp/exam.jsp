<%@ page import="OE_RM.models.Quiz" %>
<%@ page import="java.util.List" %>
<%@ page import="OE_RM.services.FileHandler" %>
<html>
<head>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">
    <title>Online Test</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="container">
    <% String role = (String) session.getAttribute("role"); %>
    <% if ("admin".equals(role)) { %>
    <h2>Exam Creation</h2>

    <!-- List of Existing Quizzes -->
    <h3>Created Quizzes</h3>
    <%
        String quizFilePath = application.getRealPath("/WEB-INF/quizzes.txt");
        FileHandler fileHandler = new FileHandler();
        List<Quiz> quizzes = fileHandler.readQuizzes(quizFilePath);
    %>
    <% if (quizzes != null && !quizzes.isEmpty()) { %>
    <table>
        <tr>
            <th>Quiz Name</th>
            <th>Module</th>
            <th>Duration</th>
            <th>Questions</th>
            <th>Actions</th>
        </tr>
        <% for (Quiz quiz : quizzes) { %>
        <tr>
            <td><%= quiz.getQuizName() %></td>
            <td><%= quiz.getModuleName() %></td>
            <td><%= quiz.getDuration() %> mins</td>
            <td><%= quiz.getTotalQuestions() %></td>
            <td>
                <a href="quiz?action=edit&moduleName=<%= quiz.getModuleName() %>"><button>View/Edit</button></a>
                <form action="quiz" method="post" style="display:inline;">
                    <input type="hidden" name="moduleName" value="<%= quiz.getModuleName() %>">
                    <input type="hidden" name="action" value="delete">
                    <button type="submit" onclick="return confirm('Are you sure you want to delete this quiz?');">Delete</button>
                </form>
            </td>
        </tr>
        <% } %>
    </table>
    <% } else { %>
    <p class="message error">No quizzes created yet!</p>
    <% } %>
