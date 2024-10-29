package com.example.messagingstompwebsocket.longpolling;

public class Post {

    private String message;
    private Long roomId;

    public Post() {
    }

    public String getMessage() {
        return message;
    }

    public Long getRoomId() {
        return roomId;
    }

    @Override
    public String toString() {
        return "PostMessage{" +
                "message='" + message + '\'' +
                ", roomId=" + roomId +
                '}';
    }
}
