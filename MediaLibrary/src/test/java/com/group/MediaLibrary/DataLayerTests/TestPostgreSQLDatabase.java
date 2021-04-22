package com.group.MediaLibrary.DataLayerTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;

import com.group.MediaLibrary.data.PostgreSQLDatabase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.ArrayList;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestPostgreSQLDatabase {

    @InjectMocks private PostgreSQLDatabase database;
    @Mock private Connection mockConnection;
    @Mock private Statement mockStatement;
    @Mock private PreparedStatement mockPreparedStatement;
    @Mock private ResultSet mockResultSet;
    @Mock private ResultSetMetaData mockResultSetMetaData;

    @BeforeAll
    public void prepareTest() throws Exception {
        MockitoAnnotations.openMocks(this);

        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockStatement.executeQuery(Mockito.anyString())).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.getMetaData()).thenReturn(mockResultSetMetaData);
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

    }

    @Test
    void testGetData() throws Exception {

        Mockito.when(mockResultSetMetaData.getColumnCount()).thenReturn(2);
        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(mockResultSet.getString(Mockito.anyInt())).thenReturn("Test Data");

        ArrayList<ArrayList<String>> arrayList2d = database.getData("SELECT * FROM mocked");

        assertEquals(2, arrayList2d.size());
        assertEquals("Test Data", arrayList2d.get(1).get(1));

    }

    @Test
    void testGetDataWithHeader() throws Exception {

        Mockito.when(mockResultSetMetaData.getColumnCount()).thenReturn(2);
        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(mockResultSet.getString(Mockito.anyInt())).thenReturn("Test Data");
        Mockito.when(mockResultSetMetaData.getPrecision(Mockito.anyInt())).thenReturn(5);
        Mockito.when(mockResultSetMetaData.getColumnLabel(Mockito.anyInt())).thenReturn("Col1").thenReturn("Col2");

        ArrayList<ArrayList<String>> arrayList2d = database.getData("SELECT * FROM mocked", true);

        for(ArrayList<String> list: arrayList2d) {
            for(String data: list) {
                System.out.print(data + ", ");
            }
            System.out.println();
        }

        assertEquals(4, arrayList2d.size());
        assertEquals("Test Data", arrayList2d.get(2).get(1));
        assertEquals("Col1", arrayList2d.get(0).get(0));
        assertEquals("Col2", arrayList2d.get(0).get(1));

    }

    @Test
    void testGetDataWithPreparedStatement() throws Exception {
        Mockito.when(mockResultSetMetaData.getColumnCount()).thenReturn(2);
        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(mockResultSet.getString(Mockito.anyInt())).thenReturn("Test Data");

        ArrayList<String> vals = new ArrayList<>();
        vals.add("Test");

        ArrayList<ArrayList<String>> arrayList2d = database.getData("SELECT * FROM mocked WHERE column = ?", vals);

        assertEquals(2, arrayList2d.size());
        assertEquals("Test Data", arrayList2d.get(1).get(1));
    }

    @Test
    void testExecuteStatement() throws Exception {

        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        ArrayList<String> vals = new ArrayList<>();
        vals.add("Test");
        vals.add("Test 2");

        int numAffected = database.setData("INSERT INTO mocked VALUES (?)", vals);

        assertEquals(1, numAffected);
        Mockito.verify(mockPreparedStatement, times(2)).setString(Mockito.anyInt(), Mockito.anyString());
    }


}
