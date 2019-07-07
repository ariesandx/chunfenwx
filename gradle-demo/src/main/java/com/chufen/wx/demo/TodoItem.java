package com.chufen.wx.demo;

/**
 * @author chunfen.wx
 */
public class TodoItem {

    private String name;
    private boolean finish;

    public TodoItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    @Override
    public String toString() {
        return "TodoItem{" +
                "name='" + name + '\'' +
                ", finish=" + finish +
                '}';
    }
}
