;/*
;----------------------------------------------------------
;
;	Class : CSI2520 Programming Paradigms
;	Project : Similarity image search - Part 4 (Scheme)
;
;	Full Name : Yassine Moumine
;
;	Student Number : 300140139
;
;----------------------------------------------------------
;*/

#lang racket

; counts the total pixels represented in a histogram.
; (countPixels '(histogram values))
(define (countPixels histogram)
  (let loop-count ((remainingHistogram histogram) (total 0))
    (if (null? remainingHistogram)
        total
        (loop-count (cdr remainingHistogram) (+ (car remainingHistogram) total)))))

; normalizes a histogram by dividing each value by the total number of pixels.
; (normalize '(histogram values) totalPixels)
(define (normalize histogram totalPixels)
  (map (lambda (value) (/ value totalPixels)) histogram))

; calculates the histogram intersection between two histograms.
; (hist-intersect '(query histogram) '(dataset histogram))
(define (hist-intersect queryHistogram datasetHistogram)
  (let loop-intersect ((qHist queryHistogram) (dHist datasetHistogram) (intersectionSum 0))
    (if (null? qHist)
        intersectionSum
        (loop-intersect (cdr qHist) (cdr dHist) (+ intersectionSum (min (car qHist) (car dHist)))))))

; sorts a list of (filename, value) pairs by value in descending order.
; (sort-by-value '((filename1 val1) (filename2 val2) ...))
(define (sort-by-value pairsList)
  (sort pairsList (lambda (pair1 pair2) (> (cdr pair1) (cdr pair2)))))

; displays the top five images based on their similarity score.
; (displayTopFive similarityScores)
(define (displayTopFive similarityScores)
  (for-each (lambda (score) (displayln score)) (take similarityScores 5)))

; performs a similarity search between a query histogram and a dataset, displaying top 5 similar images.
; (similaritySearch "queryImages/file.jpg.txt" "imageDataset2_15_20/")
(define (similaritySearch queryHistogramFile datasetDirectory)
  (let ((queryHist (read-hist-file queryHistogramFile))
        (datasetFiles (list-text-files-in-directory datasetDirectory)))
    (let ((scores (map (lambda (datasetFile)
                         (let ((datasetHist (read-hist-file (string-append datasetDirectory datasetFile))))
                           (cons datasetFile (hist-intersect (normalize queryHist (countPixels queryHist))
                                                             (normalize datasetHist (countPixels datasetHist))))))
                       datasetFiles)))
      (displayTopFive (sort-by-value scores)))))

; get the list of all textfiles in a directory
; (list-text-files-in-directory "\\imageDataset2_15_20")
(define (list-text-files-in-directory directory-path)
  (filter (lambda (file)
            (string-suffix? file ".txt"))
          (map path->string (directory-list directory-path))))

; read a histogram textfile and returns the values in a list
; (read-hist-file "\\q00.jpg.txt")
(define (read-hist-file filename) 
  (cdr (call-with-input-file filename
        (lambda (port)
          (let loop-read ((nextItem (read port)))
            (if (eof-object? nextItem) '() (cons nextItem (loop-read (read port)))))))))


(similaritySearch "queryImages/q13.jpg.txt" "imageDataset2_15_20/")