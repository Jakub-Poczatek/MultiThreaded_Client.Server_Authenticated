import java.sql.*;

public class Retrieve {
    Connection connection;
    Statement statement;

    public Retrieve(){

        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Assign1", "root", "");
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public ResultSet getStudents() throws SQLException {
        return statement.executeQuery("SELECT * FROM students");
    }

    public void increaseTot_Req(int id) throws SQLException {
        ResultSet requestResultSet = statement.executeQuery("SELECT TOT_REQ FROM students WHERE STUD_ID = " + id);
        requestResultSet.next();
        int currentReq = requestResultSet.getInt("TOT_REQ");
        statement.executeUpdate("UPDATE students SET TOT_REQ = " + (currentReq + 1) + " WHERE STUD_ID = " + id);
    }
}
