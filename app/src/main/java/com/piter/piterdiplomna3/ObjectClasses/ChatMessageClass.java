package com.piter.piterdiplomna3.ObjectClasses;

/**
 * Created by Piter on 14/10/2016.
 */

public class ChatMessageClass {
    int id;
    private int user_send_id;
    private int user_to_id;
    private String message;
    private String create_date_time;


    public ChatMessageClass(int id, int user_send_id, int user_to_id, String message, String create_date_time) {
        // TODO Auto-generated constructor stub
        super();
        this.id=id;
        this.user_send_id = user_send_id;
        this.user_to_id = user_to_id;
        this.message = message;
        this.create_date_time = create_date_time;
    }
    public ChatMessageClass(int user_send_id, int user_to_id, String message, String create_date_time) {
        // TODO Auto-generated constructor stub
        super();
        this.user_send_id = user_send_id;
        this.user_to_id = user_to_id;
        this.message = message;
        this.create_date_time = create_date_time;
    }

    public int getUser_send_id() {
        return user_send_id;
    }

    public int getUser_to_id() {
        return user_to_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreate_date_time() {
        return create_date_time;
    }
}
