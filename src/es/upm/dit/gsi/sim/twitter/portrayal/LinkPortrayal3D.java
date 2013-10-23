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
