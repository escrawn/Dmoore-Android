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
}
