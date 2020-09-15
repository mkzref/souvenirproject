package com.example.souvenir.Models;

public class help {
    private String Question;
    private String Answer;
    public help(String Question, String ID, String Answer) {
     this.Answer=Answer;
     this.ID=ID;
     this.Question=Question;

    }
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    private String ID;
    public help(){

    }

    public String getQuestion() {
        return Question;
    }

    public String getAnswer() {
        return Answer;
    }

}
