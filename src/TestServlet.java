

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
//import org.json.JSONObject;

public class TestServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String hardcoded = "{\"total_fruits\": \"69\", \"whole_fruits\": \"420\", \"total_vegies\": \"69\", \"greens_beans\": \"420\", " +
                "\"whole_grains\": \"69\", \"dairy_things\": \"69\", \"protein_food\": \"1337\", \"seas_plan_pr\": \"80\"," +
                "\"fatty_acids\": \"74\", \"refined_grain\": \"60\", \"estimated_sodium\": \"62\", \"actual_sodium\": \"67\"," +
                "\"added_sugars\": \"81\", \"saturated_fats\": \"95\"}";
        //JSONObject hardcodedJSON = new JSONObject(hardcoded);

        try (PrintWriter writer = response.getWriter()) {
            writer.println(hardcoded);
        }
    }
}
