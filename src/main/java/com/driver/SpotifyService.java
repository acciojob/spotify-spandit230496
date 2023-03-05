package com.driver;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class SpotifyService {

    //Auto-wire will not work in this case, no need to change this and add autowire

    SpotifyRepository spotifyRepository = new SpotifyRepository();

    public User createUser(String name, String mobile){

    return spotifyRepository.createUser(name,mobile);
    }

   public Artist createArtist(String name) {
  return spotifyRepository.createArtist(name);
  }

    public Album createAlbum(String title, String artistName) {

        return spotifyRepository.createAlbum(title,artistName);
  }
//
//    public Song createSong(String title, String albumName, int length) throws Exception {
//     return spotifyRepository.createSong(name);
//    }
//
//    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
//    return spotifyRepository.createAlbum(title,artistName);
//    }
//
//    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
//   return spotifyRepository.createAlbum(title,artistName);
//    }
//
//    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
//   return spotifyRepository.createAlbum(title,artistName);
//    }
//
//    public Song likeSong(String mobile, String songTitle) throws Exception {
//    return spotifyRepository.createAlbum(title,artistName);
//    }
//
//    public String mostPopularArtist() {
//    return spotifyRepository.createAlbum(title,artistName);
//    }
//
//    public String mostPopularSong() {
//    return spotifyRepository.createAlbum(title,artistName);
//    }
}
