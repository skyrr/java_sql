import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Dispatcher {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public static void main(String[] args) throws Exception {
        Dispatcher dao = new Dispatcher();
        dao.readDataBase();
    }

    public void readDataBase() throws Exception {
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            String password = "";
            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost/java_db?"
                            + "user=root&password=" + password + "");

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query
            resultSet = statement
                    .executeQuery("select * from java_db.comments");
            writeResultSet(resultSet);

            // PreparedStatements can use variables and are more efficient
            preparedStatement = connect
                    .prepareStatement("insert into  java_db.comments values (default, ?, ?, ?, ? , ?, ?)");
            // "myuser, webpage, datum, summary, COMMENTS from feedback.comments");
            // Parameters start with 1
            preparedStatement.setString(1, "MyTest");
            preparedStatement.setString(2, "TestEmail");
            preparedStatement.setString(3, "TestWebpage");
            preparedStatement.setDate(4, new java.sql.Date(2009, 11, 11));
            preparedStatement.setString(5, "TestSummary");
            preparedStatement.setString(6, "TestComment");
            preparedStatement.executeUpdate();

            preparedStatement = connect
                    .prepareStatement("SELECT myuser, webpage, datum, summary, COMMENTS from java_db.comments");
            resultSet = preparedStatement.executeQuery();
            writeResultSet(resultSet);

            // Remove again the insert comment
            preparedStatement = connect
                    .prepareStatement("delete from java_db.comments where myuser= ? ; ");
            preparedStatement.setString(1, "MyTest");
            preparedStatement.executeUpdate();

            resultSet = statement
                    .executeQuery("select * from java_db.comments");
            writeMetaData(resultSet);
            //////////////////////////////////////
            preparedStatement = connect
                    .prepareStatement("insert into  java_db.comments values (default, ?, ?, ?, ? , ?, ?)");
            // "myuser, webpage, datum, summary, COMMENTS from feedback.comments");
            // Parameters start with 1
            preparedStatement.setString(1, "MyTest");
            preparedStatement.setString(2, "TestEmail");
            preparedStatement.setString(3, "TestWebpage");
            preparedStatement.setDate(4, new java.sql.Date(new java.util.Date().getTime()));
            preparedStatement.setString(5, "TestSummary");
            preparedStatement.setString(6, "TestComment");
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }

    }

    private void writeMetaData(ResultSet resultSet) throws SQLException {
        //         Now get some metadata from the database
        // Result set get the result of the SQL query

        System.out.println("The columns in the table are: ");

        System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
        for  (int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++){
            System.out.println("Column " +i  + " "+ resultSet.getMetaData().getColumnName(i));
        }
    }

    private void writeResultSet(ResultSet resultSet) throws SQLException {
        // ResultSet is initially before the first data set
        while (resultSet.next()) {
            // It is possible to get the columns via name
            // also possible to get the columns via the column number
            // which starts at 1
            // e.g. resultSet.getSTring(2);
            String user = resultSet.getString("myuser");
            String website = resultSet.getString("webpage");
            String summary = resultSet.getString("summary");
            Date date = resultSet.getDate("datum");
            String comment = resultSet.getString("comments");
            System.out.println("User: " + user);
            System.out.println("Website: " + website);
            System.out.println("summary: " + summary);
            System.out.println("Date: " + date);
            System.out.println("Comment: " + comment);
        }
    }

    // You need to close the resultSet
    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {

        }
    }

}