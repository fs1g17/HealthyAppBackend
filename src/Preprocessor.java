//import com.sun.istack.NotNull;
import opennlp.tools.stemmer.PorterStemmer;
//import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;

public class Preprocessor {
    private PorterStemmer ps;

    public Preprocessor(){
        ps = new PorterStemmer();
    }

    //for processing MFP dataset inputs
    public String preprocessUserInput(String s){
        if(s.contains("Quick Added Calories")){
            return s;
        }

        //s = s.replaceAll(",.*\\d+.*", "");
        String[] splitByComma = s.split(",");

        if(splitByComma.length == 1){
            s = splitByComma[0];
        }
        else{
            s = "";
            for(int i=0; i<splitByComma.length; i++){
                String part = splitByComma[i];
                if(part.matches(".*[0-9]+\".*")){
                    if(i==0){
                        s = part.replaceAll(".*[0-9]+\"\\s*", "");
                        //s = part;
                    }
                    else{
                        s += " " + part;
                    }
                }
                if(!part.matches(".*[0-9]+.*") || i==0){
                    if(i==0){
                        s = part.replaceAll("[0-9]+.*", "");
                    }
                    else{
                        s += " " + part;
                    }
                }
            }
        }

        //System.out.println(s);
        s = s.replaceAll("\\([^)]*\\)", "");
        //System.out.println(s);
        String[] description = s.split(" - | -|- |-");
        //System.out.println(s);

        if(description.length >= 2){
            s = "";
            for(int i=0; i<description.length; i++){
                s += description[description.length-1-i] + " ";
            }
        }
        else{
            s = description[0];
        }
        //System.out.println(s);
        //System.out.println(s);
        s = s.replaceAll("\t", "");
        //System.out.println(s);

        String[] words = s.split(" ");

        String output;
        if(words.length == 1){
            output = ps.stem(words[0].toLowerCase());
        }
        else{
            output = ps.stem(words[0].toLowerCase());
            for(int i=1; i<words.length; i++){
                output += " " + ps.stem(words[i].toLowerCase());
            }
        }

        output = output.replaceAll("\\s{2,3}"," ");
        output = output.replace("'","''");
        return output;
    }

    public String TESTUserInput(String userInput){
        userInput = userInput.replace(" - ", " ");
        String[] clauses;
        if(userInput.contains(",")){
            clauses = userInput.split(",");
        } else {
            clauses = new String[]{userInput};
        }

        String output = "";
        for(int i=0; i<clauses.length; i++){
            String clause = clauses[i];
            clause = clause.replaceAll("\\(.*\\)", "");
            clause = clause.replaceAll("[^a-zA-Z ]","");

            char[] clauseChars = clause.toCharArray();

            //making sure every clause ends with a space but doesn't begin with one
            if(clauseChars[0]==' ' && clause.length()>1){
                clause = clause.substring(1);
            }



            if(clauseChars[clause.length()-1]!=' '){
                clause = clause + " ";
            }

            //getting rid of portion information by excluding the last clause
            //not worrying about spacing because every clause end with a space
            if(!(i==clauses.length-1) || (clauses.length==1)){
                output += clause;
            }
        }

        output = output.toLowerCase();
        output = output.replaceAll("\\s{2,5}", " ");
        output = stemAllWords(output.split(" "));
        return output;
    }

    public String TESTFNDDSInput(String input){
        input = input.replace(",","");
        input = input.replace("/"," ");
        input = input.replace(";","");
        input = input.replaceAll("[0-9%]","");
        input = input.toLowerCase();
        input = stemAllWords(input.split(" "));
        input = input.replaceAll("\\s{2,3}"," ");
        return input;
    }

    public String stemAllWords(String[] words){
        String output = "";

        for(String word : words){
            output += ps.stem(word) + " ";
        }

        output = output.toLowerCase();
        return output;
    }

