package me.shaakashee.imgroup;

import me.shaakashee.imgroup.model.Div;
import me.shaakashee.imgroup.model.HashImage;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

public class KMeans {

    public  String[] lastClusters = null;

    public void cluster(Div div, int maxReps, double c, double c2){

        //int n = 5;

        double best = -1;
        double bestDrop = -1;
        double last = -1;

        for (int n = 2; n < Math.min(128,div.getImgs().size()/3); n++){
            String[] centers = new String[n];

            init(div, n, centers);

            clusterLoop(div, maxReps, n, centers, c, c2);

            double dist = 0;
            int count = 0;

            ArrayList<Double> dists = new ArrayList<>();

            for (HashImage hashImage: div.getImgs()){

                double minDist = -1;

                for (int k = 0; k < centers.length; k++){
                    if (HashImage.getDistance(hashImage.getHash(), centers[k], c, c2) < minDist || minDist == -1){
                        minDist = HashImage.getDistance(hashImage.getHash(), centers[k], c, c2);
                    }
                }
                dists.add(dist);

                if (minDist != -1){
                    dist += minDist;
                    count++;
                }
            }

            double average = dists.stream().reduce(0.0, (d1, d2)-> d1+d2)/dists.size();

            double maxOut = Math.sqrt(dists.stream().map(d -> Math.pow(average-d, 2)).reduce(0.0, (d1, d2)-> d1+d2))/dists.size();

            double max = dists.stream().map(d -> average-d).reduce(0.0, Math::max);

            max /= maxOut;



            if (count != 0) {
                dist /= count;

                double current = Math.pow(dist, 2) * Math.sqrt(n) + max;

                System.out.println(max);
                System.out.println(dist);
                System.out.println(Math.pow(dist, 2) * Math.sqrt(n) + max);

                if ((current < best && last - current > bestDrop*0.75) || best == -1) {
                    bestDrop = last - current;
                    best = current;
                    lastClusters = centers;
                }
                last = current;
            } else if(lastClusters == null){
                lastClusters = centers;
            }

        }


        System.out.println("finished clustering");
        return;
    }

    public static void init(Div div, int n, String[] centers){
        for (int i = 0; i < n; i++){
            centers[i] = div.getImgs().get(i%div.getImgs().size()).getHash();
        }
        System.out.println(centers.length + " Clusters init");
    }

    public static void clusterLoop(Div div, int maxReps, int n, String[] centers, double c, double c2){
        boolean changed = false;
        L:
        for (int i = 0; i < maxReps; i++){
            System.out.println("Rep " + i );
            changed = false;
            ArrayList<ArrayList<String>> next = new ArrayList<>();

            for (int k = 0; k < n; k++){
                next.add(new ArrayList<>());
            }

            for (HashImage hashImage: div.getImgs()){
                    int nearest = -1;
                    double minDist = 1000;
                    for (int k = 0; k < n; k++){
                        double dist = 1000;
                            dist = HashImage.getDistance(hashImage.getHash(), centers[k], c, c2);
                        if (dist < minDist && dist >= 0){
                            nearest = k;
                            minDist = dist;
                        }
                    }
                    if (nearest != -1) {
                        next.get(nearest).add(hashImage.getHash());
                    }
            }
            for (int k = 0; k < n; k++){
                StringBuilder nextC = new StringBuilder();

                for (int j = 0; j < 158; j++){
                    int count = 0;
                    double sum = 0;

                    for (String s: next.get(k)){
                        count++;
                        sum += (s.charAt(j) == '0'? 0:1);
                    }

                    sum /= count;

                    nextC.append((sum > 0.5?"1":"0"));
                }

                centers[k] = nextC.toString();
            }
        }
    }

    public static ArrayList<HashImage> getForCluster(Div div, String[] clusters, int n, double c, double c2){

        ArrayList<HashImage> ret = new ArrayList<>();

        for (HashImage hashImage: div.getImgs()){
            int nearest = -1;
            double minDist = 1000;
            for (int i = 0; i < clusters.length; i++){
                double dist = HashImage.getDistance(hashImage.getHash(), clusters[i], c, c2);
                if (dist != -1 && dist < minDist){
                    nearest = i;
                    minDist = dist;
                }
            }

            if (nearest == n){
                ret.add(hashImage);
            }
        }

        return ret.parallelStream().sorted((h1, h2) -> Double.compare(HashImage.getDistance(clusters[n], h1.getHash(), c, c2), HashImage.getDistance(clusters[n], h2.getHash(), c, c2)))
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
