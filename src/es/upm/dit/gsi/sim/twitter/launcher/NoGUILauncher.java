package es.upm.dit.gsi.sim.twitter.launcher;
import java.io.File;

import es.upm.dit.gsi.sim.twitter.TwitterSimulation;

/**
 * Project: twitter-simulator
 * File: es.upm.dit.gsi.sim.twitter.launcher.NoGUILauncher.java
 * 
 * Grupo de Sistemas Inteligentes
 * Departamento de Ingeniería de Sistemas Telemáticos
 * Universidad Politécnica de Madrid (UPM)
 * 
 * @author Álvaro Carrera Barroso
 * @email a.carrera@gsi.dit.upm.es
 * @twitter @alvarocarrera
 * @date 03/03/2013
 * @version 0.1
 * 
 */
public class NoGUILauncher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		
		
		
//		TwitterSimulation simulation = new TwitterSimulation(
//				System.currentTimeMillis(), TwitterSimulationConfigurator.getTopicList());
		TwitterSimulation simulation = new TwitterSimulation(System.currentTimeMillis(),
				TwitterSimulationConfigurator.getInitialNetwork("twitter-info" + File.separator
						+ "followers.json", "twitter-info" + File.separator
						+ "allusers.json", null), TwitterSimulationConfigurator.getTopicList());
		simulation.start();

		// Execute the simulation step by step
		do {
			if (!simulation.schedule.step(simulation)) {
				break;
			}
		} while (simulation.schedule.getSteps() < Long.MAX_VALUE); // This is the final step.
		simulation.finish();

	}

}
