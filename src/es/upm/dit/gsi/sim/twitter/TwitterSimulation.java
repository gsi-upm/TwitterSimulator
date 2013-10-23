/*******************************************************************************
 * Copyright (c) 2013 alvarocarrera. Grupo de Sistemas Inteligentes - Universidad Politécnica de Madrid. (GSI-UPM)
 * http://www.gsi.dit.upm.es/
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 *  
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 *  
 * Contributors:
 *     alvarocarrera - initial API and implementation
 ******************************************************************************/
/**
 * es.upm.dit.gsi.sim.twitter.TwitterSimulation.java
 */
package es.upm.dit.gsi.sim.twitter;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.collections15.Factory;

import sim.engine.SimState;
import sim.field.continuous.Continuous2D;
import sim.field.continuous.Continuous3D;
import sim.field.network.Edge;
import sim.field.network.Network;
import sim.portrayal.network.SpatialNetwork2D;
import sim.portrayal3d.network.SpatialNetwork3D;
import sim.util.Bag;
import sim.util.Double2D;
import sim.util.Double3D;
import edu.uci.ics.jung.algorithms.generators.random.BarabasiAlbertGenerator;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import es.upm.dit.gsi.sim.twitter.model.event.Event;
import es.upm.dit.gsi.sim.twitter.model.user.Link;
import es.upm.dit.gsi.sim.twitter.model.user.User;
import es.upm.dit.gsi.sim.twitter.model.user.behaviour.Behaviour;
import es.upm.dit.gsi.sim.twitter.model.user.behaviour.NullBehaviour;
import es.upm.dit.gsi.sim.twitter.util.Analyzer;
import es.upm.dit.gsi.sim.twitter.util.EventManager;
import es.upm.dit.gsi.sim.twitter.util.KKVisualizator;
import es.upm.dit.gsi.sim.twitter.util.Sttoper;
import es.upm.dit.gsi.sim.twitter.util.TimeConversor;
import es.upm.dit.gsi.sim.twitter.util.TopicManager;
import es.upm.dit.gsi.sim.twitter.util.TweetManager;
import es.upm.dit.gsi.sim.twitter.util.UserManager;

/**
 * Project: twitter-simulator File:
 * es.upm.dit.gsi.sim.twitter.TwitterSimulation.java
 * 
 * This class represents and executes the simulation.
 * 
 * Grupo de Sistemas Inteligentes Departamento de Ingeniería de Sistemas
 * Telemáticos Universidad Politécnica de Madrid (UPM)
 * 
 * @author Álvaro Carrera Barroso
 * @email a.carrera@gsi.dit.upm.es
 * @twitter @alvarocarrera
 * @date 03/03/2013
 * @version 0.3
 * 
 */
public class TwitterSimulation extends SimState {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3700405083188774385L;

	/**
	 * Logger
	 */
	private Logger logger = Logger.getLogger(this.getClass().getName());

	// SIMULATION PARAMETERS //

	/**
	 * To start with a random scale free network or with an empty network with
	 * users but no links.
	 */
	private boolean randomNetwork = true;

	/**
	 * To load an initial network got by parameters.
	 */
	private boolean loadInitialNetwork = false;

	/**
	 * The number of users in the simulation.
	 */
	private int numberOfUsers = 200;

	/**
	 * Parameter for scale free Kamada-Kawai algorithm.
	 */
	private int networkDimension = 500;

	/**
	 * Average number of initial connections per user.
	 */
	private int nodesDegree = 1;

	/**
	 * How fast is the simulation? Do you want to simulate every second or
	 * periods of X secons;
	 */
	private int oneStepInSeconds = 60;

	/**
	 * When the simulation starts. Note: It is important to keep the date
	 * format.
	 */
	private String startDate = "2012-01-01 10:00:00";

	/**
	 * When the simulation finishes. Note: It is important to keep the date
	 * format.
	 */
	private String endDate = "2012-03-01 10:00:00";

