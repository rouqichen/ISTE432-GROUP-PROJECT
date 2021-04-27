package com.group.MediaLibrary.data;

import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Database connection to PostgreSQL Database
 *
 * @version 2021.04.07
 */
public class PostgreSQLDatabase {

    //genericized error messages
    private static final String EXCEPTIONMESSAGE = "Failure to perform operation";
    private static final String SQLEXCEPTIONMESSAGE = "SQL Engine Failure";
    private static final String username = "root";
    private static final String password = "student";
    private static final String dbServer = "localhost";
    private static final String dbName = "medialibrary";

    // attributes
    private Connection connection; //Database Connection Object
    private Boolean inTransaction; //whether or not we are currently in a transaction

    public PostgreSQLDatabase() {
        inTransaction = false;
    }

    /**
     * Open connection to database
     *
     * @return If connection was successful
     * @throws DataLayerException
     */
    public boolean connect() throws DataLayerException {
        //if in transaction, CAN NOT connect
        if ( inTransaction ) {
            return false;
        }

        //jdbc:postgresql://<server>/<database>?user=<user>&password=<password>"
        String connectionURI =
                "jdbc:postgresql://"
                        + dbServer + ":5432"
                        + "/" + dbName
                        + "?user=" + username
                        + "&password=" + password
                        // disable SSL to suppress the warnings, since we don't have a self-signed certificate for localhost
                        + "&ssl=false";

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(connectionURI);
        } catch(Exception e) {
            throw new DataLayerException(e, EXCEPTIONMESSAGE);
        }

