/*
----------------------------------------------------------

	Class : CSI2520 Programming Paradigms
	Project : Similarity image search - Part 1 (JAVA)

	Full Name : Yassine Moumine

	Student Number : 300140139

----------------------------------------------------------
*/
package main

import (
	"fmt"
	"image"
	_ "image/jpeg"
	"io/ioutil"
	"log"
	"math"
	"os"
	"sort"
	"strings"
	"sync"
	"time"
)

// interface for ceating sub slices for each experiment (K)
type KSlice interface {
	CreateKSlice() [][]string
}

// define an experiment
type Expo struct {
	filenames []string
	K         int
}

// structure for list of similarity indexes
type SimilarityMatch struct {
	filename   string
	similarity float64
}

// list of SimilarityMatch to use sort
type SimilarityList []SimilarityMatch

// the following are necessary definitions to use the method sort:

// 1. Less compares values of pairs at two indices.
func (p SimilarityList) Less(i, j int) bool { return p[i].similarity < p[j].similarity }

// 2. Swap swaps pairs at two indices.
func (p SimilarityList) Swap(i, j int) { p[i], p[j] = p[j], p[i] }

// 3. Len returns the length of the PairList.
func (s SimilarityList) Len() int { return len(s) }

// type for histogram
type Histo struct {
	Name string
	H    []int
	NH   []float64
}

// method for creating the K sub slices appropriate for each experiment
func (e Expo) CreateKSlice() [][]string {

	length := len(e.filenames)
	sub := int(length / e.K)
	var res [][]string

	for i := 0; i < length; i += sub {
		end := i + sub
		if end > length {
			// fix rounding up from length division
			// int (2485 / 2) = 1243 * 2 = 2486 > 2485
			end = length
		}
		res = append(res, e.filenames[i:end])
	}
	return res
}

// method for creating each experiment, it's sub slices and adding to the overall map of experiments
func createExperiments(filenames []string) (m map[string][][]string) {

	m = make(map[string][][]string)

	var cs KSlice

	// split filenames into K slices for each experiment and add it to map m
	// Experiment 1
	// k := 1;
	exp1 := Expo{filenames, 1}
	cs = exp1
	m["exp1"] = cs.CreateKSlice()

	// Experiement 2
	// k := 2;
	exp2 := Expo{filenames, 2}
	cs = exp2
	m["exp2"] = cs.CreateKSlice()

	// Experiement 3
	// k := 4;
	exp3 := Expo{filenames, 4}
	cs = exp3
	m["exp3"] = cs.CreateKSlice()

	// Experiement 4
	// k := 16;
	exp4 := Expo{filenames, 16}
	cs = exp4
	m["exp4"] = cs.CreateKSlice()

	// Experiement 5
	// k := 64;
	exp5 := Expo{filenames, 64}
	cs = exp5
	m["exp5"] = cs.CreateKSlice()

	// Experiement 6
	// k := 256;
	exp6 := Expo{filenames, 256}
	cs = exp6
	m["exp6"] = cs.CreateKSlice()

	// Experiement 7
	// k := 1048;
	exp7 := Expo{filenames, 256}
	cs = exp7
	m["exp7"] = cs.CreateKSlice()

	return
}

/* This function computes the histogram of a slice of image filenames */
func computeHistograms(imagePath []string, depth int, hChan chan<- Histo) {

	var _wg sync.WaitGroup

	// for each image in a slice
	for _, e := range imagePath {

		_wg.Add(1)

		// goroutine for each image
		go func(e string) {
			defer _wg.Done()

			// instance of computeHistogram
			histo, err := computeHistogram("imageDatasetDirectory/"+e, depth)

			if err != nil {
				return
			}

			hChan <- histo

		}(e)

	}

	_wg.Wait()

}

