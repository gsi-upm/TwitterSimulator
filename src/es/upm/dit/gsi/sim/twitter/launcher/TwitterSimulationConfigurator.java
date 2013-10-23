/**
 * es.upm.dit.gsi.sim.twitter.launcher.TwitterSimulationConfigurator.java
 */
package es.upm.dit.gsi.sim.twitter.launcher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sim.field.network.Network;
import es.upm.dit.gsi.sim.twitter.model.user.User;
import es.upm.dit.gsi.sim.twitter.model.user.behaviour.BayesianBehaviour;
import es.upm.dit.gsi.sim.twitter.util.TwitterNetworkFactory;

/**
 * Project: twitter-simulator File:
 * es.upm.dit.gsi.sim.twitter.launcher.TwitterSimulationConfigurator.java
 * 
 * Grupo de Sistemas Inteligentes Departamento de Ingeniería de Sistemas
 * Telemáticos Universidad Politécnica de Madrid (UPM)
 * 
 * @author Álvaro Carrera Barroso
 * @email a.carrera@gsi.dit.upm.es
 * @twitter @alvarocarrera
 * @date 12/03/2013
 * @version 0.1
 * 
 */
public class TwitterSimulationConfigurator {

	/**
	 * @return
	 */
	public static List<String> getTopicList() {
		List<String> topics = new ArrayList<String>();

		topics.add("SPORT");
		topics.add("FOOTBALL");
		topics.add("TV");
		topics.add("FRIENDS");
		topics.add("GAMES");
		topics.add("BASKET");
		topics.add("WINTER");
		topics.add("SUMMER");

		return topics;
	}

	/**
	 * @return an initial network to load in the simulation.
	 */
	public static Network getInitialNetwork() {
		// Build directed network
		TwitterNetworkFactory factory = new TwitterNetworkFactory();

		// Users
		for (int i = 0; i < 3; i++) {
			User user = factory.createUser();
			user.setName("Paco NEO " + user.getId());
			String filePath = "causalModels" + File.separator + "sporter.xdsl";
			new BayesianBehaviour("NEO", 600, user, filePath, 0.5);
		}

		for (int i = 0; i < 3; i++) {
			User user = factory.createUser();
			user.setName("Paquito");
		}

		for (int i = 0; i < 3; i++) {
			User user = factory.createUser();
			user.setName("Pacote");
		}
		// Links
		List<User> users = factory.getUsers();

		User follower = users.get(0);
		User followed = users.get(1);
		factory.createLink(follower, followed);

		follower = users.get(0);
		followed = users.get(4);
		factory.createLink(follower, followed);

		follower = users.get(4);
		followed = users.get(0);
		factory.createLink(follower, followed);

		follower = users.get(2);
		followed = users.get(0);
		factory.createLink(follower, followed);

		follower = users.get(2);
		followed = users.get(3);
		factory.createLink(follower, followed);

		return factory.getNetwork();
	}

	/**
	 * @param string
	 * @return
	 */
	public static Network getInitialNetwork(String followers, String usersData, String usersBehaviours) {
		TwitterNetworkFactory factory = new TwitterNetworkFactory();

		try {
			Network net = factory.readUserGraph(followers, usersData, usersBehaviours);
			return net;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
