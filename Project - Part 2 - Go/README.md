## HOW TO RUN

Please run the following command:

    go run similaritySearch.go q00.jpg imageDatasetDirectory

## EXPECTED OUTPUT

Runtime for exp7: 4.1969573s
Top 5 most similar images:

+ image: 1144.jpg, with a similarity index of: %100.000000
+ image: 3756.jpg, with a similarity index of: %66.060764
+ image: 3714.jpg, with a similarity index of: %65.968750
+ image: 3668.jpg, with a similarity index of: %64.330440
+ image: 2462.jpg, with a similarity index of: %64.289352

Runtime for exp1: 4.6144754s 
Top 5 most similar images: 

+ image: 1144.jpg, with a similarity index of: %100.000000 
+ image: 3806.jpg, with a similarity index of: %70.400463 
+ image: 3756.jpg, with a similarity index of: %66.060764 
+ image: 3714.jpg, with a similarity index of: %65.968750 
+ image: 2462.jpg, with a similarity index of: %64.289352 

Runtime for exp2: 4.0564023s 
Top 5 most similar images:

+ image: 1144.jpg, with a similarity index of: %100.000000
+ image: 3806.jpg, with a similarity index of: %70.400463
+ image: 3756.jpg, with a similarity index of: %66.060764
+ image: 3714.jpg, with a similarity index of: %65.968750
+ image: 3668.jpg, with a similarity index of: %64.330440

 ...

 ## Experiences

 In order to correctly compute the runtime of each experience comment out the lines 351 to 355:

    //Print the list of the 5 most similar images - comment the 4 lines below for better runtime results
	fmt.Print("Top 5 most similar images: \n\n")
	for e := 0; e < 5; e++ {
		fmt.Printf("+ image: %s, with a similarity index of: %%%f \n", strings.Split(histoList[e].filename, "/")[1], histoList[e].similarity*100)
	}
	fmt.Print("\n")