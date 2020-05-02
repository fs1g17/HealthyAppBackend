import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet(name = "HEICalcServlet")
public class HEICalcServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String query = request.getQueryString();
        int userID = getUserID(query);
        String date = getUserDate(query);
        System.out.println("userID = " + userID + " and date = " + date);
        JSONObject HEIScore = HEICalculator.getHEIScoreForUserByDate(userID,date);

        try (PrintWriter writer = response.getWriter()) {
            if(HEIScore == null){
                writer.println("{\"this\": \"failed\"}");
            } else {
                writer.println(HEIScore.toString());
                writer.flush();
                writer.close();
            }
        }
    }

    private int getUserID(String query){
        String userIDString = query.split("&")[0].split("=")[1];
        int userID = Integer.parseInt(userIDString);
        return userID;
    }

    private String getUserDate(String query){
        String date = query.split("&")[1].split("=")[1];
        return date;
    }
}
