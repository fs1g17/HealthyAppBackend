import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Recommender {
    static void run(int userID){
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

            ArrayList<User> usersInCluster = KMeans.calculate(points, numberOfClusters, 10000, 0, 1, 155,200);

        } catch (SQLException e){
            System.out.println("E");
        }
    }
}
