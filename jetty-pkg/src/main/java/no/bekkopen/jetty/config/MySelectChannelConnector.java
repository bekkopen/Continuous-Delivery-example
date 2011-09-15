package no.bekkopen.jetty.config;

public class MySelectChannelConnector extends org.eclipse.jetty.server.nio.SelectChannelConnector {

	public MySelectChannelConnector(final int jettyPort) {
		super();
		setPort(jettyPort);
	}

}
