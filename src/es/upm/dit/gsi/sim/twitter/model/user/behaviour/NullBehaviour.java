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
 * es.upm.dit.gsi.sim.twitter.model.user.behaviour.NullBehaviour.java
 */
package es.upm.dit.gsi.sim.twitter.model.user.behaviour;

import java.awt.Color;

import es.upm.dit.gsi.sim.twitter.TwitterSimulation;
import es.upm.dit.gsi.sim.twitter.model.user.User;

/**
 * Project: twitter-simulator
 * File: es.upm.dit.gsi.sim.twitter.model.user.behaviour.NullBehaviour.java
 * 
 * This behaviour does nothing.
 * 
 * Grupo de Sistemas Inteligentes
 * Departamento de Ingeniería de Sistemas Telemáticos
 * Universidad Politécnica de Madrid (UPM)
 * 
 * @author Álvaro Carrera Barroso
 * @email a.carrera@gsi.dit.upm.es
 * @twitter @alvarocarrera
 * @date 03/03/2013
 * @version 0.1
 * 
 */
public class NullBehaviour extends Behaviour {

	/**
	 * Constructor
	 *
	 * @param name
	 * @param boringTimer
	 * @param user
	 */
	public NullBehaviour(User user) {
		super("Null behaviour", 1, user);
	}

	/* (non-Javadoc)
	 * @see es.upm.dit.gsi.sim.twitter.model.user.behaviour.Behaviour#act(es.upm.dit.gsi.sim.twitter.TwitterSimulation)
	 */
	@Override
	public void act(TwitterSimulation simulation) {
		//Nothing to do.
	}

	/* (non-Javadoc)
	 * @see es.upm.dit.gsi.sim.twitter.model.user.behaviour.Behaviour#getColor()
	 */
	@Override
	public Color getColor() {
		return Color.black;
	}

	/* (non-Javadoc)
	 * @see es.upm.dit.gsi.sim.twitter.model.user.behaviour.Behaviour#reset()
	 */
	@Override
	public void reset() {
		// Nothing to reset
		
	}

}
