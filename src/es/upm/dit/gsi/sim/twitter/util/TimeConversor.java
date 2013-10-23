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
 * es.upm.dit.gsi.sim.twitter.util.TimeConversor.java
 */
package es.upm.dit.gsi.sim.twitter.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

import es.upm.dit.gsi.sim.twitter.TwitterSimulation;

/**
 * Project: twitter-simulator File:
 * es.upm.dit.gsi.sim.twitter.util.TimeConversor.java
 * 
 * Useful class to translate a date to steps and viceversa. 
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
public class TimeConversor {

	/**
	 * Logger
	 */
	private Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * How many seconds a simulation step is.
	 */
	private int oneStepInSeconds;

	/**
	 * When the simulation starts
	 */
	private Date initialDate;

	/**
	 * Date formatter
	 */
	private SimpleDateFormat formatter;

	/**
	 * Constructor
	 * 
	 * @param oneStepInSeconds
	 */
	public TimeConversor(int oneStepInSeconds, TwitterSimulation simulation) {
		this.oneStepInSeconds = oneStepInSeconds;
		this.formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		if (simulation == null || simulation.getStartDate() == null) {
			this.initialDate = this.getParsedDate("2006-06-15 00:00:00");
		} else {
			this.initialDate = this.getParsedDate(simulation.getStartDate());
		}
	}

	/**
	 * @return the initialDate
	 */
	public Date getInitialDate() {
		return initialDate;
	}

	/**
	 * @return
	 */
	public Calendar getCalendar(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar;
	}

	public Date getEquivalentDate(long step) {
		Calendar calendar = this.getCalendar(this.initialDate);
		calendar.add(Calendar.SECOND, (int) (step * this.oneStepInSeconds));
		return calendar.getTime();
	}

	/**
	 * @param step
	 * @return
	 */
	public int getYear(long step) {
		Date date = this.getEquivalentDate(step);
		Calendar calendar = this.getCalendar(date);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * @param step
	 * @return
	 */
	public int getMonth(long step) {
		Date date = this.getEquivalentDate(step);
		Calendar calendar = this.getCalendar(date);
		return 1 + calendar.get(Calendar.MONTH);
	}

	/**
	 * Note that here any month has 30 days.
	 * 
	 * @param step
	 * @return
	 */
	public int getDay(long step) {
		Date date = this.getEquivalentDate(step);
		Calendar calendar = this.getCalendar(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * @param step
	 * @return
	 */
	public String getWeekDay(long step) {
		Date date = this.getEquivalentDate(step);
		Calendar calendar = this.getCalendar(date);
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		switch (day % 7) {
		case 1:
			return "Sunday";
		case 2:
			return "Monday";
		case 3:
			return "Tuesday";
		case 4:
			return "Wednesday";
		case 5:
			return "Thursday";
		case 6:
			return "Friday";
		case 7:
			return "Saturday";
		default:
			// Impossible
			return null;
		}
	}

	/**
	 * @param step
	 * @return
	 */
	public int getHour(long step) {
		Date date = this.getEquivalentDate(step);
		Calendar calendar = this.getCalendar(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * @param step
	 * @return
	 */
	public int getMinute(long step) {
		Date date = this.getEquivalentDate(step);
		Calendar calendar = this.getCalendar(date);
		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * @param step
	 * @return
	 */
	public int getSecond(long step) {
		Date date = this.getEquivalentDate(step);
		Calendar calendar = this.getCalendar(date);
		return calendar.get(Calendar.SECOND);
	}

	/**
	 * @param date
	 * @return
	 */
	public long getSteps(Date date) {
		double seconds = (date.getTime() - this.initialDate.getTime()) / 1000;
		return (long) seconds / this.oneStepInSeconds;
	}

	/**
	 * @param steps
	 * @return
	 */
	public Date getDate(long step) {
		return this.getEquivalentDate(step);
	}

	/**
	 * @param date
	 * @return
	 */
	public String getFormattedDate(Date date) {
		return this.formatter.format(date);
	}

	/**
	 * @param date
	 * @return
	 */
	public Date getParsedDate(String date) {
		try {
			return this.formatter.parse(date);
		} catch (ParseException e) {
			logger.severe("Error parsing date: " + e.getMessage());
			return null;
		}
	}
}
