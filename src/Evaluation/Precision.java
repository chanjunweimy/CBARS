package Evaluation;

/**
 * Created by workshop on 9/18/2015.
 */
public class Precision {
    String[] _tags = null;
    
    public Precision(String[] tags) {
    	_tags = tags;
    }
    
	public double getPrecision(String fileName, String[] generatedResults) {
		String tag = null;
        int truePositive = 0;

        for (int i = 0; i < _tags.length; i++) {
            if (fileName.contains(_tags[i])) {
                tag = _tags[i];
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
