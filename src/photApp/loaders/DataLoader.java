package photApp.loaders;

import javafx.scene.control.TreeView;

import java.io.IOException;


public interface DataLoader {

    public void load(TreeView treeView) throws IOException;
}
