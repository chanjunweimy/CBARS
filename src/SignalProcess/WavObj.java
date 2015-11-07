package SignalProcess;

import java.util.Vector;

import javax.sound.sampled.AudioFormat;

public class WavObj {
	private AudioFormat _wavFormat;
	private long _audioFileLength;
	private short[] _signal;
	private static final double TIME_TURN = 4.5;
	private static final int SHORT_TO_BYTE = 2;
	
	public WavObj(AudioFormat wavFormat, 
			      long audioFileLength,
			      short[] signal) {
		_wavFormat = wavFormat;
		_audioFileLength = audioFileLength;
		_signal = signal;
	}
	
	public AudioFormat getFormat() {
		return _wavFormat;
	}
	
	public long getFileLength() {
		return _audioFileLength;
	}
	
	public short[] getSignal() {
		return _signal;
	}
	
	public int getFrameSize() {
		return _wavFormat.getFrameSize();
	}
	
	public float getFrameRate() {
		return _wavFormat.getFrameRate();
	}
	
	public double getFileDuration() {
		long audioFileLength = _audioFileLength;
		int frameSize = _wavFormat.getFrameSize();
        float frameRate = _wavFormat.getFrameRate();
        double durationInSeconds = (audioFileLength / (frameSize * frameRate));
		return durationInSeconds;
	}
	
	public int getNumOfFrames() {
		long audioFileLength = _audioFileLength;
		int frameSize = _wavFormat.getFrameSize();
        int numOfFrames = (int) Math.ceil((double) audioFileLength / (double) frameSize);
        return numOfFrames;
	}
	
	public void removeSignalsWithinSeconds(double second) {
		int frameSize = _wavFormat.getFrameSize();
        float frameRate = _wavFormat.getFrameRate();
		int fileLengthForTurn = (int) (frameSize * frameRate * second / SHORT_TO_BYTE);
		short[] tempSignal = new short[_signal.length - fileLengthForTurn];
		for (int i = 0; i < tempSignal.length; i++) {
			tempSignal[i] = _signal[i + fileLengthForTurn];
		}
		_signal = tempSignal;
	}
	
	public Vector<short[]> splitToSignals() {
		return splitToSignals(TIME_TURN);
	}

	/**
	 * @return
	 */
	private Vector<short[]> splitToSignals(double turnTime) {
		int frameSize = _wavFormat.getFrameSize();
        float frameRate = _wavFormat.getFrameRate();
		int fileLengthForTurn = (int) (frameSize * frameRate * turnTime / SHORT_TO_BYTE);
		int fileLengthEachSignal = _signal.length;
		int index = 0;
		
		Vector<short[]> signals = new Vector<short[]>();
		while (fileLengthEachSignal > 0) {
			if (fileLengthEachSignal >= fileLengthForTurn) {
				short[] curTurnSignal = new short[fileLengthForTurn];
				for (int i = index; i < index + fileLengthForTurn; i++) {
					curTurnSignal[i % fileLengthForTurn] = _signal[i];
				}
				signals.add(curTurnSignal);
				index += fileLengthForTurn;
			} else {
				short[] curTurnSignal = new short[fileLengthEachSignal];
				for (int i = index; i < index + fileLengthEachSignal; i++) {
					curTurnSignal[i % fileLengthForTurn] = _signal[i];
				}
				signals.add(curTurnSignal);
				index += fileLengthEachSignal;
			}
			fileLengthEachSignal -= fileLengthForTurn;
		}
		return signals;
	}
	
}
