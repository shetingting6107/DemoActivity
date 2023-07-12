package com.example.demoactivity.otto;

/**
 * 传递数据的对象
 */
public class EventData {

    private String name;
    private String message;

    public EventData(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public EventData() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
