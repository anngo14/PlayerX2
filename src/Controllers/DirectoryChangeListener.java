package Controllers;

import Models.Media;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DirectoryChangeListener implements ChangeListener<String> {
    private static List<Media> videoList;
    private ListView<Media> fileList;
    private String dirPath;

    public DirectoryChangeListener(Node n, String directoryPath){
        fileList = (ListView<Media>) n;
        dirPath = directoryPath;
        videoList = new ArrayList<>();
    }

    @Override
    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
        if(newValue == null){
            return;
        }
        if(!newValue.equals(oldValue))
        {
            videoList.clear();
            fileList.getItems().clear();

            if(newValue.equals("All Videos"))
            {
                listAllFiles(dirPath);
            }
            else
            {
                listAllFiles(dirPath.concat("\\" + newValue));
            }
            ObservableList<Media> file$ = FXCollections.observableArrayList(videoList);
            fileList.setCellFactory(x -> new MediaFormatCell());
            fileList.setItems(file$);
        }
    }

    public void listAllFiles(String path)
    {
        File folder = new File(path);
        File[] files = folder.listFiles();
        if(files == null) return;

        for (File f : files)
        {
            if (f.isFile() && VideoFolderController.isVideoFile(f))
            {
                Media temp = new Media(f.getName(), f.getAbsolutePath(), "/Assets/mediafile.png");
                videoList.add(temp);
            }
            else if (f.isDirectory())
            {
                listAllFiles(f.getAbsolutePath());
            }
        }
        Collections.sort(videoList, Media.naturalComparator);
    }

    public static List<Media> getVideoList(){ return videoList; }
}
