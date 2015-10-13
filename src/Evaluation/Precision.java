package Evaluation;

/**
 * Created by workshop on 9/18/2015.
 */
public class Precision {
    
    
    public Precision() {

    }
    
	public double getPrecision(String fileName, String[] generatedResults, String[] tags) {
		String tag = null;
        int truePositive = 0;

        for (int i = 0; i < tags.length; i++) {
            if (fileName.contains(tags[i])) {
                tag = tags[i];
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
