/**
 * es.upm.dit.gsi.sim.twitter.model.event.Event.java
 */
package es.upm.dit.gsi.sim.twitter.model.event;

import java.util.Date;
import java.util.List;

import es.upm.dit.gsi.sim.twitter.TwitterSimulation;

/**
 * Project: twitter-simulator File:
 * es.upm.dit.gsi.sim.twitter.model.event.Event.java
 * 
 * This class model an external event to analyse the response of
 * Twitter users to events such as football matches or music concerts.
 * 
 * Grupo de Sistemas Inteligentes Departamento de Ingeniería de Sistemas
 * Telemáticos Universidad Politécnica de Madrid (UPM)
 * 
 * @author Álvaro Carrera Barroso
 * @email a.carrera@gsi.dit.upm.es
 * @twitter @alvarocarrera
 * @date 26/02/2013
 * @version 0.2
 * 
 */
public class Event {

	/**
	 * ID of an event.
	 */
	private int id;

	/**
	 * Name of the event.
	 */
	private String name;

	/**
	 * Topics of the event: sports, footbal, basket, etc.
	 */
	private List<String> topics;

	/**
	 * When the event occurs.
	 */
	private Date startDate;

	/**
	 * When the event occurs.
	 */
	private Date endDate;

	/**
	 * Duration in steps
	 */
	private long duration;

	/**
	 * Constructor
	 * 
	 * @param topics
	 * @param startDate
	 * @param durationInSteps
	 *            in steps (not in seconds)
	 */
	public Event(int id, String name, List<String> topics, Date startDate,
			long durationInSteps, TwitterSimulation simulation) {
		this.id = id;
		this.name = name;
		this.topics = topics;
		this.startDate = startDate;
		long startStep = simulation.getTimeConversor().getSteps(this.startDate);
		this.duration = durationInSteps;
		this.endDate = simulation.getTimeConversor().getDate(
				startStep + durationInSteps);
	}

	/**
	 * Constructor
	 *
	 * @param id
	 * @param name
	 * @param topics
	 * @param startDate
	 * @param endDate
	 * @param simulation
	 */
	public Event(int id, String name, List<String> topics, Date startDate, Date endDate,
			TwitterSimulation simulation) {
		this.id = id;
		this.name = name;
		this.topics = topics;
		this.startDate = startDate;
		this.endDate = endDate;
		long startStep = simulation.getTimeConversor().getSteps(this.startDate);
		long endStep = simulation.getTimeConversor().getSteps(this.endDate);
		this.duration = endStep - startStep;
	}

	/**
	 * @return the topics
	 */
	public List<String> getTopics() {
		return topics;
	}

	/**
	 * @param topics
	 *            the topics to set
	 */
	public void setTopics(List<String> topics) {
		this.topics = topics;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param topic
	 */
	public void addTopic(String topic) {
		this.topics.add(topic);
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return duration of the event in steps
	 */
	public long getDurationInSteps() {
		return duration;
	}

}
