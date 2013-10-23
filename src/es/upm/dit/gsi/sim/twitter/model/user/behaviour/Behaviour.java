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
 * es.upm.dit.gsi.sim.twitter.model.user.behaviour.Behaviour.java
 */
package es.upm.dit.gsi.sim.twitter.model.user.behaviour;

import java.awt.Color;

import es.upm.dit.gsi.sim.twitter.TwitterSimulation;
import es.upm.dit.gsi.sim.twitter.model.user.User;

/**
 * Project: twitter-simulator File:
 * es.upm.dit.gsi.sim.twitter.model.user.behaviour.Behaviour.java
 * 
 * This class represent the behaviour of an user.
 * This is an abstract class and must be extended to be used.
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
public abstract class Behaviour {

	/**
	 * Type of behaviour
	 */
	private String name;

	/**
	 * In seconds
	 */
	private int boringTimer;

	/**
	 * User
	 */
	private User user;

	/**
	 * Constructor
	 * 
	 * @param name
	 * @param boringTimer
	 *            In seconds
	 */
	public Behaviour(String name, int boringTimer, User user) {
		this.name = name;
		this.user = user;
		this.user.setBehaviour(this);
		this.boringTimer = boringTimer;
	}

	/**
	 * 
	 * The user must decide to do any of the allowed actions: follow,
	 * unfollow, tweet, retweet, etc.
	 * 
	 * This method will be executed in all steps.
	 * 
	 * @param user
	 * @param simulation
	 */
	abstract public void act(TwitterSimulation simulation);

	/**
	 * @return the color to paint in the GUI.
	 */
	abstract public Color getColor();
	
	/**
	 * To ensure that an agent starts with a "clean" behaviour, not old-beliefs or goals.
	 */
	abstract public void reset();

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public User getUser() {
		return this.user;
	}
	
	/**
	 * @param user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the boringTimer
	 */
	public int getBoringTimer() {
		return boringTimer;
	}

	/**
	 * @param boringTimer
	 *            the boringTimer to set
	 */
	public void setBoringTimer(int boringTimer) {
		this.boringTimer = boringTimer;
	}

	/**
	 * Check if it is time to check the timeline.
	 * 
	 * @param simulation
	 * @return
	 */
	public boolean amIBored(TwitterSimulation simulation) {
		int steps = this.boringTimer / simulation.getOneStepInSeconds();
		if (steps <= 0) {
			return true;
		} else {
			return simulation.schedule.getSteps() % steps == 0;	
		}
	}

}
