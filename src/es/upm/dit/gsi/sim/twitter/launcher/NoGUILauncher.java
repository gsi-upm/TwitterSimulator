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
package es.upm.dit.gsi.sim.twitter.launcher;
import java.io.File;

import es.upm.dit.gsi.sim.twitter.TwitterSimulation;

/**
 * Project: twitter-simulator
 * File: es.upm.dit.gsi.sim.twitter.launcher.NoGUILauncher.java
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
public class NoGUILauncher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		
		
		
//		TwitterSimulation simulation = new TwitterSimulation(
//				System.currentTimeMillis(), TwitterSimulationConfigurator.getTopicList());
		TwitterSimulation simulation = new TwitterSimulation(System.currentTimeMillis(),
				TwitterSimulationConfigurator.getInitialNetwork("twitter-info" + File.separator
						+ "followers.json", "twitter-info" + File.separator
						+ "allusers.json", null), TwitterSimulationConfigurator.getTopicList());
		simulation.start();

		// Execute the simulation step by step
		do {
			if (!simulation.schedule.step(simulation)) {
				break;
			}
		} while (simulation.schedule.getSteps() < Long.MAX_VALUE); // This is the final step.
		simulation.finish();

	}

}
