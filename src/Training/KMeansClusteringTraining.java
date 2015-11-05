package Training;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import Distance.Euclidean;
import Feature.MFCC;
import SignalProcess.WavObj;
import SignalProcess.WaveIO;

public class KMeansClusteringTraining {
	private int _k;
	private int _dim;
	private Vector <Vector <double[]>> _clusterPoints;
	private Vector <double[]> _meanClusterPoints;
	
	private static final int DEFAULT_K = 5;
	private static final int DEFAULT_DIM = 12;
	
	private static final String DIR_TRAINING_FILES = "data/input/IEMOCAP_database";
	public static final String FILE_MODEL = "data/feature/kmeanclusteringmodel.txt";
	
	public KMeansClusteringTraining() {
		initClass(DEFAULT_K, DEFAULT_DIM);
	}
	
	public KMeansClusteringTraining(int k, int dim) {
		initClass(k, dim);
	}

	/**
	 * @param k
	 * @param dim
	 */
	private void initClass(int k, int dim) {
		setK(k);
		setDim(dim);
		setClusters(k, dim);
	}
	
	private void setK(int k) {
		_k = k;
	}
	
	public int getK() {
		return _k;
	}
	
	private void setClusters(int k, int dim) {
		_clusterPoints = new Vector <Vector <double[]>>();
		_meanClusterPoints = new Vector <double[]>();
		for (int i = 0; i < k; i++) {
			double[] initialPoint = new double[dim];
			Vector <double[]> points = new Vector < double[] >();
			for (int j = 0; j < dim; j++) {
				initialPoint[j] = i + 0.0;
			}
			points.add(initialPoint);
			_clusterPoints.add(points);
			_meanClusterPoints.add(initialPoint);
		}
	}
	
	public boolean train(double[] data) {
		if (data.length != _dim) {
			return false;
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
		Vector <double[]> clusterPointsAtIndex = _clusterPoints.get(chosenIndex);
		clusterPointsAtIndex.add(data);
		_clusterPoints.set(chosenIndex, clusterPointsAtIndex);
		
		int c = clusterPointsAtIndex.size();
		double[] avg = new double[_dim];
		for (int i = 0; i < _dim; i++) {
			avg[i] = 0;
		}
		
		for (int i = 0; i < c; i++) {
			double[] curData = clusterPointsAtIndex.get(i);
			for (int j = 0; j < _dim; j++) {
				avg[j] += curData[j];
			}
		}
		
		for (int i = 0; i < _dim; i++) {
			avg[i] /= (c + 0.0);
		}
		
		_meanClusterPoints.set(chosenIndex, avg);
		
		return true;
	}

	public int getDim() {
		return _dim;
	}

	private void setDim(int _dim) {
		this._dim = _dim;
	}
	
	public boolean saveClustersToFile(String filename) {
		//clear file
		if (!writeToFile(filename, false, "")) {
			return false;
		}
		
		//save
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < _k; i++) {
			double[] cluster = _meanClusterPoints.get(i);
			buffer.setLength(0);
			
			for (int j = 0; j < _dim; j++) {
				buffer.append(cluster[j]);
				buffer.append(" ");
			}
			buffer.append("\n");
			if (!writeToFile(filename, true, buffer.toString())) {
				return false;
			}
		}
		
		return true;
	}
	
	private boolean writeToFile(String filename, boolean isAppend, String line) {
		FileWriter fw;
		try {
			fw = new FileWriter(filename, isAppend);
			fw.write(line);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	public static void main (String[] args) {
		KMeansClusteringTraining machineLearning = new KMeansClusteringTraining();
		
		File trainingDir = new File(DIR_TRAINING_FILES);
		File[] trainingFiles = trainingDir.listFiles();
		
		int powerOfTwo = getFrameSizeThatApproxOneSecond();
    	
		WaveIO waveIO = new WaveIO();
    	MFCC mfcc = new MFCC(powerOfTwo);

		for (int i = 0; i < trainingFiles.length; i++) {
			System.out.println(i);
			WavObj waveObj = waveIO.constructWavObj(trainingFiles[i].getAbsolutePath());
			double[][] MFCC = mfcc.process(waveObj.getSignal());//13-d mfcc
			for(int j = 0; j < MFCC.length; j++) {
				if (!machineLearning.train(MFCC[j])) {
					return;
				}
			}
		}
		
		machineLearning.saveClustersToFile(FILE_MODEL);
	}

	/**
	 * @return
	 */
	public static int getFrameSizeThatApproxOneSecond() {
		int powerOfTwo = 1;
    	for (int i = 0; i < 15; i++) {
    		powerOfTwo = powerOfTwo << 1;
    	}
		return powerOfTwo;
	}
}
