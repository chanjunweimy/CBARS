package Training;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import Feature.Energy;
import Feature.MFCC;
import Feature.MagnitudeSpectrum;
import Feature.ZeroCrossing;
import SignalProcess.WaveIO;

public class AudioFeaturesGenerator {
	private static final String FILEPATH_AUDIO_TRAIN = "data/input/train";
	private static final String FILEPATH_EMOTION_TRAIN = "data/input/EmotionSpeechDatabase_Toronto";
	private static final String FILEPATH_FEATURE_OUT = "data/feature";
	
	public boolean computeMFCC(File[] audioFiles, String filename) {
		writeToFile(filename, false, "");
		for (int i = 0; i < audioFiles.length; i++) {
			String audioName = audioFiles[i].getAbsolutePath();
			WaveIO waveio = new WaveIO();
			short[] signal = waveio.readWave(audioName);
			
			MFCC mfcc = new MFCC();
			mfcc.process(signal);
			double[] mean = mfcc.getMeanFeature();
			StringBuffer buffer = new StringBuffer();
			
			buffer.append(audioFiles[i].getName());
			for (int j = 0; j < mean.length; j++) {
				buffer.append(" "); 
				buffer.append(mean[j]);
			}
			buffer.append("\n");
			if (!writeToFile(filename, true, buffer.toString())) {
				return false;
			}
		}
		return true;
	}
	
	public boolean computeEnergy(File[] audioFiles, String filename) {
		writeToFile(filename, false, "");
		for (int i = 0; i < audioFiles.length; i++) {
			String audioName = audioFiles[i].getAbsolutePath();
			WaveIO waveio = new WaveIO();
			short[] signal = waveio.readWave(audioName);
			
			Energy energy1 = new Energy();
			double[] feature = energy1.getFeature(signal);
			StringBuffer buffer = new StringBuffer();
			
			buffer.append(audioFiles[i].getName());

			for (int j = 0; j < feature.length; j++) {
				buffer.append(" ");
				buffer.append(feature[j]);
			}
			buffer.append("\n");
			if (!writeToFile(filename, true, buffer.toString())) {
				return false;
			}
		}
		return true;
	}
	
	public boolean computeMagnitudeSpectrum(File[] audioFiles, String filename) {
		writeToFile(filename, false, "");
		for (int i = 0; i < audioFiles.length; i++) {
			String audioName = audioFiles[i].getAbsolutePath();
			WaveIO waveio = new WaveIO();
			short[] signal = waveio.readWave(audioName);
			
			MagnitudeSpectrum spectrum = new MagnitudeSpectrum();
			double[] feature = spectrum.getFeature(signal);
			StringBuffer buffer = new StringBuffer();
			
			buffer.append(audioFiles[i].getName());
			for (int j = 0; j < feature.length; j++) {
				buffer.append(" ");
				buffer.append(feature[j]);
			}
			buffer.append("\n");
			if (!writeToFile(filename, true, buffer.toString())) {
				return false;
			}
		}
		return true;
	}
	
	public boolean computeZeroCrossing(File[] audioFiles, String filename) {
		writeToFile(filename, false, "");
		for (int i = 0; i < audioFiles.length; i++) {
			String audioName = audioFiles[i].getAbsolutePath();
			WaveIO waveio = new WaveIO();
			short[] signal = waveio.readWave(audioName);
			
			ZeroCrossing zc = new ZeroCrossing();
			double[] feature = zc.getFeature(signal);
			StringBuffer buffer = new StringBuffer();
			
			buffer.append(audioFiles[i].getName());

			for (int j = 0; j < feature.length; j++) {
				buffer.append(" ");
				buffer.append(feature[j]);
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
	
	public File createFile(String filepath) {
		File f = new File(filepath);
		f.getParentFile().mkdirs(); 
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return f;
	}
	
	public static void main(String[] args) {
		AudioFeaturesGenerator featureGenerator = new AudioFeaturesGenerator();

		/*
		File audioTrain = new File(FILEPATH_AUDIO_TRAIN);
		File[] audioFiles = audioTrain.listFiles();
		*/	
		File emotionTrain = new File(FILEPATH_EMOTION_TRAIN);
		File[] emotionFiles = emotionTrain.listFiles();
		
		/*
		File audioMfccFile = featureGenerator.createFile(FILEPATH_FEATURE_OUT + "/audio_mfcc.txt");
		File audioEnergyFile = featureGenerator.createFile(FILEPATH_FEATURE_OUT + "/audio_energy.txt");
		File audioSpectrumFile = featureGenerator.createFile(FILEPATH_FEATURE_OUT + "/audio_spectrum.txt");
		File audioZCFile = featureGenerator.createFile(FILEPATH_FEATURE_OUT + "/audio_zerocrossing.txt");
		*/
		File emotionMfccFile = featureGenerator.createFile(FILEPATH_FEATURE_OUT + "/emotion_mfcc.txt");
		File emotionEnergyFile = featureGenerator.createFile(FILEPATH_FEATURE_OUT + "/emotion_energy.txt");
		File emotionSpectrumFile = featureGenerator.createFile(FILEPATH_FEATURE_OUT + "/emotion_spectrum.txt");
		File emotionZCFile = featureGenerator.createFile(FILEPATH_FEATURE_OUT + "/emotion_zerocrossing.txt");
		
		/*
		if (!featureGenerator.computeMFCC(audioFiles, audioMfccFile.getAbsolutePath())) {
			System.exit(-1);
		} else if (!featureGenerator.computeEnergy(audioFiles, audioEnergyFile.getAbsolutePath())) {
			System.exit(-1);
		} else if (!featureGenerator.computeMagnitudeSpectrum(audioFiles, audioSpectrumFile.getAbsolutePath())) {
			System.exit(-1);
		} else if (!featureGenerator.computeZeroCrossing(audioFiles, audioZCFile.getAbsolutePath())) {
			System.exit(-1);
		} 
		*/
		
		if (!featureGenerator.computeMFCC(emotionFiles, emotionMfccFile.getAbsolutePath())) {
			System.exit(-1);
		} else if (!featureGenerator.computeEnergy(emotionFiles, emotionEnergyFile.getAbsolutePath())) {
			System.exit(-1);
		} else if (!featureGenerator.computeMagnitudeSpectrum(emotionFiles, emotionSpectrumFile.getAbsolutePath())) {
			System.exit(-1);
		} else if (!featureGenerator.computeZeroCrossing(emotionFiles, emotionZCFile.getAbsolutePath())) {
			System.exit(-1);
		}
	}
}
