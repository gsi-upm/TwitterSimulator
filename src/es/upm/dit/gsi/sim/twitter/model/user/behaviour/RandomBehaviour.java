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
 * es.upm.dit.gsi.sim.twitter.model.user.behaviour.RandomBehaviour.java
 */
package es.upm.dit.gsi.sim.twitter.model.user.behaviour;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import es.upm.dit.gsi.sim.twitter.TwitterSimulation;
import es.upm.dit.gsi.sim.twitter.model.event.Event;
import es.upm.dit.gsi.sim.twitter.model.tweet.Tweet;
import es.upm.dit.gsi.sim.twitter.model.user.User;

/**
 * Project: twitter-simulator File:
 * es.upm.dit.gsi.sim.twitter.model.user.behaviour.RandomBehaviour.java
 * 
 * Grupo de Sistemas Inteligentes Departamento de Ingeniería de Sistemas
 * Telemáticos Universidad Politécnica de Madrid (UPM)
 * 
 * @author Álvaro Carrera Barroso
 * @email a.carrera@gsi.dit.upm.es
 * @twitter @alvarocarrera
 * @date 26/02/2013
 * @version 0.1
 * 
 */
public class RandomBehaviour extends Behaviour {

	/**
	 * Logger
	 */
	private Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * Constructor
	 * 
	 */
	public RandomBehaviour(String name, int boringTimer, User user) {
		super(name, boringTimer, user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.upm.dit.gsi.sim.twitter.model.UserBehaviour#interact(es.upm.dit
	 * .gsi.sim.twitter.model.User,
	 * es.upm.dit.gsi.sim.twitter.TwitterSimulation)
	 */
	@Override
	public void act(TwitterSimulation simulation) {

		List<Event> currentEvents = simulation.getEventManager()
				.getCurrentEvents();

		// Variable to decide
		double random = simulation.random.nextDouble();

		if (currentEvents != null && currentEvents.size() != 0 && random < 0.5) {
			for (Event e : currentEvents) {
				List<String> eventTopics = e.getTopics();
				List<String> topics = new ArrayList<String>();
				int aux = simulation.random.nextInt(eventTopics.size());
				topics.add(eventTopics.get(aux));
				this.getUser().publish(topics, null, simulation);
			}
		} else if (this.amIBored(simulation)) {
			if (random < 0.25) {
				// publish something
				List<String> topics = new ArrayList<String>();

				List<String> allTopics = simulation.getTopicManager()
						.getTopics();
				int numOfTopics = simulation.random.nextInt(3);
				for (int i = 0; i <= numOfTopics; i++) {
					int pos = simulation.random.nextInt(allTopics.size());
					if (!topics.contains(allTopics.get(pos))) {
						topics.add(allTopics.get(pos));
					}
				}

				// mentioned users
				List<User> mentionedUsers = null;
				if (this.getUser().hasFolloweds()) {
					if (random < 0.1) {
						mentionedUsers = new ArrayList<User>();
						int user = simulation.random.nextInt(this.getUser()
								.getFolloweds().size());
						mentionedUsers.add(this.getUser().getFolloweds()
								.get(user));
					} else if (random < 0.2) {
						mentionedUsers = new ArrayList<User>();
						int numOfMentionedUsers = simulation.random.nextInt(3);
						for (int i = 0; i <= numOfMentionedUsers; i++) {
							int user = simulation.random.nextInt(this.getUser()
									.getFolloweds().size());
							if (!mentionedUsers.contains(this.getUser()
									.getFolloweds().get(user))) {
								mentionedUsers.add(this.getUser()
										.getFolloweds().get(user));
							}
						}
					}
				}

				this.getUser().publish(topics, mentionedUsers, simulation); // IMPORTANT
																			// METHOD
				logger.fine("Tweet sent by user " + this.getUser().getId());
			} else if (random < 0.50) {
				// retweet something
				if (this.getUser().hasAnyTweetInTimeline()) {
					List<Tweet> timeline = this.getUser()
							.getLastTweetsFromTimeLine(10, simulation);
					int tweetNumber = simulation.random
							.nextInt(timeline.size());
					this.getUser().retweet(timeline.get(tweetNumber),
							simulation);// IMPORTANT METHOD
				}
			} else if (random < 0.65) {
				// follow new one
				int newFollowedNumber = simulation.random.nextInt(simulation
						.getUsers().size());
				User newFollowed = simulation.getUsers().get(newFollowedNumber);
				if (!this.getUser().getFolloweds().contains(newFollowed)) {
					this.getUser().follow(newFollowed, simulation);// IMPORTANT
																	// METHOD
					logger.fine("User " + newFollowed.getId()
							+ " has a new follower: User "
							+ this.getUser().getId());
				}
			} else if (random < 0.75) {
				// unfollow someone
				if (this.getUser().hasFolloweds()) {
					int newUnfollowedNumber = simulation.random.nextInt(this
							.getUser().getFolloweds().size());
					User newUnfollowed = this.getUser().getFolloweds()
							.get(newUnfollowedNumber);
					this.getUser().unfollow(newUnfollowed, simulation);// IMPORTANT
																		// METHOD
					logger.fine("User " + newUnfollowed.getId()
							+ " has lost a follower: User "
							+ this.getUser().getId());
				}
			} else if (random < 0.80) {
				// favorite one
				if (this.getUser().hasAnyTweetInTimeline()) {
					List<Tweet> timeline = this.getUser()
							.getLastTweetsFromTimeLine(10, simulation);
					int tweetNumber = simulation.random
							.nextInt(timeline.size());
					this.getUser().addFavoriteTweet(timeline.get(tweetNumber));// IMPORTANT
																				// METHOD
					logger.fine("User " + this.getUser().getId()
							+ " has added a tweet as favorite.");
				}
			} else if (random < 0.90) {
				// reply
				if (this.getUser().hasAnyTweetInTimeline()) {
					List<Tweet> timeline = this.getUser()
							.getLastTweetsFromTimeLine(10, simulation);
					int tweetNumber = simulation.random
							.nextInt(timeline.size());
					User author = timeline.get(tweetNumber).getAuthor();
					List<User> repliedUsers = new ArrayList<User>();
					repliedUsers.add(author);
					List<String> topics = new ArrayList<String>();
					List<String> allTopics = simulation.getTopicManager()
							.getTopics();
					int numOfTopics = simulation.random.nextInt(3);
					for (int i = 0; i <= numOfTopics; i++) {
						int pos = simulation.random.nextInt(allTopics.size());
						if (!topics.contains(allTopics.get(pos))) {
							topics.add(allTopics.get(pos));
						}
					}

					// mentioned users
					List<User> mentionedUsers = null;
					if (random < 0.1) {
						mentionedUsers = new ArrayList<User>();
						int user = simulation.random.nextInt(this.getUser()
								.getFolloweds().size());
						mentionedUsers.add(this.getUser().getFolloweds()
								.get(user));
					} else if (random < 0.2) {
						mentionedUsers = new ArrayList<User>();
						int numOfMentionedUsers = simulation.random.nextInt(3);
						for (int i = 0; i <= numOfMentionedUsers; i++) {
							int user = simulation.random.nextInt(this.getUser()
									.getFolloweds().size());
							if (!mentionedUsers.contains(this.getUser()
									.getFolloweds().get(user))) {
								mentionedUsers.add(this.getUser()
										.getFolloweds().get(user));
							}
						}
					}
					this.getUser().publishReply(repliedUsers, topics,
							mentionedUsers, simulation); // IMPORTANT METHOD
					logger.fine("User " + this.getUser().getId()
							+ " has replied a tweet of User "
							+ repliedUsers.get(0));
				}
			} else if (random < 0.95) {
				// direct message
				if (this.getUser().hasFolloweds()) {
					int newFollowedNumber = simulation.random.nextInt(this
							.getUser().getFolloweds().size());
					User receiver = this.getUser().getFolloweds()
							.get(newFollowedNumber);
					List<String> topics = new ArrayList<String>();
					List<String> allTopics = simulation.getTopicManager()
							.getTopics();
					int numOfTopics = simulation.random.nextInt(3);
					for (int i = 0; i <= numOfTopics; i++) {
						int pos = simulation.random.nextInt(allTopics.size());
						if (!topics.contains(allTopics.get(pos))) {
							topics.add(allTopics.get(pos));
						}
					}

					// mentioned users
					List<User> mentionedUsers = null;
					if (random < 0.1) {
						mentionedUsers = new ArrayList<User>();
						int user = simulation.random.nextInt(this.getUser()
								.getFolloweds().size());
						mentionedUsers.add(this.getUser().getFolloweds()
								.get(user));
					} else if (random < 0.2) {
						mentionedUsers = new ArrayList<User>();
						int numOfMentionedUsers = simulation.random.nextInt(3);
						for (int i = 0; i <= numOfMentionedUsers; i++) {
							int user = simulation.random.nextInt(this.getUser()
									.getFolloweds().size());
							if (!mentionedUsers.contains(this.getUser()
									.getFolloweds().get(user))) {
								mentionedUsers.add(this.getUser()
										.getFolloweds().get(user));
							}
						}
					}
					this.getUser().sendDirectMessage(receiver, topics,
							mentionedUsers, simulation); // IMPORTANT METHOD
					logger.fine("User " + this.getUser().getId()
							+ " has sent a direct message to "
							+ receiver.getId());
				}
			} else if (random < 1) {
				this.getUser().getTimeLine(simulation); // to update the
														// timeline
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.upm.dit.gsi.sim.twitter.model.UserBehaviour#getColor()
	 */
	@Override
	public Color getColor() {
		int followers = this.getUser().getFollowers().size();
		int c = followers * 10;
		if (this.getUser().getId() == 15077189) {
			return Color.red;
		} else if (c != 0 && c < 200) {
			return new Color(c, c, c); // From black to white... Gray
		} else if (c >= 200) {
			return new Color(0, 255, 0); // Green
		} else {
			return new Color(0, 0, 0); // Black
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.upm.dit.gsi.sim.twitter.model.user.behaviour.Behaviour#reset()
	 */
	@Override
	public void reset() {
		// Nothing to reset
	}

}
