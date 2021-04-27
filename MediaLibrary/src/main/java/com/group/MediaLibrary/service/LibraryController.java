package com.group.MediaLibrary.service;

import com.group.MediaLibrary.business.Library;
import com.group.MediaLibrary.business.Media;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class LibraryController {

    @GetMapping("/library")
    public ArrayList<Media> getFullLibrary(@RequestParam(value = "id") int userToken) {

        Library library = new Library(userToken);
        return library.getLibrary();

    }

}
