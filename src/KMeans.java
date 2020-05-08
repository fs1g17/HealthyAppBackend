import java.util.*;

public class KMeans {
    static ArrayList<User> calculate(List<User> users, int k, int iterationsLimit, int min, int max, int cardinality, int userID){
        HashMap<double[], ArrayList<User>> clusters = new HashMap<>();

        for(int i=0; i<k; i++){
            double[] centroid = randomCentroid(cardinality,min,max);
            clusters.put(centroid,new ArrayList<>());
        }

        for(int iteration=0; iteration<iterationsLimit; iteration++){
            for(User user : users){
                double distance = Double.MAX_VALUE;
                double[] assignedCentroid = null;
                for(double[] centroid : clusters.keySet()){
                    double currDist = euclideanDistance(user.getVector(),centroid);
                    if(currDist < distance){
                        distance = currDist;
                        assignedCentroid = centroid;
                    }
                }
                clusters.get(assignedCentroid).add(user);
            }

            for(Map.Entry<double[],ArrayList<User>> cluster : clusters.entrySet()){
                double[] centroid = cluster.getKey();
                ArrayList<User> usersInCluster = cluster.getValue();

                if(usersInCluster.isEmpty()){
                    centroid = randomCentroid(cardinality,min,max);
                } else {
                    for(int i=0; i<cardinality; i++){
                        centroid[i] = 0.0;
                    }
                    for(User userInCluster : usersInCluster){
                        for(int i=0; i<cardinality; i++){
                            centroid[i] += userInCluster.getVector()[i];
                        }
                    }
                    for(int i=0; i<cardinality; i++){
                        centroid[i] = centroid[i]/usersInCluster.size();
                    }
                }
                if(!(iteration==iterationsLimit-1)){
                    cluster.setValue(new ArrayList<>());
                }
            }
        }

        ArrayList<User> usersOfInterest = null;
        User delete = null;
        for(Map.Entry<double[],ArrayList<User>> cluster : clusters.entrySet()){
            ArrayList<User> usersInCluster = cluster.getValue();
            for(User user : usersInCluster){
                if(user.getUserID() == userID){
                    usersOfInterest = usersInCluster;
                    delete = user;
                }
            }
        }
        usersOfInterest.remove(delete);
        return usersOfInterest;
    }

    static double[] randomCentroid(int cardinality, int min, int max){
        double[] centroid = new double[cardinality];
        for(int j=0; j<cardinality; j++){
            centroid[j] = min + (Math.random()*((max - min)));
        }
        return centroid;
    }

    static double euclideanDistance(double[] vector1, double[] vector2){
        double squareDiff = 0.0;
        for(int i=0; i<vector1.length; i++){
            squareDiff += (vector1[i] - vector2[i])*(vector1[i] - vector2[i]);
        }
        return Math.sqrt(squareDiff);
    }
}