## HOW TO RUN

### Step 1: 
#### Install SWI-Prolog

### Step 2: 
#### Open up SWI-Prolog. Once SWI-Prolog starts, you will see a command line that looks like ?-

### Step 3: 
#### In SWI-Prolog, load the 'engine.pl' file by using the consult command like so:
	?- consult('engine.pl').

### Step 4: 
#### Now that the file is loaded, you can use it. Run:
	?- similarity_search('q00.jpg.txt',S)

## EXPECTED OUTPUT
	S = [('1144.jpg.txt', 1), ('3806.jpg.txt', 0.7074537037037038), ('3714.jpg.txt', 0.668431712962963), ('3756.jpg.txt', 0.6620254629629629), ('2462.jpg.txt', 0.6491030092592592)] .