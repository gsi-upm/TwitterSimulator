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
 * es.upm.dit.gsi.sim.twitter.portrayal.LinkPortrayal2D.java
 */
package es.upm.dit.gsi.sim.twitter.portrayal;

import java.awt.Color;
import java.awt.Graphics2D;

import sim.portrayal.DrawInfo2D;
import sim.portrayal.network.EdgeDrawInfo2D;
import sim.portrayal.network.SimpleEdgePortrayal2D;

/**
 * Project: ssba-pratica2 File:
 * es.upm.dit.gsi.sim.twitter.portrayal.LinkPortrayal2D.java
 * 
 * This class represents the graphical representation of a link in the 2D
 * display.
 * 
 * Grupo de Sistemas Inteligentes Departamento de Ingeniería de Sistemas
 * Telemáticos Universidad Politécnica de Madrid (UPM)
 * 
 * @author Álvaro Carrera Barroso
 * @email a.carrera@gsi.dit.upm.es
 * @twitter @alvarocarrera
 * @date 21/02/2013
 * @version 0.1
 * 
 */
public class LinkPortrayal2D extends SimpleEdgePortrayal2D {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6107939273631471049L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see sim.portrayal.network.SimpleEdgePortrayal2D#draw(java.lang.Object,
	 * java.awt.Graphics2D, sim.portrayal.DrawInfo2D)
	 */
	@Override
	public void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
		EdgeDrawInfo2D ei = (EdgeDrawInfo2D) info;

		final int startX = (int) ei.draw.x;
		final int startY = (int) ei.draw.y;
		final int endX = (int) ei.secondPoint.x;
		final int endY = (int) ei.secondPoint.y;

		graphics.setColor(new Color(0, 200, 255));//Color light blue - near cian
		graphics.drawLine(startX, startY, endX, endY);

		// Draw the ID in the center of the link.
		// Edge e = (Edge) object;
		// Connection connection = (Connection) e.getInfo();
		// graphics.setColor(Color.blue);
		// graphics.setFont(labelFont);
		// int width =
		// graphics.getFontMetrics().stringWidth("L"+connection.getId());
		// final int midX = (int) (ei.draw.x + ei.secondPoint.x) / 2;
		// final int midY = (int) (ei.draw.y + ei.secondPoint.y) / 2;
		// graphics.drawString("L"+connection.getId(), midX - width / 2, midY);

	}

}
