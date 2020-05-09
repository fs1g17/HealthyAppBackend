import java.io.IOException;
import java.sql.*;

public class VectorGenerator {
    static Connection conn;

    static void connect() {
        String url = "jdbc:sqlite:/usr/local/tomcat/db/test.db";

        try {
            conn = DriverManager.getConnection(url);
            System.out.println("connection established successfully!");
        } catch (SQLException e) {
            System.out.println("coudlnt connect to the database!!!" + e.getMessage());
        }
    }

    //generates a feature vector for a user ID based on ALL inputs in DateFoodSetNut
    public static void generateFeatureVectorForUser(int userID){
        connect();
        try{
            FNDDSAccess FNDDS = new FNDDSAccess();
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM DateFoodSetNut WHERE user_id = " + userID + ";";
            ResultSet rs = stmt.executeQuery(sql);

            int[] vector = new int[155];
            for(int i=0; i<vector.length; i++){
                vector[i] = 0;
            }

            while(rs.next()){
                String[] codesForFoodItem = FNDDS.custom(rs.getString("food_item"));
                int WWEIACode = Integer.parseInt(codesForFoodItem[2]);
                vector[WWEIAVector.getIndex(WWEIACode)] = 1;
            }

            String vectorString = "[";
            for(int i=0; i<vector.length-1; i++){
                vectorString += vector[i]+",";
            }
            vectorString += vector[vector.length-1]+"]";
            String insert = "INSERT INTO UserVector VALUES (" + userID + ",'" + vectorString + "');";
            stmt.executeUpdate(insert);
        } catch (SQLException e){
            System.out.println("SQLException " + e.getMessage());
        } catch (IOException e){
            System.out.println("IOException " + e.getMessage());
        }
    }

    //this method generates vector from FoodSet to date - this requires the calculation of FoodSet which could be done once in a while
    public static void generateFeatureVectorForUserFromFoodSet(int userID){
        connect();
        try{
            FNDDSAccess FNDDS = new FNDDSAccess();
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM FoodSetNut WHERE user_id = " + userID + ";";
            ResultSet rs = stmt.executeQuery(sql);

            int[] vector = new int[155];
            for(int i=0; i<vector.length; i++){
                vector[i] = 0;
            }

            while(rs.next()){
                String[] codesForFoodItem = FNDDS.custom(rs.getString("food_item"));
                int WWEIACode = Integer.parseInt(codesForFoodItem[2]);
                vector[WWEIAVector.getIndex(WWEIACode)] = 1;
            }

            String vectorString = "[";
            for(int i=0; i<vector.length-1; i++){
                vectorString += vector[i]+",";
            }
            vectorString += vector[vector.length-1]+"]";
            String insert = "INSERT INTO UserVector VALUES (" + userID + ",'" + vectorString + "');";
            stmt.executeUpdate(insert);
        } catch (SQLException e){
            System.out.println("SQLException " + e.getMessage());
        } catch (IOException e){
            System.out.println("IOException " + e.getMessage());
        }
    }
}
