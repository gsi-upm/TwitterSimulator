/**
 * es.upm.dit.gsi.sim.twitter.portrayal.UserPortrayal3D.java
 */
package es.upm.dit.gsi.sim.twitter.portrayal;

import javax.media.j3d.TransformGroup;

import sim.portrayal3d.simple.SpherePortrayal3D;
import es.upm.dit.gsi.sim.twitter.model.user.User;

/**
 * Project: twitter-simulator File:
 * es.upm.dit.gsi.sim.twitter.portrayal.UserPortrayal3D.java
 * 
 * This class represents the graphical representation of a user in the 3D
 * display.
 * 
 * Grupo de Sistemas Inteligentes Departamento de Ingeniería de Sistemas
 * Telemáticos Universidad Politécnica de Madrid (UPM)
 * 
 * @author Álvaro Carrera Barroso
 * @email a.carrera@gsi.dit.upm.es
 * @twitter @alvarocarrera
 * @date 22/02/2013
 * @version 0.1
 * 
 */
public class UserPortrayal3D extends SpherePortrayal3D {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6983973368891562557L;

	// Sphere diameter
	private int diameter = 3;

	public TransformGroup getModel(Object object, TransformGroup j3dModel) {
		User user = (User) object;
		double size = Math.log(user.getFollowers().size());
		if (size <= 0) {
			size = 1;
		}

		setAppearance(
				j3dModel,
				appearanceForColors(user.getBehaviour().getColor(), null, user
						.getBehaviour().getColor(), null, 1.0D, 1.0D));

		setScale(j3dModel, size * diameter);

		return super.getModel(object, j3dModel);
	}
}
