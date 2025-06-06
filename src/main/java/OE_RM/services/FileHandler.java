package OE_RM.services;

import OE_RM.models.Student;
import OE_RM.models.User;
import OE_RM.models.Quiz;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    // Node class for Linked List
    public static class Node {
        ResultEntry data;
        Node next;

        public Node(ResultEntry data) {
            this.data = data;
            this.next = null;
        }
    }

    // Custom Linked List for ResultEntry
    public static class LinkedList {
        Node head;

        public LinkedList() {
            head = null;
        }

        // Add a new ResultEntry to the end of the list
        public void add(ResultEntry entry) {
            Node newNode = new Node(entry);
            if (head == null) {
                head = newNode;
            } else {
                Node current = head;
                while (current.next != null) {
                    current = current.next;
                }
                current.next = newNode;
            }
        }

        // Convert Linked List to array for sorting or iteration
        public ResultEntry[] toArray() {
            int size = 0;
            Node current = head;
            while (current != null) {
                size++;
                current = current.next;
            }

            ResultEntry[] array = new ResultEntry[size];
            current = head;
            for (int i = 0; i < size; i++) {
                array[i] = current.data;
                current = current.next;
            }
            return array;
        }

        // Get size of the Linked List
        public int size() {
            int size = 0;
            Node current = head;
            while (current != null) {
                size++;
                current = current.next;
            }
            return size;
        }
    }

    // Selection Sort to sort ResultEntry array by score
    public ResultEntry[] selectionSortByScore(ResultEntry[] entries) {
        int n = entries.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (entries[j].getScore() < entries[minIndex].getScore()) {
                    minIndex = j;
                }
            }
            // Swap
            ResultEntry temp = entries[i];
            entries[i] = entries[minIndex];
            entries[minIndex] = temp;
        }
        return entries;
    }
    
    public User readUser(String username, String userFilePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(userFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[0].equals(username)) {
                    if (parts[2].equals("student") && parts.length >= 4) {
                        return new Student(parts[0], parts[1], parts[3]);
                    }
                    return new User(parts[0], parts[1], parts[2]);
                }
            }
        } catch (IOException e) {
            throw new IOException("Error reading users file: " + e.getMessage(), e);
        }
        return null;
    }


public void saveUser(User user, String userFilePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFilePath, true))) {
            String line = user instanceof Student
                    ? String.format("%s,%s,student,%s", user.getUsername(), user.getPassword(), ((Student) user).getName())
                    : String.format("%s,%s,%s", user.getUsername(), user.getPassword(), user.getRole());
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            throw new IOException("Error saving user: " + e.getMessage(), e);
        }
    }

