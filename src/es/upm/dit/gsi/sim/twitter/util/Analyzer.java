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
 * es.upm.dit.gsi.sim.twitter.util.Analyzer.java
 */
package es.upm.dit.gsi.sim.twitter.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.JFrame;

import org.jfree.data.xy.XYSeries;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.media.chart.HistogramGenerator;
import sim.util.media.chart.TimeSeriesChartGenerator;
import es.upm.dit.gsi.sim.twitter.TwitterSimulation;
import es.upm.dit.gsi.sim.twitter.model.tweet.Tweet;

/**
 * Project: ssba-practica1 File: es.upm.dit.gsi.sim.twitter.util.Analyzer.java
 * 
 * This class updates the tweet chart data and checks if the simulation is
 * finished.
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
public class Analyzer implements Steppable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8968023798094928412L;

	/**
	 * Logger
	 */
	private Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * All frames
	 */
	private Map<String, JFrame> frames;

	/**
	 * All time charts.
	 */
	private Map<String, TimeSeriesChartGenerator> timeCharts;

	/**
	 * All histograms.
	 */
	private Map<String, HistogramGenerator> histogramCharts;

	/**
	 * All plots
	 */
//	private Map<String, ScatterPlotGenerator> plotCharts;

	/**
	 * All time charts data series.
	 */
	private Map<String, XYSeries> timeChartsData;

	/**
	 * Histogram data
	 */
	private Map<String, double[]> histogramChartsData;

	/**
	 * Plot data
	 */
//	private Map<String, double[][]> plotChartsData;

	/**
	 * 
	 */
	private Map<String, String> fileNames;

	/**
	 * 
	 */
	private Map<Long, double[]> tweetPerHourAllDays;
	private Map<Long, double[]> tweetPerWeekDays;

	private final String TWEET_PER_TOPIC_TIME_CHART = "Tweets per Topic";
