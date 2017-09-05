package com.dodhev.dmooretaskmanger;

/**
 * Created by Escrawn on 05/09/2017.
 */

public class Task {

    private int id;
    private String name;
    private String desc;

    public Task(int id, String name, String desc) {

        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
