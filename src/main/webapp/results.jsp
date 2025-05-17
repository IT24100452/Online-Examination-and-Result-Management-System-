<%@ page import="OE_RM.services.FileHandler.ResultEntry" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">
    <title>Results</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="container">
    <% String role = (String) session.getAttribute("role"); %>
    <% if ("admin".equals(role)) { %>
    <h2>Student Exam Results</h2>
    <% String error = (String) request.getAttribute("error"); %>
    <% if (error != null) { %>
    <p class="message error"><%= error %></p>
    <% } %>
    <% String message = (String) request.getAttribute("message"); %>
    <% if (message != null) { %>
    <p class="message success"><%= message %></p>
    <% } %>
    <% List<ResultEntry> results = (List<ResultEntry>) request.getAttribute("results"); %>
    <% if (results != null && !results.isEmpty()) { %>
    <table>
        <tr>
            <th>Student ID</th>
            <th>Module</th>
            <th>Score</th>
            <th>Actions</th>
        </tr>
        <% for (ResultEntry entry : results) { %>
        <tr>
            <td><%= entry.getStudentId() %></td>
            <td><%= entry.getModuleName() %></td>
            <td class="score"><%= entry.getScore() %></td>
            <td>
                <form action="results" method="post" style="display:inline;">
                    <input type="hidden" name="studentId" value="<%= entry.getStudentId() %>">
                    <input type="hidden" name="moduleName" value="<%= entry.getModuleName() %>">
                    <input type="hidden" name="action" value="delete">
                    <button type="submit" onclick="return confirm('Delete result for <%= entry.getStudentId() %>?');">Delete</button>
                </form>
            </td>
        </tr>
        <% } %>
    </table>
    <% } else { %>
    <p class="message error">No results available yet!</p>
    <% } %>
    <a href="adminDashboard"><button>Back to Dashboard</button></a>
    <% } else { %>
    <h2>Your Exam Results</h2>
    <% List<ResultEntry> studentResults = (List<ResultEntry>) request.getAttribute("studentResults"); %>
    <% if (studentResults != null && !studentResults.isEmpty()) { %>
    <table>
        <tr>
            <th>Module</th>
            <th>Score</th>
        </tr>
        <% for (ResultEntry entry : studentResults) { %>
        <tr>
            <td><%= entry.getModuleName() %></td>
            <td class="score"><%= entry.getScore() %></td>
        </tr>
        <% } %>
    </table>
    <% } else { %>
    <p class="message error">You haven't taken any exams yet!</p>
    <% } %>
    <a href="studentDashboard"><button>Back to Dashboard</button></a>
    <% } %>
</div>
</body>
</html>