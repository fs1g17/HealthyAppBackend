import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
    private static final String F_TOTAL = "total_fruits";
    private static final String F_WHOLE = "whole_fruits";
    private static final String V_TOTAL = "total_vegies";
    private static final String V_GREEN = "greens_beans";
    private static final String G_WHOLE = "whole_grains";
    private static final String D_TOTAL = "dairy_things";
    private static final String PF_TOTAL = "protein_food";
    private static final String PF_SEA_PLANT = "seas_plan_pr";
    private static final String FA = "fatty_acids";
    private static final String G_REFINED = "refined_grain";
    private static final String NA_EST = "estimated_sodium";
    private static final String NA_ACT ="actual_sodium";
    private static final String ADD_SUGARS = "added_sugars";
    private static final String SAT_FATS = "saturated_fats";

    public static void main(String[] args){
        /*
        String jooString = "{\"var1\": 1, \"var2\": \"val2\"}";
        String jotString = "{\"param''1\": \"val1\", \"param2\": \"val2\"}";
        JSONObject joo = new JSONObject(jooString);
        JSONObject jot = new JSONObject(jotString);
        System.out.println(joo.getInt("var1"));
        JSONArray jarr = new JSONArray();
        jarr.put(joo);
        jarr.put(jot);
        System.out.println(jarr.toString());
        jarr.remove(1);
        System.out.println(jarr);
        System.out.println(jarr.getJSONObject(0).toString());
         */

        JSONObject HEIScore = new JSONObject();
        HEIScore.put(F_TOTAL,69.0);
        HEIScore.put(F_WHOLE,79.0);
        HEIScore.put(V_TOTAL,80.0);
        HEIScore.put(V_GREEN,50.0);
        HEIScore.put(G_WHOLE,70.0);
        HEIScore.put(D_TOTAL,75.0);
        HEIScore.put(PF_TOTAL,90.0);
        HEIScore.put(PF_SEA_PLANT,95.0);
        HEIScore.put(FA,85.0);
        HEIScore.put(G_REFINED,88.0);
        HEIScore.put(NA_EST,50.0);
        HEIScore.put(NA_ACT,77.0);
        HEIScore.put(ADD_SUGARS,88.5);
        HEIScore.put(SAT_FATS,45.0);
        System.out.println(HEIScore);
        System.out.println(HEIScore.getDouble(NA_EST));

    }
}
