/**
 * Class : CSI2520 Programming Paradigms
 * Project : Similarity image search - Part 1 (JAVA)
 * Full Name : Yassine Moumine
 * Student Number : 300140139
 **/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/** Main Class **/
public class SimilaritySearch {

    public static void main(String[] args) throws IOException {

        if(args.length != 2){
            System.out.println("Missing arguments... please try again!");
        }

        //args[0] = jpg file name
        //args[1] = image dataset directory
        String filename = args[0];
        String dataset = args[1];

        // Create ColorImage object without reducing color depth
        ColorImage image = new ColorImage(filename);

        // Create ColorHistogram using filename
        ColorHistogram hist = new ColorHistogram(8);

        // Reduce color depth to 3
        image.reduceColor(3);

        // Associate color image to 3-bit hist instance
        hist.setImage(image);

        // Save normalized histogram in text file in current directory
        hist.save(filename);

        // Create Histogram Map
        ImageSearch search = new ImageSearch(dataset);

        //List for intersection results
        List<SimilarityMatch> result = new ArrayList<>();

        for(Map.Entry m:search.getHistograms().entrySet()){
            // iterate through the map then add the result of each comparison to the list
            result.add( new SimilarityMatch((String) m.getKey(), search.getHistogram((String) m.getKey()).compare(hist)));
        }

        // reverse sort to get highest at first
        result.sort(Comparator.comparingDouble(SimilarityMatch::getSimilarity).reversed());

        // Print out 5 top results
        System.out.println("Here's the top 5 most similar images to image "+ filename+":");
        for(int i = 0; i < 5;i++){
            System.out.printf("+ Image: %s, with %f%% percent similarity \n",result.get(i).getFilename(), (result.get(i).getSimilarity() *100));
        }
    }
}
