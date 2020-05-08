import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Recommender {
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

    static void run(int userIDOfInterest){
        connect();
        try{
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM UserVector;";
            ResultSet rs = stmt.executeQuery(sql);
            List<User> points = new ArrayList<>();

            while(rs.next()){
                String vectorString = rs.getString("vector");
                int userID = rs.getInt("user_id");
                vectorString = vectorString.substring(1,vectorString.length()-1);
                String[] vectorValues = vectorString.split(",");
                double[] vector = new double[155];

                for(int i=0; i<155; i++){
                    vector[i] = Double.parseDouble(vectorValues[i]);
                }

                points.add(new User(userID,vector));
            }

            int numberOfClusters = points.size()/5;

            ArrayList<User> usersInCluster = KMeans.calculate(points, numberOfClusters, 10000, 0, 1, 155,userIDOfInterest);
            ArrayList<Integer> foodCodesUserMayLike = new ArrayList<>();

            for(User user : usersInCluster){
                try{
                    String selectFoods = "SELECT food_code FROM FoodSetNut WHERE user_id = "+ user.getUserID() + ";";
                    ResultSet foodSet = stmt.executeQuery(selectFoods);

                    while(foodSet.next()){
                        foodCodesUserMayLike.add(foodSet.getInt(1));
                        System.out.println("added food code: " + foodSet.getInt(1));
                    }
                } catch (SQLException e){
                    System.out.println("failed to retrieve food set for user " + user.getUserID());
                }
            }
        } catch (SQLException e){
            System.out.println("E");
        }
    }
}
