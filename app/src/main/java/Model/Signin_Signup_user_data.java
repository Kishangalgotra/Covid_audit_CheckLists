package Model;

public class Signin_Signup_user_data {
    private String fullname;
    private String email;
    private int admin;
    public Signin_Signup_user_data(String email, String fullname,int admin){
        this.fullname = fullname;
        this.email = email;
        this.admin = admin;
    }
    public Signin_Signup_user_data(){

    }
    public int getadmin() {
        return admin;
    }

    public void setadmin(int admin) {
        this.admin = admin;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
