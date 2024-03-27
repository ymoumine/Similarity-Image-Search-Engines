/**
 * Class : CSI2520 Programming Paradigms
 * Project : Similarity image search - Part 1 (JAVA)
 * Full Name : Yassine Moumine
 * Student Number : 300140139
 **/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/** Class that computes and image and each 3 channel value (RGB) of its pixels **/
public class ColorImage {

    /** Width of image **/
    private int width;

    /** Height of image **/
    private int height;

    /** Bits per pixel **/
    private int depth;

    /** 3 channel RBG value for each pixel **/
    private int[][][] pixel;

    /** Constructor that creates an image from a file
     * @param filename image file name
     **/
    public ColorImage(String filename){

        try {
            File myObj = new File("./queryImages/"+filename);
            Scanner myReader = new Scanner(myObj);

            myReader.nextLine();
            myReader.nextLine();

                // width of image
                setWidth(myReader.nextInt());

                // height of image
                setHeight(myReader.nextInt());

                // the color space of 8-bit 2^8 = 255+1 bits that will be reduced to d-bits in reduce method
                setDepth((int)(Math.log(myReader.nextInt()+1) / Math.log(2)));

                // number of pixels from resolution
                this.pixel = new int[width][height][];

                for(int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        // 3 channel RGB values
                        setPixel(x,y, new int[]{myReader.nextInt(), myReader.nextInt(), myReader.nextInt()});
                    }
                }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }
    }

    /** Method that reduces the color space from 8-bit to a d-bit representation
     * @param d bit representation
     * **/
    public void reduceColor(int d){
        setDepth(d);
        for(int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                //right bit shifts
                pixel[x][y][0] = pixel[x][y][0]>>(8 - d); // R
                pixel[x][y][1] = pixel[x][y][1]>>(8 - d); // G
                pixel[x][y][2] = pixel[x][y][2]>>(8 - d); // B
            }
        }
    }

    /** Method that returns the 3-channel value of pixel at column i row j in the
     form of a 3-element array
     * @param i column position of pixel
     * @param j row position of pixel
     * @return 3-channel value of pixel R G B
     **/
    public int[] getPixel(int i, int j) {
        //return 3 channel value of pixel at position [i][j]
        return pixel[i][j];
    }

    /** Setter method that sets an RGB color array in pixel at position [i][j]
     * @param i x coordinate
     * @param j y coordinate
     * @param x 3 channel RGB value array
     * **/
    public void setPixel(int i, int j, int[] x) {
        pixel[i][j] = x;
    }

    /** Getter method that returns image width
     * @return width
     * **/
    public int getWidth() {
        return width;
    }

    /** Setter method that sets image width
     * @param width image width
     * **/
    public void setWidth(int width) {
        this.width = width;
    }

    /** Getter method that returns image height
     * @return image height
     * **/
    public int getHeight() {
        return height;
    }

    /** Setter method that sets image height
     * @param height image width
     * **/
    public void setHeight(int height) {
        this.height = height;
    }

    /** Getter method that returns bit representation
     * @return depth
     * **/
    public int getDepth() {
        return depth;
    }

    /** Setter method that sets bit value
     * @param depth bit value
     * **/
    public void setDepth(int depth) {
        this.depth = depth;
    }

}
