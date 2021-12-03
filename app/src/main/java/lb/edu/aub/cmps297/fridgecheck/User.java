package lb.edu.aub.cmps297.fridgecheck;

import java.util.ArrayList;

public class User {
    String Name;
    String Email;
    ArrayList<String> Wishlist;
    Boolean isAdmin;

    public User(){}

    public User(String name, String email, ArrayList<String> wishlist, Boolean isAdmin) {
        Name = name;
        Email = email;
        Wishlist = wishlist;
        this.isAdmin = isAdmin;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setWishlist(ArrayList<String> wishlist) {
        Wishlist = wishlist;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public ArrayList<String> getWishlist() {
        return Wishlist;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }
}
