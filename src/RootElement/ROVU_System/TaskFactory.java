// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package RootElement.ROVU_System;

import RootElement.ROVU_System.Task;

/************************************************************/
/**
 * 
 */
public class TaskFactory {
	/**
	 * 
	 */
	private static RootElement.ROVU_System.TaskFactory instance = new TaskFactory();

	/**
	 * 
	 */
	private TaskFactory() {
	}

	/**
	 * 
	 * @param type 
	 * @return task 
	 */
	public Task getTask(String type) {
		return null;
	}

	/**
	 * 
	 * @return factory 
	 */
	public static RootElement.ROVU_System.TaskFactory getInstance() {
		return instance;
	}
};
