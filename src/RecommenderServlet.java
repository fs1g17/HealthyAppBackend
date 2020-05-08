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
import java.util.HashSet;

@WebServlet(name = "RecommenderServlet")
public class RecommenderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try{
            PrintWriter writer = response.getWriter();
            try{
                int userID = Integer.parseInt(request.getParameter("userID"));
                JSONArray foodList = Recommender.run(200);
                writer.println(foodList.toString());
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
