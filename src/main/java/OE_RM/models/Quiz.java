package OE_RM.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Quiz {
    private String quizName;
    private String moduleName;
    private int duration;
    private List<Question> questions;

    public Quiz(String quizName, String moduleName, int duration) {
        this.quizName = quizName;
        this.moduleName = moduleName;
        this.duration = duration;
        this.questions = new ArrayList<>();
    }

    public void addQuestion(String questionText, String[] options, int correctAnswer) {
        questions.add(new Question(questionText, options, correctAnswer));
    }

    public String getQuizName() {
        return quizName; }


    public String getModuleName() {
        return moduleName; }


    public int getDuration() {
        return duration; }


    public int getTotalQuestions() {
        return questions.size(); }


    public String getQuestionText(int index) {
        return questions.get(index).getQuestionText(); }


    public String[] getOptions(int index) {
        return questions.get(index).getOptions(); }


    public int getCorrectAnswer(int index) {
        return questions.get(index).getCorrectAnswer(); }


    public List<Question> getQuestions() {
        return Collections.unmodifiableList(questions); }


    public String toFileString() {
        StringBuilder sb = new StringBuilder();
        sb.append(quizName).append(",")
                .append(moduleName).append(",")
                .append(duration).append(",")
                .append(getTotalQuestions());
        for (Question q : questions) {
            sb.append("\n").append(q.toFileString());
        }
        return sb.toString();
    }

