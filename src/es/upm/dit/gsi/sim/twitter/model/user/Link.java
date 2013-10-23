/**
 * es.upm.dit.gsi.sim.twitter.model.user.Link.java
 */
package es.upm.dit.gsi.sim.twitter.model.user;

import java.util.Date;
import java.util.logging.Logger;



/**
 * Project: twitter-simulator File:
 * es.upm.dit.gsi.sim.twitter.model.user.Link.java
 * 
 * This Java class represents a following relation in Twitter.
 * 
 * Grupo de Sistemas Inteligentes Departamento de Ingeniería de Sistemas
 * Telemáticos Universidad Politécnica de Madrid (UPM)
 * 
 * @author Álvaro Carrera Barroso
 * @email a.carrera@gsi.dit.upm.es
 * @twitter @alvarocarrera
 * @date 20/02/2013
 * @version 0.1
 * 
 */
public class Link {

	/**
	 * Logger
	 */
	private Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * Connection id - number to identify any link.
	 */
	private int id;

	/**
	 * Twitter follower user
	 */
	private User follower;
	
	/**
	 * Twitter followed user
	 */
	private User followed;
	
	/**
	 * When the follower start to follow
	 */
	private Date startRelationDate;
	
	/**
	 * When the follower finish to follow
	 */
	private Date endRelationDate;

	/**
	 * Constructor
	 *
	 * @param id
	 */
	public Link(int id) {
		this.id = id;
	}
	
	/**
	 * @param date
	 * @param follower
	 * @param followed
	 */
	public void setUsers(Date date, User follower, User followed) {
		this.startRelationDate = date;
		this.follower = follower;
		this.followed = followed;
		logger.fine("Link " + this.id + " successfully created. User " + follower.getId() + " is now following to user" + followed.getId());
	}
	
	/**
	 * Constructor
	 *
	 * @param id
	 * @param date
	 * @param follower
	 * @param followed
	 */
	public Link(int id, Date date, User follower, User followed) {
		this.id = id;
		this.setUsers(date, follower, followed);
		logger.fine("Connection " + this.id + " successfully created. Connected users: " + follower.getId() + "-" + followed.getId());
	}
	
	/**
	 * @return the follower user
	 */
	public User getFollower() {
		return this.follower;
	}
	
	/**
	 * @return the followed user
	 */
	public User getFollowed() {
		return this.followed;
	}
	
	/**
	 * @return the connection id.
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * Record the date when this relation was broken.
	 * 
	 * @param date
	 */
	public void setUnfollowDate(Date date) {
		this.endRelationDate = date;
	}
	
	/**
	 * @return
	 */
	public Date getStartRelationDate() {
		return this.startRelationDate;
	}
	
	/**
	 * @return
	 */
	public Date getEndRelationDate() {
		return this.endRelationDate;
	}

	/**
	 * @param date
	 */
	public void setFollowDate(Date date) {
		this.startRelationDate = date;
	}

}
