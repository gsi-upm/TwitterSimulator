/**
 * es.upm.dit.gsi.sim.twitter.TwitterSimulationGUI.java
 */
package es.upm.dit.gsi.sim.twitter;

import java.awt.Color;
import java.util.Map.Entry;

import javax.swing.JFrame;

import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.display3d.Display3D;
import sim.engine.SimState;
import sim.portrayal.continuous.ContinuousPortrayal2D;
import sim.portrayal.network.NetworkPortrayal2D;
import sim.portrayal.network.SpatialNetwork2D;
import sim.portrayal3d.continuous.ContinuousPortrayal3D;
import sim.portrayal3d.network.NetworkPortrayal3D;
import sim.portrayal3d.network.SpatialNetwork3D;
import es.upm.dit.gsi.sim.twitter.model.user.User;
import es.upm.dit.gsi.sim.twitter.portrayal.LinkPortrayal2D;
import es.upm.dit.gsi.sim.twitter.portrayal.LinkPortrayal3D;
import es.upm.dit.gsi.sim.twitter.portrayal.UserPortrayal2D;
import es.upm.dit.gsi.sim.twitter.portrayal.UserPortrayal3D;
import es.upm.dit.gsi.sim.twitter.util.Analyzer;

/**
 * Project: twitter-simulator
 * File: es.upm.dit.gsi.sim.twitter.TwitterSimulationGUI.java
 * 
 * This class create the GUI, the displays and the charts of the simulation.
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
public class TwitterSimulationGUI extends GUIState {

	private TwitterSimulation simulation;
	private Display2D display2D;
	private Display3D display3D;
	private JFrame networkFrame2D;
	private JFrame networkFrame3D;

	//Variables to represent the simulation in 2D and 3D.
	private NetworkPortrayal2D networkPortrayal2D;
	private ContinuousPortrayal2D usersPortrayal2D;
	private NetworkPortrayal3D networkPortrayal3D;
	private ContinuousPortrayal3D usersPortrayal3D;

	// To update charts.
	private Analyzer analyzer;

	/**
	 * Constructor
	 * 
	 * @param state
	 */
	public TwitterSimulationGUI(SimState simulation) {
		super(simulation);
		this.simulation = (TwitterSimulation) simulation;
		this.networkPortrayal2D = new NetworkPortrayal2D();
		this.usersPortrayal2D = new ContinuousPortrayal2D();
		this.networkPortrayal3D = new NetworkPortrayal3D();
		this.usersPortrayal3D = new ContinuousPortrayal3D();
		this.simulation.setGui(this);
		this.createController();
	}

	/**
	 * The controller of the gui is useful to handle charts
	 * (reset charts) and their data. 
	 * 
	 * @return
	 */
	public Controller getController() {
		return this.controller;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sim.display.GUIState#start()
	 */
	public void start() {
		super.start();
		this.setupPortrayals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sim.display.GUIState#finish()
	 */
	public void finish() {
		try {
			if (this.display2D != null) {
				this.display2D.reset();
			}
			if (this.display3D != null) {
				this.display3D.reset();
			}
		} catch (Exception e) {
			// Ignore exception
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sim.display.GUIState#load(sim.engine.SimState)
	 */
	public void load(SimState simulation) {
		super.load(simulation);
		this.setupPortrayals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sim.display.GUIState#getSimulationInspectedObject()
	 */
	public Object getSimulationInspectedObject() {
		return this.simulation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sim.display.GUIState#init(sim.display.Controller)
	 */
	public void init(Controller c) {

		super.init(c);

		// Configure display 2D
		this.display2D = new Display2D(200,
				200, this);
//		this.display2D = new Display2D(this.simulation.getNetworkDimension(),
//				this.simulation.getNetworkDimension(), this);
		this.display2D.setScale(3);
		this.display2D.setClipping(false);
		this.networkFrame2D = this.display2D.createFrame();
		this.networkFrame2D.setTitle("Twitter - 2D");
		c.registerFrame(this.networkFrame2D);
		this.networkFrame2D.setVisible(true);

		this.display2D.attach(this.networkPortrayal2D, "Links");
		this.display2D.attach(this.usersPortrayal2D, "Users");

		// Configure display 3D
		this.display3D = new Display3D(600,
				600, this);
//		this.display3D = new Display3D(this.simulation.getNetworkDimension(),
//				this.simulation.getNetworkDimension(), this);
		this.display3D.scale(0.01);
		this.display3D.setBackdrop(Color.white);
		this.networkFrame3D = this.display3D.createFrame();
		this.networkFrame3D.setTitle("Twitter - 3D");
		c.registerFrame(this.networkFrame3D);
		this.networkFrame3D.setVisible(true);
		this.display3D.setShowsAxes(false);

		this.display3D.attach(this.networkPortrayal3D, "Links");
		this.display3D.attach(this.usersPortrayal3D, "Users");

		// Prepare displays
		this.display2D.reset();
		this.display2D.setBackdrop(Color.white);
		this.display2D.repaint();
		this.networkFrame2D.setLocation(100, 100);

		this.display3D.reset();
		this.display3D.setBackdrop(Color.white);
		this.display3D.repaint();
		this.networkFrame3D.setLocation(300, 300);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sim.display.GUIState#quit()
	 */
	public void quit() {
		super.quit();
		if (this.networkFrame2D != null) {
			this.networkFrame2D.dispose();
		}
		super.quit();
		if (this.networkFrame3D != null) {
			this.networkFrame3D.dispose();
		}

		this.networkFrame2D = null;
		this.display2D = null;
		this.networkFrame3D = null;
		this.display3D = null;

		try {
			for (Entry<String, JFrame> entry : this.analyzer.getFrames()
					.entrySet()) {
				JFrame frame = entry.getValue();
				if (frame != null) {
					frame.dispose();
				}
				frame = null;
			}
		} catch (NullPointerException e) {
			// Ignore it
		}
	}

	/**
	 * Setup all portrayals for the GUI.
	 */
	private void setupPortrayals() {

		// 2D Portrayals configuration

		// Portrayals to represent network (links and users).
		this.networkPortrayal2D.setField(new SpatialNetwork2D(this.simulation
				.getUsersField2D(), this.simulation.getNetwork()));
		LinkPortrayal2D connectionPortrayal2D = new LinkPortrayal2D();
		this.networkPortrayal2D.setPortrayalForAll(connectionPortrayal2D);

		this.usersPortrayal2D.setField(this.simulation.getUsersField2D());
		UserPortrayal2D userPortrayal2D = new UserPortrayal2D();
		this.usersPortrayal2D.setPortrayalForClass(User.class,
				userPortrayal2D);

		// 3D Portrayals

		// Portrayals to represent network (links and users).
		this.networkPortrayal3D.setField(new SpatialNetwork3D(this.simulation
				.getUsersField3D(), this.simulation.getNetwork()));
		LinkPortrayal3D connectionPortrayal3D = new LinkPortrayal3D();
		this.networkPortrayal3D.setPortrayalForAll(connectionPortrayal3D);

		this.usersPortrayal3D.setField(this.simulation.getUsersField3D());
		UserPortrayal3D userPortrayal3D = new UserPortrayal3D();
		this.usersPortrayal3D.setPortrayalForClass(User.class,
				userPortrayal3D);

	}

	/**
	 * @return the simulation
	 */
	public TwitterSimulation getSimulation() {
		return simulation;
	}

	/**
	 * @param simulation
	 *            the simulation to set
	 */
	public void setSimulation(TwitterSimulation simulation) {
		this.simulation = simulation;
	}

	/**
	 * @return the display2D
	 */
	public Display2D getDisplay2D() {
		return display2D;
	}

	/**
	 * @param display2d
	 *            the display2D to set
	 */
	public void setDisplay2D(Display2D display2d) {
		display2D = display2d;
	}

	/**
	 * @return the display3D
	 */
	public Display3D getDisplay3D() {
		return display3D;
	}

	/**
	 * @param display3d
	 *            the display3D to set
	 */
	public void setDisplay3D(Display3D display3d) {
		display3D = display3d;
	}

	/**
	 * @return the networkFrame2D
	 */
	public JFrame getNetworkFrame2D() {
		return networkFrame2D;
	}

	/**
	 * @param networkFrame2D
	 *            the networkFrame2D to set
	 */
	public void setNetworkFrame2D(JFrame networkFrame2D) {
		this.networkFrame2D = networkFrame2D;
	}

	/**
	 * @return the networkFrame3D
	 */
	public JFrame getNetworkFrame3D() {
		return networkFrame3D;
	}

	/**
	 * @param networkFrame3D
	 *            the networkFrame3D to set
	 */
	public void setNetworkFrame3D(JFrame networkFrame3D) {
		this.networkFrame3D = networkFrame3D;
	}

	/**
	 * @return the networkPortrayal2D
	 */
	public NetworkPortrayal2D getNetworkPortrayal2D() {
		return networkPortrayal2D;
	}

	/**
	 * @param networkPortrayal2D
	 *            the networkPortrayal2D to set
	 */
	public void setNetworkPortrayal2D(NetworkPortrayal2D networkPortrayal2D) {
		this.networkPortrayal2D = networkPortrayal2D;
	}

	/**
	 * @return the usersPortrayal2D
	 */
	public ContinuousPortrayal2D getUsersPortrayal2D() {
		return usersPortrayal2D;
	}

	/**
	 * @param usersPortrayal2D
	 *            the usersPortrayal2D to set
	 */
	public void setUsersPortrayal2D(ContinuousPortrayal2D usersPortrayal2D) {
		this.usersPortrayal2D = usersPortrayal2D;
	}

	/**
	 * @return the networkPortrayal3D
	 */
	public NetworkPortrayal3D getNetworkPortrayal3D() {
		return networkPortrayal3D;
	}

	/**
	 * @param networkPortrayal3D
	 *            the networkPortrayal3D to set
	 */
	public void setNetworkPortrayal3D(NetworkPortrayal3D networkPortrayal3D) {
		this.networkPortrayal3D = networkPortrayal3D;
	}

	/**
	 * @return the usersPortrayal3D
	 */
	public ContinuousPortrayal3D getUsersPortrayal3D() {
		return usersPortrayal3D;
	}

	/**
	 * @param usersPortrayal3D
	 *            the usersPortrayal3D to set
	 */
	public void setUsersPortrayal3D(ContinuousPortrayal3D usersPortrayal3D) {
		this.usersPortrayal3D = usersPortrayal3D;
	}

	/**
	 * @param analyzer
	 */
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	/**
	 * @return
	 */
	public Analyzer getAnalyzer() {
		return this.analyzer;
	}
}
