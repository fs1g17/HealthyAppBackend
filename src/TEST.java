import java.sql.*;

public class TEST {

    static Connection connect() {
        Connection conn = null;
        String url = "jdbc:sqlite:C:/Users/PEEPEEPOOPOO/Documents/Databases/test.db";

        try {
            conn = DriverManager.getConnection(url);
            System.out.println("connection established successfully!");
        } catch (SQLException e) {
            System.out.println("coudlnt connect to the database!!!" + e.getMessage());
        }
        return conn;
    }
    public static void main(String[] args){
        Connection conn = connect();
        if(conn != null){
            Statement stmt = null;
            ResultSet rs = null;

            try{
                stmt = conn.createStatement();
            } catch(SQLException e){
                System.out.println("failed to create statement");
            }
            String sql = "SELECT MAX(user_id) FROM FoodDiary;";
            try{
                rs = stmt.executeQuery(sql);

                int answer = rs.getInt(1);
                System.out.println(answer);
            } catch(SQLException e){
                System.out.println("failed to execute query");
            }


        } else{
            System.out.println("FAIL");
        }

    }
}
