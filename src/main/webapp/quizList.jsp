<%@ page import="OE_RM.models.Quiz" %>
<%@ page import="java.util.List" %>
<html>
<head>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">
  <title>Available Quizzes</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="container">
  <h2>Available Modules</h2>
  <% List<Quiz> quizzes = (List<Quiz>) request.getAttribute("quizzes"); %>
  <% if (quizzes != null && !quizzes.isEmpty()) { %>
  <% for (Quiz quiz : quizzes) { %>
  <div class="quiz-card">
    <h3><%= quiz.getQuizName() %> - <%= quiz.getModuleName() %></h3>
    <p>Duration: <%= quiz.getDuration() %> minutes</p>
    <p>Questions: <%= quiz.getTotalQuestions() %></p>
    <a href="quiz?action=attempt&moduleName=<%= quiz.getModuleName() %>">
      <button>Attempt Now</button>
    </a>
  </div>
  <% } %>
  <% } else { %>
  <p class="message error">No quizzes available yet!</p>
  <% } %>
  <a href="studentDashboard"><button>Back to Dashboard</button></a>
</div>
</body>
</html>
