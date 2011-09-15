package no.bekkopen.jetty.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Properties;

public final class SystemPropertiesLoader {

	private static final String CONFIG = "config";

	private static LocalHostResolver localHostResolver = new LocalHostResolver();

	private SystemPropertiesLoader() {
	}

	public static void loadConfig() throws IOException {
		loadProperties(CONFIG);

	}

	private static void loadProperties(final String configKey)
			throws IOException {
		final String configValue = System.getProperty(configKey);
		if (configValue == null) {
			throw new RuntimeException("System property \"" + configKey
					+ "\" not set");
		}
		File configFile = new File(configValue);
		Properties properties = new Properties();
		if (configFile.exists()) {
			FileInputStream configStream = new FileInputStream(configFile);
			try {
				properties.load(configStream);
			} finally {
				configStream.close();
			}

			String hostName = localHostResolver.getLocalHost();
			String propertyHostName = properties.getProperty("hostname");
			if (!hostName.equals(propertyHostName)) {
				String message = "System property hostname is not correct. "
						+ "Property file says " + propertyHostName
						+ ", but the real name is " + hostName + ".";
				throw new RuntimeException(message);
			}

			setProperties(properties);
			if (CONFIG.equals(configKey)) {
				System.out.println("System properties from "
						+ configFile.getAbsolutePath() + ": "
						+ properties.toString());
			}
		} else {
			throw new RuntimeException("Couldn't find "
					+ configFile.getAbsolutePath());
		}
	}

	private static void setProperties(final Properties properties) {
		Enumeration<Object> propEnum = properties.keys();
		while (propEnum.hasMoreElements()) {
			String property = (String) propEnum.nextElement();
			System.setProperty(property, properties.getProperty(property));
		}
	}

	static void setLocalHostResolver(final LocalHostResolver localHostResolver) {
		SystemPropertiesLoader.localHostResolver = localHostResolver;
	}

	static class LocalHostResolver {

		String getLocalHost() throws UnknownHostException {
			return InetAddress.getLocalHost().getHostName();
		}
	}

}
