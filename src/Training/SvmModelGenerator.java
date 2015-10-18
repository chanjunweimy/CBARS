package Training;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import Evaluation.EvaluationFacade;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

public class SvmModelGenerator {
	private static final String EXT = ".wav";
	public static final String SVM_MODEL_FILE = AudioFeaturesGenerator.FILEPATH_FEATURE_OUT + "/svm_model.txt";
		
	svm_model _model = null;
	public SvmModelGenerator() {
		_model = trainModel();
	}
	
	public boolean saveModelFile(String filename) {
		try {
			svm.svm_save_model(filename, _model);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	private svm_problem retrieveSvmProblemFromFiles() {
		svm_problem prob = new svm_problem();
		HashMap <String, double[]> emotionMfcc = readFeature(AudioFeaturesGenerator.EMOTION_MFCC);
		
		int size = emotionMfcc.size();
		
		double[] outcomes = new double[size];
		svm_node[][] datas = null;
		int index = 0;
		for (Map.Entry<String, double[]> entry : emotionMfcc.entrySet()) {
		    String key = entry.getKey();
		    outcomes[index] = retrieveOutcome(key);
		    
		    double[] value = entry.getValue();
		    if (datas == null) {
		    	datas = new svm_node[size][value.length];
		    }
		    
		    for (int i = 0; i < value.length; i++) {
		    	datas[index][i] = new svm_node();
		    	datas[index][i].index = i + 1;
		    	datas[index][i].value = value[i];
		    }
		    index++;
		}
		
		prob.l = size;
		prob.x = datas;
		prob.y = outcomes;
		
		return prob;
	}
	
	private svm_parameter getTrainingParameter() {
		svm_parameter param = new svm_parameter();
		param.svm_type = svm_parameter.NU_SVC;
		param.kernel_type = svm_parameter.RBF;
		param.gamma = 1;
		param.eps = 0.001;
		param.cache_size = 100;
		param.C = 1;
		param.nu = 0.5;
		param.shrinking = 1;
		param.probability = 0;
		
		return param;	
	}
	
	private svm_model trainModel() {
		svm_problem prob = retrieveSvmProblemFromFiles();
		svm_parameter param = getTrainingParameter();
		svm_model model = svm.svm_train(prob, param);
		return model;
		
	}
	
	private double retrieveOutcome(String key) {
		String[] tags = EvaluationFacade.EMOTION_TAGS;
		for (int i = 0; i < tags.length; i++) {
			if (key.endsWith(tags[i] + EXT)) {
				return i + 1;
			}
		}
		return 0;
	}
	
	private HashMap<String, double[]> readFeature(String featurePath){
        HashMap<String, double[]> fList = new HashMap<>();
        try{
            FileReader fr = new FileReader(featurePath);
            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine();
            while(line != null){
            	line = line.replaceAll("\\s", " ");
                String[] split = line.trim().split(" ");
                if (split.length < 2)
                    continue;
                double[] fs = new double[split.length - 1];
                for (int i = 1; i < split.length; i ++){
                    fs[i-1] = Double.valueOf(split[i]);
                }

                fList.put(split[0], fs);

                line = br.readLine();
            }
            br.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return fList;
    }
	
	public static void main (String[] args) {
		SvmModelGenerator svm = new SvmModelGenerator();
		svm.saveModelFile(SVM_MODEL_FILE);
	}
}
