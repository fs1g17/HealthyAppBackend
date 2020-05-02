import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(name = "UploadServlet")
public class UploadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        try{
            PrintWriter writer = response.getWriter();
            try{
                int userID = Integer.parseInt(request.getParameter("userID"));
                String date = request.getParameter("date");
                String jsonString = request.getParameterValues("foodList")[0];

                System.out.println("userID: " + userID + ", date: " + date + ", foodList: " + jsonString);

                JSONArray allMeals = new JSONArray(jsonString);
                //writer.println("message received");

                HashSet<String> foodSet = new HashSet<>();

                //we get the set of foods in interest for that day
                for(int i=0; i<allMeals.length(); i++){
                    JSONObject meal = allMeals.getJSONObject(i);
                    JSONArray foodList = meal.getJSONArray("food_list");
                    for(int j=0; j<foodList.length(); j++){
                        JSONObject foodItem = foodList.getJSONObject(j);
                        foodSet.add(foodItem.toString());

                        System.out.println("++");
                    }
                }

                Upload.uploadFoodSet(userID,date,foodSet);
                //writer.println("upload successful");

                JSONObject HEI = HEICalculator.getHEIScoreForUserByDate(userID,date);
                writer.println(HEI.toString());

                writer.flush();
                writer.close();

            } catch (JSONException e){
                System.out.println("not a json");
                writer.println("upload failed");
            }
        } catch(IOException e){
            System.out.println("failed to generate writer");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
