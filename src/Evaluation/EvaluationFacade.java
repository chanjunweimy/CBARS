package Evaluation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import Tool.Stats;

public class EvaluationFacade {
	public static final String FILEPATH_AUDIO_TRAIN = "data/input/train";
	public static final String FILEPATH_AUDIO_TEST = "data/input/test";
	public static final String[] AUDIO_TAGS = { "bus", "busystreet", "office",
			"openairmarket", "park", "quietstreet", "restaurant",
			"supermarket", "tube", "tubestation" };

	public static final String FILEPATH_EMOTION_TRAIN = "data/input/EmotionSpeechDatabase_Toronto";
	public static final String FILEPATH_EMOTION_TEST = "data/input/emotionTest";
	public static final String[] EMOTION_TAGS = { "angry", "disgust", "fear",
			"happy", "neutral", "sad", "surprise" };
	public static final int TOP_N = 20;

	private Precision _audioPrecision = null;
	private Recall _audioRecall = null;
	private MapAtN _audioMap = null;
	private Precision _emotionPrecision = null;
	private Recall _emotionRecall = null;
	private MapAtN _emotionMap = null;

	public EvaluationFacade() {
		_audioPrecision = new Precision(AUDIO_TAGS);
		_audioRecall = new Recall(FILEPATH_AUDIO_TRAIN, AUDIO_TAGS);
		_audioMap = new MapAtN(FILEPATH_AUDIO_TRAIN, AUDIO_TAGS, TOP_N);

		_emotionPrecision = new Precision(EMOTION_TAGS);
		_emotionRecall = new Recall(FILEPATH_EMOTION_TRAIN, EMOTION_TAGS);
		_emotionMap = new MapAtN(FILEPATH_EMOTION_TRAIN, EMOTION_TAGS, TOP_N);
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

	public boolean evaluateTest(String testFiles[],
			ArrayList<String[]> generatedResults, String resultFile,
			String resultFileHeader, boolean isAudio) {
		writeToFile(resultFile, false, resultFileHeader);

		StringBuffer lineBuffer = new StringBuffer();

		double[] precisions = new double[testFiles.length];
		double[] recalls = new double[testFiles.length];
		double[] maps = new double[testFiles.length];

		for (int i = 0; i < testFiles.length; i++) {
			lineBuffer.setLength(0);
			String testFile = testFiles[i];
			String[] generatedResult = generatedResults.get(i);

			if (isAudio) {
				precisions[i] = _audioPrecision.getPrecision(testFile,
						generatedResult);
				recalls[i] = _audioRecall.getRecall(testFile, generatedResult);
				maps[i] = _audioMap.getMapAtN(testFile, generatedResult);
			} else {
				precisions[i] = _emotionPrecision.getPrecision(testFile,
						generatedResult);
				recalls[i] = _emotionRecall.getRecall(testFile, generatedResult);
				maps[i] = _emotionMap.getMapAtN(testFile, generatedResult);
			}

			lineBuffer.append(testFile);
			lineBuffer.append(" precision: ");
			lineBuffer.append(precisions[i]);
			lineBuffer.append(" recall: ");
			lineBuffer.append(recalls[i]);
			lineBuffer.append(" map: ");
			lineBuffer.append(maps[i]);
			lineBuffer.append(" resultList: ");
			lineBuffer.append(generatedResult.toString());

			if (!writeToFile(resultFile, true, lineBuffer.toString())) {
				return false;
			}
		}

		double avgPrecision = Stats.mean(precisions);
		double avgRecall = Stats.mean(recalls);
		double avgMapAtN = Stats.mean(maps);
		double varPrecision = Stats.variance(precisions);
		double varRecall = Stats.variance(recalls);
		double varMapAtN = Stats.variance(maps);
		lineBuffer.setLength(0);
		lineBuffer.append("precision: (average) ");
		lineBuffer.append(avgPrecision);
		lineBuffer.append(" (variance) ");
		lineBuffer.append(varPrecision);
		lineBuffer.append(" recall: (average) ");
		lineBuffer.append(avgRecall);
		lineBuffer.append(" (variance) ");
		lineBuffer.append(varRecall);
		lineBuffer.append(" map@N: (average) ");
		lineBuffer.append(avgMapAtN);
		lineBuffer.append(" (variance) ");
		lineBuffer.append(varMapAtN);
		return writeToFile(resultFile, true, lineBuffer.toString());
	}

	
}
