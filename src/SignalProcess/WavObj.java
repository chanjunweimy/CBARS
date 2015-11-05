package SignalProcess;

import javax.sound.sampled.AudioFormat;

public class WavObj {
	private AudioFormat _wavFormat;
	private long _audioFileLength;
	private short[] _signal;

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
	
}
