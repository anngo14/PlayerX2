package Models;

import Helpers.ViewType;

public class Tile {
    private String title;
    private String icon;
    private String path;
    private ViewType viewType;

    public Tile(){
        title = "";
        icon = "";
        path = "";
        viewType = null;
    }

    public Tile(String title, String icon, String path, ViewType viewType){
        this.title = title;
        this.icon = icon;
        this.path = path;
        this.viewType = viewType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ViewType getViewType() {
        return viewType;
    }

    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
    }
}
