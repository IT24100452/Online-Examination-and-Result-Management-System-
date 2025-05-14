<%@ page import="OE_RM.models.Quiz" %>
<%@ page import="OE_RM.services.FileHandler" %>
<%@ page import="java.util.List" %>
<html>
<head>
  <title>Admin Dashboard</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">
  <script>
    function showSection(sectionId) {
      document.querySelectorAll('.content-section').forEach(section => {
        section.style.display = 'none';
      });
      document.getElementById(sectionId).style.display = 'block';
    }
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
  </script>
</head>
<body>
<div class="dashboard-header">
  <div class="container">
    <h2 style="text-align: center; margin-bottom: 20px; color: #000 !important;">Admin Dashboard</h2>
    <div class="dashboard-options-wrapper">
      <div class="dashboard-options">
        <button onclick="showSection('exam-creation-section')">Exam Creation</button>
        <button onclick="showSection('results-section')">Student Results</button>
        <button onclick="window.location.href='userManagement'">User Management</button>
        <form action="logout" method="post" style="display:inline;">
          <button type="submit">Logout</button>
        </form>
      </div>
    </div>
    <% String successMessage = (String) session.getAttribute("successMessage"); %>
    <% if (successMessage != null) { %>
    <p class="message success"><%= successMessage %></p>
    <% session.removeAttribute("successMessage"); %>
    <% } %>
    <% String error = request.getParameter("error"); %>
    <% if ("quizNotFound".equals(error)) { %>
    <p class="message error">Quiz not found!</p>
    <% } %>
  </div>
</div>

<div class="container">
  <div id="exam-creation-section" class="content-section" style="display: none;">
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

    <h3>Create New Quiz</h3>
    <form action="quiz" method="post" id="quizForm">
      <input type="text" name="quizName" placeholder="Quiz Name" required>
      <input type="text" name="moduleName" placeholder="Module Name" required>
      <input type="number" name="duration" placeholder="Duration (in minutes)" min="1" required>
      <div id="questions">
        <div class="question">
          <input type="text" name="question" placeholder="Enter Question" required>
          <input type="text" name="options" placeholder="Option 1,Option 2,Option 3,Option 4" required>
          <input type="number" name="correct" placeholder="Correct Option (0-3)" min="0" max="3" required>
        </div>
      </div>
      <button type="button" onclick="addQuestion()">Add Another Question</button>
      <button type="submit" name="action" value="save">Save Quiz</button>
    </form>
  </div>

  <div id="results-section" class="content-section" style="display: none;">
    <h3>Student Exam Results</h3>
    <%
      String resultsFilePath = application.getRealPath("/WEB-INF/results.txt");
      List<FileHandler.ResultEntry> results = fileHandler.readResults(resultsFilePath);
    %>
    <% if (results != null && !results.isEmpty()) { %>
    <table>
      <tr>
        <th>Student ID</th>
        <th>Module</th>
        <th>Score</th>
      </tr>
      <% for (FileHandler.ResultEntry entry : results) { %>
      <tr>
        <td><%= entry.getStudentId() %></td>
        <td><%= entry.getModuleName() %></td>
        <td class="score"><%= entry.getScore() %></td>
      </tr>
      <% } %>
    </table>
    <% } else { %>
    <p class="message error">No results available yet!</p>
    <% } %>
  </div>
</div>
</body>
</html>