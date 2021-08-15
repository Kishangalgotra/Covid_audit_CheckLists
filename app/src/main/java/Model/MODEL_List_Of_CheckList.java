package Model;

public class MODEL_List_Of_CheckList {

    String CheckList;
    boolean isSelected;
    String checkListname;
    String MainList;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getMainList() {
        return MainList;
    }

    public void setMainList(String mainList) {
        MainList = mainList;
    }


    public String getCheckListname() {
        return checkListname;
    }

    public void setCheckListname(String checkListname) {
        this.checkListname = checkListname;
    }

    public String getCheckList() {
        return CheckList;
    }

    public void setCheckList(String checkList) {
        CheckList = checkList;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public MODEL_List_Of_CheckList(String checkList, boolean isSelected,String checkListname,String MainList) {
        this.CheckList = checkList;
        this.isSelected = isSelected;
        this.checkListname = checkListname;
        this.MainList= MainList;
    }

}
