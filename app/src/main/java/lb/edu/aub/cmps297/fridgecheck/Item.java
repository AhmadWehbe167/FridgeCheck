package lb.edu.aub.cmps297.fridgecheck;

public class Item {
    String uid;
    String Image;
    String Type;
    String category;
    String description;
    String itemName;
    String stock;
    int price;

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public Item(){}

    public Item(String uid, String image, String type, String category, String description, String itemName, String stock, int price) {
        this.uid = uid;
        this.Image = image;
        this.Type = type;
        this.category = category;
        this.description = description;
        this.itemName = itemName;
        this.stock = stock;
        this.price = price;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        this.Type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public Number getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
