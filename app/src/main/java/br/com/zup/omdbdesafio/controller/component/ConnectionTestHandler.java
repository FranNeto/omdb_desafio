package br.com.zup.omdbdesafio.controller.component;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import br.com.zup.omdbdesafio.controller.listener.IConnectionTestListener;



/**
 * 
 * Handler used to test Connection.
 * 
 * @author 
 *
 */
public class ConnectionTestHandler extends Handler{

	private final transient IConnectionTestListener listener;
//	private final transient CloudEnum cloudEnum;
	
	/**
	 * Constructor
	 * 
	 * @param listener
	 */
	public ConnectionTestHandler(final  IConnectionTestListener listener) {
		super();
		this.listener = listener;
//		this.cloudEnum = null;
	}
	
	/**
	 * Constructor
	 * 
	 * @param listener
	 */
	/*public ConnectionTestHandler(final  IConnectionTestListener listener, final CloudEnum cloudEnum) {
		super();
		this.listener = listener;
		this.cloudEnum = cloudEnum;
	}*/
	
	/**
	 * @param msg
	 */
	@Override
	public void handleMessage(final Message msg) {
		boolean result;
		if (msg.what != 1) { // code if not connected
			result = false;
	    } else { // code if connected
	    	result = true;
	    }
		Log.e("Connection result ",getClass().getTypeParameters()+": " + result);
		listener.endConnectionTest(result);
		/*if(cloudEnum != null){
			listener.endConnectionTest(result, cloudEnum);
		}*/
	}

}