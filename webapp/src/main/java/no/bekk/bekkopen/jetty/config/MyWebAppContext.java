package no.bekk.bekkopen.jetty.config;

import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.webapp.WebAppContext;

public class MyWebAppContext extends WebAppContext {

	public MyWebAppContext(final String webApp, final String contextPath) {
		super(webApp, contextPath);
		final SessionHandler sessionHandler = getSessionHandler();
		final HashSessionManager sessionManager = (HashSessionManager) sessionHandler.getSessionManager();
		sessionManager.setSessionPath(contextPath);
	}

}
