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

Data layer exceptions are passed up to the business layer. The business layer handles exceptions and passes/returns only relevant/genericized fail messages to the presentation layer/user. 
Presentation layer exceptions are handled immediately and are not shown directly to the user.

Example below:

### Data Layer Class

```java
import java.sql.*;
import javax.swing.*;
import java.awt.*;


class DataLayerClass{
   private final String URI;
   private final String DRIVER;
   public  String USER      = new String("root");
   private String password  = new String("student");
   public  String database  = new String("search");
   private Connection conn;
   private Statement stmt;
   private ResultSet rs;


public DataLayerClass() {

       URI = "jdbc:mysql://localhost/student";

       DRIVER   = "com.mysql.cj.jdbc.Driver";
       USER     = "root";
       password = "student";

}//end of Constructor



   public boolean connect() {
      conn = null;
      try  {
         Class.forName(DRIVER);
      }
      catch (ClassNotFoundException cnfe) {
		 cnfe.printStackTrace();

         return false;
      }// end of catch

      try  {
         conn = DriverManager.getConnection(URI, USER, password);
         return true;
      }
      catch(SQLException sqle) {
         sqle.printStackTrace();
         return false;
      }//end of catch
   }//end of connect

   public boolean close() {
      boolean valid = true;
      try {
         if(conn!=null) conn.close();
      }
      catch(SQLException sqle) {
         sqle.printStackTrace();
      }//end of catch
      return true;
   }//end of close
   
   
   /*
    * insert data into user 
    */
   public int adduser(String sql1) {
      int cd = 0;
      try {
         Statement stmt = conn.createStatement();
         int rs = stmt.executeUpdate(sql1);
         //record update
         cd = rs;
         
      }
      catch(SQLException sqle){
         cd = -1;
         sqle.printStackTrace();
      }
   
      return cd;
   }// End of method to Delete
}
```

