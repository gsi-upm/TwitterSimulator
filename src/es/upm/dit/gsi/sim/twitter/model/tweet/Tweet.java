/**
 * es.upm.dit.gsi.sim.twitter.model.Tweet.java
 */
package es.upm.dit.gsi.sim.twitter.model.tweet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.upm.dit.gsi.sim.twitter.TwitterSimulation;
import es.upm.dit.gsi.sim.twitter.model.user.User;

/**
 * Project: twitter-simulator
 * File: es.upm.dit.gsi.sim.twitter.model.tweet.Tweet.java
 * 
 * This model represents a Tweet, i.e. a message of Twitter.
 * This object class can represents a tweet, a direct message
 * and a reply. This can be checked by public methods such as,
 * isDirectMessage() or isReply(). If both are false, it is
 * a regular tweet.  
 * 
 * Grupo de Sistemas Inteligentes
 * Departamento de Ingeniería de Sistemas Telemáticos
 * Universidad Politécnica de Madrid (UPM)
 * 
 * @author Álvaro Carrera Barroso
 * @email a.carrera@gsi.dit.upm.es
 * @twitter @alvarocarrera
 * @date 03/03/2013
 * @version 0.2
 * 
 */
public class Tweet {

	/**
	 * The step when the tweet was published.
	 */
	private long timestamp;

	/**
	 * When the tweet was deleted.
	 */
	private long deletedTimestamp;

	/**
	 * The author of the tweet.
	 */
	private User author;

	/**
	 * List of users that retweet this tweet and when they did it(step).
	 */
	private Map<User, Long> retweeters;

	/**
	 * Topic of the tweet.
	 */
	private List<String> topics;

	/**
	 * Direct Message or not.
	 */
	private boolean directMessage;

	/**
	 * If it is a reply or a hidden mention.
	 */
	private boolean reply;

	/**
	 * Replied users.
	 */
	private List<User> repliedUsers;

	/**
	 * Mentioned users.
	 */
	private List<User> mentionedUsers;

	/**
	 * User that receives the direct message.
	 */
	private User directMessageReceiverUser;

	/**
	 * Constructor
	 * 
	 * @param step
	 * @param author
	 * @param topics
	 */
	public Tweet(long step, User author, List<String> topics) {
		this.timestamp = step;
		this.author = author;
		this.topics = topics;
		this.retweeters = new HashMap<User, Long>();
		this.reply = false;
		this.directMessage = false;
	}

	/**
	 * When a new user retweets.
	 * @param user
	 * @param simulation
	 */
	public void addRetweeter(User user, TwitterSimulation simulation) {
		this.retweeters.put(user, simulation.schedule.getSteps());
	}

	/**
	 * 
	 * When the tweet was published.
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * 
	 * When the tweet was deleted.
	 * @return the deletedTimestamp
	 */
	public long getDeletedTimestamp() {
		return deletedTimestamp;
	}

	/**
	 * @param deletedTimestamp
	 *            the deletedTimestamp to set
	 */
	public void setDeletedTimestamp(long deletedTimestamp) {
		this.deletedTimestamp = deletedTimestamp;
	}

	/**
	 * @return the author
	 */
	public User getAuthor() {
		return author;
	}

	/**
	 * @return the retweeters
	 */
	public Map<User, Long> getRetweeters() {
		return retweeters;
	}

	/**
	 * @return the topics list
	 */
	public List<String> getTopics() {
		return this.topics;
	}

	/**
	 * @return the isReply
	 */
	public boolean isReply() {
		return reply;
	}

	/**
	 * @return the isDirectMessage
	 */
	public boolean isDirectMessage() {
		return directMessage;
	}

	/**
	 * @param directMessage
	 *            the isDirectMessage to set
	 */
	public void setDirectMessage(User receiver) {
		this.directMessage = true;
		this.directMessageReceiverUser = receiver;
	}

	/**
	 * @return
	 */
	public User getDirectMessageReceiverUser() {
		if (this.directMessageReceiverUser != null) {
			return this.directMessageReceiverUser;
		} else {
			return null;
		}
	}

	/**
	 * @return the repliedUsers
	 */
	public List<User> getRepliedUsers() {
		return repliedUsers;
	}

	/**
	 * @param repliedUsers
	 *            the repliedUsers to set
	 */
	public void setRepliedUsers(List<User> repliedUsers) {
		this.reply = true;
		this.repliedUsers = repliedUsers;
	}

	/**
	 * @return the mentionedUsers
	 */
	public List<User> getMentionedUsers() {
		return mentionedUsers;
	}

	/**
	 * @param mentionedUsers
	 *            the mentionedUsers to set
	 */
	public void setMentionedUsers(List<User> mentionedUsers) {
		this.mentionedUsers = mentionedUsers;
	}

	/**
	 * @return
	 */
	public boolean isRetweet() {
		return this.retweeters.size() > 0;
	}

}
