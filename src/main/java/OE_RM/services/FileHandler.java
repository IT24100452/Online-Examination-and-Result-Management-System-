package OE_RM.services;

import OE_RM.models.Student;
import OE_RM.models.User;
import OE_RM.models.Quiz;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
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

  
