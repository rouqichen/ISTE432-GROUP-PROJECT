# ISTE432-GROUP-PROJECT

## Team Members and Roles

- Rouqi Chen
- Liam Bewley
- Harsh Mathur
- Steve Jackling

## Background
The digital age has seen a great shift in the way we consume and store media. For many, streaming services have replaced the need to buy movies or TV shows. But there are those who still wish to own their media to view at any time regardless of current streaming licenses and which services they currently have access to. While buying content digitally can be convenient to save storage space, there are also many different storefronts and options, such as apple, amazon, google, youtube, roku, etc. Compound that with a physical library of DVDs and/or Blu-Rays and the convenience of the digital age is lost in confusion and disorganization.

## Project Description
Our project is an application to let users track and sort their full collection of owned media. A user or users from a household can enter the title of a movie or show that they own, either as a DVD, Blu-Ray, or through a digital storefront like iTunes, Amazon, or Google, enter where they own it, and IMDB can be used to fill data about it (picture, description, error check the title, etc.), then they can view everything they own and where/how they own it. Each household must create an account.

## Project Requirements

1. Users logging in with username and password
2. Users can easily add media they own to their library
3. Users can search their library
4. Digitally owned media should link to itself on the storefront the user owns it from
5. Movies they do not own can be recommended for purchase

## Business Rules

- Users must create an account/login before they can use the application
- A user must add media to their library before it can be searched
- At minimum, title and platform (where/how it is owned) must be entered for any given media

## Technologies Used

- JAVA
- MYSQL

## Design Patterns

### Template Method
The Template Method pattern will be used to reduce redundancy in saving different types of media. A master save method will exist in the Media superclass which will call component methods that can be overridden by Movie and TVShow subclasses. Common parts, such as preview images, genre, title, description, etc. can stay in the superclass. Differences, such as saving a rating that must exist either in the MPAA rating system or the FCC's TV rating system, or core details such as a movie's run time or the number of a show's seasons and episodes in each season, can be done in those subclasses overriding a method intended for that use. See below example:
[Diagram.pdf](https://github.com/rouqichen/ISTE432-GROUP-PROJECT/files/6093527/Diagram.pdf)


```java
public class Media {
    public void saveMedia() {
        saveGenres();
        savePreviewImage();
        saveTypeSpecific();
        saveRating();
    }
    
    //common
    public void saveGenres() {
        ...
    }
    
    public void savePreviewImage() {
        ...
    }
    
    //to be overridden
    public void saveTypeSpecific() {
       ...
    }
    
    public void saveRating() {
       ...
    }
}

public class Movie extends Media {
    @override
    public void saveTypeSpecific() {
        saveRuntime();
    }
    
    @override
    public void saveRating() {
        saveMPAA();
    }
}

public class TVShow extends Media {
    @override
    public void saveTypeSpecific() {
        saveSeasonsOwned();
    }
    
    @override
    public void saveRating() {
        saveTVRating();
    }
}
```

## Timeline

- Design - March 5, 2021
- Layering - March 12, 2021
- Exception Handling - March 26, 2021
- Refactoring April 9, 2021
- Testing - April 23, 2021
- Packaging - April 30, 2021
- Finalized Code - May 5, 2021

## Layers
<img width="462" alt="layer" src="https://user-images.githubusercontent.com/71988428/111248269-f6d09c80-85df-11eb-8cd3-43226cc9f755.png">


### Presentation Layer

Web Interface
Display data to the users.

- HTML/PHP/CSS
- MYSQL

### Business Layer

The business layer is responsible for core program logic. 
Media is a parent class to Movie and TVShow with code and functions that are consistent across both media types.
Library contains a list of media a user owns, as well as methods to search and sort that list.

- Movie
- TVShow
- Media
- User
- Library

### Data Layer

The data layer consists of data access objects used to retrieve data out of the database. These can post, put, get and delete rows from the database.

- MovieDAO
- TVShowDAO
- UserDAO

## Exception Handling

Data layer exceptions are passed up to the business layer. The business layer handles exceptions and passes only relevant/genericized fail messages to the presentation layer/user. Example below:

```java
public class DataLayerClass {
    public boolean connect() throws DataLayerException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(connectionURI);
        } catch(Exception e) {
            throw new DataLayerException(e, "JDBC Driver not found");
        }
    }
    
    public int setData(String updateString) throws DataLayerException {

        int numAffected = -1;

        try {
            Statement statement = connection.createStatement();
            numAffected = statement.executeUpdate(updateString);
        } catch (SQLException sqle) {
            throw new DataLayerException(sqle, "SQL error occured");
        } catch (Exception e) {
            throw new DataLayerException(e, "Error updating rows");
        }

        return numAffected;

    }
}

public class BusinessLayerClass {
    public String getMovieJSON(int id) {
        try {
            Movie movie = MovieDAO.get(id);
            String jsonString = JSONConverter.getMovie(movie);
            return jsonString;
        } catch (DataLayerException dle) {
            return "\"Error\": \"Error occured getting movie details\""
        }
    }
    
    public String getTVShowJSON(int id) {
        try {
            TvShow tvShow = tvShow.get(id);
            String jsonString = JSONConverter.getTvShow(tvShow);
            return jsonString;
        } catch (DataLayerException dle) {
            return "Error occured: Unable to grab Tv Show JSON specifications";
        }
    }
    
    public String getUserJSON(int id) {
        try {
            UserDAO user = UserDAO.get(id);
            String jsonString = JSONConverter.getUser(user);
            return jsonString;
        } catch (DataLayerException dle) {
            return "Error occured: Unable to grab user JSON specifications";
        }
    }
    
    
}
```
