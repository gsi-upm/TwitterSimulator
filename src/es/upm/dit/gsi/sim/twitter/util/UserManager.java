/**
 * es.upm.dit.gsi.sim.twitter.util.UserManager.java
 */
package es.upm.dit.gsi.sim.twitter.util;

import java.io.File;

import sim.engine.SimState;
import sim.engine.Steppable;
import es.upm.dit.gsi.sim.twitter.TwitterSimulation;
import es.upm.dit.gsi.sim.twitter.model.user.User;
import es.upm.dit.gsi.sim.twitter.model.user.behaviour.BayesianBehaviour;
import es.upm.dit.gsi.sim.twitter.model.user.behaviour.RandomBehaviour;

/**
 * Project: twitter-simulator File:
 * es.upm.dit.gsi.sim.twitter.util.UserManager.java
 * 
 * This class configure all user behaviours and can create new users during the
 * simulation.
 * 
 * Grupo de Sistemas Inteligentes Departamento de Ingeniería de Sistemas
 * Telemáticos Universidad Politécnica de Madrid (UPM)
 * 
 * @author Álvaro Carrera Barroso
 * @email a.carrera@gsi.dit.upm.es
 * @twitter @alvarocarrera
 * @date 28/02/2013
 * @version 0.1
 * 
 */
public class UserManager implements Steppable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5328946800470291765L;

	/**
	 * Pending users to be painted
	 */
	private boolean pendingUsers;

	public UserManager() {
		this.pendingUsers = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sim.engine.Steppable#step(sim.engine.SimState)
	 */
	@Override
	public void step(SimState simstate) {
		TwitterSimulation simulation = (TwitterSimulation) simstate;
		double random = simulation.random.nextDouble();
		if (random <= 0.01) {
			User user = new User(simulation.getUsers().size());
			user.setName("NEO " + user.getId());
			String filePath = "causalModels" + File.separator + "sporter.xdsl";
			new BayesianBehaviour("NEO", 600, user, filePath, 0.5);
			simulation.getUsers().add(user);
			simulation.getNetwork().addNode(user);
			simulation.setNumberOfUsers(simulation.getUsers().size() + 1);
			simulation.schedule.scheduleRepeating(user, 1, 1);
			this.pendingUsers = true;
		}
	}

	/**
	 * @return
	 */
	public boolean arePendingUsersToPaint() {
		return this.pendingUsers;
	}

	/**
	 * @param value
	 */
	public void setPendingUsers(boolean value) {
		this.pendingUsers = value;
	}

	/**
	 * @param simulation
	 */
	public void configureUserBehaviours(TwitterSimulation simulation) {

		// Register all users
		// (As users are Steppeables, they must be scheduled.)
		for (User user : simulation.getUsers()) {
			simulation.schedule.scheduleRepeating(user, 1, 1);
		}
		
		// Add behaviours
		for (User user : simulation.getUsers()) {
			if (user.getBehaviour() == null) {
				new RandomBehaviour("Dummy", 600, user);
			}
		}
	}

}