        return true; // connection successful
    }

    /**
     * Close database connection
     *
     * @return boolean If connection was closed successfully
     */
    public boolean close() throws DataLayerException {

        //if in transaction, CAN NOT close
        if ( inTransaction ) {
            return false;
        }

        try {
            connection.close();
        } catch (Exception e) {
            throw new DataLayerException(e, EXCEPTIONMESSAGE);
        }
        return true; //closed successfully
    }

    /**
     * Perform a SELECT operation on the database
     *
     * @param query SQL string containing a query
     * @return ArrayList<ArrayList<String>> 2D ArrayList containing the query results
     */
    public ArrayList<ArrayList<String>> getData(String query) throws DataLayerException {

        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        ResultSet rs;


        try {
            Statement statement = connection.createStatement();
            rs = statement.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int numCols = rsmd.getColumnCount();

            //while there are more rows, add row to result
            while(rs.next()) {
                ArrayList<String> row = new ArrayList<String>();

                //process row
                for(int i = 1; i <= numCols; i++) {
                    String res = rs.getString(i);
                    row.add(res);
                }

                result.add(row);
            }

        } catch (Exception e) {
            throw new DataLayerException(e, EXCEPTIONMESSAGE);
        }

        return result;

    }

    /**
     * Perform a SELECT operation on the database
     *
     * @param query SQL string containing a query
     * @param includeHeader Whether or not to include metadata
     * @return ArrayList<ArrayList<String>> 2D ArrayList containing the query results
     */
    public ArrayList<ArrayList<String>> getData(String query, boolean includeHeader) throws DataLayerException{
        //use default method for no header
        if ( !includeHeader ) {
            return getData(query);
        }

        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        ResultSet rs;

        try {
            Statement statement = connection.createStatement();
            rs = statement.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int numCols = rsmd.getColumnCount();

            //first row is field names
            ArrayList<String> names = new ArrayList<String>();
            for(int i = 1; i <= numCols; i++) {
                names.add(rsmd.getColumnLabel(i));
            }
            result.add(names);

            //second row is field widths
            ArrayList<String> widths = new ArrayList<String>();
            for(int i = 1; i <= numCols; i++) {
                widths.add(Integer.toString(rsmd.getPrecision(i)));
            }
            result.add(widths);

            //while there are more rows, add row to result
            while(rs.next()) {
                ArrayList<String> row = new ArrayList<String>();

                //process row
                for(int i = 1; i <= numCols; i++) {
                    String res = rs.getString(i);
                    row.add(res);
                }

                result.add(row);
            }

        } catch (Exception e) {
            throw new DataLayerException(e, EXCEPTIONMESSAGE);
        }

        return result;

    }

    /**
     * Perform a SELECT operation on the database
     *
     * @param query SQL string containing a query
     * @param vals List of values to query with
     * @return ArrayList<ArrayList<String>> 2D ArrayList containing the query results
     */
    public ArrayList<ArrayList<String>> getData(String query, List<String> vals) throws DataLayerException {

        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        ResultSet rs;
        ResultSetMetaData rsmd;
        int numCols;

        try {
            //prepare statement
            PreparedStatement statement = prepare(query);

            //insert each string value
            for (int i = 0; i < vals.size(); i++) {
                statement.setString(i+1, vals.get(i));
            }

            //perform query
            rs = statement.executeQuery();
            rsmd = rs.getMetaData();
            numCols = rsmd.getColumnCount();

            //add results to ArrayList
            while (rs.next()) {
                ArrayList<String> row = new ArrayList<String>();

                //process row
                for (int i = 1; i <= numCols; i++) {
                    row.add(rs.getString(i));
                }

                result.add(row);
            }

        } catch (SQLException sqle) {
            throw new DataLayerException(sqle, SQLEXCEPTIONMESSAGE);
        } catch (Exception e) {
            throw new DataLayerException(e, EXCEPTIONMESSAGE);
        }

        return result;
    }

    /**
     *
     * @param updateString SQL String with the DB operation to be performed
     * @return Number of rows of data affected
     */
    public int setData(String updateString) throws DataLayerException {

        int numAffected = -1;

        try {
            Statement statement = connection.createStatement();
            numAffected = statement.executeUpdate(updateString);
        } catch (Exception e) {
            throw new DataLayerException(e, EXCEPTIONMESSAGE);
        }

        return numAffected;

    }

    /**
     *
     * @param updateString SQL String with the DB operation to be performed
     * @return Number of rows of data affected
     */
    public int setData(String updateString, List<String> vals) throws DataLayerException {

        return executeStatement(updateString, vals);

    }

    /**
     * @param SQLString SQL String to turn into a prepared statement
     * @return PreparedStatement of passed SQL String
     */
    private PreparedStatement prepare(String SQLString) throws DataLayerException {

        try {
            return connection.prepareStatement(SQLString);
        } catch (SQLException sqle) {
            throw new DataLayerException(sqle, EXCEPTIONMESSAGE);
        }

    }

    /**
     *
     */
    private int executeStatement(String SQLString, List<String> vals) throws DataLayerException {
        int numAffected = -1;

        try {
            PreparedStatement statement = prepare(SQLString);

            //insert each string value
            for(int i = 1, j = 0; j < vals.size(); i++, j++) {
                if(vals.get(j).equalsIgnoreCase("null")) {
                    j++;
                    statement.setNull(i, Integer.parseInt(vals.get(j)));
                } else {
                    statement.setString(i, vals.get(j));
                }
            }

            numAffected = statement.executeUpdate();

        } catch (SQLException sqle) {
            throw new DataLayerException(sqle, SQLEXCEPTIONMESSAGE);
        }catch (Exception e) {
            throw new DataLayerException(e, EXCEPTIONMESSAGE);
        }

        return numAffected;
    }

    /**
     * Start a transaction
     */
    public void startTransaction() throws DataLayerException {
        inTransaction = true;

        try {

            connection.setAutoCommit(false);

        } catch (SQLException sqle) {
            throw new DataLayerException(sqle, SQLEXCEPTIONMESSAGE);
        }
    }

    /**
     * End a transaction by committing
     */
    public void commitTransaction() throws DataLayerException {

        try {
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException sqle) {
            throw new DataLayerException(sqle, SQLEXCEPTIONMESSAGE);
        }

        inTransaction = false;
    }

    /**
     * End a transaction by rolling back
     */
    public void rollbackTransaction() throws DataLayerException {

        try {
            connection.rollback();
            connection.setAutoCommit(true);
        } catch (SQLException sqle) {
            throw new DataLayerException(sqle, SQLEXCEPTIONMESSAGE);
        }

        inTransaction = false;

    }

    /**
     * Helper to format java Date objects for SQL
     * @param date
     * @return
     */
    public static String formatDate(GregorianCalendar date) {
        return String.format("%s-%s-%s", date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
    }

}
