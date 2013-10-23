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
 * es.upm.dit.gsi.sim.twitter.portrayal.UserPortrayal2D.java
 */
package es.upm.dit.gsi.sim.twitter.portrayal;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;

import sim.portrayal.DrawInfo2D;
import sim.portrayal.simple.OvalPortrayal2D;
import es.upm.dit.gsi.sim.twitter.model.user.User;

/**
 * Project: twitter-simulator File:
 * es.upm.dit.gsi.sim.twitter.portrayal.UserPortrayal2D.java
 * 
 * This class represents the graphical representation of a user in the 2D
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
public class UserPortrayal2D extends OvalPortrayal2D {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2138627603200523845L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see sim.portrayal.SimplePortrayal2D#draw(java.lang.Object,
	 * java.awt.Graphics2D, sim.portrayal.DrawInfo2D)
	 */
	@Override
	public void draw(Object object, Graphics2D graphics, DrawInfo2D info) {

		User user = (User) object;

		// Scale the size of the user based on number of connections.
		int numberOfFollowers = user.getFollowers().size();
		double scale = Math.log(numberOfFollowers);
		if (scale <= 1) {
			scale = 1;
		}
		final double width = 10 * scale;
		final double height = 10 * scale;

		// Draw the user
		final int x = (int) (info.draw.x - width / 2.0);
		final int y = (int) (info.draw.y - height / 2.0);
		final int w = (int) (width);
		final int h = (int) (height);
		
		Color color = user.getBehaviour().getColor();
		
		graphics.setColor(color);
		graphics.fillOval(x, y, w, h);

		// Draw the ID
		// graphics.setColor(Color.black);
		// graphics.drawString("C"+user.getId(), x - 3, y);

	}

	/**
	 * The portrayal of the current device can draw a geometric figure or print
	 * a predefine image. This method is called when the device has been linked
	 * to be draw as an image.
	 * 
	 * @param path
	 *            path to the file that contains the image.
	 * @param x
	 *            the X position coordinate.
	 * @param y
	 *            the Y position coordinate.
	 * @param w
	 *            the wide of the portrayal.
	 * @param h
	 *            the height of the portrayal.
	 * @param graphics
	 *            the graphics object to draw the image on.
	 */
	public void putImage(String path, int x, int y, int w, int h,
			Graphics2D graphics) {
		ImageIcon i = new ImageIcon(path);
		Image image = i.getImage();
		ImageObserver io = new ImageObserver() {

			public boolean imageUpdate(Image img, int infoflags, int x, int y,
					int width, int height) {
				return false;
			}
		};

		graphics.drawImage(image, x, y, w * 3, h * 3, io);
	}

}
