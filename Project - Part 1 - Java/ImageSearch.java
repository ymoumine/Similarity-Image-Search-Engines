/**
 * Class : CSI2520 Programming Paradigms
 * Project : Similarity image search - Part 1 (JAVA)
 * Full Name : Yassine Moumine
 * Student Number : 300140139
 **/

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/** Class that sets a record Map of the histogram of each image in given dataset **/
public class ImageSearch {

    private final Map<String, ColorHistogram> histograms;

    public ImageSearch(String datasetDirectory){
        this.histograms = new HashMap<>();
        loadDatasetHistograms(datasetDirectory);
    }

    /** Method that load list og histograms from dataset to Map
     * @param datasetDirectory dataset directory
     **/
    public void loadDatasetHistograms(String datasetDirectory) {

        File directory = new File(datasetDirectory);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {

                if (file.isFile() && file.getName().endsWith(".txt")) {

                    // new reader since its already pre-computed histograms
                    try {
                        Scanner myReader = new Scanner(file);
                        while (myReader.hasNextInt()) {
                            int size = myReader.nextInt();
                            int depth = (int) Math.log(size) / 3; // log2(512) / 3 = 9 / 3 = 3 bits

                            // fully compute and normalize colorHistogram instance for each element in list at index i
                            ColorHistogram histogram = new ColorHistogram(file.getName().replace(".txt", ""));

                            double[] hist = new double[size];
                            for(int j = 0; j < hist.length; j++){
                                hist[j] = myReader.nextInt();
                            }

                            hist = getNormalizedHistogram(hist);
                            //set histogram
                            histogram.setHistogram(hist);
                            //normalize histogram

                            //set histogram from text file
                            histograms.put(histogram.getFilename(), histogram);
                        }
                        myReader.close();
                    } catch (FileNotFoundException e) {
                        System.out.println("An error occurred.");
                    }

                }
            }
        }

    }

    /** Method that returns the normalized histogram
     * @param hist dataset image histogram
     * @return normalized histogram
     **/
    public double[] getNormalizedHistogram(double[] hist){
        double[] normal = new double[hist.length];
        // we don't have total number of pixels (480x360)
        // so we can use sum of all total colors
        // before normalization
        double totalPixels = Arrays.stream(hist).sum();

        for (int i = 0; i < hist.length; i++) {
            normal[i] = hist[i] / totalPixels;
        }

        return normal;
    }

    /** Getter Method for Map of histograms
     * @return Map of histograms
     **/
    public Map<String, ColorHistogram> getHistograms() {
        return histograms;
    }

    /** Method that returns histogram from image name
     * @param imageName dataset image name
     * @return histogram
     **/
    public ColorHistogram getHistogram(String imageName) {
        return histograms.get(imageName);
    }
}
