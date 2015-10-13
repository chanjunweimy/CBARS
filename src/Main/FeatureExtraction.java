import Feature.Energy;
import Feature.MFCC;
import Feature.MagnitudeSpectrum;
import Feature.ZeroCrossing;
import SignalProcess.WaveIO;
import Distance.Bhattacharyya;
import Distance.Chebychev;
import Distance.CityBlock;
import Distance.Cosine;
import Distance.Euclidean;
import Distance.RBFKernel;

/**
 * Created by workshop on 9/18/2015.
 * Show examples for different feature extraction methods, including Energy, MagnitudeSpectrum, MFCC and Zero-Crossing;
 */
public class FeatureExtraction {
    public static void main(String[] args){
        WaveIO waveIO1 = new WaveIO();
        short[] signals1 = waveIO1.readWave("data/input/test/office/office102.wav");

        WaveIO waveIO2 = new WaveIO();
        short[] signals2 = waveIO2.readWave("data/input/test/bus/bus80.wav");

        Energy energy1 = new Energy();
        double[] eFeature1 = energy1.getFeature(signals1);

        Energy energy2 = new Energy();
        double[] eFeature2 = energy2.getFeature(signals2);

        MagnitudeSpectrum ms1 = new MagnitudeSpectrum();
        double[] msFeature1 = ms1.getFeature(signals1);

        MagnitudeSpectrum ms2 = new MagnitudeSpectrum();
        double[] msFeature2 = ms2.getFeature(signals2);

        MFCC mfcc = new MFCC();
        double[][] MFCC = mfcc.process(signals1);//13-d mfcc

        double[] mean = mfcc.getMeanFeature();



        ZeroCrossing zc = new ZeroCrossing();
        double[] zero1 = zc.getFeature(signals1);
        double[] zero2 = zc.getFeature(signals2);

        Cosine cosine = new Cosine();
        double ss = cosine.getDistance(msFeature1, msFeature2);

        Euclidean euclidean = new Euclidean();
        double ee = euclidean.getDistance(msFeature1, msFeature2);

        CityBlock cityBlock = new CityBlock();
        double cc = cityBlock.getDistance(msFeature1, msFeature2);
        
        /**
         * not using mahalanobis distance as the 
         * inverse of covariance matrix is quite hard to implement
         * 
         * since mahalanobis distance assume the samples are all distributed
         * about the centre of mass, it might not be so suitable for
         * Feature extraction of speech, so this is another reason why we
         * don't implement it.
         */
        //Mahalanobis mahalanobis = new Mahalanobis();
        //double mm = mahalanobis.getDistance(msFeature1, msFeature2, MFCC);
        
        Bhattacharyya bhat = new Bhattacharyya();
        double bb = bhat.getDistance(msFeature1, msFeature2);
        
        Chebychev cheby = new Chebychev();
        double ch = cheby.getDistance(msFeature1, msFeature2);
        
        RBFKernel kernel = new RBFKernel();
        double rbf = kernel.getDistance(msFeature1, msFeature2);

        System.out.println(ss);
        System.out.println(ee);
        System.out.println(cc);
        //System.out.println(mm);
        System.out.println(bb);
        System.out.println(ch);
        System.out.println(rbf);
    }
}
