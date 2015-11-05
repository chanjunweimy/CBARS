package Search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;

import SignalProcess.WavObj;
import SignalProcess.WaveIO;
import Training.KMeansClusteringTrainer;
import Distance.Euclidean;
import Feature.MFCC;

public class KMeansClusteringClassifier {
	private int _k = 0;
	private int _dim = 0;
	private Vector <double[]> _meanClusterPoints;
	
	public static final String DIR_TESTING_FILES = "data/input/emotionTest";


	
	public KMeansClusteringClassifier(String filename) {
		readFromFile(filename);
	}

	private void readFromFile(String filename) {
		_meanClusterPoints = new Vector <double[]>();
		try{
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine();
            _k = 0;
            while(line != null){
            	String[] tokens = line.split(" ");

            	if (_dim == 0) {
            		_dim = tokens.length;
            	}
            	
            	double[] data = new double[_dim];
            	for (int i = 0; i < tokens.length; i++) {
            		data[i] = Double.parseDouble(tokens[i]);
            	}
            	
            	_meanClusterPoints.add(data);
            	
            	_k++;
                line = br.readLine();
            }
            br.close();
        }catch (Exception e){
            e.printStackTrace();
        }		
	}
	
	public int calculateNearestCluster(double[] data) {
		if (data.length != _dim) {
			return -1;
		}
		
		Euclidean euclidean = Euclidean.getObject();
		double minDistance = 0;
		int chosenIndex = 0;
		for (int i = 0; i < _k; i++) {
			double distance = euclidean.getDistance(data, _meanClusterPoints.get(i));
			if (i == 0) {
				minDistance = distance;
			} else if (minDistance > distance) {
				chosenIndex = i;
				minDistance = Math.min(minDistance, distance);
			}
		}
		return chosenIndex;
	}
    
	
	public static void main (String[] args) {
		String fileName = KMeansClusteringTrainer.FILE_MODEL;
		KMeansClusteringClassifier classifier = new KMeansClusteringClassifier(fileName);		
		
		int powerOfTwo = KMeansClusteringTrainer.getFrameSizeThatApproxOneSecond();
    	
		WaveIO waveIO = new WaveIO();
    	MFCC mfcc = new MFCC(powerOfTwo);

    	/*
    	File testingDir = new File(DIR_TESTING_FILES);
		File[] testingFiles = testingDir.listFiles();
		
		showClusters(classifier, waveIO, mfcc, testingFiles);
		*/
		
    	File trainingDir = new File(KMeansClusteringTrainer.DIR_TRAINING_FILES);
		File[] trainingFiles = trainingDir.listFiles();
		showClusters(classifier, waveIO, mfcc, trainingFiles);
	}

	/**
	 * @param classifier
	 * @param waveIO
	 * @param mfcc
	 * @param files
	 */
	private static void showClusters(KMeansClusteringClassifier classifier,
			WaveIO waveIO, MFCC mfcc, File[] files) {
		
		for (int i = 0; i < files.length; i++) {
			System.out.println(files[i].getName());
			WavObj waveObj = waveIO.constructWavObj(files[i].getAbsolutePath());
			Vector <short[]> signals = waveObj.splitToSignals();
			for (int j = 0; j < signals.size(); j++) {
				mfcc.process(signals.get(j));//13-d mfcc
				double[] meanMfcc = mfcc.getMeanFeature();
				int index = classifier.calculateNearestCluster(meanMfcc);
				System.out.print(index + " ");
			}

			System.out.println("end");

		}
	}
}
