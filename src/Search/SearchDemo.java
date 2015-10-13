package Search;

import Feature.Energy;
import Feature.MFCC;
import Feature.MagnitudeSpectrum;
import Feature.ZeroCrossing;
import SignalProcess.WaveIO;
import Distance.Cosine;
import Tool.SortHashMapByValue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

/**
 * Created by workshop on 9/18/2015.
 */
public class SearchDemo {
    /**
     * Please replace the 'trainPath' with the specific path of train set in your PC.
     */
    protected final static String trainPath = "data/input/train";

    private HashMap<String, double[]> _audioEnergy = null;
    private HashMap<String, double[]> _audioMfcc = null;
    private HashMap<String, double[]> _audioSpectrum = null;
    private HashMap<String, double[]> _audioZeroCrossing = null;
    private HashMap<String, double[]> _emotionEnergy = null;
    private HashMap<String, double[]> _emotionMfcc = null;
    private HashMap<String, double[]> _emotionSpectrum = null;
    private HashMap<String, double[]> _emotionZeroCrossing = null;

    public SearchDemo() {
    	_audioEnergy = readFeature("data/feature/audio_energy.txt");
    	_audioMfcc = readFeature("data/feature/audio_mfcc.txt");
    	_audioSpectrum = readFeature("data/feature/audio_spectrum.txt");
    	_audioZeroCrossing = readFeature("data/feature/audio_zerocrossing.txt");
    	_emotionEnergy = readFeature("data/feature/emotion_energy.txt");
    	_emotionMfcc = readFeature("data/feature/emotion_mfcc.txt");
    	_emotionSpectrum = readFeature("data/feature/emotion_spectrum.txt");
    	_emotionZeroCrossing = readFeature("data/feature/emotion_zerocrossing.txt");
    }

