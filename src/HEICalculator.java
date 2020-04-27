import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.*;

import java.util.ArrayList;
import java.util.HashMap;

public class HEICalculator {
    private static Connection conn;

    private static void connect() {
        //String url = "jdbc:sqlite:H:/sqlite/test.db";
        //String url = "jdbc:sqlite:C:/sqlite/test.db";
        String url = "jdbc:sqlite:/usr/local/tomcat/db/test.db";

        try {
            //Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
            System.out.println("connection established successfully!");
        } catch (SQLException e) {
            System.out.println("coudlnt connect to the database!!!" + e.getMessage());
        }
    }

    public static JSONObject getHEIScoreForUserByDate(int userID, String date){
        connect();
        try{
            Statement stmt = conn.createStatement();
            int user_id = userID;

            String sql = "SELECT * FROM DateFoodSetNut WHERE user_id = " + user_id + " AND date = '" + date + "';";
            ResultSet rs = stmt.executeQuery(sql);
            FNDDSAccess FNDDS = null;
            try{
                FNDDS = new FNDDSAccess();
                System.out.println("connected to the FNDDS database");
            } catch (IOException e){
                System.out.println("couldn't connect to FNDDS database");
            }

            //ArrayList<Integer> foodCodes = new ArrayList<>();
            HashMap<Integer,int[]> foodCodesAndNutValue = new HashMap<>();
            ArrayList<Integer> WWEIACodes = new ArrayList<>();

            while(rs.next()){
                String[] codesForFoodItem = FNDDS.fuzzyWuzzy(rs.getString("food_item"));
                int foodCode = Integer.parseInt(codesForFoodItem[1]);
                String nutrition = rs.getString("food_nutrition");

                if(nutrition.toCharArray()[0] == '['){
                    JSONArray nutritionArray = new JSONArray(nutrition);
                    if(nutritionArray.length() >= 5){
                        JSONObject MFPCaloriesObject = nutritionArray.getJSONObject(0);
                        JSONObject MFPSodiumObject = nutritionArray.getJSONObject(4);
                        int MFPCalories = Integer.parseInt(MFPCaloriesObject.getString("value").replace(",",""));
                        int MFPSodium = Integer.parseInt(MFPSodiumObject.getString("value").replace(",",""));
                        foodCodesAndNutValue.put(foodCode,
                                new int[] {MFPCalories,MFPSodium});
                        //System.out.println("food_code: " + foodCode + " calories: " + MFPCalories + " sodium: " + MFPSodium);
                    }
                }
            }

            FPEDAccess FPED = new FPEDAccess();
            double[] HEIScore = FPED.calculateHEI(foodCodesAndNutValue);

            String[] components = new String[]{"total_fruits","whole_fruits","total_vegies","greens_beans",
                    "whole_grains","dairy_things","protein_food","seas_plan_pr",
                    "fatty_acids","refined_grain","estimated_sodium","actual_sodium",
                    "added_sugars","saturated_fats"};


            JSONObject HEI = new JSONObject(components);
            HEI.put("total_fruits",HEIScore[0]);
            HEI.put("whole_fruits",HEIScore[1]);
            HEI.put("total_vegies",HEIScore[2]);
            HEI.put("greens_beans",HEIScore[3]);
            HEI.put("whole_grains",HEIScore[4]);
            HEI.put("dairy_things",HEIScore[5]);
            HEI.put("protein_food",HEIScore[6]);
            HEI.put("seas_plan_pr",HEIScore[7]);
            HEI.put("fatty_acids",HEIScore[8]);
            HEI.put("refined_grain",HEIScore[9]);
            HEI.put("estimated_sodium",HEIScore[10]);
            HEI.put("actual_sodium",HEIScore[11]);
            HEI.put("added_sugars",HEIScore[12]);
            HEI.put("saturated_fats",HEIScore[13]);

            return HEI;

        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e){
            System.out.println("couldn't make MDBAccess instance " + e.getMessage());
        }
        return null;
    }
}
