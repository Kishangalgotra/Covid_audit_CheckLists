package Model;

public class Users_data {
    String name;
    String rating;
    String selected_items;

    public Users_data(String name, String rating, String selected_items) {
        this.name = name;
        this.rating = rating;
        this.selected_items = selected_items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getSelected_items() {
        return selected_items;
    }

    public void setSelected_items(String selected_items) {
        this.selected_items = selected_items;
    }

}