	// END OF SIMULATION PARAMETERS //
	
	/**
	 * Logging level
	 */
	Level level = Level.INFO;

	/**
	 * It represents the user networks.
	 */
	private Network network;
	/**
	 * The initial network.
	 */
	private Network initialNetwork;
	/**
	 * List of all users.
	 */
	private List<User> users;
	/**
	 * List of all links.
	 */
	private List<Link> links;

	/**
	 * List of all deleted links.
	 */
	private List<Link> deletedLinks;

	/**
	 * Event Manager
	 */
	private EventManager eventManager;

	/**
	 * Tweet Manager
	 */
	private TweetManager tweetManager;

	/**
	 * Topics Manager
	 */
	private TopicManager topicManager;

	/**
	 * User Manager
	 */
	private UserManager userManager;

	/**
	 * 2D space to place users in the 2D gui.
	 */
	private Continuous2D usersField2D;

	/**
	 * 3D space to place users in the 3D gui.
	 */
	private Continuous3D usersField3D;

	/**
	 * Object to convert steps to Date Java objects and viceversa.
	 */
	private TimeConversor timeConversor;

	/**
	 * GUI Object
	 */
	private TwitterSimulationGUI gui;

	/**
	 * Analyzer that creates, paint, and update all charts. Furthermore, it
	 * saves data in CSV files.
	 */
	private Analyzer analyzer;

	/**
	 * Constructor
	 * 
	 * @param seed
	 */
	public TwitterSimulation(long seed, List<String> topics) {
		super(seed);
		Logger globalLogger = logger.getParent();
		globalLogger.setLevel(level);
		for (Handler handler : globalLogger.getHandlers()) {
			handler.setLevel(level);
		}
		this.topicManager = new TopicManager();
		this.topicManager.setTopics(topics);
	}

