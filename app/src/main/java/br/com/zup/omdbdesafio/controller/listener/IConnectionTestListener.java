package br.com.zup.omdbdesafio.controller.listener;


public interface IConnectionTestListener {

	/**
	 * Finish the progress Dialog
	 * 
	 * @param isSent Boolean
	 */
	void endConnectionTest(final Boolean isSent);
}
