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
  User userToAdd= new User();
        userToAdd.setName(name);
        userToAdd.setMobile(mobile);
        users.add(userToAdd);


        return userToAdd;
    }

    public Artist createArtist(String name) {

        Artist artisttoAdd= new Artist();
        artisttoAdd.setName(name);
        artisttoAdd.setLikes(0);
        artists.add(artisttoAdd);
        artistAlbumMap.put(artisttoAdd,new ArrayList<>());

        return artisttoAdd;
    }

    public Album createAlbum(String title, String artistName) {
        Artist artistPresent = null;

        for(Artist artist:artists){
            if(artist.getName().equals(artistName)){
                artistPresent=artist;
                break;
            }
        }
        if(artistPresent==null){
            artistPresent = createArtist(artistName);

            Album album = new Album();

            album.setTitle(title);
            album.setReleaseDate(new Date());

            albums.add(album);

            List<Album> artistAlbumSongList = new ArrayList<>();
            artistAlbumSongList.add(album);
            artistAlbumMap.put(artistPresent,artistAlbumSongList);

            return album;
        }
        else {
            Album album = new Album();

            album.setTitle(title);
            album.setReleaseDate(new Date());

            albums.add(album);

            List<Album>artistAlbumSongList = artistAlbumMap.get(artistName);

            if(artistAlbumSongList == null){
                artistAlbumSongList = new ArrayList<>();
            }
            artistAlbumSongList.add(album);
            artistAlbumMap.put(artistPresent,artistAlbumSongList);

            return album;
        }
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        Album songAlbum = null;
        for(Album album1:albums){
            if(album1.getTitle()==albumName){
                songAlbum=album1;
                break;
            }
        }
        if(songAlbum==null)
            throw new Exception("Album does not exist");
        else {
            Song song = new Song();
            song.setTitle(title);
            song.setLength(length);
            song.setLikes(0);

            songs.add(song);

            if(albumSongMap.containsKey(songAlbum)){
                List<Song> list = albumSongMap.get(songAlbum);
                list.add(song);
                albumSongMap.put(songAlbum,list);
            }else{
                List<Song> songList = new ArrayList<>();
                songList.add(song);
                albumSongMap.put(songAlbum,songList);
            }

            return song;
        }
    }


    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {

        User userPlayList = null;
        for(User creator:users){
            if(creator.getMobile().equals(mobile)){
                userPlayList=creator;
                break;
            }
        }
        if(userPlayList==null)
            throw new Exception("User does not exist");
        else {
            Playlist playlist = new Playlist();
            playlist.setTitle(title);
            playlists.add(playlist);

            List<Song> l = new ArrayList<>();
            for(Song song:songs){
                if(song.getLength()==length){
                    l.add(song);
                }
            }
            playlistSongMap.put(playlist,l);

            List<User> list = new ArrayList<>();
            list.add(userPlayList);
            playlistListenerMap.put(playlist,list);

            creatorPlaylistMap.put(userPlayList,playlist);



            if(userPlaylistMap.containsKey(userPlayList)){
                List<Playlist> userPlayList2 = userPlaylistMap.get(userPlayList);
                userPlayList2.add(playlist);
                userPlaylistMap.put(userPlayList,userPlayList2);
            }else{
                List<Playlist> plays = new ArrayList<>();
                plays.add(playlist);
                userPlaylistMap.put(userPlayList,plays);
            }

            return playlist;
        }
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {

        User Listener = null;
        for (User user1:users){
            if (user1.getMobile() == mobile){
                Listener = user1;
                break;
            }
        }
        if (Listener == null){
            throw new Exception("Listener does not exist");
        }
        else {
            Playlist playlist = new Playlist();
            playlist.setTitle(title);
            playlists.add(playlist);

            List<Song> playListByName = new ArrayList<>();
            for(Song song:songs){
                if(songTitles.contains(song.getTitle())){
                    playListByName.add(song);
                }
            }
            playlistSongMap.put(playlist,playListByName);

            List<User> list = new ArrayList<>();
            list.add(Listener);
            playlistListenerMap.put(playlist,list);

            creatorPlaylistMap.put(Listener,playlist);

            if(userPlaylistMap.containsKey(Listener)){
                List<Playlist> userPlayList = userPlaylistMap.get(Listener);
                userPlayList.add(playlist);
                userPlaylistMap.put(Listener,userPlayList);
            }
            else{
                List<Playlist> plays = new ArrayList<>();
                plays.add(playlist);
                userPlaylistMap.put(Listener,plays);
            }

            return playlist;
        }
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {

        User user = null;
        for(User user1:users){
            if(user1.getMobile()==mobile){
                user=user1;
                break;
            }
        }
        if(user==null)
            throw new Exception("User does not exist");

        Playlist playlist = null;
        for(Playlist playlist1:playlists){
            if(playlist1.getTitle()==playlistTitle){
                playlist=playlist1;
                break;
            }
        }
        if(playlist==null)
            throw new Exception("Playlist does not exist");

        if(creatorPlaylistMap.containsKey(user))
            return playlist;

        List<User> listener = playlistListenerMap.get(playlist);
        for(User user1:listener){
            if(user1==user)
                return playlist;
        }

        listener.add(user);
        playlistListenerMap.put(playlist,listener);

        List<Playlist> playlists1 = userPlaylistMap.get(user);
        if(playlists1 == null){
            playlists1 = new ArrayList<>();
        }
        playlists1.add(playlist);
        userPlaylistMap.put(user,playlists1);

        return playlist;
    }

//    public Song likeSong(String mobile, String songTitle) throws Exception {
//
//        User user = null;
//        Song newSong=new Song();
//
//            return newSong;
//
//    }
//
//    public String mostPopularArtist() {
//
//        return "";
//    }
}