package com.group.MediaLibrary.service;

import com.group.MediaLibrary.business.Library;
import com.group.MediaLibrary.business.Media;
import com.group.MediaLibrary.business.Movie;
import com.group.MediaLibrary.service.request.MovieRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class LibraryController {

    @GetMapping("/library")
    public ArrayList<Media> getFullLibrary(@RequestParam(value = "userToken") int userToken) {

        Library library = new Library(userToken);
        library.loadLibrary();
        return library.getLibrary();

    }

    @PostMapping(value = "/library/movie", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String saveMovieToLibrary(@RequestBody MovieRequest movieRequest) {

        Library library = new Library(movieRequest.getUserToken());

        if(movieRequest.getMovieid() < 1) {
            //not an existing movie
            Movie movie = new Movie();



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

}
