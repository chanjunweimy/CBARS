package Evaluation;

/**
 * Created by workshop on 9/18/2015.
 */
public class Precision {
    public static final String FILEPATH_IMAGE_TRAIN= "/data/input/train";
    public static final String FILEPATH_IMAGE_TEST = "/data/input/test";
    public static final String[] TAGS = {"bus", "busystreet", "office", "openairmarket", 
    		"park", "quietstreet", "restaurant", "supermarket", "tube", "tubestation"}; 
    
    public Precision() {

    }
    
	public double getPrecision(String fileName, String[] generatedResults) {
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
    	
        int falsePositive = generatedResults.length - truePositive;
        double precision = truePositive / (truePositive + falsePositive);

        return precision;
	}
}
