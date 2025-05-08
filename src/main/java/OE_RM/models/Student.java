package OE_RM.models;

public class Student extends User {
    private String name;
    private int score;

    public Student(String username, String password, String name) {
        super(username, password, "student");
        this.name = name;
        this.score = 0; // Default score
    }

    public String getStudentId() {
        return getUsername(); // Username is the student ID
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}