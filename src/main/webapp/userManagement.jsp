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
    <h3>Add User</h3>
    <form action="userManagement" method="post">
        <input type="text" name="username" placeholder="Username" required>
        <input type="text" name="password" placeholder="Password" required>
        <select name="role" required>
            <option value="">Select Role</option>
            <option value="admin">Admin</option>
            <option value="student">Student</option>
        </select>
        <input type="text" name="name" placeholder="Name (required for student)" id="nameField">
        <input type="hidden" name="action" value="add">
        <button type="submit">Add User</button>
    </form>
    <h3>Existing Users</h3>
    <%
        List<User> users = (List<User>) request.getAttribute("users");
        if (users != null && !users.isEmpty()) {
    %>
    <table>
        <tr>
            <th>Username</th>
            <th>Role</th>
            <th>Name</th>
            <th>Actions</th>
        </tr>
        <% for (User user : users) { %>
        <tr>
            <td><%= user.getUsername() %></td>
            <td><%= user.getRole() %></td>
            <td><%= (user instanceof OE_RM.models.Student) ? ((OE_RM.models.Student) user).getName() : "-" %></td>
            <td>
                <form action="userManagement" method="post" style="display:inline;">
                    <input type="text" name="newPassword" placeholder="New Password" required>
                    <input type="text" name="newName" placeholder="New Name (for student)"
                           value="<%= (user instanceof OE_RM.models.Student) ? ((OE_RM.models.Student) user).getName() : "" %>">
                    <input type="hidden" name="username" value="<%= user.getUsername() %>">
                    <input type="hidden" name="action" value="update">
                    <button type="submit">Update</button>
                </form>
                <form action="userManagement" method="post" style="display:inline;">
                    <input type="hidden" name="username" value="<%= user.getUsername() %>">
                    <input type="hidden" name="action" value="delete">
                    <button type="submit" onclick="return confirm('Are you sure you want to delete this user?');">Delete</button>
                </form>
            </td>
        </tr>
        <% } %>
    </table>
    <% } else { %>
    <p class="message error">No users found.</p>
    <% } %>
</div>
</body>
</html>

   
