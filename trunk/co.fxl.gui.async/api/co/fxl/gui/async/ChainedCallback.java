package co.fxl.gui.async;

/**
 * Chained async callback.
 * 
 * @author Oriel Maute
 *
 * @param <T>
 * @param <R>
 */
public abstract class ChainedCallback<T, R> implements ICallback<T> {

	/**
	 * Next callback to invoke
	 */
	private ICallback<R> nextCallback;
		
	/**
	 * Constructor
	 */
	public ChainedCallback() {
		nextCallback = null;
	}
	
	/**
	 * Constructor
	 * @param pNextCallback 
	 */
	public ChainedCallback(ChainedCallback<R, ?> pNextCallback) {
		nextCallback = pNextCallback;
	}
	
	/**
	 * Set next async callback to invoke.
	 * @param pNextCallback next async callback to invoke
	 */
	public void setNextCallback(ICallback<R> pNextCallback) {
		nextCallback = pNextCallback;
	}
	
	/**
	 * Get next async callback to invoke
	 * @return next async callback to invoke
	 */
	public ICallback<R> getNextCallback() {
		return nextCallback;
	}
		
	/**
	 * Propagate error to next async callback.
	 */
	public void onFail(Throwable caught) {
		 if (nextCallback != null) {
			 nextCallback.onFail(caught);
		 }
	}

	public abstract void onSuccess(T result);	
	
}
