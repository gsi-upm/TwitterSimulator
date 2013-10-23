/**
 * es.upm.dit.gsi.sim.twitter.util.KKVisualizator.java
 */
package es.upm.dit.gsi.sim.twitter.util;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.continuous.Continuous2D;
import sim.field.continuous.Continuous3D;
import sim.field.network.Edge;
import sim.field.network.Network;
import sim.util.Bag;
import sim.util.Double2D;
import sim.util.Double3D;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import es.upm.dit.gsi.sim.twitter.TwitterSimulation;
import es.upm.dit.gsi.sim.twitter.model.user.Link;
import es.upm.dit.gsi.sim.twitter.model.user.User;

/**
 * Project: twitter-simulator File:
 * es.upm.dit.gsi.sim.twitter.util.KKVisualizator.java
 * 
 * Updates the location of users using Kamada-Kawai algorithm.
 * 
 * Grupo de Sistemas Inteligentes Departamento de Ingeniería de Sistemas
 * Telemáticos Universidad Politécnica de Madrid (UPM)
 * 
 * @author Álvaro Carrera Barroso
 * @email a.carrera@gsi.dit.upm.es
 * @twitter @alvarocarrera
 * @date 27/02/2013
 * @version 0.1
 * 
 */
public class KKVisualizator implements Steppable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5670879472931245412L;
	/**
	 * Logger
	 */
	private Logger logger = Logger.getLogger(this.getClass().getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see sim.engine.Steppable#step(sim.engine.SimState)
	 */
	@Override
	public void step(SimState simstate) {
		TwitterSimulation simulation = (TwitterSimulation) simstate;

		if (simulation.getGui() != null) {
			if (simulation.getUserManager() != null
					&& simulation.getUserManager().arePendingUsersToPaint()) {
				this.updateUsersPosition(simulation);

				Continuous2D usersField2D = new Continuous2D(0.1, simulation
						.getGui().getDisplay2D().getSize().getHeight(),
						simulation.getGui().getDisplay2D().getSize().getWidth());
				simulation.setUsersField2D(usersField2D);


				for (User user : simulation.getUsers()) {
					simulation.getUsersField2D().setObjectLocation(
							user,
							new Double2D(user.getPosition()[0], user
									.getPosition()[1]));
				}

				simulation.setUsersField2D(usersField2D);

				Continuous3D usersField3D = new Continuous3D(
						0.1,
						simulation.getGui().getDisplay3D().getSize()
								.getHeight(),
						simulation.getGui().getDisplay3D().getSize().getWidth(),
						simulation.getGui().getDisplay3D().getSize()
								.getHeight());
				simulation.setUsersField3D(usersField3D);


				// This offset is to improve the visualization.
				User orig = simulation.getUsers().get(0);
				double[] offset = orig.getPosition();

				for (User user : simulation.getUsers()) {
					double z = (user.getFollowers().size() - 1)
							* simulation.getNetworkDimension() / 70;
					Double3D pos = new Double3D(
							(user.getPosition()[0]) - offset[0], z
									- offset[2],
							(user.getPosition()[1]) - offset[1]); 
					// It changes Y/Z only for visualization reasons.
					
					
					usersField3D.setObjectLocation(user, pos);
				}

				simulation.setUsersField3D(usersField3D);
				simulation.getUserManager().setPendingUsers(false);
			} else {

				// This offset is to improve the visualization.
				User orig = simulation.getUsers().get(0);
				double[] offset = orig.getPosition();
				for (User user : simulation.getUsers()) {
					double z = (user.getFollowers().size() - 1)
							* simulation.getNetworkDimension() / 70;
					Double3D pos = new Double3D((user.getPosition()[0])
							- offset[0], z - offset[2], (user.getPosition()[1])
							- offset[1]); 
					// It changes Y/Z only for visualization reasons.

					
					simulation.getUsersField3D().setObjectLocation(user, pos);
				}

			}
		}
	}

	/**
	 * Build a scale free network using simulation parameters.
	 * 
	 * @return
	 */
	public void updateUsersPosition(TwitterSimulation simulation) {

		if (simulation.getUserManager().arePendingUsersToPaint()) {

			Network network = simulation.getNetwork();
			Graph<User, Link> graph = new SparseMultigraph<User, Link>();
			// Create graph
			Bag usersBag = network.getAllNodes();
			for (Object o : usersBag) {
				User user = (User) o;
				graph.addVertex(user);
			}
			for (Object o : usersBag) {
				User user = (User) o;
				Bag linksFromUserBag = network.getEdgesOut(user);
				for (Object l : linksFromUserBag) {
					Edge edge = (Edge) l;
					Link link = (Link) edge.getInfo();
					if (link.getFollower().equals(user)) {
						graph.addEdge(link, link.getFollower(),
								link.getFollowed());
					} else {
						logger.severe("Not consistent link.");
					}
				}
			}

			// Calculate positions using Kamada-Kawai algorithm.
			KKLayout<User, Link> layout = new KKLayout<User, Link>(graph);
			// The BasicVisualizationServer<V,E> is parameterized by the edge
			// types
			BasicVisualizationServer<User, Link> vv = new BasicVisualizationServer<User, Link>(
					layout);
			vv.setPreferredSize(new Dimension(simulation.getNetworkDimension(),
					simulation.getNetworkDimension())); // Sets the viewing

			// Updates positions
			List<User> users = new ArrayList<User>(graph.getVertices());
			for (User user : users) {
				// Get position
				double[] position = new double[3];
				position[0] = layout.getX(user);
				position[1] = layout.getY(user);
				position[2] = (user.getFollowers().size() - 1)
						* simulation.getNetworkDimension() / 70;

				logger.finest("User " + user.getId() + " in position "
						+ position[0] + "-" + position[1]);
				user.setPosition(position);

			}
		} else {
			for (User user : simulation.getUsers()) {
				// Get position
				double[] oldPosition = user.getPosition();
				double[] position = new double[3];
				position[0] = oldPosition[0];
				position[1] = oldPosition[1];
				position[2] = (user.getFollowers().size() - 1)
						* simulation.getNetworkDimension() / 70;

				logger.finest("User " + user.getId() + " in position "
						+ position[0] + "-" + position[1]);
				user.setPosition(position);

			}
		}
	}

}
