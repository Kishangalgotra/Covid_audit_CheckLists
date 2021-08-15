package Model;

public class Auditing_Request_Model {
    String User_Nam ;
    String user_ID;
    public Auditing_Request_Model(String user_Nam, String user_ID) {
        User_Nam = user_Nam;
        this.user_ID = user_ID;
    }
    public String getUser_Nam() {
        return User_Nam;
    }

    public void setUser_Nam(String user_Nam) {
        User_Nam = user_Nam;
    }

    public String getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }


}
