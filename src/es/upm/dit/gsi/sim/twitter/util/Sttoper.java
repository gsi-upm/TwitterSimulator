/**
 * es.upm.dit.gsi.sim.twitter.util.Sttoper.java
 */
package es.upm.dit.gsi.sim.twitter.util;

import java.util.Date;
import java.util.logging.Logger;

import sim.engine.SimState;
import sim.engine.Steppable;
import es.upm.dit.gsi.sim.twitter.TwitterSimulation;

/**
 * Project: twitter-simulator
 * File: es.upm.dit.gsi.sim.twitter.util.Sttoper.java
 * 
 * Check if the simulation it is done.
 * 
 * Grupo de Sistemas Inteligentes
 * Departamento de Ingeniería de Sistemas Telemáticos
 * Universidad Politécnica de Madrid (UPM)
 * 
 * @author Álvaro Carrera Barroso
 * @email a.carrera@gsi.dit.upm.es
 * @twitter @alvarocarrera
 * @date 27/02/2013
 * @version 0.1
 * 
 */
public class Sttoper implements Steppable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6479828324313925570L;
	/**
	 * Logger
	 */
	private Logger logger = Logger.getLogger(this.getClass().getName());

	/* (non-Javadoc)
	 * @see sim.engine.Steppable#step(sim.engine.SimState)
	 */
	@Override
	public void step(SimState simstate) {
		TwitterSimulation simulation = (TwitterSimulation) simstate;
		Date endDate = simulation.getTimeConversor().getParsedDate(simulation.getEndDate());
		long simulationFinalStep = simulation.getTimeConversor().getSteps(endDate);
		long currentStep = simulation.schedule.getSteps();
		
		// Check if the simulation is finished.
		if (currentStep>=simulationFinalStep) {
			logger.info("Simulation finish in step: " + currentStep + " Date: " + simulation.getTimeConversor().getFormattedDate(endDate));
			simulation.finish();
		} else if (currentStep % 500 == 0) {
			logger.info("Running step: " + currentStep);
		}
	}

}