public void updateUser(String username, String newPassword, String newName, String userFilePath) throws IOException {
        List<String> lines = new ArrayList<>();
        boolean updated = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(userFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    String password = newPassword != null && !newPassword.isEmpty() ? newPassword : parts[1];
                    if (parts[2].equals("student")) {
                        String name = newName != null && !newName.isEmpty() ? newName : parts[3];
                        line = String.format("%s,%s,student,%s", username, password, name);
                    } else {
                        line = String.format("%s,%s,%s", username, password, parts[2]);
                    }
                    updated = true;
                }
                lines.add(line);
            }
        }
        if (!updated) {
            throw new IOException("User not found: " + username);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFilePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IOException("Error updating user: " + e.getMessage(), e);
        }
    }
 public void deleteUser(String username, String userFilePath) throws IOException {
        List<String> lines = new ArrayList<>();
        boolean deleted = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(userFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(username + ",")) {
                    lines.add(line);
                } else {
                    deleted = true;
                }
            }
        }
        if (!deleted) {
            throw new IOException("User not found: " + username);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFilePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IOException("Error deleting user: " + e.getMessage(), e);
        }
    }

     public void saveQuizzes(List<Quiz> quizzes, String quizFilePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(quizFilePath))) {
            for (Quiz quiz : quizzes) {
                writer.write(quiz.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IOException("Error saving quizzes: " + e.getMessage(), e);
        }
    }

    public List<Quiz> readQuizzes(String quizFilePath) throws IOException {
        List<Quiz> quizzes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(quizFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] header = line.split(",");
                    if (header.length < 4) continue;
                    String quizName = header[0];
                    String moduleName = header[1];
                    int duration;
                    int numQuestions;
                    try {
                        duration = Integer.parseInt(header[2]);
                        numQuestions = Integer.parseInt(header[3]);
                    } catch (NumberFormatException e) {
                        continue;
                    }
                    Quiz quiz = new Quiz(quizName, moduleName, duration);
                    for (int i = 0; i < numQuestions; i++) {
                        line = reader.readLine();
                        if (line != null) {
                            String[] parts = line.split(",");
                            if (parts.length < 6) continue;
                            String questionText = parts[0];
                            String[] options = new String[4];
                            System.arraycopy(parts, 1, options, 0, 4);
                            int correct;
                            try {
                                correct = Integer.parseInt(parts[5]);
                            } catch (NumberFormatException e) {
                                continue;
                            }
                            quiz.addQuestion(questionText, options, correct);
                        }
                    }
                    quizzes.add(quiz);
                }
            }
        } catch (IOException e) {
            throw new IOException("Error reading quizzes: " + e.getMessage(), e);
        }
        return quizzes;
    }
    
    public void updateQuizzes(String quizName, Quiz updatedQuiz, String quizFilePath) throws IOException {
        List<Quiz> quizzes = readQuizzes(quizFilePath);
        boolean updated = false;

        for (int i = 0; i < quizzes.size(); i++) {
            if (quizzes.get(i).getQuizName().equals(quizName)) {
                quizzes.set(i, updatedQuiz);
                updated = true;
                break;
            }
        }

        if (!updated) {
            throw new IOException("Quiz not found: " + quizName);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(quizFilePath))) {
            for (Quiz quiz : quizzes) {
                writer.write(quiz.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IOException("Error updating quizzes: " + e.getMessage(), e);
        }
    }
    
    public void deleteQuizzes(String quizName, String quizFilePath) throws IOException {
        List<Quiz> quizzes = readQuizzes(quizFilePath);
        boolean deleted = false;

        for (int i = 0; i < quizzes.size(); i++) {
            if (quizzes.get(i).getQuizName().equals(quizName)) {
                quizzes.remove(i);
                deleted = true;
                break;
            }
        }

        if (!deleted) {
            throw new IOException("Quiz not found: " + quizName);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(quizFilePath))) {
            for (Quiz quiz : quizzes) {
                writer.write(quiz.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IOException("Error deleting quiz: " + e.getMessage(), e);
        }
    }
    public void saveResults(List<ResultEntry> results, String resultsFilePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(resultsFilePath))) {
            for (ResultEntry entry : results) {
                writer.write(entry.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IOException("Error saving results: " + e.getMessage(), e);
        }
    }
    
public List<ResultEntry> readResults(String resultsFilePath) throws IOException {
        List<ResultEntry> results = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(resultsFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String studentId = parts[0];
                    String moduleName = parts[1];
                    int score;
                    try {
                        score = Integer.parseInt(parts[2]);
                    } catch (NumberFormatException e) {
                        continue;
                    }
                    results.add(new ResultEntry(studentId, moduleName, score));
                }
            }
        } catch (IOException e) {
            throw new IOException("Error reading results: " + e.getMessage(), e);
           
            // Convert LinkedList to ArrayList 
        List<ResultEntry> resultList = new ArrayList<>();
        Node current = results.head;
        while (current != null) {
            resultList.add(current.data);
            current = current.next;
        }
        return results;
    }
    
    public List<Quiz> readAttempt(String studentId, String quizFilePath, String resultsFilePath) throws IOException {
        List<Quiz> allQuizzes = readQuizzes(quizFilePath);
        List<Quiz> availableQuizzes = new ArrayList<>();
        List<ResultEntry> results = readResults(resultsFilePath);

        for (Quiz quiz : allQuizzes) {
            boolean hasAttempted = false;
            for (ResultEntry entry : results) {
                if (entry.getStudentId().equals(studentId) && entry.getModuleName().equals(quiz.getModuleName())) {
                    hasAttempted = true;
                    break;
                }
            }
            if (!hasAttempted) {
                availableQuizzes.add(quiz);
            }
        }

        return availableQuizzes;
    }
    public void updateAttempt(Student student, String moduleName, int score, String resultsFilePath) throws IOException {
        List<ResultEntry> results = readResults(resultsFilePath);
        boolean exists = false;

        for (ResultEntry entry : results) {
            if (entry.getStudentId().equals(student.getStudentId()) && entry.getModuleName().equals(moduleName)) {
                entry.setScore(score);
                exists = true;
                break;
            }
        }

        if (!exists) {
            results.add(new ResultEntry(student.getStudentId(), moduleName, score));
        }

        saveResults(results, resultsFilePath);
    }
    
public static class ResultEntry {
        private String studentId;
        private String moduleName;
        private int score;

        public ResultEntry(String studentId, String moduleName, int score) {
            this.studentId = studentId;
            this.moduleName = moduleName;
            this.score = score;
        }

        public String getStudentId() {
            return studentId; }

        public String getModuleName() {
            return moduleName; }

        public int getScore() {
            return score; }

        public void setScore(int score) {
            this.score = score; }

        @Override
        public String toString() {

            return studentId + "," + moduleName + "," + score;
        }
    }
}
