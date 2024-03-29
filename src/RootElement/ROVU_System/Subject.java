// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package RootElement.ROVU_System;

import java.util.ArrayList;
import java.util.List;

/************************************************************/
/**
 * 
 */
public abstract class Subject {
	
	List<ObserverInterface> observers = new ArrayList<ObserverInterface>();
	private int state;
	
	public int getState(){
		return this.state;
	}
	
	public void setState(int state){
		this.state = state;
		this.notifyAllObservers();
	}
	
	public void attach(ObserverInterface sr1) {
		this.observers.add(sr1);
	}

	public void detach(ObserverInterface o) {
		this.observers.remove(observers.indexOf(o)); // ? code
	}

	public void notifyAllObservers() {
		for(ObserverInterface observer : this.observers){
			observer.update();
		}
	}
};