	/**
	 * @param seed
	 * @param network
	 */
	public TwitterSimulation(long seed, Network network, List<String> topics) {
		super(seed);
		Logger globalLogger = logger.getParent();
		globalLogger.setLevel(level);
		for (Handler handler : globalLogger.getHandlers()) {
			handler.setLevel(level);
		}
		this.topicManager = new TopicManager();
		this.topicManager.setTopics(topics);
		this.initialNetwork = network;
		this.loadInitialNetwork = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sim.engine.SimState#start()
	 */
	public void start() {

		logger.info("Configuring simulation...");
		super.start();
		this.timeConversor = new TimeConversor(this.oneStepInSeconds, this);
		this.eventManager = new EventManager(this);
		this.tweetManager = new TweetManager();
		this.userManager = new UserManager();

		// Check dates
		if (this.timeConversor.getParsedDate(this.startDate).compareTo(
				this.timeConversor.getParsedDate(this.endDate)) > 0) {
			logger.severe("Not consistent start and end dates. The simulation is going to finish... :( snif snif");
			this.finish();
			System.exit(1);
		} else {

			// Build the network
			logger.info("Building network...");
			if (!this.loadInitialNetwork) {
				if (randomNetwork) {
					this.network = this.buildScaleFreeNetwork();
				} else {
					this.network = this.emptyNetwork();
				}
			} else if (this.initialNetwork != null) {
				logger.info("Initial network loaded. No random network created.");
				this.loadInitialNetwork(this.initialNetwork);
			} else {
				logger.severe("No initial network to load. The simulation is finishing... :( snif snif");
				this.finish();
				System.exit(1);
			}
			logger.info("Done!");

			// Configure 2D and 3D spaces
			this.build2DSpace();
			this.build3DSpace();

			logger.info("Starting twitter simulation...");

			// EventManager
			this.schedule.scheduleRepeating(this.eventManager, 0, 1);

			// Kamada-Kawai Visualizator
			KKVisualizator kkv = new KKVisualizator();
			this.schedule.scheduleRepeating(kkv, 0, 1);

			// UserManager
			this.userManager.configureUserBehaviours(this);
			this.schedule.scheduleRepeating(this.userManager, 0, 1);

			// Check if all users has a behaviour
			// If not, put a null behaviour.
			for (User user : users) {
				if (user.getBehaviour() == null) {
					new NullBehaviour(user);
				}
			}

			// Create or reuse (from previous simulation) the analyzer
			if (this.gui == null) {
				this.analyzer = new Analyzer(this);
			} else if (this.gui.getAnalyzer() == null) {
				this.analyzer = new Analyzer(this);
				this.gui.setAnalyzer(this.analyzer);
			} else {
				this.analyzer = this.gui.getAnalyzer();
			}
			this.analyzer.setupAllCharts(this);
			this.schedule.scheduleRepeating(analyzer, 3, 1);

			// Stopper
			Sttoper stopper = new Sttoper();
			this.schedule.scheduleRepeating(stopper, 4, 1);

			logger.info("Twitter simulation ready. There we go...");
		}
	}

	/**
	 * Create and place all users in the 2D space
	 */
	private void build2DSpace() {

		try {

			this.usersField2D = new Continuous2D(0.1, this.getGui()
					.getDisplay2D().getSize().getHeight(), this.getGui()
					.getDisplay2D().getSize().getWidth());
		} catch (NullPointerException e) {
			this.usersField2D = new Continuous2D(0.1,
					this.getNetworkDimension(), this.networkDimension);
		}

		this.setUsersField2D(this.usersField2D);

		for (User user : this.getUsers()) {
			this.usersField2D.setObjectLocation(user,
					new Double2D(user.getPosition()[0], user.getPosition()[1]));
		}
	}

	/**
	 * Create and place all users in the 3D space
	 */
	private void build3DSpace() {
		this.usersField3D = new Continuous3D(0.1, this.networkDimension,
				this.networkDimension, this.networkDimension);

		// This offset is to improve the visualization.
		User orig = this.getUsers().get(0);
		double[] offset = orig.getPosition();

		for (User user : this.getUsers()) {
			double z = (user.getFollowers().size() - 1)
					* this.getNetworkDimension() / 70;
			Double3D pos = new Double3D((user.getPosition()[0]) - offset[0], z
					- offset[2], (user.getPosition()[1]) - offset[1]); // It
																		// changes
																		// Y/Z
																		// only
																		// for
																		// visualization
																		// reasons.

			this.usersField3D.setObjectLocation(user, pos);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sim.engine.SimState#finish()
	 */
	public void finish() {
		super.finish();
		if (this.gui != null) {
			gui.finish();
		}
	}

	/**
	 * This method initializes some attributes to load all components of the
	 * initial network.
	 * 
	 */
	private void loadInitialNetwork(Network initialNetwork) {
		this.network = new Network(true);
		this.users = new ArrayList<User>();
		this.links = new ArrayList<Link>();
		this.deletedLinks = new ArrayList<Link>();
		Bag usersBag = initialNetwork.getAllNodes();
		for (Object o : usersBag.objs) {
			if (o != null) {
				User origUser = (User) o;
				User user = new User(origUser.getId());
				user.setName(origUser.getName());
				Behaviour b = origUser.getBehaviour();
				if (b != null) {
					b.reset();
					b.setUser(user);
					user.setBehaviour(b);
				}
				this.users.add(user);
				this.network.addNode(user);
			}
		}
		for (Object o : usersBag.objs) {
			if (o != null) {
				User origUser = (User) o;
				Bag linksBag = initialNetwork.getEdgesOut(origUser);
				for (Object ob : linksBag.objs) {
					if (ob != null) {
						Edge edge = (Edge) ob;
						Link origLink = (Link) edge.getInfo();
						User origFollower = origLink.getFollower();
						User origFollowed = origLink.getFollowed();
						User follower = null;
						User followed = null;
						for (User user : this.users) {
							int id = user.getId();
							if (follower == null && id == origFollower.getId()) {
								follower = user;
							} else if (followed == null
									&& id == origFollowed.getId()) {
								followed = user;
							} else if (followed != null && follower != null) {
								break;
							}
						}
						Link link = new Link(origLink.getId(),
								this.getCurrentDate(), follower, followed);
						this.links.add(link);
						follower.addFollowed(followed);
						followed.addFollower(follower);
						this.network.addEdge(link.getFollower(),
								link.getFollowed(), link);
					} else {
						logger.fine("Null Edge found in the initial network.");
					}
				}
			}
		}
		this.numberOfUsers = this.users.size();
		// To ensure that the kkvisualizator update the users positions.
		this.userManager.setPendingUsers(true);
	}

	/**
	 * Build a network without connections. Only users randomly placed.
	 * 
	 * @return
	 */
	private Network emptyNetwork() {
		// Build directed network
		Network newNetwork = new Network(true);

		// Users
		this.users = new ArrayList<User>();
		for (int i = 0; i < numberOfUsers; i++) {
			User newUser = new User(i);
			double[] position = new double[3];
			position[0] = this.random.nextDouble() * 0.95
					* this.networkDimension;
			position[1] = this.random.nextDouble() * 0.95
					* this.networkDimension;
			position[2] = this.random.nextDouble() * 0.95
					* this.networkDimension;
			newUser.setPosition(position);
			this.users.add(newUser);
			newNetwork.addNode(newUser);
		}

		// Links
		this.links = new ArrayList<Link>();
		this.deletedLinks = new ArrayList<Link>();
		return newNetwork;
	}

	/**
	 * Build a scale free network using simulation parameters.
	 * 
	 * @return
	 */
	private Network buildScaleFreeNetwork() {

		// Object to build the graph in the Barabasi algorithm implementation.
		Factory<Graph<User, Link>> graphFactory = new Factory<Graph<User, Link>>() {

			public Graph<User, Link> create() {
				return new SparseMultigraph<User, Link>();
			}
		};

		// Object to build the users in the Barabasi algorithm
		// implementation.
		Factory<User> vertexFactory = new Factory<User>() {
			private int id = 0;

			public User create() {
				return new User(id++);
			}
		};

		// Object to build the connections in the Barabasi algorithm
		// implementation.
		Factory<Link> edgeFactory = new Factory<Link>() {
			private int id = 0;

			public Link create() {
				return new Link(id++);
			}
		};

		// Seed users for Barabasi algorithm implementation.
		Set<User> seeds = new HashSet<User>();
		User n1 = new User(1);
		seeds.add(n1);
		User n2 = new User(2);
		seeds.add(n2);

		// Build scale free network using Barabasi algorithm.
		BarabasiAlbertGenerator<User, Link> barabasi = new BarabasiAlbertGenerator<User, Link>(
				graphFactory, vertexFactory, edgeFactory, 1, this.nodesDegree,
				seeds);
		Graph<User, Link> graph = barabasi.create();
		barabasi.evolveGraph(this.numberOfUsers - 1); // This is because we
														// start the
														// algorithm with
														// one user.

		// Calculate positions using Kamada-Kawai algorithm.
		KKLayout<User, Link> layout = new KKLayout<User, Link>(graph);
		// The BasicVisualizationServer<V,E> is parameterized by the edge types
		BasicVisualizationServer<User, Link> vv = new BasicVisualizationServer<User, Link>(
				layout);
		vv.setPreferredSize(new Dimension(this.networkDimension,
				this.networkDimension)); // Sets the viewing

		// Build directed network
		Network newNetwork = new Network(true);

		this.users = new ArrayList<User>(graph.getVertices());
		for (User user : users) {
			// Get position
			double[] position = new double[3];
			position[0] = layout.getX(user);
			position[1] = layout.getY(user);
			// position[2]=layout.getZ(user);
			// For 3D Kamada-Kawai algorithm... Pending...
			position[2] = 0;

			logger.finest("User " + user.getId() + " in position "
					+ position[0] + "-" + position[1]);
			user.setPosition(position);

			// Add node to the network.
			newNetwork.addNode(user);
		}
		// Add connections to the network.
		this.links = new ArrayList<Link>(graph.getEdges());
		for (Link link : links) {
			Pair<User> users = graph.getEndpoints(link);
			link.setUsers(this.getCurrentDate(), users.getFirst(),
					users.getSecond());
			users.getFirst().addFollowed(users.getSecond());
			users.getSecond().addFollower(users.getFirst());
			newNetwork.addEdge(users.getFirst(), users.getSecond(), link);
		}
		this.deletedLinks = new ArrayList<Link>();
		return newNetwork;
	}

	/**
	 * @return the randomNetwork
	 */
	public boolean isRandomNetwork() {
		return randomNetwork;
	}

	/**
	 * @param randomNetwork
	 *            the randomNetwork to set
	 */
	public void setRandomNetwork(boolean randomNetwork) {
		this.randomNetwork = randomNetwork;
	}

	/**
	 * @return
	 */
	public boolean isLoadInitialNetwork() {
		return loadInitialNetwork;
	}

	/**
	 * @param loadInitialNetwork
	 */
	public void setLoadInitialNetwork(boolean loadInitialNetwork) {
		this.loadInitialNetwork = loadInitialNetwork;
	}

	/**
	 * @return the numberOfUsers
	 */
	public int getNumberOfUsers() {
		return numberOfUsers;
	}

	/**
	 * @param numberOfUsers
	 *            the numberOfUsers to set
	 */
	public void setNumberOfUsers(int numberOfUsers) {
		this.numberOfUsers = numberOfUsers;
	}

	/**
	 * @return the networkDimension
	 */
	public int getNetworkDimension() {
		return networkDimension;
	}

	/**
	 * @param networkDimension
	 *            the networkDimension to set
	 */
	public void setNetworkDimension(int networkDimension) {
		this.networkDimension = networkDimension;
	}

	/**
	 * @return the oneStepInSeconds
	 */
	public int getOneStepInSeconds() {
		return this.oneStepInSeconds;
	}

	/**
	 * @param oneStepInSeconds
	 *            the oneStepInSeconds to set
	 */
	public void setOneStepInSeconds(int oneStepInSeconds) {
		if (oneStepInSeconds <= 0) {
			logger.warning("A step must represent a value higher or equal than 1. So a step will be fixed as 1 second.");
			this.oneStepInSeconds = 1;
		} else {
			this.oneStepInSeconds = oneStepInSeconds;
		}
	}

	/**
	 * @return the averageNodeDegree
	 */
	public int getNodesDegree() {
		return nodesDegree;
	}

	/**
	 * @param averageNodeDegree
	 *            the averageNodeDegree to set
	 */
	public void setNodesDegree(int averageNodeDegree) {
		this.nodesDegree = averageNodeDegree;
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return
	 */
	public Date getCurrentDate() {
		long step = this.schedule.getSteps();
		if (this.timeConversor != null) {
			Date currentDate = this.timeConversor.getDate(step);
			return currentDate;
		} else {
			return null;
		}
	}

	/**
	 * @return the userField2D
	 */
	public Continuous2D getUsersField2D() {
		return usersField2D;
	}

	/**
	 * @return the userField3D
	 */
	public Continuous3D getUsersField3D() {
		return usersField3D;
	}

	/**
	 * @param userField2D
	 *            the userField2D to set
	 */
	public void setUsersField2D(Continuous2D usersField2D) {
		this.usersField2D = usersField2D;
		if (this.gui != null) {
			this.gui.getUsersPortrayal2D().setField(usersField2D);
			this.gui.getNetworkPortrayal2D().setField(
					new SpatialNetwork2D(usersField2D, this.network));
		}

	}

	/**
	 * @param userField3D
	 *            the userField3D to set
	 */
	public void setUsersField3D(Continuous3D usersField3D) {
		this.usersField3D = usersField3D;
		if (this.gui != null) {
			this.gui.getUsersPortrayal3D().setField(usersField3D);
			this.gui.getNetworkPortrayal3D().setField(
					new SpatialNetwork3D(usersField3D, this.network));
		}
	}

	/**
	 * @return the timeConversor
	 */
	public TimeConversor getTimeConversor() {
		return timeConversor;
	}

	/**
	 * @return
	 */
	public List<Event> getCurrentEvents() {
		if (this.eventManager != null) {
			return this.eventManager.getCurrentEvents();
		} else {
			return null;
		}
	}

	/**
	 * @return
	 */
	public List<Event> getPendingEvents() {
		if (this.eventManager != null) {
			return this.eventManager.getPendingEvents();
		} else {
			return null;
		}
	}

	/**
	 * @return
	 */
	public List<Event> getPastEvents() {
		if (this.eventManager != null) {
			return this.eventManager.getPastEvents();
		} else {
			return null;
		}
	}

	/**
	 * @return the gui
	 */
	public TwitterSimulationGUI getGui() {
		return gui;
	}

	/**
	 * @param gui
	 *            the gui to set
	 */
	public void setGui(TwitterSimulationGUI gui) {
		this.gui = gui;
	}

	/**
	 * @param network
	 */
	public void setNetwork(Network network) {
		this.network = network;

	}

	/**
	 * @return the tweetManager
	 */
	public TweetManager getTweetManager() {
		return tweetManager;
	}

	/**
	 * @return the topicsManager
	 */
	public TopicManager getTopicManager() {
		return topicManager;
	}

	/**
	 * @return the userManager
	 */
	public UserManager getUserManager() {
		return userManager;
	}

	/**
	 * @return
	 */
	public EventManager getEventManager() {
		return this.eventManager;
	}

	/**
	 * @return
	 */
	public Analyzer getAnalyzer() {
		return this.analyzer;
	}

	/**
	 * @return the network
	 */
	public Network getNetwork() {
		return this.network;
	}

	/**
	 * @return the users
	 */
	public List<User> getUsers() {
		return this.users;
	}

	/**
	 * Create a link between two users.
	 * 
	 * @param follower
	 * @param followed
	 */
	public void createNewLink(User follower, User followed) {
		Link link = new Link(this.links.size(),
				this.timeConversor.getDate(this.schedule.getSteps()), follower,
				followed);
		this.links.add(link);
		this.network.addEdge(follower, followed, link);

	}

	/**
	 * Delete a link between two users and store it in deletedLinks list.
	 * 
	 * @param follower
	 * @param followed
	 */
	public void destroyLink(User follower, User followed) {
		Link link = null;
		for (Link l : this.links) {
			if (l.getFollower().equals(follower)
					&& l.getFollowed().equals(followed)) {
				link = l;
			}
		}
		if (link != null) {
			Bag edges = this.network.getEdgesOut(follower);
			for (int i = 0; i < edges.size(); i++) {
				Edge edge = (Edge) edges.getValue(i);
				Link l = (Link) edge.getInfo();
				if (l.getFollowed().equals(followed)
						&& l.getFollower().equals(follower)) {
					this.network.removeEdge((Edge) edges.get(i));
					this.links.remove(link);
					link.setUnfollowDate(this.getCurrentDate());
					this.deletedLinks.add(link);
					logger.fine("Link " + link.getId()
							+ " has been succesfully destroyed.");
					break;
				}
			}
		} else {
			logger.warning("Link has not been destroyed, because it does not exist. Follower: "
					+ follower.getId() + " and Followed: " + followed.getId());
		}
	}

	/**
	 * @return
	 */
	public List<Link> getDeletedLinks() {
		return this.deletedLinks;
	}

}
