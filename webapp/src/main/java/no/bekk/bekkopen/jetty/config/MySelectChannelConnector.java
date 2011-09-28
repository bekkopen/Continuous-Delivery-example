package no.bekk.bekkopen.jetty.config;

public class MySelectChannelConnector extends org.eclipse.jetty.server.nio.SelectChannelConnector {

	private final int lowResourcesMaxIdleTime = 5000;
	private final int lowResourcesConnections = 5000;
	private final boolean statsOn = false;
	private final int acceptors = 2;
	private final int maxIdleTime = 30000;

	public MySelectChannelConnector() {
		super();
		final String jettyPort = System.getProperty("jetty.port");
		if (jettyPort != null) {
			setPort(Integer.parseInt(jettyPort));
		} else {
			throw new RuntimeException("Unable to start: System property 'jetty.port' is missing.");
		}
		setMaxIdleTime(maxIdleTime);
		setAcceptors(acceptors);
		setStatsOn(statsOn);
		setLowResourcesConnections(lowResourcesConnections);
		setLowResourcesMaxIdleTime(lowResourcesMaxIdleTime);
	}

}
