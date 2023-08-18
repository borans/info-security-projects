public class Message {

    private String message_id;
    private String content;
    private String password;
    private User user;


    // CONSTRUCTOR


    public Message(String message_id, String content, String password, User user) {
        this.message_id = message_id;
        this.content = content;
        this.password = password;
        this.user = user;
    }

    public String getMessage_id() {
        return message_id;
    }

    public String getContent() {
        return content;
    }

    public String getPassword() {
        return password;
    }

    public User getUser() {
        return user;
    }
}

