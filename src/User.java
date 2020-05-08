public class User {
    private int userID;
    private double[] vector;

    public User(int userID, double[] vector){
        this.userID = userID;
        this.vector = vector;
    }

    public int getUserID() {
        return userID;
    }

    public double[] getVector() {
        return vector;
    }
}
