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
 * es.upm.dit.gsi.sim.twitter.util.TweetManager.java
 */
package es.upm.dit.gsi.sim.twitter.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.upm.dit.gsi.sim.twitter.TwitterSimulation;
import es.upm.dit.gsi.sim.twitter.model.tweet.Tweet;
import es.upm.dit.gsi.sim.twitter.model.user.User;

/**
 * Project: twitter-simulator File:
 * es.upm.dit.gsi.sim.twitter.util.TweetManager.java
 * 
 * This class handle all tweets. It is the unique class where
 * Tweets are created.
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
public class TweetManager {

	/**
	 * List of all tweets.
	 */
	private List<Tweet> tweets;

	/**
	 * List of deleted tweets and when.
	 */
	private Map<Tweet, Long> deletedTweets;

	/**
	 * Constructor
	 * 
	 */
	public TweetManager() {
		this.tweets = new ArrayList<Tweet>();
		this.deletedTweets = new HashMap<Tweet, Long>();
	}

	/**
	 * @return
	 */
	public Tweet createTweet(TwitterSimulation simulation, User author,
			List<String> topics, List<User> mentionedUsers) {
		Tweet tweet = new Tweet(simulation.schedule.getSteps(), author, topics);
		if (mentionedUsers != null) {
			tweet.setMentionedUsers(mentionedUsers);
			for (User mentioned : mentionedUsers) {
				mentioned.addMentions(tweet);
			}
		}
		this.tweets.add(tweet);
		return tweet;
	}

	/**
	 * @param tweet
	 */
	public void deleteTweet(Tweet tweet, long step) {
		this.tweets.remove(tweet);
		this.deletedTweets.put(tweet, step);
	}

	/**
	 * @param step
	 * @param author
	 * @param receiver
	 * @param topics
	 * @return
	 */
	public Tweet createDirectMessage(TwitterSimulation simulation, User author,
			User receiver, List<String> topics, List<User> mentionedUsers) {
		Tweet tweet = this.createTweet(simulation, author, topics,
				mentionedUsers);
		tweet.setDirectMessage(receiver);
		this.tweets.add(tweet);
		receiver.addDirectMessage(tweet);
		return tweet;
	}

	/**
	 * @param step
	 * @param author
	 * @param repliedUsers
	 * @param topics
	 * @param mentionedUsers
	 * @return
	 */
	public Tweet createReply(TwitterSimulation simulation, User author,
			List<User> repliedUsers, List<String> topics,
			List<User> mentionedUsers) {
		Tweet tweet = this.createTweet(simulation, author, topics,
				mentionedUsers);
		tweet.setRepliedUsers(repliedUsers);
		for (User replied : repliedUsers) {
			replied.addMentions(tweet);
		}
		return tweet;
	}

	/**
	 * @return all tweets
	 */
	public List<Tweet> getTweets() {
		return this.tweets;
	}
}
