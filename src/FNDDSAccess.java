import com.healthmarketscience.jackcess.*;
import me.xdrop.fuzzywuzzy.FuzzySearch;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import org.apache.commons.text.similarity.*;

public class FNDDSAccess {
    private String pathOfFNDDS = "C:/Users/PEEPEEPOOPOO/IP/FNDDS.mdb";
    //private String pathOfFNDDS = "H:/IP_resources/current/FNDDS.mdb";
    private static final String foodCodeCol = "Food code";
    private static final String nutCodeCol = "Nutrient code";
    private static final String nutValCol = "Nutrient value";
    private static final String addDescription = "Additional food description";
    private static final String mainFoodDescCol = "Main food description";
    private static final String WWEIACode = "WWEIA Category code";

    private static final int SODIUM_CODE = 307;
    private static final int FASATS_CODE = 606;
    private static final int KCAL_CODE = 208;
    private static final int POLY_CODE = 646;
    private static final int MONO_CODE = 645;

    private Table AddDesc;
    private Table FNDDSNutVal;
    private Table MainFoodDescTable;
    private Preprocessor preprocessor;

    protected static Connection conn;

    public static void main(String[] args){
        Preprocessor pp = new Preprocessor();
        String one = pp.TESTFNDDSInput("Bread - Whole-wheat, 2 oz");
        String two = pp.TESTFNDDSInput("Industrial oil as ingredient in food");
        System.out.println(one);
        System.out.println(two);
        JaccardSimilarity jd = new JaccardSimilarity();
        double sim = jd.apply(one,two);
        System.out.println(sim);
    }

    public FNDDSAccess() throws IOException {
        //org.apache.log4j.BasicConfigurator.configure();
        AddDesc = DatabaseBuilder.open(new File(pathOfFNDDS)).getTable("AddFoodDesc");
        FNDDSNutVal = DatabaseBuilder.open(new File(pathOfFNDDS)).getTable("FNDDSNutVal");
        MainFoodDescTable = DatabaseBuilder.open(new File(pathOfFNDDS)).getTable("MainFoodDesc");
        preprocessor = new Preprocessor();
    }

