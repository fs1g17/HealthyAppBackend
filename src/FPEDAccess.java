import com.healthmarketscience.jackcess.*;
import org.apache.log4j.varia.NullAppender;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class FPEDAccess {
    //private String pathOfFNDDS = "C:/Users/PEEPEEPOOPOO/IP/FPED.mdb";
    //private String pathOfFNDDS = "H:/IP_resources/current/FPED.mdb";
    private String pathOfFNDDS = "/usr/local/tomcat/db/FPED.mdb";

    private static final String FOODCODE = "FOODCODE";
    private static final String F_CITMLB = "F_CITMLB";
    private static final String F_OTHER  = "F_OTHER";
    private static final String F_JUICE  = "F_JUICE";
    private static final String F_TOTAL  = "F_TOTAL";
    private static final String V_DRKGR  = "V_DRKGR";
    private static final String V_REDOR_TOMATO = "V_REDOR_TOMATO";
    private static final String V_REDOR_OTHER  = "V_REDOR_OTHER";
    private static final String V_REDOR_TOTAL  = "V_REDOR_TOTAL";
    private static final String V_STARCHY_POTATO = "V_STARCHY_POTATO";
    private static final String V_STARCHY_OTHER  = "V_STARCHY_OTHER";
    private static final String V_STARCHY_TOTAL  = "V_STARCHY_TOTAL";
    private static final String V_OTHER = "V_OTHER";
    private static final String V_TOTAL = "V_TOTAL";
    private static final String V_LEGUMES = "V_LEGUMES";
    private static final String G_WHOLE = "G_WHOLE";
    private static final String G_REFINED = "G_REFINED";
    private static final String G_TOTAL = "G_TOTAL";
    private static final String PF_MEAT = "PF_MEAT";
    private static final String PF_CUREDMEAT = "PF_CUREDMEAT";
    private static final String PF_ORGAN = "PF_ORGAN";
    private static final String PF_POULT = "PF_POULT";
    private static final String PF_SEAFD_HI = "PF_SEAFD_HI";
    private static final String PF_SEAFD_LOW = "PF_SEAFD_LOW";
    private static final String PF_MPS_TOTAL = "PF_MPS_TOTAL";
    private static final String PF_EGGS = "PF_EGGS";
    private static final String PF_SOY = "PF_SOY";
    private static final String PF_NUTSDS = "PF_NUTSDS";
    private static final String PF_LEGUMES = "PF_LEGUMES";
    private static final String PF_TOTAL = "PF_TOTAL";
    private static final String D_MILK = "D_MILK";
    private static final String D_YOGURT = "D_YOGURT";
    private static final String D_CHEESE = "D_CHEESE";
    private static final String D_TOTAL = "D_TOTAL";
    private static final String OILS = "OILS";
    private static final String SOLID_FATS = "SOLID_FATS";
    private static final String ADD_SUGARS = "ADD_SUGARS";
    private static final String A_DRINKS = "A_DRINKS";

    private Table table;

    public FPEDAccess() throws IOException {
        org.apache.log4j.BasicConfigurator.configure();
        table = DatabaseBuilder.open(new File(pathOfFNDDS)).getTable("FPED_1516");
    }

    //                           HashMap foodCode -> {MFPCalories, MFPSodium}
    public double[] calculateHEI(HashMap<Integer, int[]> foodCodesAndCalories){
        double[] HEIValues = new double[14];

        //double totalF_CITMLB = 0;
        //double totalF_OTHER = 0;
        double totalF_JUICE = 0;
        double totalF_TOTAL = 0;
        double totalV_DRKGR = 0;
        //double totalV_REDOR_TOMATO = 0;
        //double totalV_REDOR_OTHER = 0;
        //double totalV_REDOR_TOTAL = 0;
        //double totalV_STARCHY_POTATO = 0;
        //double totalV_STARCHY_OTHER = 0;
        //double totalV_STARCHY_TOTAL = 0;
        //double totalV_OTHER = 0;
        double totalV_TOTAL = 0;
        double totalV_LEGUMES = 0;
        double totalG_WHOLE = 0;
        double totalG_REFINED = 0;
        //double totalG_TOTAL = 0;
        //double totalPF_MEAT = 0;
        //double totalPF_CUREDMEAT = 0;
        //double totalPF_ORGAN = 0;
        //double totalPF_POULT = 0;
        double totalPF_SEAFD_HI = 0;
        double totalPF_SEAFD_LOW = 0;
        double totalPF_MPS_TOTAL = 0;
        double totalPF_EGGS = 0;
        double totalPF_SOY = 0;
        double totalPF_NUTSDS = 0;
        double totalPF_LEGUMES = 0;
        //double totalPF_TOTAL = 0;
        //double totalD_MILK = 0;
        //double totalD_YOGURT = 0;
        //double totalD_CHEESE = 0;
        double totalD_TOTAL = 0;
        //double totalOILS = 0;
        //double totalSOLID_FATS = 0;
        double totalADD_SUGARS = 0;
        //double totalA_DRINKS = 0;
        double totalSODIUM = 0;
        double totalFASATS = 0;
        double totalPOLY = 0;
        double totalMONO = 0;
        double totalKCAL = 0;
        double MFPSODIUM = 0;
        try{
            Cursor cursor = table.getDefaultCursor();
            cursor.beforeFirst();
            FNDDSAccess FNDDS = new FNDDSAccess();
            Set<Integer> foodCodes = foodCodesAndCalories.keySet();

            for(Integer foodCode : foodCodes){
                int kcalFromMFPDataset = foodCodesAndCalories.get(foodCode)[0];
                int MFPsalt = foodCodesAndCalories.get(foodCode)[1];
                Row row = CursorBuilder.findRow(table, Collections.singletonMap(FOODCODE, foodCode));
                //{SODIUM_CODE, FASATS_CODE, KCAL_CODE, POLY_CODE, MONO_CODE};
                double[] nutInfo = FNDDS.getInfo(foodCode);
                double sodium = nutInfo[0];
                double fasats = nutInfo[1];
                double kcalFromFNDDS = nutInfo[2];
                double poly = nutInfo[3];
                double mono = nutInfo[4];
                double ratio = kcalFromMFPDataset/kcalFromFNDDS;
                ratio = (double)Math.round(ratio * 1000d) / 1000d;

                if(row != null){
                    //totalF_CITMLB += row.getDouble(F_CITMLB)*ratio;
                    //totalF_OTHER += row.getDouble(F_OTHER)*ratio;
                    totalF_JUICE += row.getDouble(F_JUICE)*ratio;
                    totalF_TOTAL += row.getDouble(F_TOTAL)*ratio;
                    totalV_DRKGR += row.getDouble(V_DRKGR)*ratio;
                    //totalV_REDOR_TOMATO += row.getDouble(V_REDOR_TOMATO)*ratio;
                    //totalV_REDOR_OTHER += row.getDouble(V_REDOR_OTHER)*ratio;
                    //totalV_REDOR_TOTAL += row.getDouble(V_REDOR_TOTAL)*ratio;
                    //totalV_STARCHY_POTATO += row.getDouble(V_STARCHY_POTATO)*ratio;
                    //totalV_STARCHY_OTHER += row.getDouble(V_STARCHY_OTHER)*ratio;
                    //totalV_STARCHY_TOTAL += row.getDouble(V_STARCHY_TOTAL)*ratio;
                    //totalV_OTHER += row.getDouble(V_OTHER)*ratio;
                    totalV_TOTAL += row.getDouble(V_TOTAL)*ratio;
                    totalV_LEGUMES += row.getDouble(V_LEGUMES)*ratio;
                    totalG_WHOLE += row.getDouble(G_WHOLE)*ratio;
                    totalG_REFINED += row.getDouble(G_REFINED)*ratio;
                    //totalG_TOTAL += row.getDouble(G_TOTAL)*ratio;
                    //totalPF_MEAT += row.getDouble(PF_MEAT)*ratio;
                    //totalPF_CUREDMEAT += row.getDouble(PF_CUREDMEAT)*ratio;
                    //totalPF_ORGAN += row.getDouble(PF_ORGAN)*ratio;
                    //totalPF_POULT += row.getDouble(PF_POULT)*ratio;
                    totalPF_SEAFD_HI += row.getDouble(PF_SEAFD_HI)*ratio;
                    totalPF_SEAFD_LOW += row.getDouble(PF_SEAFD_LOW)*ratio;
                    totalPF_MPS_TOTAL += row.getDouble(PF_MPS_TOTAL)*ratio;
                    totalPF_EGGS += row.getDouble(PF_EGGS)*ratio;
                    totalPF_SOY += row.getDouble(PF_SOY)*ratio;
                    totalPF_NUTSDS += row.getDouble(PF_NUTSDS)*ratio;
                    totalPF_LEGUMES += row.getDouble(PF_LEGUMES)*ratio;
                    //totalPF_TOTAL += row.getDouble(PF_TOTAL)*ratio;
                    //totalD_MILK += row.getDouble(D_MILK)*ratio;
                    //totalD_YOGURT += row.getDouble(D_YOGURT)*ratio;
                    //totalD_CHEESE += row.getDouble(D_CHEESE)*ratio;
                    totalD_TOTAL += row.getDouble(D_TOTAL)*ratio;
                    //totalOILS += row.getDouble(OILS)*ratio;
                    //totalSOLID_FATS += row.getDouble(SOLID_FATS)*ratio;
                    totalADD_SUGARS += row.getDouble(ADD_SUGARS)*ratio;
                    //totalA_DRINKS += row.getDouble(A_DRINKS)*ratio;
                    totalSODIUM += sodium*ratio;
                    totalFASATS += fasats*ratio;
                    totalPOLY += poly*ratio;
                    totalMONO += mono*ratio;
                    totalKCAL += kcalFromMFPDataset;
                    MFPSODIUM += MFPsalt;
                }
                else{
                    System.out.println("no row with foodcode " + foodCode + " found");
                }
            }

            double total_fruit = totalF_TOTAL;
            double total_whole_fruit = totalF_TOTAL - totalF_JUICE;
            double total_whole_grains = totalG_WHOLE;
            double total_dairy = totalD_TOTAL; //nonfat fraction?
            double total_protein_foods = totalPF_MPS_TOTAL + totalPF_EGGS +
                    totalPF_NUTSDS + totalPF_SOY + totalPF_LEGUMES;
            double total_seafood_plant_protein = totalPF_SEAFD_LOW + totalPF_SEAFD_HI +
                    totalPF_NUTSDS + totalPF_SOY + totalPF_LEGUMES;
            double total_greens_beans = totalV_LEGUMES + totalV_DRKGR;
            double total_vegetables = totalV_TOTAL;
            double total_fatty_acids = (totalMONO + totalPOLY)/totalFASATS;
            double total_refined_grains = totalG_REFINED;
            //there are 4 calories in a gram of sugar
            double total_percentage_intake_added_sugar = totalADD_SUGARS*4/totalKCAL;
            //there are 9 calories in a gram of saturated fat
            double total_percentage_intake_sat_fat = totalFASATS*9/totalKCAL;
            double RATIO = 1000/totalKCAL;

            double value_total_fruits;
            if(total_fruit * RATIO >= 0.8){
                value_total_fruits = 5;
            }
            else{
                value_total_fruits = 5 * (total_fruit * RATIO / 0.8);
            }

            double value_whole_fruits;
            if(total_whole_fruit * RATIO >= 0.4){
                value_whole_fruits = 5;
            }
            else{
                value_whole_fruits = 5 * (total_whole_fruit * RATIO / 0.4);
            }

            double value_total_vegetables;
            if(total_vegetables * RATIO >= 1.1){
                value_total_vegetables = 5;
            }
            else{
                value_total_vegetables = 5 * (total_vegetables * RATIO / 1.1);
            }

            double value_greens_beans;
            if(total_greens_beans * RATIO >= 0.2){
                value_greens_beans = 5;
            }
            else{
                value_greens_beans = 5 * (total_greens_beans * RATIO / 0.2);
            }

            double value_whole_grains;
            if(total_whole_grains * RATIO >= 1.5){
                value_whole_grains = 10;
            }
            else{
                value_whole_grains = 10 * (total_whole_grains * RATIO / 1.5);
            }

            double value_dairy;
            if(total_dairy * RATIO >= 1.3){
                value_dairy = 10;
            }
            else{
                value_dairy = 10 * (total_dairy * RATIO / 1.3);
            }

            double value_total_protein_foods;
            if(total_protein_foods * RATIO >= 2.5){
                value_total_protein_foods = 5;
            }
            else{
                value_total_protein_foods = 5 * (total_protein_foods * RATIO / 2.5);
            }

            double value_seafood_plant_proteins;
            if(total_seafood_plant_protein * RATIO >= 0.8){
                value_seafood_plant_proteins = 5;
            }
            else{
                value_seafood_plant_proteins = 5 * (total_seafood_plant_protein * RATIO / 0.8);
            }

            double value_fatty_acid_ratio;
            if(total_fatty_acids >= 2.5){
                value_fatty_acid_ratio = 10;
            }
            else{
                value_fatty_acid_ratio = 10 * (total_fatty_acids / 2.5);
            }

            double value_refined_grains;
            if(total_refined_grains * RATIO <= 1.8){
                value_refined_grains = 10;
            }
            else{
                value_refined_grains = 10 * (1.8 / (total_refined_grains * RATIO));
            }

            //divide by 100 because it's in mg, want it in grams
            double value_estimated_sodium;
            if((totalSODIUM/1000) * RATIO <= 0.7){
                value_estimated_sodium = 10;
            }
            else{
                value_estimated_sodium = 10 * (0.7/((totalSODIUM/1000) * RATIO));
            }

            double value_actual_sodium;
            if((MFPSODIUM/1000) * RATIO <= 0.7){
                value_actual_sodium = 10;
            }
            else{
                value_actual_sodium = 10 * (0.7/((MFPSODIUM/1000) * RATIO));
            }

            double value_added_sugars;
            if(total_percentage_intake_added_sugar <= (totalKCAL * 0.065)){
                value_added_sugars = 10;
            }
            else{
                value_added_sugars = 10 * ((totalKCAL * 0.065)/total_percentage_intake_added_sugar);
            }

            double value_saturated_fats;
            if(total_percentage_intake_sat_fat  <= (totalKCAL * 0.08)){
                value_saturated_fats = 10;
            }
            else{
                value_saturated_fats = 10 * ((totalKCAL * 0.08)/total_percentage_intake_sat_fat);
            }

            HEIValues[0] = value_total_fruits;
            HEIValues[1] = value_whole_fruits;
            HEIValues[2] = value_total_vegetables;
            HEIValues[3] = value_greens_beans;
            HEIValues[4] = value_whole_grains;
            HEIValues[5] = value_dairy;
            HEIValues[6] = value_total_protein_foods;
            HEIValues[7] = value_seafood_plant_proteins;
            HEIValues[8] = value_fatty_acid_ratio;
            HEIValues[9] = value_refined_grains;
            HEIValues[10] = value_estimated_sodium;
            HEIValues[11] = value_actual_sodium;
            HEIValues[12] = value_added_sugars;
            HEIValues[13] = value_saturated_fats;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return HEIValues;
    }

}