//	private final String TOPIC_HISTOGRAM = "Topic Histogram";
	private final String TWEETS_HOUR_HISTOGRAM = "Tweets per Hour Histogram";
	private final String TWEETS_WEEKDAY_HISTOGRAM = "Tweets per Weekday Histogram";

	/**
	 * Constructor
	 * 
	 */
	public Analyzer(TwitterSimulation simulation) {
		this.frames = new HashMap<String, JFrame>();
		this.timeCharts = new HashMap<String, TimeSeriesChartGenerator>();
		this.timeChartsData = new HashMap<String, XYSeries>();
		this.histogramCharts = new HashMap<String, HistogramGenerator>();
		this.histogramChartsData = new HashMap<String, double[]>();
//		this.plotCharts = new HashMap<String, ScatterPlotGenerator>();
//		this.plotChartsData = new HashMap<String, double[][]>();
		this.fileNames = new HashMap<String, String>();

	}

	/**
	 * This method must be executed every time that the simulation start.
	 */
	public void setupAllCharts(TwitterSimulation simulation) {
		// First chart - "Tweets per Topic in the current step"
		this.createTweetsPerTopicTimeChart(simulation);

		// Second chart - "Tweets topics histogram"
		// this.createTweetsTopicsHistogramChart(simulation);

		// Third chart - "Tweets per hour histogram"
		this.createTweetsPerHourHistogramChart(simulation);

		// Forth chart - "Tweets per week day histogram"
		this.createTweetPerWeekDayHistogramChart(simulation);
	}

	/**
	 * @param simulation
	 */
	private void createTweetPerWeekDayHistogramChart(
			TwitterSimulation simulation) {
		String title = this.TWEETS_WEEKDAY_HISTOGRAM;
		this.tweetPerWeekDays = new HashMap<Long, double[]>();

		if (simulation.getGui() != null && !this.frames.containsKey(title)) {
			this.addHistogramChart(title, "WeekDay",
					"Average quantity of tweets");
			double[] data = new double[0];
			this.histogramChartsData.put(title, data);
			this.histogramCharts.get(title).addSeries(data, 7, title, null);
			JFrame frame = this.histogramCharts.get(title).createFrame();
			frame.setTitle(title);
			simulation.getGui().getController().registerFrame(frame);
			frame.setVisible(false);
			frame.setLocation(300, 300);
			this.frames.put(title, frame);
		} else if (simulation.getGui() == null) {
			double[] data = new double[0];
			this.histogramChartsData.put(title, data);
		} else {
			double[] data = new double[0];
			this.histogramChartsData.put(title, data);
			try {
				this.histogramCharts.get(title).updateSeries(0, data);
			} catch (ArrayIndexOutOfBoundsException e) {
				this.histogramCharts.get(title).removeAllSeries();
				this.histogramCharts.get(title).addSeries(data, 7, title, null);
			}
			this.histogramCharts.get(title).update();
		}

		String fileName = title + ".csv";
		this.fileNames.put(title, fileName);
		try {
			FileWriter writer = new FileWriter(fileName);
			writer.append("Step");
			writer.append(',');
			writer.append("Sunday");
			writer.append(',');
			writer.append("Monday");
			writer.append(',');
			writer.append("Tuesday");
			writer.append(',');
			writer.append("Wednesday");
			writer.append(',');
			writer.append("Thursday");
			writer.append(',');
			writer.append("Friday");
			writer.append(',');
			writer.append("Saturday");
			writer.append('\n');
			writer.flush();
			writer.close();
		} catch (IOException e) {
			logger.warning("Simulation results are not being recorded in a CSV file. Exception: "
					+ e.getMessage());
		}

		logger.fine("Histogram chart for Tweet per Week Day successfully created.");
	}

	/**
	 * @param simulation
	 */
	private void createTweetsPerHourHistogramChart(TwitterSimulation simulation) {
		String title = this.TWEETS_HOUR_HISTOGRAM;
		this.tweetPerHourAllDays = new HashMap<Long, double[]>();

		if (simulation.getGui() != null && !this.frames.containsKey(title)) {
			this.addHistogramChart(title, "Hour", "Average quantity of tweets");
			double[] data = new double[0];
			this.histogramChartsData.put(title, data);
			this.histogramCharts.get(title).addSeries(data, 24, title, null);
			JFrame frame = this.histogramCharts.get(title).createFrame();
			frame.setTitle(title);
			simulation.getGui().getController().registerFrame(frame);
			frame.setVisible(false);
			frame.setLocation(300, 200);
			this.frames.put(title, frame);
		} else if (simulation.getGui() == null) {
			double[] data = new double[0];
			this.histogramChartsData.put(title, data);
		} else {
			double[] data = new double[0];
			this.histogramChartsData.put(title, data);
			try {
				this.histogramCharts.get(title).updateSeries(0, data);
			} catch (ArrayIndexOutOfBoundsException e) {
				this.histogramCharts.get(title).removeAllSeries();
				this.histogramCharts.get(title)
						.addSeries(data, 24, title, null);
			}
			this.histogramCharts.get(title).update();
		}

		String fileName = title + ".csv";
		this.fileNames.put(title, fileName);
		try {
			FileWriter writer = new FileWriter(fileName);
			writer.append("Step");
			for (int i = 0; i < 24; i++) {
				writer.append(',');
				writer.append(Integer.toString(i));
			}
			writer.append('\n');
			writer.flush();
			writer.close();
		} catch (IOException e) {
			logger.warning("Simulation results are not being recorded in a CSV file. Exception: "
					+ e.getMessage());
		}

		logger.fine("Histogram chart for Tweet per Hour successfully created.");
	}

