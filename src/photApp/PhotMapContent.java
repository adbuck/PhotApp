package photApp;

import javafx.scene.control.TreeItem;

public class PhotMapContent
{
    private String selectedItem;
    private TreeItem<String> compiler;
    private TreeItem<String> mapLink;
    private TreeItem<String> comments;


    public PhotMapContent(String selectedItem) {
        this.selectedItem = selectedItem;
    }

    public PhotMapContent() {
        this.selectedItem = selectedItem;
    }
    ////////////////////////////////////
    //          GETTERS              //
    ///////////////////////////////////
    public TreeItem<String> getCompiler() {
        return compiler;
    }

    public TreeItem<String> getMapLink() {
        return mapLink;
    }

    public TreeItem<String> getComments() {
        return comments;
    }


    ////////////////////////////////////
    //          SETTERS              //
    ///////////////////////////////////
    public void setCompiler(TreeItem<String> compiler) {
        this.compiler = compiler;
    }

    public void setMapLink(TreeItem<String> mapLink) {
        this.mapLink = mapLink;
    }

    public void setComments(TreeItem<String> comments) {
        this.comments = comments;
    }

}
