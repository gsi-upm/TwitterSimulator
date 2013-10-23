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
 * es.upm.dit.gsi.sim.twitter.model.user.User.java
 */
package es.upm.dit.gsi.sim.twitter.model.user;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import sim.engine.SimState;
import sim.engine.Steppable;
import es.upm.dit.gsi.sim.twitter.TwitterSimulation;
import es.upm.dit.gsi.sim.twitter.model.tweet.Tweet;
import es.upm.dit.gsi.sim.twitter.model.user.behaviour.Behaviour;

/**
 * Project: twitter-simulator File:
 * es.upm.dit.gsi.sim.twitter.model.user.User.java
 * 
 * This Java class represents a Twitter user.
 * 
 * Grupo de Sistemas Inteligentes Departamento de Ingeniería de Sistemas
 * Telemáticos Universidad Politécnica de Madrid (UPM)
 * 
 * @author Álvaro Carrera Barroso
 * @email a.carrera@gsi.dit.upm.es
 * @twitter @alvarocarrera
 * @date 20/02/2013
 * @version 0.1
 * 
 */
public class User implements Steppable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2285825503406663583L;

	/**
	 * Logger
	 */
	private Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * Graphical position - X/Y/Z
	 */
	private double[] position;

	/**
	 * User id - number to identify any user.
	 */
	private int id;

	/**
	 * User name - non-unique id. The name can be equal in several users.
	 */
	private String name;

	/**
	 * The behaviour of the agent.
	 */
	private Behaviour behaviour;

	/**
	 * List of all followers of the user.
	 */
	private List<User> followers;

	/**
	 * List of all user that the user follows.
	 */
	private List<User> followeds;

	/**
	 * List of tweets sent by the user.
	 */
	private List<Tweet> tweets;

	/**
	 * List of favorites tweets.
	 */
	private List<Tweet> favoritesTweets;

	/**
	 * List of mentions and replies.
	 */
	private List<Tweet> mentions;

	/**
	 * List of direct messages.
	 */
	private List<Tweet> directMessages;

	/**
	 * When was the last time that
	 */
	private long updateTweetsInStep;

	/**
	 * Current timeline.
	 */
	private List<Tweet> timeline;

	/**
	 * Constructor of the user
	 * 
	 */
	public User(int id) {
		this.id = id;
		this.followers = new ArrayList<User>();
		this.followeds = new ArrayList<User>();
		this.tweets = new ArrayList<Tweet>();
		this.timeline = new ArrayList<Tweet>();
		this.setPosition(new double[3]);
		this.updateTweetsInStep = 0;
		logger.fine("User " + id + " successfully created.");
	}

	/**
	 * @return the position
	 */
	public double[] getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(double[] position) {
		this.position = position;
	}

	/**
	 * @return
	 */
	public List<Tweet> getTimeLine(TwitterSimulation simulation) {
		long currentStep = simulation.schedule.getSteps();
		if (currentStep > this.updateTweetsInStep) {
			for (User followed : this.followeds) {
				for (Tweet tweet : followed
						.getTweetsSince(this.updateTweetsInStep)) {
					if (tweet.isRetweet() || !tweet.isReply()
							&& !tweet.isDirectMessage()) {
						if (!this.timeline.contains(tweet)) {
							try {
								this.timeline.add((int) tweet.getTimestamp(),
										tweet);
							} catch (IndexOutOfBoundsException e) {
								this.timeline.add(tweet);
							}
						}
					} else if (tweet.isReply()) {
						List<User> repliedUsers = tweet.getRepliedUsers();
						boolean visible = false;
						for (User replied : repliedUsers) {
							if (this.followeds.contains(replied)) {
								visible = true;
							} else {
								visible = false;
								break;
							}
						}
						if (visible && !this.timeline.contains(tweet)) {
							try {
								this.timeline.add((int) tweet.getTimestamp(),
										tweet);
							} catch (IndexOutOfBoundsException e) {
								this.timeline.add(tweet);
							}
						}
					}
				}
			}
		}
		return this.timeline;
	}

	/**
	 * @param step
	 * @return
	 */
	public List<Tweet> getTweetsSince(long step) {
		List<Tweet> queryResult = new ArrayList<Tweet>();
		for (Tweet tweet : this.tweets) {
			if (tweet.getTimestamp() >= step) {
				queryResult.add(tweet);
			}
		}
		return queryResult;
	}

	/**
	 * @param tweetsQuantity
	 * @return
	 */
	public List<Tweet> getLastTweetsFromTimeLine(int tweetsQuantity,
			TwitterSimulation simulation) {
		List<Tweet> timeLine = this.getTimeLine(simulation);
		if (tweetsQuantity >= timeLine.size()) {
			return timeLine;
		} else {
			List<Tweet> recentTweets = timeLine.subList(timeLine.size()
					- tweetsQuantity - 1, timeLine.size() - 1);
			return recentTweets;
		}
	}

	/**
	 * @param topics
	 * @param mentionedUsers
	 * @param simulation
	 */
	public void publish(List<String> topics, List<User> mentionedUsers,
			TwitterSimulation simulation) {
		Tweet tweet = simulation.getTweetManager().createTweet(simulation,
				this, topics, mentionedUsers);
		this.tweets.add(tweet);
	}

	/**
	 * @param receiver
	 * @param topics
	 * @param mentionedUsers
	 * @param simulation
	 */
	public void sendDirectMessage(User receiver, List<String> topics,
			List<User> mentionedUsers, TwitterSimulation simulation) {
		Tweet tweet = simulation.getTweetManager().createDirectMessage(
				simulation, this, receiver, topics, mentionedUsers);
		this.addDirectMessage(tweet);
	}

	/**
	 * @return
	 */
	public List<Tweet> getSentDirectMessages() {
		List<Tweet> sent = new ArrayList<Tweet>();
		if (this.directMessages != null) {
			for (Tweet dm : this.directMessages) {
				if (dm.getAuthor() == this) {
					sent.add(dm);
				}
			}
		}
		if (sent.size() == 0) {
			return null;
		} else {
			return sent;
		}
	}

	/**
	 * @return
	 */
	public List<Tweet> getReceivedDirectMessages() {
		List<Tweet> received = new ArrayList<Tweet>();
		if (this.directMessages != null) {
			for (Tweet dm : this.directMessages) {
				if (dm.getAuthor() != this) {
					received.add(dm);
				}
			}
		}
		if (received.size() == 0) {
			return null;
		} else {
			return received;
		}
	}

	/**
	 * @param repliedUsers
	 * @param topics
	 * @param mentionedUsers
	 * @param simulation
	 */
	public void publishReply(List<User> repliedUsers, List<String> topics,
			List<User> mentionedUsers, TwitterSimulation simulation) {
		Tweet twett = simulation.getTweetManager().createReply(simulation,
				this, repliedUsers, topics, mentionedUsers);
		this.tweets.add(twett);
	}

	/**
	 * @param tweet
	 */
	public void delete(Tweet tweet, TwitterSimulation simulation) {
		simulation.getTweetManager().deleteTweet(tweet,
				simulation.schedule.getSteps());
		this.tweets.remove(tweet);
	}

	/**
	 * @param tweet
	 */
	public void retweet(Tweet tweet, TwitterSimulation simulation) {
		tweet.addRetweeter(this, simulation);
		this.tweets.add(tweet);
	}

	/**
	 * @param user
	 *            to follow
	 */
	public void follow(User user, TwitterSimulation simulation) {
		simulation.createNewLink(this, user);
		this.followeds.add(user);
		user.addFollower(this);
	}

	/**
	 * @param user
	 *            to unfollow
	 */
	public void unfollow(User user, TwitterSimulation simulation) {
		simulation.destroyLink(this, user);
		this.followeds.remove(user);
		user.removeFollower(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sim.engine.Steppable#step(sim.engine.SimState)
	 */
	public void step(SimState simstate) {
		TwitterSimulation simulation = (TwitterSimulation) simstate;
		this.behaviour.act(simulation);
	}

	/**
	 * @param follower
	 *            to add
	 */
	public void addFollower(User user) {
		this.followers.add(user);
	}

	/**
	 * @param followed
	 *            to add
	 */
	public void addFollowed(User user) {
		this.followeds.add(user);
	}

	/**
	 * @param follower
	 *            to remove
	 */
	public void removeFollower(User user) {
		this.followers.remove(user);
	}

	/**
	 * @param follower
	 *            to remove
	 */
	public void removeFollowed(User user) {
		this.followeds.remove(user);
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the followers
	 */
	public List<User> getFollowers() {
		return followers;
	}

	/**
	 * @return the followeds
	 */
	public List<User> getFolloweds() {
		return followeds;
	}

	/**
	 * @return the tweets
	 */
	public List<Tweet> getTweets() {
		return tweets;
	}

	/**
	 * @param behaviour
	 */
	public void setBehaviour(Behaviour behaviour) {
		this.behaviour = behaviour;
	}

	/**
	 * @return
	 */
	public Behaviour getBehaviour() {
		return this.behaviour;
	}

	/**
	 * @return
	 */
	public List<Tweet> getFavoriteTweets() {
		return this.favoritesTweets;
	}

	/**
	 * @param tweet
	 */
	public void addFavoriteTweet(Tweet tweet) {
		if (this.favoritesTweets == null) {
			this.favoritesTweets = new ArrayList<Tweet>();
		}
		this.favoritesTweets.add(tweet);
	}

	/**
	 * @return
	 */
	public List<Tweet> getMentions() {
		return this.mentions;
	}

	/**
	 * @param tweet
	 */
	public void addMentions(Tweet tweet) {
		if (this.mentions == null) {
			this.mentions = new ArrayList<Tweet>();
		}
		this.mentions.add(tweet);
	}

	/**
	 * @return
	 */
	public List<Tweet> getDirectMessages() {
		return this.directMessages;
	}

	/**
	 * @param tweet
	 */
	public void addDirectMessage(Tweet tweet) {
		if (this.directMessages == null) {
			this.directMessages = new ArrayList<Tweet>();
		}
		this.directMessages.add(tweet);
	}

	/**
	 * @return
	 */
	public boolean hasFolloweds() {
		return this.followeds != null && this.followeds.size() > 0;
	}

	/**
	 * @return
	 */
	public boolean hasFolowers() {
		return this.followers != null && this.followers.size() > 0;
	}

	/**
	 * @return
	 */
	public boolean hasAnyTweetInTimeline() {
		return this.timeline != null && this.timeline.size() > 0;
	}

}
