package uk.ac.ucl.cs.caaa.classifier;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A thread handling a data source.
 * 
 * @author rax
 *
 */
public class DataHandler extends Thread {
	
	private long refreshRate = 1;
	
	private long refreshDelta = 0;

	private DataSource<?> source;
	
	private PropertyChangeSupport dataSupport;
	
	private boolean run = false;
	
	/**
	 * Base constructor which initialise common fields such 
	 * as the property change support.
	 * 
	 * <p>All the other constructors must invoke this.
	 */
	private DataHandler() {
		dataSupport = new PropertyChangeSupport(this);
	}
	
	public DataHandler(long refreshRate, long refreshDelta,
			DataSource<?> source) {
		this();
		this.refreshRate = refreshRate;
		this.refreshDelta = refreshDelta;
		this.source = source;
		setName(getClass().getName() + ": " + source.getId());
	}
	
	
	@Override
	public void run() {
		run = true;
		Object oldValue = null;
		Object newValue = null;
		while (run) {
			try {
				long delay = (long)(refreshRate + refreshDelta * (0.5 - Math.random()));
				if (delay < 0) {
					delay = 1;
				}
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				if (!run) {
					break;
				}
			}
			newValue = source.getValue();
			dataSupport.firePropertyChange(source.getId(), null, newValue);
			oldValue = newValue;
		}
	}

	/**
	 * Stops the data generation and interrupts the thread if it is running.
	 */
	public void stopDataGeneration() {
		if (run) {
			this.interrupt();
			run = false;
		}
	}

	public void addPropertyChangeListener(PropertyChangeListener arg0) {
		dataSupport.addPropertyChangeListener(arg0);
	}

	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		dataSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener arg0) {
		dataSupport.removePropertyChangeListener(arg0);
	}

	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		dataSupport.removePropertyChangeListener(propertyName, listener);
	}

	public DataSource<?> getDataSource() {
		return source;
	}

	public long getRefreshRate() {
		return refreshRate;
	}

	public void setRefreshRate(long refreshRate) {
		this.refreshRate = refreshRate;
	}

	public long getRefreshDelta() {
		return refreshDelta;
	}

	public void setRefreshDelta(long refreshDelta) {
		this.refreshDelta = refreshDelta;
	}

	public DataSource<?> getSource() {
		return source;
	}

	public void setSource(DataSource<?> source) {
		this.source = source;
	}
}