### BusinessLayerClass
```java
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
    
     public void updateMovieStatement(string movieName) throws Exception {

        try {


            Statement statement = con.connect().createStatement();

            ResultSet rs = statement.executeQuery("select * from updateMovieStatement where moviename = movieName");

            while (rs.next()) {
                // get the filepath of the PDF document
                String path1 = rs.getString(2);
                int getNum= rs.getInt(1);
                // while running the process, update status : Processing
                updateProcess_DB(getNum);

               // call the index function
                
                conn.extractDocuments(path1);
                // After completing the process, update status: Complete
               updateComplete_DB(getNum);

             // if error occurs 
// call this method updateError_DB(getNum);


                }


        }catch(SQLException|IOException e){
            e.printStackTrace();

        }

public void addMovie(int _id, String _movieName){
      
      String MovieName;
      
         int rows = 0;
         System.out.println("-----INSERT started-----");
         try{
            stmt = conn.createStatement();
            String sql = "INSERT INTO media(id, MovieName) VALUES (_id, '" + MovieName  + "');";
            System.out.println("Command to be executed: " + sql);
            rows = stmt.executeUpdate(sql);
            System.out.println("-----INSERT finished-----");
         }//try
         catch(SQLException sqle){
            System.out.println("INSERT FAILED!!!!");
            System.out.println("ERROR MESSAGE IS -> "+sqle);
            sqle.printStackTrace();
           
         }
         
      }//end of addMovie
      
      public void addTV(int _id, String _tvName){
      
      String MovieName;
      
         int rows = 0;
         System.out.println("-----INSERT started-----");
         try{
            stmt = conn.createStatement();
            String sql = "INSERT INTO media(id, TvShowName) VALUES (_id, '" + _tvName  + "');";
            System.out.println("Command to be executed: " + sql);
            rows = stmt.executeUpdate(sql);
            System.out.println("-----INSERT finished-----");
         }//try
         catch(SQLException sqle){
            System.out.println("INSERT FAILED!!!!");
            System.out.println("ERROR MESSAGE IS -> "+sqle);
            sqle.printStackTrace();
           
         }
         
      }//end of addTvShow
      
      public void addUser(int _id, String _userName){
      
      String MovieName;
      
         int rows = 0;
         System.out.println("-----INSERT started-----");
         try{
            stmt = conn.createStatement();
            String sql = "INSERT INTO media(id, UserName) VALUES (_id, '" + _userName  + "');";
            System.out.println("Command to be executed: " + sql);
            rows = stmt.executeUpdate(sql);
            System.out.println("-----INSERT finished-----");
         }//try
         catch(SQLException sqle){
            System.out.println("INSERT FAILED!!!!");
            System.out.println("ERROR MESSAGE IS -> "+sqle);
            sqle.printStackTrace();
           
         }
         
      }//end of addUser


    }
    
}
```
### Presentation Layer
```java
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

public class PresentationLayer extends JFrame {
      
   Register rg = new Register(); 
   Login lg = new Login();
      
   JPanel north = new JPanel();
   JPanel jp1 = new JPanel();
   JPanel jp2 = new JPanel();
   JPanel jp3 = new JPanel();
      

   //text fields
   JTextField jtf = new JTextField(20);
   JPasswordField jtf2 = new JPasswordField(20);
   JTextField name = new JTextField(null,12);
   JPasswordField word = new JPasswordField(null,12);

   //Panels
   JPanel northPanel = new JPanel();
   JPanel centerPanel = new JPanel();
   
   //Labels
   JLabel username = new JLabel("Username:");
   JLabel password = new JLabel("Password:");
   
   //JButtons
   JButton register = new JButton("Register");
   JButton login = new JButton("Login");

   //IO 
   File text = new File("Text.txt");
   File file = new File("Registered_list.csv");
   BufferedReader br = null;
   FileWriter fw = null;
   //int and Array 
   int line = 0;
   String a[];


      
      /*
       * class client
       * sets gui and the null password bubbles
       */
   public PresentationLayer(){
      
      setLocationRelativeTo(null);
         
   
      word.setEchoChar('\u2022');
      add(northPanel, BorderLayout.NORTH);
      northPanel.setLayout(new GridLayout(3,2));
      northPanel.add(username);
      northPanel.add(name);
      northPanel.add(password);
      northPanel.add(word);
      username.setHorizontalAlignment(SwingConstants.RIGHT);
      password.setHorizontalAlignment(SwingConstants.RIGHT);
   
   
         
      add(centerPanel, BorderLayout.CENTER);
      centerPanel.setLayout(new FlowLayout());
      centerPanel.add(register);
      centerPanel.add(login);
         
      register.addActionListener(rg);
      login.addActionListener(lg);
   
      pack();
      setDefaultCloseOperation(HIDE_ON_CLOSE);
      setTitle("ISTE 432");
      setVisible(true); 
         
      
   }  
      

      
  /**
    * main, creates an Instance of the class and the GUI
    */   
   public static void main(String [] args){
      new PresentationLayer();
   }

  /**
    * Sub class, handles all the user and password registering.
    **/ 
   class Register implements ActionListener
   {
      public void actionPerformed(ActionEvent ae)
      {
         if(ae.getActionCommand().equals("Register"))
         {
            line=0;
            int num = 0;
            try{
               if(file.exists() == true)
               {
                  if(name.getText().equals(""))
                  {JOptionPane.showMessageDialog(null, "Username can't be blank!");}
                  else if(word.getText().equals(""))
                  {JOptionPane.showMessageDialog(null, "Password can't be blank!");}
                  else{
                     fw =new FileWriter(file,true);// when the file is already exists
                     br = new BufferedReader(new FileReader(file));// create a bufferedreader
                     while (br.readLine() != null)//read line
                     {
                        line++;  
                     }
                     a=new String[line];
                     br = new BufferedReader(new FileReader(file));// create a bufferedreader
                     for(int i = 0;i<(line);i++)
                     {
                        a[i] = br.readLine();
                     }
                  
                     for(int i=0;i<line;i++)
                     {
                        if(a[i].equals(name.getText()))
                        {JOptionPane.showMessageDialog(null, "Username registered!");  
                           num=-1;
                           break;
                        }
                        else{
                           i++;}
                     }
                     if(num == -1)
                     {}
                     else{ 
                     //call method in here
                        String user = getSHA(name.getText());
                        String pass = getSHA(word.getText());
                        fw.write("\n"+user);
                        fw.write("\n"+pass);
                        fw.flush();
                        fw.close();
                        name.setText(null);
                        word.setText(null);
                        JOptionPane.showMessageDialog(null, "Username register success!");
                     
                     }
                   
                  
                  }
               
                
               }//if file.exists == true
               else
               {  
                  if(name.getText().equals(null))
                  {JOptionPane.showMessageDialog(null, "Username can't be blank!");}
                  else if(word.getText().equals(null))
                  {JOptionPane.showMessageDialog(null, "Password can't be blank!");}
                  else{
                  
                     /**writing the user and password, hashed so not in plain
                       * text to the CSV file
                       **/
                     fw=new FileWriter(file);
                     String user = getSHA(name.getText());
                     String pass = getSHA(word.getText());
                     fw.write("\n"+user);
                     fw.write("\n"+pass);
                     fw.flush();
                     fw.close();
                  
                     name.setText(null);
                     word.setText(null);
                     JOptionPane.showMessageDialog(null, "Username register success!");
                  
                  }
               }//else
            }//try
            catch(FileNotFoundException fe)
            {
               JOptionPane.showMessageDialog(null, "Missing file or no data to read.");//file not found exception
            
            
            }
            catch(IOException ioe)
            {
               JOptionPane.showMessageDialog(null, "ERROR!");//IOexception
            
            }
         }
      }
   }//end of class register
   
   /**
    * Sub class, handles the login check, creates the file and reads it in
    */
   class Login implements ActionListener
   {
      int allow;
      public void actionPerformed(ActionEvent ae)
      {
         
         if(ae.getActionCommand().equals("Login"))
         {
            try{
               int num = 0;
               line=0;
            
               fw =new FileWriter(file,true);// when the file is already exists
               br = new BufferedReader(new FileReader(file));// create a bufferedreader
               while (br.readLine() != null)//read line
               {
                  line++;  
               }
               a=new String[line];
               br = new BufferedReader(new FileReader(file));// create a bufferedreader
               for(int i = 0;i<(line);i++)
               {
                  a[i] = br.readLine();
               }
               if(name.getText().equals(""))
               {JOptionPane.showMessageDialog(null, "Username can't be blank!");}
               else if(word.getText().equals(""))
               {JOptionPane.showMessageDialog(null, "Password can't be blank!");}
               else{
                  for(int i=0;i<line;i++)
                  {
                     /**
                       *The zero-th element in the array is blank therefore we 
                       *check the first and second elements.
                       **/                     
                     if(a[i+1].equals(getSHA(name.getText())) && a[i+2].equals(
                        getSHA(word.getText())))
                     {  
                        num=1;
                        break; 
                          
                     }else{
                        i++;
                     } // end of else statement
                     
                  } // end of for loop
                  
                  if(num == 1)
                  {
                     JOptionPane.showMessageDialog(null, "Login success!");
                     setVisible(false);
                  
                  }else
                  {
                     JOptionPane.showMessageDialog(null, "Wrong username or password!");
                     setAllow(0);
                  }
               }
            
            }//try
            catch(FileNotFoundException fe)
            {
               JOptionPane.showMessageDialog(null, "Missing file or no data to read.");//file not found exception
            
            
            }
            catch(IOException ioe)
            {
               JOptionPane.showMessageDialog(null, "ERROR!");//IOexception
            
            }
         }
      
      }//actionPerformed
      public int getAllow()
      {
         return allow;
      }
      public void setAllow(int i)
      {
         allow = i;
      }      
   }//class login
        
        
        //method of hashing
        
   public static String getSHA(String input) 
   { 
   
      try { 
      
            // Static getInstance method is called with hashing SHA 
         MessageDigest md = MessageDigest.getInstance("SHA-256"); 
      
            // digest() method called 
            // to calculate message digest of an input 
            // and return array of byte 
         byte[] messageDigest = md.digest(input.getBytes()); 
      
            // Convert byte array into signum representation 
         BigInteger no = new BigInteger(1, messageDigest); 
      
            // Convert message digest into hex value 
         String hashtext = no.toString(16); 
      
         while (hashtext.length() < 32) { 
            hashtext = "0" + hashtext; 
         } 
      
         return hashtext; 
      } 
      
        // For specifying wrong message digest algorithms 
      catch (NoSuchAlgorithmException e) { 
         System.out.println("Exception thrown"
                               + " for incorrect algorithm: " + e); 
      
         return null; 
      } 
   }   
}

```
## Performance and Refactoring

