//source: https://en.wikipedia.org/wiki/Levenshtein_distance
//source: https://stackoverflow.com/questions/5859561/getting-the-closest-string-match
public class LevDis {

    public static int calculate(String source, String target){
        //2D arrays are index as matrix[column][row]
        int width = source.length()+1;
        int height = target.length()+1;
        int[][] matrix = new int[width][height];

        for(int i=0; i<width; i++){
            matrix[i][0] = i;
        }

        for(int j=1; j<height; j++){
            matrix[0][j] = j;
        }

        char[] srcChar = source.toCharArray();
        char[] trgChar = target.toCharArray();
        for(int j=1; j<height; j++){
            for(int i=1; i<width; i++){
                int subCost = 1;
                if(srcChar[i-1] == trgChar[j-1]){
                    subCost = 0;
                }

                int a = Integer.min((matrix[i-1][j]+1),(matrix[i][j-1]+1));
                int b = matrix[i-1][j-1] + subCost;
                matrix[i][j] = Integer.min(a,b);
            }
        }
        return matrix[width-1][height-1];
    }
}