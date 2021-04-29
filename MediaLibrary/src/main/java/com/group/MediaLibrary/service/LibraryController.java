package com.group.MediaLibrary.service;

import com.group.MediaLibrary.business.Library;
import com.group.MediaLibrary.business.Media;
import com.group.MediaLibrary.business.Movie;
import com.group.MediaLibrary.business.TvShow;
import com.group.MediaLibrary.service.request.MovieRequest;
import com.group.MediaLibrary.service.request.ShowRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;

@RestController
public class LibraryController {

    @GetMapping("/library")
    public ArrayList<Media> getFullLibrary(@RequestParam(value = "userToken") int userToken) {

        Library library = new Library(userToken);
        library.loadLibrary();
        return library.getLibrary();

    }

    @GetMapping("/library/search/genre")
    public ArrayList<Media> getLibraryByGenre(
            @RequestParam(value = "userToken") int userToken,
            @RequestParam(value = "genre") String genre
    ) {

        Library library = new Library(userToken);
        library.loadLibraryByGenre(genre);
        return library.getLibrary();

    }

    @GetMapping("/library/search/title")
    public ArrayList<Media> getLibraryByTitle(
            @RequestParam(value = "userToken") int userToken,
            @RequestParam(value = "title") String title
    ) {

        Library library = new Library(userToken);
        library.loadLibraryByTitle(title);
        return library.getLibrary();

    }

    @GetMapping("/library/search/length")
    public ArrayList<Media> getLibraryByTitle(
            @RequestParam(value = "userToken") int userToken,
            @RequestParam(value = "max", defaultValue = "" + Integer.MAX_VALUE) int maxLength,
            @RequestParam(value = "mix", defaultValue = "0") int minLength
    ) {

        Library library = new Library(userToken);
        library.loadLibraryByLength(minLength, maxLength);
        return library.getLibrary();

    }

    @PostMapping(value = "/library/movie", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String saveMovieToLibrary(@RequestBody MovieRequest movieRequest) {

        Library library = new Library(movieRequest.getUserToken());

        if(movieRequest.getMovieid() < 1) {
            //not an existing movie
            Movie movie = new Movie(movieRequest.getMovieid());

            movie.setTitle(movieRequest.getTitle());
            movie.setRuntime(movieRequest.getRuntime());
            movie.setMpaaRating(movieRequest.getMpaaRating());
            movie.setGenres(movieRequest.getGenres());
            movie.setRelease(Date.valueOf(movieRequest.getRelease()));
            movie.setImage(movieRequest.getImage());
            movie.setDescription(movieRequest.getDescription());

            if (library.saveNewMovie(movie, movieRequest.getLocation())) {
                return "\"success\":\"" + movie.getTitle() + " has been added to your library\"";
            } else {
                return "\"error\":\"Error saving " + movieRequest.getTitle() + " to your library\"";
            }
        //adding a known movie already in database
        } else {
            if (library.saveExistingMovie(new Movie(movieRequest.getMovieid()), movieRequest.getLocation())) {
                return "\"success\":\"" + movieRequest.getTitle() + " has been added to your library\"";
            } else {
                return "\"error\":\"Error saving " + movieRequest.getTitle() + " to your library\"";
            }
        }
    }

    @PostMapping(value = "/library/show", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String saveShowToLibrary(@RequestBody ShowRequest showRequest) {

        Library library = new Library(showRequest.getUserToken());

        if(showRequest.getShowId() < 1) {
            //not an existing show
            TvShow show = new TvShow(showRequest.getShowId());

            show.setTitle(showRequest.getTitle());
            show.setEpisodeLength(showRequest.getEpisodeLength());
            show.setTvRating(showRequest.getTvRating());
            show.setGenres(showRequest.getGenres());
            show.setRelease(Date.valueOf(showRequest.getRelease()));
            show.setImage(showRequest.getImage());
            show.setDescription(showRequest.getDescription());

            if (library.saveNewShow(show, showRequest.getLocation())) {
                return "\"success\":\"" + show.getTitle() + " has been added to your library\"";
            } else {
                return "\"error\":\"Error saving " + showRequest.getTitle() + " to your library\"";
            }
            //adding a known show already in database
        } else {
            if (library.saveExistingShow(new TvShow(showRequest.getShowId()), showRequest.getLocation())) {
                return "\"success\":\"" + showRequest.getTitle() + " has been added to your library\"";
            } else {
                return "\"error\":\"Error saving " + showRequest.getTitle() + " to your library\"";
            }
        }
    }

}
