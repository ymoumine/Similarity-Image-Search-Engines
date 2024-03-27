/**
 * Class : CSI2520 Programming Paradigms
 * Project : Similarity image search - Part 1 (JAVA)
 * Full Name : Yassine Moumine
 * Student Number : 300140139
 **/

/** Class that saves the similarity record of each filename in previous dataset **/
public class SimilarityMatch {
    private final String filename;
    private final double similarity;

    /** Constructor for SimilarityMatch Instance
     * @param filename dataset directory
     * @param similarity dataset directory
     **/
    public SimilarityMatch(String filename, double similarity) {
        this.filename = filename;
        this.similarity = similarity;
    }

    /** Getter method to return filename
     * @return filename
     **/
    public String getFilename() {
        return filename;
    }

    /** Getter method to return similarity/intersection value
     * @return similarity/intersection value
     **/
    public double getSimilarity() {
        return similarity;
    }
}
