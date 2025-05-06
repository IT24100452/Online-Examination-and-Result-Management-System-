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

  