package com.example.app;

public class Expense {

    private long id; // required field
    private String type; // required field
    private String amount; // required field
    private String time; // required field
    private String additional_comments; // optional field

    public Expense(
            long id, String type, String amount, String time, String additional_comments
    ) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.time = time;
        this.additional_comments = additional_comments;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAdditional_comments() {
        return additional_comments;
    }

    public void setAdditional_comments(String additional_comments) {
        this.additional_comments = additional_comments;
    }
}
