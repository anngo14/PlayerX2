package Models;

import Helpers.ViewType;

import java.util.ArrayList;
import java.util.List;

public class Users {
    private String name;
    private List<Tile> tileList;

    public Users(){
        name = "";
        tileList = new ArrayList<>();
        tileList.add(new Tile("YouTube", "/Assets/youtube.png", "https://www.youtube.com/", ViewType.YOUTUBEVIEW));
        tileList.add(new Tile("Videos", "/Assets/movie.png", "", ViewType.VIDEOVIEW));
        tileList.add(new Tile("Music", "/Assets/music.png", "", ViewType.MUSICVIEW));
        tileList.add(new Tile("Spotify", "/Assets/spotify.png", "https://open.spotify.com/", ViewType.SPOTIFYVIEW));
    }

    public Users(String name){
        this.name = name;
        tileList = new ArrayList<>();
        tileList.add(new Tile("YouTube", "/Assets/youtube.png", "https://www.youtube.com/", ViewType.YOUTUBEVIEW));
        tileList.add(new Tile("Videos", "/Assets/movie.png", "", ViewType.VIDEOVIEW));
        tileList.add(new Tile("Music", "/Assets/music.png", "", ViewType.MUSICVIEW));
        tileList.add(new Tile("Spotify", "/Assets/spotify.png", "https://open.spotify.com/", ViewType.SPOTIFYVIEW));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Tile> getTileList() {
        return tileList;
    }

    public void setTileList(List<Tile> tileList) {
        this.tileList = tileList;
    }
}
