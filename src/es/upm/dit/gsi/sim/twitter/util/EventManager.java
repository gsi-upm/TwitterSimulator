/**
 * es.upm.dit.gsi.sim.twitter.util.EventManager.java
 */
package es.upm.dit.gsi.sim.twitter.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import sim.engine.SimState;
import sim.engine.Steppable;
import es.upm.dit.gsi.sim.twitter.TwitterSimulation;
import es.upm.dit.gsi.sim.twitter.model.event.Event;

/**
 * Project: twitter-simulator File:
 * es.upm.dit.gsi.sim.twitter.util.EventManager.java
 * 
 * This class must create and update all events.
 * 
 * Grupo de Sistemas Inteligentes Departamento de Ingeniería de Sistemas
 * Telemáticos Universidad Politécnica de Madrid (UPM)
 * 
 * @author Álvaro Carrera Barroso
 * @email a.carrera@gsi.dit.upm.es
 * @twitter @alvarocarrera
 * @date 26/02/2013
 * @version 0.1
 * 
 */
public class EventManager implements Steppable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 87907348431073538L;

	/**
	 * Logger
	 */
	private Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * List of all happen events.
	 */
	private List<Event> pastEvents;

	/**
	 * List of pending events.
	 */
	private List<Event> pendingEvents;

	/**
	 * List of current events.
	 */
	private List<Event> currentEvents;

	/**
	 * To count how many events are in the simulation.
	 */
	private int eventsId;

	/**
	 * Constructor
	 * 
	 */
	public EventManager(TwitterSimulation simulation) {
		this.pastEvents = new ArrayList<Event>();
		this.pendingEvents = new ArrayList<Event>();
		this.currentEvents = new ArrayList<Event>();
		this.eventsId = 0;
		this.registerEvents(simulation);
	}

	/**
	 * Create and register all events for the whole simulation.
	 */
	private void registerEvents(TwitterSimulation simulation) {

		// Football match
		List<String> topics = new ArrayList<String>();
		topics.add("SPORT");
		topics.add("FOOTBALL");
		Date d1 = simulation.getTimeConversor().getParsedDate(
				"2012-01-01 20:00:00");
		Date d2 = simulation.getTimeConversor().getParsedDate(
				"2012-01-01 22:30:00");
		this.registerNewEvent("Madrid-Barça", topics, d1, d2, simulation);

		// Film in TV
		topics = new ArrayList<String>();
		topics.add("TV");
		topics.add("FRIENDS");
		d1 = simulation.getTimeConversor().getParsedDate("2012-01-03 15:30:00");
		d2 = simulation.getTimeConversor().getParsedDate("2012-01-03 18:00:00");
		this.registerNewEvent("Peli de siesta de Antena3", topics, d1, d2,
				simulation);
	}

	/**
	 * @param name
	 * @param topics
	 * @param startDate
	 * @param endDate
	 * @param simulation
	 */
	public void registerNewEvent(String name, List<String> topics,
			Date startDate, Date endDate, TwitterSimulation simulation) {
		Event event = new Event(this.eventsId++, name, topics, startDate,
				endDate, simulation);
		this.addEvent(event);
	}

	/**
	 * @param name
	 * @param topics
	 * @param startDate
	 * @param durationInSteps
	 *            in steps (not seconds)
	 * @param simulation
	 */
	public void registerNewEvent(String name, List<String> topics,
			Date startDate, long durationInSteps, TwitterSimulation simulation) {
		Event event = new Event(this.eventsId++, name, topics, startDate,
				durationInSteps, simulation);
		this.addEvent(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sim.engine.Steppable#step(sim.engine.SimState)
	 */
	@Override
	public void step(SimState simstate) {

		TwitterSimulation simulation = (TwitterSimulation) simstate;
		long currentStep = simulation.schedule.getSteps();
		List<Event> eventsToStart = new ArrayList<Event>();
		List<Event> eventsToFinish = new ArrayList<Event>();

		// Check if pending events must be modified
		for (Event eventUnderReview : this.pendingEvents) {
			Date startDate = eventUnderReview.getStartDate();
			Date endDate = eventUnderReview.getEndDate();
			long startStep = simulation.getTimeConversor().getSteps(startDate);
			long endStep = simulation.getTimeConversor().getSteps(endDate);
			if (currentStep > endStep) {
				eventsToFinish.add(eventUnderReview);
			} else if (currentStep >= startStep) {
				eventsToStart.add(eventUnderReview);
			}
		}
		// Check if currents events must be modified
		for (Event eventUnderReview : this.currentEvents) {
			Date startDate = eventUnderReview.getStartDate();
			Date endDate = eventUnderReview.getEndDate();
			long startStep = simulation.getTimeConversor().getSteps(startDate);
			long endStep = simulation.getTimeConversor().getSteps(endDate);
			if (startStep <= currentStep && currentStep > endStep) {
				eventsToFinish.add(eventUnderReview);
			}
		}

		// Modify events
		for (Event event : eventsToStart) {
			this.activeEvent(event);
		}
		for (Event event : eventsToFinish) {
			this.finishEvent(event);
		}
	}

	/**
	 * @param event
	 */
	private void activeEvent(Event event) {
		if (this.pendingEvents.contains(event)) {
			this.currentEvents.add(event);
			this.pendingEvents.remove(event);
		} else {
			logger.warning("Event not actived. EventID: " + event.getId() + " Event Name: " + event.getName());
		}
	}

	/**
	 * @param event
	 */
	private void finishEvent(Event event) {
		if (this.currentEvents.contains(event)) {
			this.currentEvents.remove(event);
		} else if (this.pendingEvents.contains(event)) {
			this.pendingEvents.add(event);
		} else {
			logger.warning("Event not finished. EventID: " + event.getId() + " Event Name: " + event.getName());
			return;
		}
		this.pastEvents.add(event);
	}

	/**
	 * @return the pastEvents
	 */
	public List<Event> getPastEvents() {
		return pastEvents;
	}

	/**
	 * @return the pendingEvents
	 */
	public List<Event> getPendingEvents() {
		return pendingEvents;
	}

	/**
	 * @return the currentEvents
	 */
	public List<Event> getCurrentEvents() {
		return currentEvents;
	}

	/**
	 * @param event
	 * @return
	 */
	public void addEvent(Event event) {
		this.pendingEvents.add(event);
	}

}
