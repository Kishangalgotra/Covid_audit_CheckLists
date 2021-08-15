package Model;

public class MODEL_ADMI_REJECTED_LIST {

    String Auditor_UID = "";
    String Client_UID  = "";
    String Client_name  = "";
    String Client_rating = "";
    String Client_selected_items = "";
    boolean isSelected = false;
    String Auditor_NAME = "";

    public MODEL_ADMI_REJECTED_LIST(String auditor_UID, String auditor_NAME, String client_UID, String client_name,
                                    String client_rating, String client_selected_items, boolean isSelected) {
        this.Auditor_UID = auditor_UID;
        this.Auditor_NAME = auditor_NAME;
        this.Client_UID = client_UID;
        this.Client_name = client_name;
        this.Client_rating = client_rating;
        this.Client_selected_items = client_selected_items;
        this.isSelected = isSelected;
    }

    public String getAuditor_UID() {
        return Auditor_UID;
    }

    public void setAuditor_UID(String auditor_UID) {
        Auditor_UID = auditor_UID;
    }

    public String getAuditor_NAME() {
        return Auditor_NAME;
    }

    public void setAuditor_NAME(String auditor_NAME) {
        Auditor_NAME = auditor_NAME;
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

}