    /***
     * Get the feature of train set via the specific feature extraction method, and write it into offline file for efficiency;
     * Please modify this function, select or combine the methods (in the Package named 'Feature') to extract feature, such as Zero-Crossing, Energy, Magnitude-
     * Spectrum and MFCC by yourself.
     * @return the map of training features, Key is the name of file, Value is the array/vector of features.
     * 
     * This method has been deprecated as there is a later method defined in AudioFeaturesGenerator.java
     * @deprecated
     */
    public HashMap<String,double[]> trainFeatureList(){
        File trainFolder = new File(trainPath);
        File[] trainList = trainFolder.listFiles();

        HashMap<String, double[]> featureList = new HashMap<>();
        try {

            FileWriter fw = new FileWriter("data/feature/allFeature.txt");

            for (int i = 0; i < trainList.length; i++) {
                WaveIO waveIO = new WaveIO();
                short[] signal = waveIO.readWave(trainList[i].getAbsolutePath());

                /**
                 * Example of extracting feature via MagnitudeSpectrum, modify it by yourself.
                 */
                MagnitudeSpectrum ms = new MagnitudeSpectrum();
                double[] msFeature = ms.getFeature(signal);

                /**
                 * Write the extracted feature into offline file;
                 */
                featureList.put(trainList[i].getName(), msFeature);

                String line = trainList[i].getName() + "\t";
                for (double f: msFeature){
                    line += f + "\t";
                }

                fw.append(line+"\n");

                System.out.println("@=========@" + i);
            }
            fw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        try {


        }catch (Exception e){
            e.printStackTrace();
        }


        return featureList;
    }

    /***
     * Get the distances between features of the selected query audio and ones of the train set;
     * Please modify this function, select or combine the suitable and feasible methods (in the package named 'Distance') to calculate the distance,
     * such as CityBlock, Cosine and Euclidean by yourself.
     * @param query the selected query audio file;
     * @return the top 20 similar audio files;
     */
    public ArrayList<String> resultListOfEnergy(String query, boolean isAudio){
        WaveIO waveIO = new WaveIO();

        short[] inputSignal = waveIO.readWave(query);
        Energy ms = new Energy();
        double[] msFeature1 = ms.getFeature(inputSignal);
        HashMap<String, Double> simList = new HashMap<String, Double>();

        /**
         * Example of calculating the distance via Cosine Similarity, modify it by yourself please.
         */
        Cosine cosine = new Cosine();

        /**
         * Load the offline file of features (the result of function 'trainFeatureList()'), modify it by yourself please;
         */
        HashMap<String, double[]> trainFeatureList = null;
        
        if (isAudio) {
        	trainFeatureList = _audioEnergy;
        } else {
        	trainFeatureList = _emotionEnergy;
        }

//        System.out.println(trainFeatureList.size() + "=====");
        for (Map.Entry<String, double[]> f: trainFeatureList.entrySet()){
            simList.put((String)f.getKey(), cosine.getDistance(msFeature1, (double[]) f.getValue()));
        }

        SortHashMapByValue sortHM = new SortHashMapByValue(20);
        ArrayList<String> result = sortHM.sort(simList);

        String out = query + ":";
        for(int j = 0; j < result.size(); j++){
            out += "\t" + result.get(j);
        }

        System.out.println(out);
        return result;
    }
    
    /***
     * Get the distances between features of the selected query audio and ones of the train set;
     * Please modify this function, select or combine the suitable and feasible methods (in the package named 'Distance') to calculate the distance,
     * such as CityBlock, Cosine and Euclidean by yourself.
     * @param query the selected query audio file;
     * @return the top 20 similar audio files;
     */
    public ArrayList<String> resultListOfMfcc(String query, boolean isAudio){
        WaveIO waveIO = new WaveIO();

        short[] inputSignal = waveIO.readWave(query);
        MFCC ms = new MFCC();
        ms.process(inputSignal);
        double[] msFeature1 = ms.getMeanFeature();
        HashMap<String, Double> simList = new HashMap<String, Double>();

        /**
         * Example of calculating the distance via Cosine Similarity, modify it by yourself please.
         */
        Cosine cosine = new Cosine();

        /**
         * Load the offline file of features (the result of function 'trainFeatureList()'), modify it by yourself please;
         */
        HashMap<String, double[]> trainFeatureList = null;
        
        if (isAudio) {
        	trainFeatureList = _audioMfcc;
        } else {
        	trainFeatureList = _emotionMfcc;
        }

//        System.out.println(trainFeatureList.size() + "=====");
        for (Map.Entry<String, double[]> f: trainFeatureList.entrySet()){
            simList.put((String)f.getKey(), cosine.getDistance(msFeature1, (double[]) f.getValue()));
        }

        SortHashMapByValue sortHM = new SortHashMapByValue(20);
        ArrayList<String> result = sortHM.sort(simList);

        String out = query + ":";
        for(int j = 0; j < result.size(); j++){
            out += "\t" + result.get(j);
        }

        System.out.println(out);
        return result;
    }
    
    
    /***
     * Get the distances between features of the selected query audio and ones of the train set;
     * Please modify this function, select or combine the suitable and feasible methods (in the package named 'Distance') to calculate the distance,
     * such as CityBlock, Cosine and Euclidean by yourself.
     * @param query the selected query audio file;
     * @return the top 20 similar audio files;
     */
    public ArrayList<String> resultListOfZeroCrossing(String query, boolean isAudio){
        WaveIO waveIO = new WaveIO();

        short[] inputSignal = waveIO.readWave(query);
        ZeroCrossing ms = new ZeroCrossing();
        double[] msFeature1 = ms.getFeature(inputSignal);
        HashMap<String, Double> simList = new HashMap<String, Double>();

        /**
         * Example of calculating the distance via Cosine Similarity, modify it by yourself please.
         */
        Cosine cosine = new Cosine();

        /**
         * Load the offline file of features (the result of function 'trainFeatureList()'), modify it by yourself please;
         */
        HashMap<String, double[]> trainFeatureList = null;
        
        if (isAudio) {
        	trainFeatureList = _audioZeroCrossing;
        } else {
        	trainFeatureList = _emotionZeroCrossing;
        }

//        System.out.println(trainFeatureList.size() + "=====");
        for (Map.Entry<String, double[]> f: trainFeatureList.entrySet()){
            simList.put((String)f.getKey(), cosine.getDistance(msFeature1, (double[]) f.getValue()));
        }

        SortHashMapByValue sortHM = new SortHashMapByValue(20);
        ArrayList<String> result = sortHM.sort(simList);

        String out = query + ":";
        for(int j = 0; j < result.size(); j++){
            out += "\t" + result.get(j);
        }

        System.out.println(out);
        return result;
    }
    
    /***
     * Get the distances between features of the selected query audio and ones of the train set;
     * Please modify this function, select or combine the suitable and feasible methods (in the package named 'Distance') to calculate the distance,
     * such as CityBlock, Cosine and Euclidean by yourself.
     * @param query the selected query audio file;
     * @return the top 20 similar audio files;
     */
    public ArrayList<String> resultListOfSpectrum(String query, boolean isAudio){
        WaveIO waveIO = new WaveIO();

        short[] inputSignal = waveIO.readWave(query);
        MagnitudeSpectrum ms = new MagnitudeSpectrum();
        double[] msFeature1 = ms.getFeature(inputSignal);
        HashMap<String, Double> simList = new HashMap<String, Double>();

        /**
         * Example of calculating the distance via Cosine Similarity, modify it by yourself please.
         */
        Cosine cosine = new Cosine();

        /**
         * Load the offline file of features (the result of function 'trainFeatureList()'), modify it by yourself please;
         */
        HashMap<String, double[]> trainFeatureList = null;
        
        if (isAudio) {
        	trainFeatureList = _audioSpectrum;
        } else {
        	trainFeatureList = _emotionSpectrum;
        }

//        System.out.println(trainFeatureList.size() + "=====");
        for (Map.Entry<String, double[]> f: trainFeatureList.entrySet()){
            simList.put((String)f.getKey(), cosine.getDistance(msFeature1, (double[]) f.getValue()));
        }

        SortHashMapByValue sortHM = new SortHashMapByValue(20);
        ArrayList<String> result = sortHM.sort(simList);

        String out = query + ":";
        for(int j = 0; j < result.size(); j++){
            out += "\t" + result.get(j);
        }

        System.out.println(out);
        return result;
    }
    
    

    /**
     * Load the offline file of features (the result of function 'trainFeatureList()');
     * @param featurePath the path of offline file including the features of training set.
     * @return the map of training features, Key is the name of file, Value is the array/vector of features.
     */
    private HashMap<String, double[]> readFeature(String featurePath){
        HashMap<String, double[]> fList = new HashMap<>();
        try{
            FileReader fr = new FileReader(featurePath);
            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine();
            while(line != null){

                String[] split = line.trim().split("\t");
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

    public static void main(String[] args){
        SearchDemo searchDemo = new SearchDemo();
        /**
         * Example of searching, selecting 'bus2.wav' as query;
         */
        searchDemo.resultListOfSpectrum("data/input/test/bus2.wav", true);
    }
}
