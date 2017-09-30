package com.dodhev.dmooretaskmanger;

import java.util.List;

/**
 * Created by Escrawn on 05/09/2017.
 */

public class User {

    private int id;
    private String name;
    private String userName;
    public List<Task> task;

    public User(int id, String name, String userName, List<Task> task) {

        this.id = id;
        this.name = name;
        this.userName = userName;
        this.task = task;
    }

    public User(int id){
        this.id = id;
    }

    public User(int id,String userName){
        this.id = id;
        this.userName = userName;
    }

    public User(){
        this.id = 0;
        this.name = "";
        this.userName = "";
        this.task = null;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUserName() {
        return userName;
    }

    public List<Task> getTask() {
        return task;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTask(List<Task> task) {
        this.task = task;
    }
}