/* This function computes the histogram of the specified jpeg image and reduces it to the number of bits given by the depth parameter. */
func computeHistogram(imagePath string, depth int) (Histo, error) {

	//defer wg.Done()

	// Open the JPEG file
	file, err := os.Open(imagePath)

	if err != nil {
		return Histo{"", nil, nil}, err
	}
	defer file.Close()

	// Decode the JPEG image
	img, _, err := image.Decode(file)
	if err != nil {
		return Histo{"", nil, nil}, err
	}

	// Get the dimensions of the image
	bounds := img.Bounds()
	width, height := bounds.Max.X, bounds.Max.Y

	// create histo
	h := Histo{imagePath, make([]int, int(math.Pow(2, float64(depth*3)))), make([]float64, int(math.Pow(2, float64(depth*3))))}

	// Display RGB values for the first 5x5 pixels
	for y := 0; y < height; y++ {
		for x := 0; x < width; x++ {

			// Convert the pixel to RGBA
			red, green, blue, _ := img.At(x, y).RGBA()
			// A color's RGBA method returns values in the range [0, 65535].
			// Shifting by 8 reduces this to the range [0, 255].

			red = red >> 8 >> (8 - depth)
			blue = blue >> 8 >> (8 - depth)
			green = green >> 8 >> (8 - depth)

			// Display the RGB values
			//fmt.Printf("Pixel at (%d, %d): R=%d, G=%d, B=%d\n", x, y, red, green, blue)

			// computer index
			index := (red << (2 * depth)) + (green << depth) + blue

			// count histogram
			h.H[index]++
		}
	}

	//fmt.Print(width * height)

	// normalize histogram
	normalize(&h, width*height)

	return h, nil
}

func normalize(hist *Histo, totalPixels int) {
	for i := range hist.H {
		hist.NH[i] = float64(hist.H[i]) / float64(totalPixels)
	}
}

func readDataset(folder string) (filenames []string) {

	files, err := ioutil.ReadDir(folder)
	if err != nil {
		log.Fatal(err)
	}

	// get the list of jpg files
	for _, file := range files {
		if strings.HasSuffix(file.Name(), ".jpg") {
			filenames = append(filenames, file.Name())
		}
	}

	// return the array of stored filenames
	return
}

func compare(query *Histo, data *Histo) (d float64) {
	//defer wg.Done()
	for i := range query.NH {
		d += math.Min(query.NH[i], data.NH[i])
		//fmt.Print(query.NH[i])
	}

	return
}

func main() {
	// read the image name from command line
	args := os.Args

	// get the list of all image filename in the dataset directory
	df := readDataset(args[2])

	// 3.1 Split this list into K slices - experiment in map m
	m := createExperiments(df)

	var wg sync.WaitGroup

	var debut time.Time

	//  thread for each experiment - compute dataset histograms and runtime for each experiment
	for expName, slices := range m {

		// Create a channel of histograms each experiment
		expCh := make(chan Histo)

		debut = time.Now()

		// goroutine for each experiment/thread
		go func(slices [][]string, expCh chan<- Histo) {
			//var Swg sync.WaitGroup
			wg.Add(len(slices))
			for _, slice := range slices {
				// 3.2 send each slice to the go function computeHistograms
				// goroutine for each slice
				go func(slice []string) {
					defer wg.Done()
					computeHistograms(slice, 3, expCh)
				}(slice)
			}
			// close threads
			wg.Wait()

			// close channels
			close(expCh)

		}(slices, expCh)

		var wg_ sync.WaitGroup

		// path to query image
		qPath := "queryImages/" + args[1]

		// initiate query histo
		var qHist Histo

		wg_.Add(1)

		// Create a separate thread to open the query image and compute its histogram
		go func() {
			defer wg_.Done()
			var err error
			qHist, err = computeHistogram(qPath, 3)

			if err != nil {
				fmt.Print("Error occured when computing query histogram!")
			}
		}()

		var histoList SimilarityList

		// thread to read when the histogram is recieved
		var _wg sync.WaitGroup
		_wg.Add(1)
		go func(expCh <-chan Histo) {

			defer _wg.Done()

			for histo := range expCh {

				// Compare dataset histograms with query histogram
				d := compare(&qHist, &histo)

				// save SimilarityMatch in a list to later sort
				histoList = append(histoList, SimilarityMatch{histo.Name, d})
			}

			// Sort the similarity list in descending order based on similarity (in Less)
			sort.Sort(sort.Reverse(histoList))

			//computer runtime of each experiment
			fin := time.Now()
			fmt.Printf("Runtime for %s: %s \n", expName, fin.Sub(debut))

			//Print the list of the 5 most similar images - comment the 4 lines below for better runtime results
			fmt.Print("Top 5 most similar images: \n\n")
			for e := 0; e < 5; e++ {
				fmt.Printf("+ image: %s, with a similarity index of: %%%f \n", strings.Split(histoList[e].filename, "/")[1], histoList[e].similarity*100)
			}
			fmt.Print("\n")
		}(expCh)

		// Wait for all goroutines
		_wg.Wait()
		wg.Wait()
	}
}
