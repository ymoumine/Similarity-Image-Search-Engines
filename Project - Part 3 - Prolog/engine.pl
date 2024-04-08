% dataset(DirectoryName)
% this is where the image dataset is located
dataset('.\\imageDataset2_15_20\\').

% query(DirectoryName)
% this is where query histogram files are located
query('.\\queryImages\\').

% directory_textfiles(DirectoryName, ListOfTextfiles)
% produces the list of text files in a directory
directory_textfiles(D,Textfiles):- directory_files(D,Files), include(isTextFile, Files, Textfiles).
isTextFile(Filename):-string_concat(_,'.txt',Filename).

% read_query(Filename,ListOfNumbers)
% get the full path for each query image and read its histogram
read_query(Filename, Histo):- query(P),atom_concat( P, Filename, FullPath),read_hist_file(FullPath, Histo).

% read_hist_file(Filename,ListOfNumbers)
% reads a histogram file and produces a list of numbers (bin values)
read_hist_file(Filename,Numbers):- open(Filename,read,Stream),read_line_to_string(Stream,_),
                                   read_line_to_string(Stream,String), close(Stream),
								   atomic_list_concat(List, ' ', String),atoms_numbers(List,Numbers).
								   
% similarity_search(QueryFile, DatasetDirectory, HistoFileList, SimilarImageList)
% returns the list of images similar to the query image
% similar images are specified as (ImageName, SimilarityScore)
% predicat dataset/1 provides the location of the image set
similarity_search(QueryFile,SimilarList) :- dataset(D), directory_textfiles(D,TxtFiles),
                                            similarity_search(QueryFile,D,TxtFiles,SimilarList).


% similarity_search(QueryFile, DatasetDirectory, HistoFileList, SimilarImageList)
similarity_search(QueryFile,DatasetDirectory, DatasetFiles,Best):- read_query(QueryFile,QueryHisto), 
                                            compare_histograms(QueryHisto, DatasetDirectory, DatasetFiles, Scores), 
                                            sort(2,@>,Scores,Sorted),take(Sorted,5,Best).

% compare_histograms(QueryHisto,DatasetDirectory,DatasetFiles,Scores)
% compares a query histogram with a list of histogram files 
compare_histograms(QueryHisto, DatasetDirectory, DatasetFiles, Scores) :-
    findall((Filename, S),
            (member(Filename, DatasetFiles),
             atom_concat(DatasetDirectory, Filename, FullPath), % Get the full path to dataset images
             read_hist_file(FullPath, DatasetHisto), % Read the dataset histogram
             histogram_intersection(QueryHisto, DatasetHisto, S)), % Calculate the intersection
            Scores).

% histogram_intersection(Histogram1, Histogram2, Score)
% compute the intersection similarity score between two histograms
% Score is between 0.0 and 1.0 (1.0 for identical histograms)
histogram_intersection(H1, H2, S) :-
    maplist(d, H1, H2, MinPairs),
    sumlist(MinPairs, SumMinPairs),
    sumlist(H1, SumH1),
    S is SumMinPairs / SumH1.
d(X, Y, Min) :- Min is min(X, Y).

% take(List,K,KList)
% extracts the K first items in a list
take(Src,N,L) :- findall(E, (nth1(I,Src,E), I =< N), L).

% atoms_numbers(ListOfAtoms,ListOfNumbers)
% converts a list of atoms into a list of numbers
atoms_numbers([],[]).
atoms_numbers([X|L],[Y|T]):- atom_number(X,Y), atoms_numbers(L,T).
atoms_numbers([X|L],T):- \+atom_number(X,_), atoms_numbers(L,T).