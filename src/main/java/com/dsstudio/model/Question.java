package com.dsstudio.model;

import java.util.List;

public class Question {
    private final String id;
    private final String topic;
    private final String category; // CONCEPT, OUTPUT, DEBUG, COMPLEXITY, DRY_RUN
    private final String difficulty; // BEGINNER, INTERMEDIATE, ADVANCED
    private final String questionText;
    private final List<String> options;
    private final int correctIndex;
    private final String explanation;
    private final String codeSnippet;
    private final int xpReward;

    public Question(String id, String topic, String category, String difficulty, 
                    String questionText, List<String> options, int correctIndex, 
                    String explanation, String codeSnippet, int xpReward) {
        this.id = id;
        this.topic = topic;
        this.category = category;
        this.difficulty = difficulty;
        this.questionText = questionText;
        this.options = options;
        this.correctIndex = correctIndex;
        this.explanation = explanation;
        this.codeSnippet = codeSnippet;
        this.xpReward = xpReward;
    }

    public String getId() { return id; }
    public String getTopic() { return topic; }
    public String getCategory() { return category; }
    public String getDifficulty() { return difficulty; }
    public String getQuestionText() { return questionText; }
    public List<String> getOptions() { return options; }
    public int getCorrectIndex() { return correctIndex; }
    public String getExplanation() { return explanation; }
    public String getCodeSnippet() { return codeSnippet; }
    public int getXpReward() { return xpReward; }
}
