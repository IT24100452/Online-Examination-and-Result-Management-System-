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


   