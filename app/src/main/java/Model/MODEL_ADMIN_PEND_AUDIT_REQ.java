package Model;

public class MODEL_ADMIN_PEND_AUDIT_REQ {

    String Auditor_UID = "";
    String Auditor_NAME = "";
    String Client_UID  = "";
    String Client_name  = "";
    String Client_rating = "";
    String Client_selected_items = "";
    boolean isSelected = false;
    String randomUID ="";

    public String getRandomUID() {
        return randomUID;
    }

    public void setRandomUID(String randomUIDs) {
        randomUID = randomUIDs;
    }

    public String getAuditor_UID() {
        return Auditor_UID;
    }

    public void setAuditor_UID(String auditor_UID) {
        Auditor_UID = auditor_UID;
    }

    public String getClient_UID() {
        return Client_UID;
    }

    public void setClient_UID(String client_UID) {
        Client_UID = client_UID;
    }

    public String getClient_name() {
        return Client_name;
    }

    public void setClient_name(String client_name) {
        Client_name = client_name;
    }

    public String getClient_rating() {
        return Client_rating;
    }

    public void setClient_rating(String client_rating) {
        Client_rating = client_rating;
    }

    public String getClient_selected_items() {
        return Client_selected_items;
    }

    public void setClient_selected_items(String client_selected_items) {
        Client_selected_items = client_selected_items;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getAuditor_NAME() {
        return Auditor_NAME;
    }

    public void setAuditor_NAME(String auditor_NAME) {
        Auditor_NAME = auditor_NAME;
    }

    public boolean is_pending_auditor_isSelected() {
        return isSelected;
    }

    public MODEL_ADMIN_PEND_AUDIT_REQ(String auditor_UID, String auditor_NAME,String client_UID, String client_name,
                                      String client_rating, String client_selected_items,boolean isselected) {
        Auditor_UID = auditor_UID;
        Client_UID = client_UID;
        Client_name = client_name;
        Client_rating = client_rating;
        Client_selected_items = client_selected_items;
        Auditor_NAME = auditor_NAME;
        isSelected = isselected;
    }

}
