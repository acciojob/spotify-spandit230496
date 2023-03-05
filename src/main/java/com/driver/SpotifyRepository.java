package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        for(User userinDB: users){
            if(userinDB.getMobile().equals(mobile)){
                return userinDB;
            }
        }
        User newUser= new User(name,mobile);
        users.add(newUser);
        return newUser;
    }

    public Artist createArtist(String name) {
        for(Artist artistinDB: artists){
            if(artistinDB.getName().equals(name))
                return artistinDB;
        }
        Artist newArtist = new Artist(name);
        artists.add(newArtist);
        return newArtist;
    }

    public Album createAlbum(String title, String artistName) {
        //create artist obj
        Artist newArtist= createArtist(artistName);
        for(Album album : albums){
            if(album.getTitle().equals(title))
                return  album;
        }
        //create new album if not in our DB
        Album newAlbum = new Album(title);
        //adding album to listDB
        albums.add(newAlbum);

        //putting all artist with their playlist
        List<Album> albumList = new ArrayList<>();
        if(artistAlbumMap.containsKey(newArtist)){
            albumList=artistAlbumMap.get(newArtist);
        }
        albumList.add(newAlbum);
        artistAlbumMap.put(newArtist,albumList);
        return newAlbum;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        Album album = new Album();
        int albumPresence = 0;
        for(Album CurrAlbum : albums){
            if(CurrAlbum.getTitle().equals(albumName)){
                album=CurrAlbum;
                albumPresence = 1;
                break;
            }
        }
        if(albumPresence == 0){
            throw new Exception("Album does not exist");
        }
        Song newSong = new Song(title,length);
        //adding song to list songs
        songs.add(newSong);

        //adding album n its song to albumsongsMap
        List<Song> songsAlbumlist= new ArrayList<>();
        if(albumSongMap.containsKey(album)){
            songsAlbumlist=albumSongMap.get(album);
        }
        songsAlbumlist.add(newSong);
        albumSongMap.put(album,songsAlbumlist);

        return newSong;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        for(Playlist playlistBylength : playlists){
            if(playlistBylength.getTitle().equals(title))
                return  playlistBylength;
        }
        Playlist playlisthavingLength = new Playlist(title);
        // adding playlist to playlists list
        playlists.add(playlisthavingLength);

        List<Song> liOfSong= new ArrayList<>();
        for(Song song : songs){
            if(song.getLength()==length){
                liOfSong.add(song);
            }
        }
        playlistSongMap.put(playlisthavingLength, liOfSong);

        User currUser= new User();
        int flag= 0;
        for(User user: users){
            if(user.getMobile().equals(mobile)){
                currUser=user;
                flag= 1;
                break;
            }
        }
        if (flag==0){
            throw new Exception("User  You are looking for does not exist");
        }

        List<User> userslist = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlisthavingLength)){
            userslist=playlistListenerMap.get(playlisthavingLength);
        }
        userslist.add(currUser);
        playlistListenerMap.put(playlisthavingLength,userslist);

        creatorPlaylistMap.put(currUser,playlisthavingLength);

        List<Playlist>userplaylists = new ArrayList<>();
        if(userPlaylistMap.containsKey(currUser)){
            userplaylists=userPlaylistMap.get(currUser);
        }
        userplaylists.add(playlisthavingLength);
        userPlaylistMap.put(currUser,userplaylists);
        return playlisthavingLength;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        for(Playlist playlisthavingName : playlists){
            if(playlisthavingName.getTitle().equals(title))
                return  playlisthavingName;
        }
        Playlist playlist = new Playlist(title);
        // adding playlist to playlists list
        playlists.add(playlist);

        List<Song> temp= new ArrayList<>();
        for(Song song : songs){
            if(songTitles.contains(song.getTitle())){
                temp.add(song);
            }
        }
        playlistSongMap.put(playlist,temp);

        User currUser= new User();
        int flag= 0;
        for(User user: users){
            if(user.getMobile().equals(mobile)){
                currUser=user;
                flag=1;
                break;
            }
        }
        if (flag==0){
            throw new Exception("User You are looking for does not exist");
        }

        List<User> userslist = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlist)){
            userslist=playlistListenerMap.get(playlist);
        }
        userslist.add(currUser);
        playlistListenerMap.put(playlist,userslist);

        creatorPlaylistMap.put(currUser,playlist);

        List<Playlist>userplaylists = new ArrayList<>();
        if(userPlaylistMap.containsKey(currUser)){
            userplaylists=userPlaylistMap.get(currUser);
        }
        userplaylists.add(playlist);
        userPlaylistMap.put(currUser,userplaylists);

        return playlist;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        int flag =0;
        Playlist playlist = new Playlist();
        for(Playlist curplaylist: playlists){
            if(curplaylist.getTitle().equals(playlistTitle)){
                playlist=curplaylist;
                flag=1;
                break;
            }
        }
        if (flag==0){
            throw new Exception("Playlist You are looking for does not exist");
        }

        User currUser= new User();
        int flag2= 0;
        for(User user: users){
            if(user.getMobile().equals(mobile)){
                currUser=user;
                flag2= 1;
                break;
            }
        }
        if (flag2==0){
            throw new Exception("User You are looking for does not exist");
        }

        List<User> userslist = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlist)){
            userslist=playlistListenerMap.get(playlist);
        }
        if(!userslist.contains(currUser))
            userslist.add(currUser);
        playlistListenerMap.put(playlist,userslist);
        if(creatorPlaylistMap.get(currUser)!=playlist)
            creatorPlaylistMap.put(currUser,playlist);
        List<Playlist>userplaylists = new ArrayList<>();
        if(userPlaylistMap.containsKey(currUser)){
            userplaylists=userPlaylistMap.get(currUser);
        }
        if(!userplaylists.contains(playlist))userplaylists.add(playlist);
        userPlaylistMap.put(currUser,userplaylists);


        return playlist;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        User currUser= new User();
        int flag2= 0;
        for(User user: users){
            if(user.getMobile().equals(mobile)){
                currUser=user;
                flag2= 1;
                break;
            }
        }
        if (flag2==0){
            throw new Exception("User You are looking for does not exist");
        }

        Song song = new Song();
        int flag = 0;
        for(Song cursong : songs){
            if(cursong.getTitle().equals(songTitle)){
                song=cursong;
                flag=1;
                break;
            }
        }
        if (flag==0){
            throw new Exception("Song You are looking for does not exist");
        }

        //public HashMap<Song, List<User>> songLikeMap;
        List<User> users = new ArrayList<>();
        if(songLikeMap.containsKey(song)){
            users=songLikeMap.get(song);
        }
        if (!users.contains(currUser)){
            users.add(currUser);
            songLikeMap.put(song,users);
            song.setLikes(song.getLikes()+1);
            Album album = new Album();
            for(Album curAlbum : albumSongMap.keySet()){
                List<Song> temp = albumSongMap.get(curAlbum);
                if(temp.contains(song)){
                    album=curAlbum;
                    break;
                }
            }
            Artist artist = new Artist();
            for(Artist curArtist : artistAlbumMap.keySet()){
                List<Album> temp = artistAlbumMap.get(curArtist);
                if(temp.contains(album)){
                    artist=curArtist;
                    break;
                }
            }

            artist.setLikes(artist.getLikes()+1);
        }
        return song;
    }

    public String mostPopularArtist() {

        String name="";
        int maxLikes = -10000;
        for(Artist artist : artists){
            maxLikes= Math.max(maxLikes,artist.getLikes());
        }
        for(Artist artist : artists){
            if(maxLikes==artist.getLikes()){
                name=artist.getName();
            }
        }
        return name;
    }

    public String mostPopularSong() {

        String name="";
        int maxLikes = -10000;
        for(Song song : songs){
            maxLikes=Math.max(maxLikes,song.getLikes());
        }
        for(Song song : songs){
            if(maxLikes==song.getLikes())
                name=song.getTitle();
        }
        return name;
    }
}