//	 /**
//	 * @param simulation
//	 */
//	 private void createTweetsTopicsHistogramChart(TwitterSimulation
//	 simulation) {
//	 String title = this.TOPIC_HISTOGRAM;
//	 String[] topics = (String[]) simulation.getTopicManager().getTopics().toArray();
//	
//	 if (simulation.getGui() != null && !this.frames.containsKey(title)) {
//	 this.addHistogramChart(title, "Topics", "Number of tweets");
//	 double[] data = new double[0];
//	 this.histogramChartsData.put(title, data);
//	 this.histogramCharts.get(title).addSeries(data, topics.length,
//	 title, null);
//	 JFrame frame = this.histogramCharts.get(title).createFrame();
//	 frame.setTitle(title);
//	 simulation.getGui().getController().registerFrame(frame);
//	 frame.setVisible(false);
//	 frame.setLocation(300, 150);
//	 this.frames.put(title, frame);
//	 } else if (simulation.getGui() == null) {
//	 double[] data = new double[0];
//	 this.histogramChartsData.put(title, data);
//	 } else {
//	 double[] data = new double[0];
//	 this.histogramChartsData.put(title, data);
//	 try {
//	 this.histogramCharts.get(title).updateSeries(0, data);
//	 } catch (ArrayIndexOutOfBoundsException e) {
//	 this.histogramCharts.get(title).removeAllSeries();
//	 this.histogramCharts.get(title).addSeries(data, topics.length,
//	 title, null);
//	 }
//	 this.histogramCharts.get(title).update();
//	 }
//	
//	
//	
//	 String fileName = title+".csv";
//	 this.fileNames.put(title, fileName);
//	 try {
//	 FileWriter writer = new FileWriter(fileName);
//	 writer.append("Step");
//	 for (String topic : topics) {
//	 writer.append(',');
//	 writer.append(topic);
//	 }
//	 writer.append('\n');
//	 writer.flush();
//	 writer.close();
//	 } catch (IOException e) {
//	 logger.warning("Simulation results are not being recorded in a CSV file. Exception: "
//	 + e.getMessage());
//	 }
//	
//	 logger.fine("Histogram chart for Tweet per Topic successfully created.");
//	 }

	/**
	 * 
	 */
	private void createTweetsPerTopicTimeChart(TwitterSimulation simulation) {
		String title = this.TWEET_PER_TOPIC_TIME_CHART;

		List<String> topics = simulation.getTopicManager().getTopics();

		if (simulation.getGui() != null && !this.frames.containsKey(title)) {
			this.addTimeSerieChart(title, "Step", "Number of tweets");
			for (String topic : topics) {
				XYSeries topicSerie = new XYSeries(topic, false);
				this.timeCharts.get(title).addSeries(topicSerie, null);
				this.timeChartsData.put(topic, topicSerie);
			}
			JFrame frame = this.timeCharts.get(title).createFrame();
			frame.setTitle(title);
			simulation.getGui().getController().registerFrame(frame);
			frame.setVisible(false);
			frame.setLocation(200, 100);
			this.frames.put(title, frame);
		} else if (simulation.getGui() == null) {
			for (String topic : topics) {
				XYSeries topicSerie = new XYSeries(topic, false);
				this.timeChartsData.put(topic, topicSerie);
			}
		} else {
			for (String topic : topics) {
				this.timeChartsData.get(topic).clear();
			}
		}

		String fileName = title + ".csv";
		this.fileNames.put(title, fileName);
		try {
			FileWriter writer = new FileWriter(fileName);
			writer.append("Step");
			for (String topic : topics) {
				writer.append(',');
				writer.append(topic);
			}
			writer.append('\n');
			writer.flush();
			writer.close();
		} catch (IOException e) {
			logger.warning("Simulation results are not being recorded in a CSV file. Exception: "
					+ e.getMessage());
		}

		logger.fine("Histogram chart for Trending Topics successfully created.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sim.engine.Steppable#step(sim.engine.SimState)
	 */
	public void step(SimState state) {
		TwitterSimulation simulation = (TwitterSimulation) state;

		// Tweets per Topic chart data update
		this.updateTopicsTimeChart(simulation);

		// Tweets topics histogram
		// this.updateTopicsHistogram(simulation);

		// Tweets per Hour histogram data every day is updated
		this.updateTweetPerHourHistogram(simulation);

		// Tweets per Week Day histogram data every week is updated
		this.updateTweetPerWeekDayHistogram(simulation);
		//
		// this.updateTweetPerZonePlot(simulation);

		// ScatterPlotGenerator scatter = new ScatterPlotGenerator();
		// scatter.setTitle(scatterID);
		// scatter.setXAxisLabel(xAxisLabel);
		// scatter.setYAxisLabel(yAxisLabel);
		// this.scatterPlots.put(scatterID, scatter);
		//
	}

	/**
	 * @param simulation
	 */
	private void updateTweetPerWeekDayHistogram(TwitterSimulation simulation) {
		long currentStep = simulation.schedule.getSteps();
		TimeConversor tc = simulation.getTimeConversor();

		// Calculate how many steps is a day
		Date now = tc.getDate(currentStep);
		Calendar c = tc.getCalendar(now);
		c.add(Calendar.WEEK_OF_YEAR, -1);
		Date lastWeek = c.getTime();
		long lastWeekStep = tc.getSteps(lastWeek);
		long updateTimer = currentStep - lastWeekStep;

		if (currentStep % updateTimer == 0) {

			String title = this.TWEETS_WEEKDAY_HISTOGRAM;
			long lastCheck = currentStep - updateTimer;
			List<Tweet> tweets = simulation.getTweetManager().getTweets();
			List<Tweet> lastTimeWindowTweets = new ArrayList<Tweet>();
			for (Tweet t : tweets) {
				if (t.getTimestamp() >= lastCheck) {
					lastTimeWindowTweets.add(t);
				}
			}
			double[] currentData = new double[7];

			for (Tweet t : lastTimeWindowTweets) {
				Date date = tc.getEquivalentDate(t.getTimestamp());
				Calendar calendar = tc.getCalendar(date);
				int day = calendar.get(Calendar.DAY_OF_WEEK);
				currentData[day - 1]++;
			}
			boolean realData = false;
			for (int i = 0; i < currentData.length; i++) {
				if (currentData[i] != 0) {
					realData = true;
					break;
				}
			}

			if (realData) {

				double[] previousAverageData = this.histogramChartsData
						.get(title);
				if (previousAverageData.length == 0) {
					previousAverageData = new double[7];
				}

				double[] averageData = new double[7];
				int previousDataSize = this.tweetPerWeekDays.size();
				for (int i = 0; i < averageData.length; i++) {
					double aux = currentData[i]
							+ (previousAverageData[i] * previousDataSize);
					aux = aux / (previousDataSize + 1);
					averageData[i] = (int) aux;
				}

				int size = 0;
				for (int i = 0; i < averageData.length; i++) {
					size = (int) (size + averageData[i]);
				}
				int intSize = (int) size;
				double[] chartData = new double[intSize];
				int index = 0;
				for (int i = 0; i < averageData.length; i++) {
					for (int j = 0; j < averageData[i]; j++) {
						chartData[index] = i;
						index++;
					}
				}
				if (simulation.getGui() != null) {
					try {
						this.histogramCharts.get(title).updateSeries(0,
								chartData);
					} catch (ArrayIndexOutOfBoundsException e) {
						this.histogramCharts.get(title).removeAllSeries();
						this.histogramCharts.get(title).addSeries(chartData, 7,
								title, null);
					}
					this.histogramCharts.get(title).update();
				}

				this.tweetPerWeekDays.put(currentStep, currentData);
				this.histogramChartsData.put(title, averageData);

				String fileName = this.fileNames.get(title);
				try {
					FileWriter writer = new FileWriter(fileName, true);
					writer.append(Long.toString(currentStep));
					for (int i = 0; i < currentData.length; i++) {
						writer.append(',');
						writer.append(Double.toString(currentData[i]));
					}
					writer.append('\n');
					writer.flush();
					writer.close();
				} catch (IOException e) {
					logger.warning("Simulation results are not being recorded in a CSV file. Exception: "
							+ e.getMessage());
				}
			}
		}
	}

	/**
	 * @param simulation
	 */
	private void updateTweetPerHourHistogram(TwitterSimulation simulation) {

		long currentStep = simulation.schedule.getSteps();
		TimeConversor tc = simulation.getTimeConversor();

		// Calculate how many steps is a day
		Date now = tc.getDate(currentStep);
		Calendar c = tc.getCalendar(now);
		c.add(Calendar.DAY_OF_MONTH, -1);
		Date lastDay = c.getTime();
		long lastDayStep = tc.getSteps(lastDay);
		long updateTimer = currentStep - lastDayStep;

		if (currentStep % updateTimer == 0) {

			String title = this.TWEETS_HOUR_HISTOGRAM;
			long lastCheck = currentStep - updateTimer;
			List<Tweet> tweets = simulation.getTweetManager().getTweets();
			List<Tweet> lastTimeWindowTweets = new ArrayList<Tweet>();
			for (Tweet t : tweets) {
				if (t.getTimestamp() >= lastCheck) {
					lastTimeWindowTweets.add(t);
				}
			}
			double[] currentData = new double[24];

			for (Tweet t : lastTimeWindowTweets) {
				int hour = tc.getHour(t.getTimestamp());
				currentData[hour]++;
			}
			boolean realData = false;
			for (int i = 0; i < currentData.length; i++) {
				if (currentData[i] != 0) {
					realData = true;
					break;
				}
			}

			if (realData) {

				double[] previousAverageData = this.histogramChartsData
						.get(title);
				if (previousAverageData.length == 0) {
					previousAverageData = new double[24];
				}

				double[] averageData = new double[24];
				int previousDataSize = this.tweetPerHourAllDays.size();
				for (int i = 0; i < averageData.length; i++) {
					double aux = currentData[i]
							+ (previousAverageData[i] * previousDataSize);
					aux = aux / (previousDataSize + 1);
					averageData[i] = (int) aux;
				}

				int size = 0;
				for (int i = 0; i < averageData.length; i++) {
					size = (int) (size + averageData[i]);
				}
				int intSize = (int) size;
				double[] chartData = new double[intSize];
				int index = 0;
				for (int i = 0; i < averageData.length; i++) {
					for (int j = 0; j < averageData[i]; j++) {
						chartData[index] = i;
						index++;
					}
				}
				if (simulation.getGui() != null) {
					try {
						this.histogramCharts.get(title).updateSeries(0,
								chartData);
					} catch (ArrayIndexOutOfBoundsException e) {
						this.histogramCharts.get(title).removeAllSeries();
						this.histogramCharts.get(title).addSeries(chartData,
								24, title, null);
					}
					this.histogramCharts.get(title).update();
				}

				this.tweetPerHourAllDays.put(currentStep, currentData);
				this.histogramChartsData.put(title, averageData);

				String fileName = this.fileNames.get(title);
				try {
					FileWriter writer = new FileWriter(fileName, true);
					writer.append(Long.toString(currentStep));
					for (int i = 0; i < currentData.length; i++) {
						writer.append(',');
						writer.append(Double.toString(currentData[i]));
					}
					writer.append('\n');
					writer.flush();
					writer.close();
				} catch (IOException e) {
					logger.warning("Simulation results are not being recorded in a CSV file. Exception: "
							+ e.getMessage());
				}
			}
		}
	}

	// /**
	// * @param simulation
	// */
	// private void updateTopicsHistogram(TwitterSimulation simulation) {
	//
	// long currentStep = simulation.schedule.getSteps();
	// TweetTopic[] topics = TweetTopic.values();
	// String title = this.TOPIC_HISTOGRAM;
	// double[] data = this.histogramChartsData.get(title);
	// List<Double> list;
	// if (data == null) {
	// data = new double[1];
	// list = new ArrayList<Double>();
	// } else {
	// list = new ArrayList<Double>();
	// for (Double d : data) {
	// list.add(d);
	// }
	// }
	//
	// for (int i = 0; i < topics.length; i++) {
	// XYSeries serie = this.timeChartsData.get(topics[i].toString());
	// double tweets = serie.getDataItem((int) currentStep).getYValue();
	// for (int l = 0; l < tweets; l++) {
	// list.add((double) i);
	// }
	// }
	//
	// Object[] auxData = list.toArray();
	// double[] updatedData = new double[data.length + auxData.length];
	// for (int i = 0; i < data.length; i++) {
	// updatedData[i] = data[i];
	// }
	// for (int i = 0; i < auxData.length; i++) {
	// updatedData[data.length + i] = (Double) auxData[i];
	// }
	// if (simulation.getGui() != null) {
	// this.histogramCharts.get(title).updateSeries(0, updatedData);
	// this.histogramCharts.get(title).update();
	// }
	//
	// this.histogramChartsData.put(title, updatedData);
	//
	// }

	/**
	 * @param simulation
	 */
	private void updateTopicsTimeChart(TwitterSimulation simulation) {

		long currentStep = simulation.schedule.getSteps();

		List<Tweet> allTweets = simulation.getTweetManager().getTweets();

		List<String> topics = simulation.getTopicManager().getTopics();
		Map<String, Integer> counters = new HashMap<String, Integer>();
		for (String topic : topics) {
			counters.put(topic, 0);
		}
		for (Tweet tweet : allTweets) {
			if (tweet.getTimestamp() == currentStep) {
				List<String> topicList = tweet.getTopics();
				for (String topic : topicList) {
					int counter = counters.get(topic);
					counter++;
					counters.put(topic, counter);
				}
			}
		}
		for (String topic : topics) {
			XYSeries serie = this.timeChartsData.get(topic);
			serie.add(currentStep, counters.get(topic));
		}

		String title = this.TWEET_PER_TOPIC_TIME_CHART;
		String fileName = this.fileNames.get(title);
		try {
			FileWriter writer = new FileWriter(fileName, true);
			writer.append(Long.toString(currentStep));
			for (String topic : topics) {
				writer.append(',');
				writer.append(Integer.toString(counters.get(topic)));
			}
			writer.append('\n');
			writer.flush();
			writer.close();
		} catch (IOException e) {
			logger.warning("Simulation results are not being recorded in a CSV file. Exception: "
					+ e.getMessage());
		}

	}

	/**
	 * @param title
	 * @param xAxisLabel
	 * @param yAxisLabel
	 */
	private void addTimeSerieChart(String title, String xAxisLabel,
			String yAxisLabel) {
		TimeSeriesChartGenerator chart = new TimeSeriesChartGenerator();
		chart.setTitle(title);
		chart.setXAxisLabel(xAxisLabel);
		chart.setYAxisLabel(yAxisLabel);
		this.timeCharts.put(title, chart);
	}

	/**
	 * @param title
	 * @param xAxisLabel
	 * @param yAxisLabel
	 */
	private void addHistogramChart(String title, String xAxisLabel,
			String yAxisLabel) {
		HistogramGenerator chart = new HistogramGenerator();
		chart.setTitle(title);
		chart.setXAxisLabel(xAxisLabel);
		chart.setYAxisLabel(yAxisLabel);
		this.histogramCharts.put(title, chart);
	}

//	/**
//	 * @param title
//	 * @param xAxisLabel
//	 * @param yAxisLabel
//	 */
//	private void addScatterPlotChart(String title, String xAxisLabel,
//			String yAxisLabel) {
//		ScatterPlotGenerator chart = new ScatterPlotGenerator();
//		chart.setTitle(title);
//		chart.setXAxisLabel(xAxisLabel);
//		chart.setYAxisLabel(yAxisLabel);
//		this.plotCharts.put(title, chart);
//	}

	/**
	 * @return
	 */
	public Map<String, JFrame> getFrames() {
		return this.frames;
	}

}