    public static void main(String[] args){
        Preprocessor preprocessor = new Preprocessor();
        String input = "Apple roll 5% fat";
        System.out.println(preprocessor.TESTFNDDSInput(input));
    }
}



/*
//import com.sun.istack.NotNull;
import opennlp.tools.stemmer.PorterStemmer;
//import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;

public class Preprocessor {
    private PorterStemmer ps;

    public Preprocessor(){
        ps = new PorterStemmer();
    }

    //for processing MFP dataset inputs
    public String preprocessUserInput(String s){
        if(s.contains("Quick Added Calories")){
            return s;
        }

        //s = s.replaceAll(",.*\\d+.*", "");
        String[] splitByComma = s.split(",");

        if(splitByComma.length == 1){
            s = splitByComma[0];
        }
        else{
            s = "";
            for(int i=0; i<splitByComma.length; i++){
                String part = splitByComma[i];
                if(part.matches(".*[0-9]+\".*")){
                    if(i==0){
                        s = part.replaceAll(".*[0-9]+\"\\s*", "");
                        //s = part;
                    }
                    else{
                        s += " " + part;
                    }
                }
                if(!part.matches(".*[0-9]+.*") || i==0){
                    if(i==0){
                        s = part.replaceAll("[0-9]+.*", "");
                    }
                    else{
                        s += " " + part;
                    }
                }
            }
        }

        //System.out.println(s);
        s = s.replaceAll("\\([^)]*\\)", "");
        //System.out.println(s);
        String[] description = s.split(" - | -|- |-");
        //System.out.println(s);

        if(description.length >= 2){
            s = "";
            for(int i=0; i<description.length; i++){
                s += description[description.length-1-i] + " ";
            }
        }
        else{
            s = description[0];
        }
        //System.out.println(s);
        //System.out.println(s);
        s = s.replaceAll("\t", "");
        //System.out.println(s);

        String[] words = s.split(" ");

        String output;
        if(words.length == 1){
            output = ps.stem(words[0].toLowerCase());
        }
        else{
            output = ps.stem(words[0].toLowerCase());
            for(int i=1; i<words.length; i++){
                output += " " + ps.stem(words[i].toLowerCase());
            }
        }

        output = output.replaceAll("\\s{2,3}"," ");
        output = output.replace("'","''");
        return output;
    }

    public String TESTUserInput(String userInput){
        userInput = userInput.replace(" - ", " ");
        String[] clauses = userInput.split(",");

        String output = "";
        for(int i=0; i<clauses.length; i++){
            String clause = clauses[i];
            clause = clause.replaceAll("\\(.*\\)", "");
            clause = clause.replaceAll("[^a-zA-Z ]","");
            char[] clauseChars = clause.toCharArray();

            //making sure every clause ends with a space but doesn't begin with one
            if(clauseChars[0]==' ' && clause.length()>1){
                clause = clause.substring(1);
            }

            if(clauseChars[clause.length()-1]!=' '){
                clause = clause + " ";
            }

            //getting rid of portion information by excluding the last clause
            //not worrying about spacing because every clause end with a space
            if(!(i==clauses.length-1)){
                output += clause;
            }
        }

        output = output.toLowerCase();
        output = output.replaceAll("\\s{2,5}", " ");
        output = stemAllWords(output.split(" "));
        return output;
    }

    public String TESTFNDDSInput(String input){
        input = input.replace(",","");
        input = stemAllWords(input.split(" "));
        input = input.replace("/"," ");
        input = input.replace(";","");
        input = input.toLowerCase();
        return input;
    }

    public String stemAllWords(String[] words){
        String output = "";

        for(String word : words){
            output += ps.stem(word) + " ";
        }

        output = output.toLowerCase();
        return output;
    }

    public static void main(String[] args){
        Preprocessor preprocessor = new Preprocessor();
        String input = "Quick Added Calories, 1,900 calories";
        String processed = preprocessor.preprocessUserInput(input);
        System.out.println("input: " + input);
        System.out.println("output: " + processed);
    }
}
 */
