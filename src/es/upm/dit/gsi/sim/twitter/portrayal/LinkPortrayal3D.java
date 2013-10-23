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
 * es.upm.dit.gsi.sim.twitter.portrayal.LinkPortrayal3D.java
 */
package es.upm.dit.gsi.sim.twitter.portrayal;

import java.awt.Color;

import sim.field.network.Edge;
import sim.portrayal3d.network.CylinderEdgePortrayal3D;

/**
 * Project: twitter-simulator File:
 * es.upm.dit.gsi.sim.twitter.portrayal.LinkPortrayal3D.java
 * 
 * This class represents the graphical representation of a connection in the 3D
 * display.
 * 
 * Grupo de Sistemas Inteligentes Departamento de Ingenier�a de Sistemas
 * Telem�ticos Universidad Polit�cnica de Madrid (UPM)
 * 
 * @author �lvaro Carrera Barroso
 * @email a.carrera@gsi.dit.upm.es
 * @twitter @alvarocarrera
 * @date 22/02/2013
 * @version 0.1
 * 
 */
public class LinkPortrayal3D extends CylinderEdgePortrayal3D {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2562696277053534378L;
	
	public LinkPortrayal3D() {
		super(appearanceForColor(new Color(0, 200, 255)), null, null, 0.1);
		//Color light blue - near cian
	}

	public String getLabel(Edge e) {
		return "";
	}

}
