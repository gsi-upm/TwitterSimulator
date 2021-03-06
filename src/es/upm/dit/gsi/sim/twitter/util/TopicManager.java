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
 * es.upm.dit.gsi.sim.twitter.model.TweetTopic.java
 */
package es.upm.dit.gsi.sim.twitter.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: twitter-simulator
 * File: es.upm.dit.gsi.sim.twitter.model.TweetTopic.java
 * 
 * This class represents to any content that a tweet could contain.
 * Any content in the simulation is modelled as TweetTopic.
 * An event has topics, a tweet has topics, etc.
 * 
 * Grupo de Sistemas Inteligentes
 * Departamento de Ingeniería de Sistemas Telemáticos
 * Universidad Politécnica de Madrid (UPM)
 * 
 * @author Álvaro Carrera Barroso
 * @email a.carrera@gsi.dit.upm.es
 * @twitter @alvarocarrera
 * @date 26/02/2013
 * @version 0.2
 * 
 */
public class TopicManager {
	
	/**
	 * Possible topics for the simulation.
	 */
	private List<String> topics = new ArrayList<String>();
	
	/**
	 * Constructor
	 *
	 */
	public TopicManager () {
		this.topics = new ArrayList<String>();
	}
	
	/**
	 * @param s
	 */
	public  void addTopic(String topic) {
		this.topics.add(topic);
	}

	/**
	 * @return
	 */
	public List<String> getTopics() {
		return this.topics;
	}

	/**
	 * @param values
	 */
	public void setTopics(List<String> topics) {
		this.topics = topics;
	}
}