    protected static void connect() {
        String url = "jdbc:sqlite:C:/sqlite/test.db";
        try {
            conn = DriverManager.getConnection(url);
            //System.out.println("connection to database established");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //  {match,foodCode,WWEIACode}
    protected String[] fuzzyWuzzy(String userInput){
        String uneditedInput = userInput;
        String match = null;
        int foodCodeMatch = 0;
        int WWEIACodeMatch = 0;
        try{
            Cursor cursor = MainFoodDescTable.getDefaultCursor();
            cursor.beforeFirst();

            int dist = 0;
            String procInput = null;

            while(cursor.moveToNextRow()){
                Row row = cursor.getCurrentRow();
                String foodDescription = row.getString(mainFoodDescCol);
                int foodCode = row.getInt(foodCodeCol);
                String addDesc = getAddFoodDesc(foodCode);
                foodDescription += " " + addDesc;

                procInput = preprocessor.TESTUserInput(userInput);
                foodDescription = preprocessor.TESTFNDDSInput(foodDescription);

                int currDist = FuzzySearch.tokenSortPartialRatio(procInput,foodDescription);

                if(currDist > dist){
                    dist = currDist;
                    match = row.getString(mainFoodDescCol);
                    foodCodeMatch = row.getInt(foodCodeCol);
                    WWEIACodeMatch = row.getInt(WWEIACode);
                }
            }
        } catch (IOException e){
            System.out.println("IOException");
        }
        System.out.println("MATCHED: " +uneditedInput + " WITH: " + match);
        return new String[]{match,foodCodeMatch+"",WWEIACodeMatch+""};
    }

    protected String jaccSim(String userInput){
        JaccardSimilarity jd = new JaccardSimilarity();
        String match = null;
        try{
            Cursor cursor = MainFoodDescTable.getDefaultCursor();
            cursor.beforeFirst();

            int foodCode = 0;
            int WWEIACode = 0;
            double similarity = 0.0;

            while(cursor.moveToNextRow()){
                Row row = cursor.getCurrentRow();
                String foodDescription = row.getString(mainFoodDescCol);
                int currFoodCode = row.getInt(foodCodeCol);
                String addDesc = getAddFoodDesc(currFoodCode);
                foodDescription += " " + addDesc;

                userInput = preprocessor.TESTUserInput(userInput);
                foodDescription = preprocessor.TESTFNDDSInput(foodDescription);

                double jacDis = jd.apply(userInput,foodDescription);

                if(jacDis > similarity){
                    match = row.getString(mainFoodDescCol);
                    foodCode = currFoodCode;
                    WWEIACode = row.getInt(this.WWEIACode);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return match;
    }

    protected String levDis(String userInput){
        LevenshteinDistance levDis = new LevenshteinDistance();
        String match = null;
        try{
            Cursor cursor = MainFoodDescTable.getDefaultCursor();
            cursor.beforeFirst();

            int foodCode = 0;
            int WWEIACode = 0;
            int distance = Integer.MAX_VALUE;

            while(cursor.moveToNextRow()){
                Row row = cursor.getCurrentRow();
                String foodDescription = row.getString(mainFoodDescCol);
                int currFoodCode = row.getInt(foodCodeCol);
                String addDesc = getAddFoodDesc(currFoodCode);
                foodDescription += " " + addDesc;

                userInput = preprocessor.TESTUserInput(userInput);
                foodDescription = preprocessor.TESTFNDDSInput(foodDescription);

                if(levDis.apply(userInput,foodDescription) < distance){
                    match = row.getString(mainFoodDescCol);
                    foodCode = currFoodCode;
                    WWEIACode = row.getInt(this.WWEIACode);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return match;
    }

    protected String customStringMatch(String userInput){
        LevenshteinDistance levDis = new LevenshteinDistance();
        String match = null;
        try{
            Cursor cursor = MainFoodDescTable.getDefaultCursor();
            cursor.beforeFirst();

            int foodCode = 0;
            int WWEIACode = 0;
            int distance = Integer.MAX_VALUE;

            while(cursor.moveToNextRow()){
                Row row = cursor.getCurrentRow();
                String foodDescription = row.getString(mainFoodDescCol);
                int currFoodCode = row.getInt(foodCodeCol);
                String addDesc = getAddFoodDesc(currFoodCode);
                foodDescription += " " + addDesc;

                userInput = preprocessor.TESTUserInput(userInput);
                foodDescription = preprocessor.TESTFNDDSInput(foodDescription);

                int sumOfShortestDistances = closestWordLevDis(userInput,foodDescription);
                if(sumOfShortestDistances < distance){
                    distance = sumOfShortestDistances;
                    match = row.getString(mainFoodDescCol);
                    foodCode = row.getInt(foodCodeCol);
                    WWEIACode = row.getInt(this.WWEIACode);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return match;
    }

    private int closestWordLevDis(String userInput, String FNDDSDesc){
        String[] input = userInput.split(" ");
        String[] FNDDS = FNDDSDesc.split(" ");
        LevenshteinDistance levDis = new LevenshteinDistance();

        int sumOfShortestDistances = 0;
        for(String word : input){
            int shortestDistance = Integer.MAX_VALUE;
            for(String otherWord : FNDDS){
                int currentDistance = levDis.apply(word,otherWord);
                if(currentDistance<shortestDistance){
                    shortestDistance = currentDistance;
                }
            }
            sumOfShortestDistances += shortestDistance;
        }
        return sumOfShortestDistances;
    }

    public Cursor getMainFoodDescCursor(){
        return MainFoodDescTable.getDefaultCursor();
    }

    public String getAddFoodDesc(int foodCode){
        String addDesc = "";

        try{
            IndexCursor cursor = CursorBuilder.createCursor(AddDesc.getIndex(foodCodeCol));

            for(Row row : cursor.newEntryIterable(foodCode)){
                addDesc += row.getString(addDescription) + " ";
            }
        } catch(IOException e){
            System.out.println("couldn't retrieve additional information");
        }

        return  addDesc;
    }

    protected double[] getInfo(int foodItemCode){
        try{
            int[] nutArr = new int[] {SODIUM_CODE, FASATS_CODE, KCAL_CODE, POLY_CODE, MONO_CODE};
            double[] info = new double[5];
            for(int i=0; i<nutArr.length; i++){
                HashMap<String,Object> criteria = new HashMap<>();
                criteria.put(foodCodeCol,foodItemCode);
                criteria.put(nutCodeCol,nutArr[i]);
                Row row = CursorBuilder.findRow(FNDDSNutVal, criteria);

                if(row != null){
                    info[i] = row.getDouble(nutValCol);
                }
                else{
                    info[i] = 0;
                }
            }
            return info;
        } catch (IOException e) {
            e.printStackTrace();
            return new double[] {-1.0};
        }
    }
}

