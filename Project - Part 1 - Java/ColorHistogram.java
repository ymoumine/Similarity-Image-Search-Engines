/**
 * Class : CSI2520 Programming Paradigms
 * Project : Similarity image search - Part 1 (JAVA)
 * Full Name : Yassine Moumine
 * Student Number : 300140139
 **/

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/** Class that computes the histogram values of a given ColorImage **/
public class ColorHistogram {

    /** Histogram value of each value for a given image **/
    private double[] histogram;

    /** Histogram size **/
    private int size;

    /** bit representation **/
    private int depth;

    /** Image filename **/
    private String filename;

    /** Associated image **/
    private ColorImage givenImage;

    /** Constructor that creates a class instance for d-bit image
     * @param d number of bits **/
    public ColorHistogram(int d) {
        this.size = (int)Math.pow(2, d * 3);
        this.histogram = new double[size];
        this.depth = d;
    }

    /** Constructor that creates a class instance from a text file
     * @param filename name of image file **/
    public ColorHistogram(String filename){
        this.filename = filename;
    }

    /** Method that associate an image with a histogram instance
     * @param image ColorImage instance **/
    public void setImage(ColorImage image){

        this.givenImage = image;

        int width = givenImage.getWidth();
        int height = givenImage.getHeight();
        int depth = givenImage.getDepth();

        //reduce pixel values
        givenImage.reduceColor(getDepth());

        //b
        this.size = (int)Math.pow(2, depth * 3);
        this.histogram = new double[size];

        //get RGB values
        for(int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                //givenImage.reduceColor(depth);

                int[] color = givenImage.getPixel(x,y);
                // color[0] => R'
                // color[1] => G'
                // color[2] => B'


                //count # of pixels for each color
                this.histogram[(color[0] << (2 * depth)) + (color[1] << depth) + (color[2])]++;
            }
        }

        //normalize histogram
        this.histogram = getNormalizedHistogram();
    }

    /** Method that returns the normalized histogram of the image
     * @return normalized histogram
     **/
    public double[] getNormalizedHistogram(){
        double[] normal = new double[size];
        int totalPixels = givenImage.getWidth() * givenImage.getHeight();

        for (int i = 0; i < size; i++) {
            normal[i] = histogram[i] / totalPixels;
        }

        return normal;
    }

    /** Getter method that image histogram
     * @return histogram
     * **/
    public double[] getHistogram(){
        return histogram;
    }

    public void setHistogram(double[] histogram){
        this.histogram = histogram;
    }

    /** Method that returns the intersection between two histograms
     * @param hist two histograms?
     * @return Intersection between two histograms
     **/
    public double compare(ColorHistogram hist){
        double d = 0;
        double[] other = hist.getHistogram();

        for (int i = 0; i< histogram.length; i++){
            d += Math.min(other[i],this.histogram[i]);
        }

        return d;
    }

    /** Method saves the histogram into a text file
     * @param filename saved text file
     **/
    public void save(String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename.replace(".ppm",".txt")));

        writer.write(histogram.length+"\n");

        writer.write(this.givenImage.getWidth() + " ");
        writer.write(this.givenImage.getHeight() + "\n");

        for (double v : histogram) {
            writer.write(v + " ");
        }

        writer.close();
    }

    /** Getter method to return depth
     * @return depth
     **/
    public int getDepth() {
        return depth;
    }

    /** Getter method to return filename
     * @return filename
     **/
    public String getFilename() {return filename;}
}
