<%@ page import="OE_RM.models.Student" %>
<%@ page import="OE_RM.models.Quiz" %>
<%@ page import="OE_RM.services.FileHandler" %>
<%@ page import="OE_RM.services.FileHandler.ResultEntry" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>Student Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">
</head>
<body>
<div class="dashboard-header">
    <div class="container">
        <h2 style="text-align: center; margin-bottom: 20px; color: #000 !important;">Student Dashboard</h2>
        <div class="dashboard-options-wrapper">
            <div class="dashboard-options">
                <a href="quizList"><button>Take Exam</button></a>
                <a href="results"><button>View Results</button></a>
                <form action="logout" method="post" style="display:inline;">
                    <button type="submit">Logout</button>
                </form>
            </div>
        </div>
        <% String submissionMessage = (String) session.getAttribute("submissionMessage"); %>
        <% if (submissionMessage != null) { %>
        <p class="message success"><%= submissionMessage %></p>
        <% session.removeAttribute("submissionMessage"); %>
        <% } %>
        <% String error = request.getParameter("error"); %>
        <% if ("quizNotFound".equals(error)) { %>
        <p class="message error">Quiz not found!</p>
        <% } %>
    </div>
</div>
<div class="container">
    <%
        Object userObj = session.getAttribute("user");
        if (userObj == null || !(userObj instanceof Student)) {
            response.sendRedirect("login");
            return;
        }
        Student student = (Student) userObj;
        String studentId = student.getStudentId(); // Using getStudentId() as per previous updates
    %>
    <h3>Your Available Exams</h3>
    <%
        String quizFilePath = application.getRealPath("/WEB-INF/quizzes.txt");
        String resultsFilePath = application.getRealPath("/WEB-INF/results.txt");
        FileHandler fileHandler = new FileHandler();
        List<Quiz> quizzes = null;
        List<ResultEntry> results = null;
        try {
            quizzes = fileHandler.readQuizzes(quizFilePath);
            results = fileHandler.readResults(resultsFilePath);
        } catch (Exception e) {
            // Log the exception if needed, but display a user-friendly message
    %>
    <p class="message error">Error loading exams. Please try again later.</p>
    <%
            return;
        }
    %>
    <% if (quizzes != null && !quizzes.isEmpty()) { %>
    <table>
        <tr>
            <th>Quiz Name</th>
            <th>Module</th>
            <th>Duration</th>
            <th>Questions</th>
            <th>Action</th>
        </tr>
        <% for (Quiz quiz : quizzes) { %>
        <tr>
            <td><%= quiz.getQuizName() %></td>
            <td><%= quiz.getModuleName() %></td>
            <td><%= quiz.getDuration() %> mins</td>
            <td><%= quiz.getTotalQuestions() %></td>
            <td>
                <%
                    boolean hasAttempted = false;
                    if (results != null) {
                        for (ResultEntry entry : results) {
                            if (entry.getStudentId().equals(studentId) && entry.getModuleName().equals(quiz.getModuleName())) {
                                hasAttempted = true;
                                break;
                            }
                        }
                    }
                    if (hasAttempted) {
                %>
                <button disabled style="background: #ccc; cursor: not-allowed;">Attempted</button>
                <p style="color: #dc2626; font-size: 12px; margin: 5px 0;">You have already attempted this quiz.</p>
                <% } else { %>
                <a href="quiz?action=attempt&moduleName=<%= quiz.getModuleName() %>"><button>Attempt</button></a>
                <% } %>
            </td>
        </tr>
        <% } %>
    </table>
    <% } else { %>
    <p class="message error">No exams available yet!</p>
    <% } %>
</div>
</body>
</html>
