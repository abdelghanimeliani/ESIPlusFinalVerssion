package com.example.esiapp;

public class Info
{
    private String Gender ;
    private String Grad ;
    private String Groupe ;
    private String email ;
    private String message ;
    private String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private  String name;

    public Info(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Info(String gender, String grad, String groupe)
    {
        this.Gender = gender;
        this.Grad = grad;
        this.Groupe = groupe;
    }


    public Info(String name, String email, String message,String answer)
    {
        this.email = email;
        this.message = message;
        this.answer = answer;
        this.name = name;

    }




    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getGrad() {
        return Grad;
    }

    public void setGrad(String grad) {
        Grad = grad;
    }

    public String getGroupe() {
        return Groupe;
    }

    public void setGroupe(String groupe) {
        Groupe = groupe;
    }
}

