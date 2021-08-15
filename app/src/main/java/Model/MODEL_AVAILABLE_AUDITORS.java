package Model;

public class MODEL_AVAILABLE_AUDITORS {
    String User_Nam ;
    String user_ID;

    public MODEL_AVAILABLE_AUDITORS(String user_Nam, String user_ID) {
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
