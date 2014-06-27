/*******************************************************************************
 * Copyright (c) 2013 alvarocarrera. Grupo de Sistemas Inteligentes - Universidad Polit�cnica de Madrid. (GSI-UPM)
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
 * es.upm.dit.gsi.sim.twitter.util.TwitterNetworkFactory.java
 */
package es.upm.dit.gsi.sim.twitter.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import sim.field.network.Network;
import sim.util.Bag;
import es.upm.dit.gsi.sim.twitter.model.user.Link;
import es.upm.dit.gsi.sim.twitter.model.user.User;
import es.upm.dit.gsi.sim.twitter.model.user.behaviour.BayesianBehaviour;

/**
 * Project: twitter-simulator File:
 * es.upm.dit.gsi.sim.twitter.util.TwitterNetworkFactory.java
 * 
 * Factory to load network of Twitter users from JSON files or to create a
 * network manually.
 * 
 * Grupo de Sistemas Inteligentes Departamento de Ingenier�a de Sistemas
 * Telem�ticos Universidad Polit�cnica de Madrid (UPM)
 * 
 * @author �lvaro Carrera Barroso
 * @email a.carrera@gsi.dit.upm.es
 * @twitter @alvarocarrera
 * @date 08/03/2013
 * @version 0.1
 * 
 */
public class TwitterNetworkFactory {

	/**
	 * 
	 */
	Logger logger = Logger.getLogger(TwitterNetworkFactory.class.getName());

	/**
	 * 
	 */
	private Network network;

	/**
	 * 
	 */
	private int usersCounter;

	/**
	 * 
	 */
	private int linksCounter;

	/**
	 * Constructor
	 * 
	 */
	public TwitterNetworkFactory() {
		this.network = new Network(true);
		this.usersCounter = 0;
		this.linksCounter = 0;
	}

	/**
	 * @return
	 */
	public User createUser() {
		User newUser = new User(this.usersCounter++);
		this.network.addNode(newUser);
		return newUser;
	}

	/**
	 * @return
	 */
	public User createUser(int id) {
		User newUser = new User(id);
		this.usersCounter++;
		this.network.addNode(newUser);
		return newUser;
	}

	/**
	 * @param network
	 * @param follower
	 * @param followed
	 * @param linkId
	 * @return
	 */
	public Link createLink(User follower, User followed) {
		Link link = new Link(this.linksCounter++);
		link.setUsers(null, follower, followed);
		follower.addFollowed(followed);
		followed.addFollower(follower);
		this.network.addEdge(follower, followed, link);
		return link;
	}

	/**
	 * @return
	 */
	public Network getNetwork() {
		return this.network;
	}

	/**
	 * @return
	 */
	public List<User> getUsers() {
		Object[] objs = this.network.getAllNodes().objs;
		List<User> users = new ArrayList<User>();
		for (Object o : objs) {
			User user = (User) o;
			users.add(user);
		}
		return users;
	}

	/**
	 * @param json
	 * @return
	 * @throws IOException
	 */
	public Network readUserGraph(String followersFile, String usersDataFile,
			String usersBehavioursFile) throws IOException {

		this.network = new Network(true);

		JSONObject jsonFollowers = null;
		JSONObject jsonUsers = null;
		JSONObject jsonBehaviours = null;

		String text = null;
		try {
			text = this.readFile(followersFile);
			jsonFollowers = new JSONObject(text);
		} catch (Exception e) {
			logger.warning("No followers JSON file loaded. The simulation is finishing...");
			System.exit(1);
		}
		try {
			text = this.readFile(usersDataFile);
			jsonUsers = new JSONObject(text);
		} catch (Exception e) {
			logger.warning("No users data JSON file loaded.");
		}
		try {
			text = this.readFile(usersBehavioursFile);
			jsonBehaviours = new JSONObject(text);
		} catch (Exception e) {
			logger.warning("No users behaviours JSON file loaded.");
		}

		@SuppressWarnings("unchecked")
		Set<String> keys = jsonFollowers.keySet();
		for (String key : keys) {
			this.createUserFromJSON(Integer.parseInt(key), jsonUsers,
					jsonBehaviours);
		}

		for (String userId : keys) {
			User user = this.getUser(Integer.parseInt(userId));
			JSONArray followers = jsonFollowers.getJSONArray(userId);
			int length = followers.length();
			for (int i = 0; i < length; i++) {
				int followerId = followers.getInt(i);
				if (this.getUser(followerId) == null) {
					this.createUserFromJSON(followerId, jsonUsers,
							jsonBehaviours);
				}
				this.createLink(this.getUser(followerId), user);
			}
		}

		return this.network;
	}

	/**
	 * @param id
	 * @param jsonUsersData
	 * @param jsonBehavioursData
	 * @return
	 */
	private User createUserFromJSON(int id, JSONObject jsonUsersData,
			JSONObject jsonBehavioursData) {
		User user = this.createUser(id);
		try {
			JSONObject jsonUser = jsonUsersData.getJSONObject(Integer
					.toString(id));
			user.setName(jsonUser.getString("name"));
			logger.finer("User with ID: " + id + " set name to: "
					+ user.getName());
		} catch (Exception e) {
			logger.fine("No info for user with ID: " + id);
		}
		try {
			JSONObject jsonBehaviour = jsonBehavioursData.getJSONObject(Integer
					.toString(id));
			new BayesianBehaviour("Bayes-" + user.getName(),
					jsonBehaviour.getInt("boring"), user,
					jsonBehaviour.getString("path"),
					jsonBehaviour.getDouble("threshold"));
		} catch (Exception e) {
			logger.fine("No behaviour defined for user with ID: " + id
					+ ". RandomBehaviour will be set.");
		}
		return user;
	}

	/**
	 * @param id
	 * @return
	 */
	private User getUser(int id) {
		Bag users = this.network.getAllNodes();
		for (Object obj : users.objs) {
			User user = (User) obj;
			if (user != null && user.getId() == id) {
				return user;
			}
		}
		return null;
	}

	/**
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private String readFile(String file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");

		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(ls);
		}
		reader.close();
		return stringBuilder.toString();
	}
}
