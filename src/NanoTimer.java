import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NanoTimer {
	private static final long NANOS_IN_SECONDS = 1000000000;
	
	private long timeStart;
	private long timeEnd;
	
	public NanoTimer(){
		reset();
	}
	
	public void start(){
		timeStart = System.nanoTime();
	}
	
	public void stop(){
		timeEnd = System.nanoTime();
	}
	
	
	public void reset(){
		timeStart = 0;
		timeEnd = 0;
	}
	
	public long getLapsed(){
		return (timeEnd-timeStart);
	}
	
	public String getFormattedTimeLapsed(){
		String toReturn = getLapsed() + " ns";
		if(getLapsed() >= NANOS_IN_SECONDS / 1000){
			DecimalFormat df = new DecimalFormat("#0.000");
			double inSeconds = 1.0 * getLapsed() / NANOS_IN_SECONDS;  
			toReturn += " (" + df.format(inSeconds) + " sec)";
		}else{
			toReturn += " (< 0.001 sec)";
		}
		
		return toReturn;
	}
}
