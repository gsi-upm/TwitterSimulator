/**
 * es.upm.dit.gsi.sim.twitter.model.user.behaviour.BayesianBehaviour.java
 */
package es.upm.dit.gsi.sim.twitter.model.user.behaviour;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import smile.Network;
import es.upm.dit.gsi.sim.twitter.TwitterSimulation;
import es.upm.dit.gsi.sim.twitter.model.event.Event;
import es.upm.dit.gsi.sim.twitter.model.tweet.Tweet;
import es.upm.dit.gsi.sim.twitter.model.user.User;

/**
 * Project: twitter-simulator File:
 * es.upm.dit.gsi.sim.twitter.model.user.behaviour.BayesianBehaviour.java
 * 
 * Grupo de Sistemas Inteligentes Departamento de Ingeniería de Sistemas
 * Telemáticos Universidad Politécnica de Madrid (UPM)
 * 
 * @author Álvaro Carrera Barroso
 * @email a.carrera@gsi.dit.upm.es
 * @twitter @alvarocarrera
 * @date 08/03/2013
 * @version 0.1
 * 
 */
public class BayesianBehaviour extends Behaviour {

	private final int PUBLISH_TWEET = 0;
	private final int RETWEET = 1;
	private final int FOLLOW = 2;
	private final int UNFOLLOW = 3;
	private final int FAVORITE = 4;
	private final int PUBLISH_REPLY = 5;
	private final int SEND_DM = 6;
	private final int DELETE_TWEET = 7;

	private final int numberOfActions = 8;

	/**
	 * XDSL file to load the causal model
	 */
	private String causalModelFilePath;

	/**
	 * Input of the causal model.
	 */
	private Map<String, String> evidences;

	/**
	 * To decide if the agent must do something.
	 */
	private double threshold;
	
	/**
	 * List of topics of interest to reason with them. 
	 */
	private List<String> interestingTopics;

	/**
	 * @param name
	 * @param boringTimer
	 * @param user
	 * @param causalModelFilePath
	 */
	public BayesianBehaviour(String name, int boringTimer, User user,
			String causalModelFilePath, double threshold) {
		super(name, boringTimer, user);
		this.threshold = threshold;
		this.causalModelFilePath = causalModelFilePath;
		this.evidences = new HashMap<String, String>();
		this.interestingTopics = new ArrayList<String>();

		Network net = new Network();
		net.readFile(this.causalModelFilePath);
		String[] nodes = net.getAllNodeIds();
		for (String id : nodes) {
			if (id.startsWith("topic_")) {
				this.interestingTopics.add(id.substring(6));				
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.upm.dit.gsi.sim.twitter.model.user.behaviour.Behaviour#act(es.upm.
	 * dit.gsi.sim.twitter.TwitterSimulation)
	 */
	@Override
	public void act(TwitterSimulation simulation) {

		List<Event> events = simulation.getCurrentEvents();
		for (Event e : events) {
			this.reasoningCycle(e);
		}
		// List<Tweet> tweets = this.getUser().getTimeLine(simulation);
		List<Tweet> tweets = this.getUser().getLastTweetsFromTimeLine(20,
				simulation);
		for (Tweet t : tweets) {
			this.reasoningCycle(t);
		}

	}

	private void reasoningCycle(Object obj) {
		// 1. add evidences - percept
		Tweet t = null;
		Event e = null;
		if (obj.getClass().equals(Tweet.class)) {
			t = (Tweet) obj;
			this.addEvidence("info_type", "tweet");
			List<String> topics = t.getTopics();
			for (String topic : topics) {
				if (this.interestingTopics.contains(topic)) {
					this.addEvidence("topic_"+topic, "YES");
				}
			}
		} else if (obj.getClass().equals(Event.class)) {
			e = (Event) obj;
			this.addEvidence("info_type", "event");
			List<String> topics = e.getTopics();
			for (String topic : topics) {
				if (this.interestingTopics.contains(topic)) {
					this.addEvidence("topic_"+topic, "YES");
				}
			}
		}
		
		
		// 2. decideAction - reasoning process
		List<Integer> actions = this.decideAction();

		// 3. performAction - act
		for (int action : actions) {
			switch (action) {
			case PUBLISH_TWEET:
				// this.getUser().publish(topics, mentionedUsers, simulation);
				break;
			case RETWEET:
				// this.getUser().publish(topics, mentionedUsers, simulation);
				break;
			case FOLLOW:
				// this.getUser().publish(topics, mentionedUsers, simulation);
				break;
			case UNFOLLOW:
				// this.getUser().publish(topics, mentionedUsers, simulation);
				break;
			case FAVORITE:
				// this.getUser().publish(topics, mentionedUsers, simulation);
				break;
			case PUBLISH_REPLY:
				// this.getUser().publish(topics, mentionedUsers, simulation);
				break;
			case SEND_DM:
				// this.getUser().publish(topics, mentionedUsers, simulation);
				break;
			case DELETE_TWEET:
				// this.getUser().publish(topics, mentionedUsers, simulation);
				break;
			}
		}

		// 4. clear old percepts - remove non-relevant evidences (past
		// evidences)
		this.evidences.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.upm.dit.gsi.sim.twitter.model.user.behaviour.Behaviour#getColor()
	 */
	@Override
	public Color getColor() {
		return Color.MAGENTA;
	}

	/**
	 * 
	 */
	private List<Integer> decideAction() {
		Network net = new Network();
		net.readFile(this.causalModelFilePath);

		for (Entry<String, String> evidence : this.evidences.entrySet()) {
			net.setEvidence(evidence.getKey(), evidence.getValue());
		}

		net.updateBeliefs();

		// [0]=NO state - [1]=YES state
		double[] probs = new double[this.numberOfActions];
		probs[0] = net.getNodeValue("publish_tweet")[1];
		probs[1] = net.getNodeValue("retweet")[1];
		probs[2] = net.getNodeValue("follow")[1];
		probs[3] = net.getNodeValue("unfollow")[1];
		probs[4] = net.getNodeValue("favorite")[1];
		probs[5] = net.getNodeValue("publish_reply")[1];
		probs[6] = net.getNodeValue("send_DM")[1];
		probs[7] = net.getNodeValue("delete_tweet")[1];

		List<Integer> actionsToDo = new ArrayList<Integer>();

		for (int i = 0; i < numberOfActions; i++) {
			if (probs[i] >= threshold) {
				actionsToDo.add(i);
			}
		}

		return actionsToDo;
	}

	/**
	 * @param variableName
	 * @param status
	 */
	private void addEvidence(String variableName, String status) {
		this.evidences.put(variableName, status);
	}

	/* (non-Javadoc)
	 * @see es.upm.dit.gsi.sim.twitter.model.user.behaviour.Behaviour#reset()
	 */
	@Override
	public void reset() {
		this.evidences = new HashMap<String, String>();
		this.interestingTopics = new ArrayList<String>();

		Network net = new Network();
		net.readFile(this.causalModelFilePath);
		String[] nodes = net.getAllNodeIds();
		for (String id : nodes) {
			if (id.startsWith("topic_")) {
				this.interestingTopics.add(id.substring(6));				
			}
		}
	}

}
