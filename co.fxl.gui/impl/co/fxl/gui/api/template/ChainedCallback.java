/**
 * This file is part of FXL GUI API.
 *  
 * FXL GUI API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * FXL GUI API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with FXL GUI API.  If not, see <http://www.gnu.org/licenses/>.
 */
package co.fxl.gui.api.template;

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
