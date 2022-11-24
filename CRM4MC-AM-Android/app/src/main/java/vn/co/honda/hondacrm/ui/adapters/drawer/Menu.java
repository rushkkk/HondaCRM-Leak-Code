package vn.co.honda.hondacrm.ui.adapters.drawer;

public class Menu {
    private int id;
    private String itemName;
    private int itemIcon;

    public Menu(int id, String itemName, int itemIcon) {
        this.id = id;
        this.itemName = itemName;
        this.itemIcon = itemIcon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemIcon() {
        return itemIcon;
    }

    public void setItemIcon(int icon) {
        this.itemIcon = itemIcon;
    }
}