The most notable refactoring to take place occured due to a change in requirements. We changed from using MySQL to using a PostgreSQL database. Thanks to the structure of the code, this affected only the database itself, it's class (MySqlDatabase.java -> PostgreSQLDatabase.java) and a single method in that class. The change can be found in the file [MediaLibrary/src/main/java/com/group/MediaLibrary/data/PostgreSQLDatabase.java at line 36](https://github.com/rouqichen/ISTE432-GROUP-PROJECT/blob/main/MediaLibrary/src/main/java/com/group/MediaLibrary/data/PostgreSQLDatabase.java#L36)

## Testing

To unit test sections of our code we are using JUnit and Mockito. JUnit is a common annotation based unit testing framework which used to easily write and run unit tests within an IDE. Mockito is a mocking framework used to compartmentalize code for the purpose of testing (i.e. a method that involves another class, rather than letting the test run the methods of both classes, mockito creates a fake version of the second class, then the method being called is mocked and returns a response specified through mockito instead). An example of this can be seen in the test class for the database. The mock is set up in a method on line 29 ([MediaLibrary/src/test/java/com/group/MediaLibrary/DataLayerTests/TestPostgreSQLDatabase.java](https://github.com/rouqichen/ISTE432-GROUP-PROJECT/blob/main/MediaLibrary/src/test/java/com/group/MediaLibrary/DataLayerTests/TestPostgreSQLDatabase.java#L29)), which mocks a number of JDBC related classes to contain the output to being dependant on only the method being tested, and to make no real calls to the database. Each unit test then specifies what methods it expects to be called on those mocked objects, and what to return when they are called (as seen in the same file at line 57).
