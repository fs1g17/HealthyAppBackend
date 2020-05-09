import jdk.swing.interop.SwingInterOpUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class Recommender {
    static Connection conn;
    private int MAX_USER_ID_IN_FOOD_SET_NUT = 109;

    static void connect() {
        String url = "jdbc:sqlite:/usr/local/tomcat/db/test.db";

        try {
            conn = DriverManager.getConnection(url);
            System.out.println("connection established successfully!");
        } catch (SQLException e) {
            System.out.println("coudlnt connect to the database!!!" + e.getMessage());
        }
    }

    public JSONArray run(int userIDOfInterest, String date){
        connect();
        JSONArray foodList = null;
        try{
            FNDDSAccess FNDDS = new FNDDSAccess();
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM UserVector;";
            ResultSet rs = stmt.executeQuery(sql);
            List<User> points = new ArrayList<>();
            boolean doesFVForUserExist = false;

            System.out.println("GETTING USER FEATURE VECTORS");

            while(rs.next()){
                String vectorString = rs.getString("vector");
                int userID = rs.getInt("user_id");
                if(userID == userIDOfInterest){
                    doesFVForUserExist = true;
                }
                vectorString = vectorString.substring(1,vectorString.length()-1);
                String[] vectorValues = vectorString.split(",");
                double[] vector = new double[155];

                for(int i=0; i<155; i++){
                    vector[i] = Double.parseDouble(vectorValues[i]);
                }

                points.add(new User(userID,vector));
            }

            System.out.println("GOT USER FEATURE VECTORS");

            if(!doesFVForUserExist){
                VectorGenerator.generateFeatureVectorForUser(userIDOfInterest);
                System.out.println("FEATURE VECTOR FOR USER " + userIDOfInterest + " DOESN'T EXIST, GENERATING");
            }

            int numberOfClusters = points.size()/5;

            System.out.println("RUNNING KMEANS");
            ArrayList<User> usersInCluster = KMeans.calculate(points, numberOfClusters, 10000, 0, 1, 155,userIDOfInterest);
            System.out.println("K-MEANS RETURNED CLUSTER OF SIZE " + usersInCluster.size());
            HashSet<Integer> foodCodesUserMayLike = new HashSet<>();

            for(User user : usersInCluster){
                String selectFoods = "SELECT food_item FROM FoodSetNut WHERE user_id = " + user.getUserID() + ";";
                ResultSet foodSet = stmt.executeQuery(selectFoods);

                while (foodSet.next()){
                    String foodItem = foodSet.getString(1);
                    System.out.println("retrieved food_item: " + foodItem);
                    //match code WWEIACode
                    String[] foodCodes = FNDDS.custom(foodItem);
                    int foodCode = Integer.parseInt(foodCodes[1]);
                    System.out.println("MATCHED FOOD CODE: " + foodCode + " FROM USER " + user.getUserID());
                    foodCodesUserMayLike.add(foodCode);
                }
            }

            System.out.println("GENERATED SET OF FOODS THE USER MAY LIKE OF SIZE " + foodCodesUserMayLike.size());

            //foodItem JSONObjects have this format: {"food_code":x,"hei_score":{"cat1":val1,"cat2":val2,...,"cat13":val13}}
            JSONArray heiScoresOfFoods = HEICalculator.getHEIScoreOfFoodItemsByFoodCode(foodCodesUserMayLike);
            System.out.println("RETRIEVED HEI SCORES OF FOODS IN FOOD SET");
            HashMap<Integer,Double> avgHEI = new HashMap<>();

            for(int i=0; i<heiScoresOfFoods.length(); i++){
                JSONObject foodItem = heiScoresOfFoods.getJSONObject(i);
                int foodCode = foodItem.getInt("food_code");
                JSONObject foodHEIScore = foodItem.getJSONObject("hei_score");

                String[] components = new String[]{"total_fruits","whole_fruits","total_vegies","greens_beans",
                        "whole_grains","dairy_things","protein_food","seas_plan_pr","fatty_acids","refined_grain",
                        "estimated_sodium", "added_sugars","saturated_fats"};

                double avgHEIOfFoodItem = 0.0;
                for(String component : components){
                    avgHEIOfFoodItem += foodHEIScore.getDouble(component);
                }
                avgHEIOfFoodItem = avgHEIOfFoodItem/components.length;
                avgHEI.put(foodCode,avgHEIOfFoodItem);
            }

            HashSet<Integer> topFiveBestFoods = new HashSet<>();
            for(int i=0; i<5; i++){
                Integer bestFoodCode = 0;
                Double bestDiff = 0.0;
                for(Map.Entry<Integer,Double> entry : avgHEI.entrySet()){
                    Integer foodCode = entry.getKey();
                    Double diff = entry.getValue();
                    if(diff > bestDiff){
                        bestFoodCode = foodCode;
                        bestDiff = diff;
                    }
                }
                topFiveBestFoods.add(bestFoodCode);
                avgHEI.remove(bestFoodCode);
            }

            System.out.println("SIZE OF TOP5BESTFOODS IS " + topFiveBestFoods.size());

            foodList = FNDDS.getFoodItems(topFiveBestFoods);
            System.out.println(foodList.toString());
        } catch (SQLException e){
            System.out.println("E");
        } catch (IOException e){
            System.out.println("E");
        }
        return foodList;
    }

}
