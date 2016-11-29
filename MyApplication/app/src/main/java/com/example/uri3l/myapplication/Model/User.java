package com.example.uri3l.myapplication.Model;

public class User {
    private int ID;
    private String FirstName;
    private String LastName;
    private String Username;
    private String Password;
    public User(int ID,String FN,String LN,String UserName,String Pass){
        this.ID=ID;
        this.FirstName=FN;
        this.LastName=LN;
        this.Username=UserName;
        this.Password=Pass;
    }
    public int getID(){return ID;}
    public String getFirstName(){
        return FirstName;
    }
    public void setFirstName(String value){
        this.FirstName=value;
    }
    public String getLastName(){
        return LastName;
    }
    public void setLastName(String value){
        this.LastName=value;
    }
    public String getUsername(){
        return Username;
    }
    public void setUsername(String value){this.Username=value;}
    public String getPassword(){
        return Password;
    }
    public void setPassword(String value){
        this.Password=value;
    }
}
