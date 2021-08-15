package Model;

public class Model_admin_auditor {
        private String user_ID;
        private String user_Name;

    public String getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }

    public String getUser_Name() {
        return user_Name;
    }

    public void setUser_Name(String user_Name) {
        this.user_Name = user_Name;
    }

    public boolean isAdmin_auditor_isSelected() {
        return admin_auditor_isSelected;
    }

    public void setAdmin_auditor_isSelected(boolean admin_auditor_isSelected) {
        this.admin_auditor_isSelected = admin_auditor_isSelected;
    }

    private boolean admin_auditor_isSelected = false;

    public Model_admin_auditor(String user_ID, String user_Name, boolean admin_auditor_isSelected) {
        this.user_ID = user_ID;
        this.user_Name = user_Name;
        this.admin_auditor_isSelected = admin_auditor_isSelected;
    }

}

