package photApp;

import javafx.scene.control.TreeItem;

public class Content
{
    private String selectedItem;
    private TreeItem<String> title;
    private TreeItem<String> link;
    private TreeItem<String> descrip;
    private TreeItem<String> category;
    private TreeItem<String> type;

    public Content(String selectedItem) {
        this.selectedItem = selectedItem;
    }

    public Content() {
        this.selectedItem = selectedItem;
    }
    ////////////////////////////////////
    //          GETTERS              //
    ///////////////////////////////////
    public TreeItem<String> getTitle() {
        return title;
    }

    public TreeItem<String> getLink() {
        return link;
    }

    public TreeItem<String> getCategory() {
        return category;
    }

    public TreeItem<String> getDescrip() {
        return descrip;
    }
    public TreeItem<String> getType() {
        return type;
    }

    ////////////////////////////////////
    //          SETTERS              //
    ///////////////////////////////////
    public void setDescrip(TreeItem<String> descrip) {
        this.descrip = descrip;
    }

    public void setCategory(TreeItem<String> category) {
        this.category = category;
    }

    public void setLink(TreeItem<String> link) {
        this.link = link;
    }

    public void setTitle(TreeItem<String> title) {
        this.title = title;
    }

    public void setType(TreeItem<String> type) {
        this.type = type;
    }
}
