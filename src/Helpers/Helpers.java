package Helpers;

import Controllers.*;
import Models.Users;
import Models.Tile;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Helpers {
    private static Users lastUser = null;
    private static Users currentUser = null;
    private static List<Users> users = new ArrayList<>();
    private static JSONParser jsonParser;
    private static FXMLLoader loader;
    private static Controller controller;
    private static FadeTransition fade;

    private static final String defaultUser = "C:\\PlayerX\\Users\\LastUser.json";
    private static final String defaultUsers = "C:\\PlayerX\\Users\\Users.json";

    public static boolean addUser(Users user){
        if(validUser(user)){
            currentUser = user;
            users.add(user);
            writeUserJSON(user);
            writeUsersJSON(users);
            return true;
        }
        return false;
    }

    public static boolean deleteUser(Users user){
        boolean result = users.remove(user);
        if(users.size() == 0){
            try {
                new FileWriter(defaultUser, false).close();
                new FileWriter(defaultUsers, false).close();
                currentUser = null;
                lastUser = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else{
            currentUser = users.get(0);
            writeUsersJSON(users);
            writeUserJSON(currentUser);
        }
        return result;
    }

    public static boolean validUser(Users user){
        if(user == null) return false;
        for(Users u: users){
            if(u.getName().equals(user.getName())) return false;
        }
        return !user.getName().matches("\\s*");
    }

    public static Users getLastUser() {
        if(lastUser == null){
            jsonParser = new JSONParser();
            try (FileReader reader = new FileReader(defaultUser))
            {
                Object userObj = jsonParser.parse(reader);
                JSONObject userJson = (JSONObject) userObj;
                lastUser = new Users(userJson.get("name").toString());

                JSONArray tilesJson = (JSONArray) userJson.get("tiles");
                List<Tile> tileList = new ArrayList<>();
                for(Object tileObj: tilesJson){
                    JSONObject tileJson = (JSONObject) tileObj;

                    Tile tile = new Tile();
                    tile.setTitle(tileJson.get("title").toString());
                    tile.setIcon(tileJson.get("icon").toString());
                    tile.setPath(tileJson.get("path").toString());
                    tile.setViewType(ViewType.valueOf(tileJson.get("viewType").toString()));
                    tileList.add(tile);
                }
                lastUser.setTileList(tileList);
            } catch (FileNotFoundException e) {
                try {
                    File directory = new File("C:\\PlayerX\\Users\\");
                    directory.mkdirs();
                    new FileWriter(defaultUser, false).close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
        return lastUser;
    }

    public static List<Users> getAllUsers(){
        if(users.size() == 0){
            jsonParser = new JSONParser();
            try (FileReader reader = new FileReader(defaultUsers))
            {
                Object usersObj = jsonParser.parse(reader);
                JSONArray usersJson = (JSONArray) usersObj;
                users = new ArrayList<>();
                for(Object userObj: usersJson){
                    JSONObject userJson = (JSONObject) userObj;
                    Users tmpUser = new Users(userJson.get("name").toString());

                    JSONArray tilesJson = (JSONArray) userJson.get("tiles");
                    List<Tile> tmpTileList = new ArrayList<>();
                    for(Object tileObj: tilesJson){
                        JSONObject tileJson = (JSONObject) tileObj;

                        Tile tile = new Tile();
                        tile.setTitle(tileJson.get("title").toString());
                        tile.setIcon(tileJson.get("icon").toString());
                        tile.setPath(tileJson.get("path").toString());
                        tile.setViewType(ViewType.valueOf(tileJson.get("viewType").toString()));
                        tmpTileList.add(tile);
                    }
                    tmpUser.setTileList(tmpTileList);
                    users.add(tmpUser);
                }
            } catch (FileNotFoundException e) {
                try {
                    File directory = new File("C:\\PlayerX\\Users\\");
                    directory.mkdirs();
                    new FileWriter(defaultUsers, false).close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                return null;
            }
        }
        return users;
    }

    public static void setUsers(List<Users> users){
        Helpers.users = users;
    }

    public static Users getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(Users currentUser) {
        Helpers.currentUser = currentUser;
    }

    public static void setLastuser(Users user){
        lastUser = user;
    }

    public static void switchViews(ViewType view, StackPane panel){
        if(view == null) return;
        loader = new FXMLLoader();

        switch(view){
            case HOMEVIEW:
                loader = new FXMLLoader(Helpers.class.getResource("/Views/HomeView.fxml"));
                controller = new HomeController();
                break;
            case INTROVIEW:
                loader = new FXMLLoader(Helpers.class.getResource("/Views/IntroView.fxml"));
                controller = new IntroController();
                break;
            case WELCOMEVIEW:
                loader = new FXMLLoader(Helpers.class.getResource("/Views/WelcomeView.fxml"));
                controller = new WelcomeController();
                break;
            case SWITCHUSERVIEW:
                loader = new FXMLLoader(Helpers.class.getResource("/Views/SwitchUserView.fxml"));
                controller = new SwitchUserController();
                break;
            case NEWUSERVIEW:
                loader = new FXMLLoader(Helpers.class.getResource("/Views/NewUserView.fxml"));
                controller = new NewUserController();
                break;
            case EDITUSERVIEW:
                loader = new FXMLLoader(Helpers.class.getResource("/Views/NewUserView.fxml"));
                controller = new EditUserController();
                break;
            case SETTINGSVIEW:
                loader = new FXMLLoader(Helpers.class.getResource("/Views/SettingsView.fxml"));
                controller = new SettingsController();
                break;
            default:
            	break;
        }

        try {
            loader.setController(controller);
            StackPane pane = loader.load();
            panel.getChildren().setAll(pane);
            fade = new FadeTransition(Duration.millis(500), panel);
            fade.setFromValue(0);
            fade.setToValue(1);

            fade.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void switchToTilesView(ViewType view, StackPane panel, Tile tile){
        switch(view){
            case VIDEOVIEW:
                loader = new FXMLLoader(Helpers.class.getResource("/Views/FolderExplorerView.fxml"));
                controller = new VideoFolderController(tile);
                break;
            case MUSICVIEW:
                loader = new FXMLLoader(Helpers.class.getResource("/Views/FolderExplorerView.fxml"));
                controller = new MusicFolderController(tile);
                break;
            case NEWFOLDERVIEW:
                loader = new FXMLLoader(Helpers.class.getResource("/Views/NewTileFolderView.fxml"));
                controller = new NewTileFolderController();
                break;
            case EDITFOLDERVIEW:
                loader = new FXMLLoader(Helpers.class.getResource("/Views/NewTileFolderView.fxml"));
                controller = new EditTileFolderController(tile);
                break;
            case YOUTUBEVIEW:
            case SPOTIFYVIEW:
                try {
                    Desktop.getDesktop().browse(new URL(tile.getPath()).toURI());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            default: 
            	break;
        }
        try {
            loader.setController(controller);
            StackPane pane = loader.load();
            panel.getChildren().setAll(pane);
            fade = new FadeTransition(Duration.millis(500), panel);
            fade.setFromValue(0);
            fade.setToValue(1);

            fade.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static int confirmationBox(String msg)
    {
        JFrame jf=new JFrame();
        jf.setAlwaysOnTop(true);
        JLabel label = new JLabel(msg);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        UIManager.put("OptionPane.minimumSize",new Dimension(150,100));
        int input = JOptionPane.showConfirmDialog(jf, label);
        return input;
    }

    public static void writeUserJSON(Users user){
        JSONObject userJson = new JSONObject();
        userJson.put("name", user.getName());

        JSONArray tilesJson = new JSONArray();
        for(Tile tile: user.getTileList()){
            JSONObject tmp = new JSONObject();
            tmp.put("title", tile.getTitle());
            tmp.put("icon", tile.getIcon());
            tmp.put("path", tile.getPath());
            tmp.put("viewType", tile.getViewType().toString());
            tilesJson.add(tmp);
        }
        userJson.put("tiles", tilesJson);

        try (FileWriter file = new FileWriter(defaultUser)) {
            file.write(userJson.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeUsersJSON(List<Users> users){
        JSONArray usersJson = new JSONArray();
        for(Users user: users){
            JSONObject userJson = new JSONObject();
            userJson.put("name", user.getName());

            JSONArray tilesJson = new JSONArray();
            for(Tile tile: user.getTileList()){
                JSONObject tmp = new JSONObject();
                tmp.put("title", tile.getTitle());
                tmp.put("icon", tile.getIcon());
                tmp.put("path", tile.getPath());
                tmp.put("viewType", tile.getViewType().toString());
                tilesJson.add(tmp);
            }
            userJson.put("tiles", tilesJson);
            usersJson.add(userJson);

            try (FileWriter file = new FileWriter(defaultUsers)) {
                file.write(usersJson.toJSONString());
                file.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
