import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;
import java.util.HashSet;
import java.util.Iterator;

public class Upload {
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

    public static void uploadFoodSet(int userID, String date, HashSet<String> foodSet){
        connect();
        try{
            //DELETING OLD VALUES IF THEY EXIST
            Statement stmt = conn.createStatement();
            Statement insert = conn.createStatement();

            //get highest available row_id value, stored in this special entry
            String sql = "SELECT * FROM DateFoodSetNut WHERE user_id = -1;";
            ResultSet rs = stmt.executeQuery(sql);
            int rowIDStart = rs.getInt("row_id")*-1;

            //check if entries for current day and user exist, if so we delete them and reuse freed up row_id values
            String select = "SELECT * FROM DateFoodSetNut WHERE user_id = " + userID + " AND date = '" + date + "' LIMIT 1;";
            ResultSet oldEntries = stmt.executeQuery(select);
            if(oldEntries.next()){
                System.out.println("deleting existing values");
                rowIDStart = oldEntries.getInt(1);
            } else{
                System.out.println("proceeding");
            }

            //deleting outdated entries
            String delete = "DELETE FROM DateFoodSetNut WHERE user_id = " + userID + " AND date = '" + date + "';";
            stmt.executeUpdate(delete);


            //INSERTING NEW VALUES
            Iterator<String> itr = foodSet.iterator();
            while(itr.hasNext()){
                JSONObject jsonFoodInfo = new JSONObject(itr.next());
                String foodItem = jsonFoodInfo.getString("food_item");
                int calories = jsonFoodInfo.getInt("calories");
                int sodium = jsonFoodInfo.getInt("sodium");
                JSONObject jsonCal = new JSONObject();
                JSONObject jsonFats = new JSONObject();
                JSONObject jsonSalt = new JSONObject();
                JSONObject jsonCarbs = new JSONObject();
                JSONObject jsonProtein = new JSONObject();
                jsonCal.put("name","Calories");
                jsonCal.put("value",calories+"");
                jsonSalt.put("name","Sodium");
                jsonSalt.put("value",sodium+"");
                jsonFats.put("name","Fat");
                jsonFats.put("value","-1");
                jsonCarbs.put("name","Carbs");
                jsonCarbs.put("value","-1");
                jsonProtein.put("name","Protein");
                jsonProtein.put("value","-1");
                JSONArray nutrition = new JSONArray();
                nutrition.put(jsonCal);
                nutrition.put(jsonCarbs);
                nutrition.put(jsonFats);
                nutrition.put(jsonProtein);
                nutrition.put(jsonSalt);
                String insertQuery = "INSERT INTO DateFoodSetNut VALUES (" + rowIDStart + ", " + userID + ", '" + date + "', '" + foodItem + "', '" + nutrition.toString() + "');";
                rowIDStart++;

                System.out.println("--");
                try{
                    insert.executeUpdate(insertQuery);
                } catch (SQLException e){
                    System.out.println("couldn't execute: " + insertQuery);
                    System.out.println(e.getMessage());
                    System.out.println("---------------------------------------");
                }
            }

            rowIDStart *= -1;
            String updateMaxValue = "UPDATE DateFoodSetNut SET row_id = " + rowIDStart + " WHERE user_id = -1;";

            //TODO: enable this
            //insert.executeUpdate(updateMaxValue);
        } catch (SQLException e){

        } catch(JSONException e){

        }
    }
}
