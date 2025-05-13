<html>
<head>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">
    <title>Login - Online Exam System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="container">
    <h2>Welcome to the Online Exam System</h2>
    <% String error = request.getParameter("error"); %>
    <% if ("invalid".equals(error)) { %>
    <p class="message error">Invalid username or password!</p>
    <% } else if ("invalidRole".equals(error)) { %>
    <p class="message error">Invalid user role! Please contact support.</p>
    <% } %>
    <% String message = (String) request.getAttribute("message"); %>
    <% if (message != null) { %>
    <p class="message success"><%= message %></p>
    <% } %>
    <% String errorAttr = (String) request.getAttribute("error"); %>
    <% if (errorAttr != null) { %>
    <p class="message error"><%= errorAttr %></p>
    <% } %>
    <form action="login" method="post">
        <input type="text" name="username" placeholder="Username" required>
        <input type="password" name="password" placeholder="Password" required>
        <button type="submit">Login</button>
    </form>

</div>
</body>
</html>
