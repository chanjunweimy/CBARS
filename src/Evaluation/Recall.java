package Evaluation;

import java.io.File;
import java.util.TreeMap;

/**
 * Created by workshop on 10/1/2015.
 */
public class Recall {
    public static final String FILEPATH_IMAGE_TRAIN= "/data/input/train";
    public static final String FILEPATH_IMAGE_TEST = "/data/input/test";
    public static final String[] TAGS = {"bus", "busystreet", "office", "openairmarket", 
    		"park", "quietstreet", "restaurant", "supermarket", "tube", "tubestation"}; 
    
    private static File folder;
    private static File[] listOfTrainImages;
    private static TreeMap<String, Integer> nameToFreq;
    
    public Recall() {
    	folder = new File(FILEPATH_IMAGE_TRAIN);
    	listOfTrainImages = folder.listFiles();
    	int counter = 0;
    	
    	// Initialize the TreeMap
    	for (int i = 0; i < TAGS.length; i++) {
    		nameToFreq.put(TAGS[i], 0);
    	}
    	
    	for (int i = 0; i < listOfTrainImages.length; i++) {
    		if (listOfTrainImages[i].isFile()) {
    			String fileName = listOfTrainImages[i].getName();
    			for (int j = 0; j < TAGS.length; j++) {
    				if (fileName.contains(TAGS[j])) {
    					counter = nameToFreq.get(TAGS[j]) + 1;
    					nameToFreq.put(TAGS[j], counter);
    				}
    			}
    		}
    	}
    }
    
    public double getRecall(String fileName, String[] generatedResults) {
		String tag = null;
        int truePositive = 0;

        for (int i = 0; i < TAGS.length; i++) {
            if (fileName.contains(TAGS[i])) {
                tag = TAGS[i];
            }
        }
        
        for (int i = 0; i < generatedResults.length; i++) {
            if (generatedResults[i].contains(tag)) {
                truePositive++;
            }
        }
        
        int falseNegative = nameToFreq.get(tag) - truePositive;
        double recall = truePositive / (truePositive + falseNegative);
        
        return recall;
    }
}
