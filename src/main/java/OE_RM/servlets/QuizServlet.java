package OE_RM.servlets;

import OE_RM.models.Student;
import OE_RM.models.Quiz;
import OE_RM.services.FileHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/quiz")
public class QuizServlet extends HttpServlet {
    private FileHandler fileHandler;

    @Override
    public void init() throws ServletException {
        fileHandler = new FileHandler();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String moduleName = request.getParameter("moduleName");
        String quizFilePath = getServletContext().getRealPath("/WEB-INF/quizzes.txt");
        List<Quiz> quizzes = fileHandler.readQuizzes(quizFilePath);

        if ("attempt".equals(action) && moduleName != null) {
            Quiz quiz = quizzes.stream()
                    .filter(q -> q.getModuleName().equals(moduleName))
                    .findFirst()
                    .orElse(null);
            if (quiz != null) {
                request.setAttribute("quiz", quiz);
                request.getRequestDispatcher("exam.jsp").forward(request, response);
            } else {
                response.sendRedirect("studentDashboard?error=quizNotFound");
            }
        } else if ("edit".equals(action) && moduleName != null) {
            Quiz quiz = quizzes.stream()
                    .filter(q -> q.getModuleName().equals(moduleName))
                    .findFirst()
                    .orElse(null);
            if (quiz != null) {
                request.setAttribute("quiz", quiz);
                request.getRequestDispatcher("exam.jsp").forward(request, response);
            } else {
                response.sendRedirect("adminDashboard?error=quizNotFound");
            }
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String moduleName = request.getParameter("moduleName");
        String role = (String) request.getSession().getAttribute("role");
        String quizFilePath = getServletContext().getRealPath("/WEB-INF/quizzes.txt");
        String resultsFilePath = getServletContext().getRealPath("/WEB-INF/results.txt");
        List<Quiz> quizzes = fileHandler.readQuizzes(quizFilePath);

        if ("submit".equals(action) && moduleName != null && "student".equals(role)) {
            Student student = (Student) request.getSession().getAttribute("user");
            if (student == null) {
                response.sendRedirect("login");
                return;
            }
            Quiz quiz = quizzes.stream()
                    .filter(q -> q.getModuleName().equals(moduleName))
                    .findFirst()
                    .orElse(null);
            if (quiz != null) {
                int correctAnswers = 0;
                for (int i = 0; i < quiz.getTotalQuestions(); i++) {
                    String selected = request.getParameter("answer" + i);
                    if (selected != null) {
                        int answer = Integer.parseInt(selected);
                        if (answer == quiz.getCorrectAnswer(i)) {
                            correctAnswers++;
                        }
                    }
                }
                double percentageScore = (correctAnswers * 100.0) / quiz.getTotalQuestions();
                student.setScore((int) percentageScore);
                fileHandler.updateResult(student, moduleName, null, resultsFilePath);
                request.getSession().setAttribute("submissionMessage", "Quiz submitted successfully");
                response.sendRedirect("studentDashboard");
            } else {
                response.sendRedirect("studentDashboard?error=quizNotFound");
            }

            } else if ("save".equals(action) && "admin".equals(role)) {
            String quizName = request.getParameter("quizName");
            String durationStr = request.getParameter("duration");
            String[] questions = request.getParameterValues("question");
            String[] options = request.getParameterValues("options");
            String[] correctAnswers = request.getParameterValues("correct");

            if (quizName == null || moduleName == null || durationStr == null || questions == null || options == null || correctAnswers == null) {
                request.setAttribute("error", "All fields are required to create a quiz.");
                request.getRequestDispatcher("exam.jsp").forward(request, response);
                return;
            }

            int duration;
            try {
                duration = Integer.parseInt(durationStr);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid duration format.");
                request.getRequestDispatcher("exam.jsp").forward(request, response);
                return;
            }

            // Check for duplicate module name
            if (quizzes.stream().anyMatch(q -> q.getModuleName().equals(moduleName))) {
                request.setAttribute("error", "A quiz with this module name already exists.");
                request.getRequestDispatcher("exam.jsp").forward(request, response);
                return;
            }

            Quiz newQuiz = new Quiz(quizName, moduleName, duration);
            for (int i = 0; i < questions.length; i++) {
                String[] optionArray = options[i].split(",");
                if (optionArray.length != 4) {
                    request.setAttribute("error", "Each question must have exactly 4 options.");
                    request.getRequestDispatcher("exam.jsp").forward(request, response);
                    return;
                }
                int correct;
                try {
                    correct = Integer.parseInt(correctAnswers[i]);
                    if (correct < 0 || correct > 3) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Correct answer must be a number between 0 and 3.");
                    request.getRequestDispatcher("exam.jsp").forward(request, response);
                    return;
                }
                newQuiz.addQuestion(questions[i], optionArray, correct);
            }
            
            Quiz existingQuiz = quizzes.stream()
                    .filter(q -> q.getModuleName().equals(moduleName))
                    .findFirst()
                    .orElse(null);
            if (existingQuiz == null) {
                response.sendRedirect("adminDashboard?error=quizNotFound");
                return;
            }

            quizzes.remove(existingQuiz);
            Quiz updatedQuiz = new Quiz(quizName, moduleName, duration);
            for (int i = 0; i < questions.length; i++) {
                String[] optionArray = options[i].split(",");
                if (optionArray.length != 4) {
                    request.setAttribute("error", "Each question must have exactly 4 options.");
                    request.getRequestDispatcher("exam.jsp").forward(request, response);
                    return;
                }
                int correct;
                try {
                    correct = Integer.parseInt(correctAnswers[i]);
                    if (correct < 0 || correct > 3) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Correct answer must be a number between 0 and 3.");
                    request.getRequestDispatcher("exam.jsp").forward(request, response);
                    return;
                }
                updatedQuiz.addQuestion(questions[i], optionArray, correct);
            }

            quizzes.add(updatedQuiz);
            fileHandler.saveQuizzes(quizzes, quizFilePath);
            request.getSession().setAttribute("successMessage", "Quiz updated successfully!");
            response.sendRedirect("adminDashboard");
        } else if ("delete".equals(action) && "admin".equals(role)) {
            Quiz quizToDelete = quizzes.stream()
                    .filter(q -> q.getModuleName().equals(moduleName))
                    .findFirst()
                    .orElse(null);
            if (quizToDelete != null) {
                quizzes.remove(quizToDelete);
                fileHandler.saveQuizzes(quizzes, quizFilePath);
                request.getSession().setAttribute("successMessage", "Quiz deleted successfully!");
            }
            response.sendRedirect("adminDashboard");
        } else {
            response.sendRedirect("login");
        }
    }
}
