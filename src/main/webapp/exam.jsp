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

    <!-- Quiz Creation/Edit Form -->
    <h3><%= request.getAttribute("quiz") != null ? "Edit Quiz" : "Create New Quiz" %></h3>
    <% String message = (String) request.getAttribute("message"); %>
    <% if (message != null) { %>
    <p class="message success"><%= message %></p>
    <% } %>
    <form action="quiz" method="post" id="quizForm">
        <input type="text" name="quizName" placeholder="Quiz Name" value="<%= request.getAttribute("quiz") != null ? ((Quiz)request.getAttribute("quiz")).getQuizName() : "" %>" required>
        <input type="text" name="moduleName" placeholder="Module Name" value="<%= request.getAttribute("quiz") != null ? ((Quiz)request.getAttribute("quiz")).getModuleName() : "" %>" required>
        <input type="number" name="duration" placeholder="Duration (in minutes)" min="1" value="<%= request.getAttribute("quiz") != null ? ((Quiz)request.getAttribute("quiz")).getDuration() : "" %>" required>
        <div id="questions">
            <% Quiz quiz = (Quiz) request.getAttribute("quiz"); %>
            <% if (quiz != null) { %>
            <% for (Quiz.Question q : quiz.getQuestions()) { %>
            <div class="question">
                <input type="text" name="question" value="<%= q.getQuestionText() %>" required>
                <input type="text" name="options" value="<%= String.join(",", q.getOptions()) %>" required>
                <input type="number" name="correct" value="<%= q.getCorrectAnswer() %>" min="0" max="3" required>
            </div>
            <% } %>
            <% } else { %>
            <div class="question">
                <input type="text" name="question" placeholder="Enter Question" required>
                <input type="text" name="options" placeholder="Option 1,Option 2,Option 3,Option 4" required>
                <input type="number" name="correct" placeholder="Correct Option (0-3)" min="0" max="3" required>
            </div>
            <% } %>
        </div>
        <button type="button" onclick="addQuestion()">Add Another Question</button>
        <button type="submit" name="action" value="<%= quiz != null ? "edit" : "save" %>"><%= quiz != null ? "Update Quiz" : "Save Quiz" %></button>
        <% if (quiz != null) { %>
        <button type="submit" name="action" value="delete" formaction="quiz">Delete Quiz</button>
        <% } %>
    </form>
    <div style="text-align: center; margin-top: 20px;">
        <a href="adminDashboard"><button class="secondary">Back to Dashboard</button></a>
    </div>
    <script>
        function addQuestion() {
            const div = document.createElement('div');
            div.className = 'question';
            div.innerHTML = `
                <input type="text" name="question" placeholder="Enter Question" required><br>
                <input type="text" name="options" placeholder="Option 1,Option 2,Option 3,Option 4" required><br>
                <input type="number" name="correct" placeholder="Correct Option (0-3)" min="0" max="3" required><br>
            `;
            document.getElementById('questions').appendChild(div);
        }
        <% if (message != null) { %>
        document.getElementById("quizForm").reset();
        <% } %>
    </script>
    <% } else { %>
