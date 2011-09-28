package no.bekk.bekkopen.jetty.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import javax.naming.ConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

public final class SystemPropertiesLoader {

	static final String ENVIRONMENT_GROUP_KEY = "environmentGroup";

	private static final String DOT = "\\.";

	static final Set<String> ENVIRONMENTS = Sets.newHashSet("node1", "node2", "node3");

	private static String environment;
	static final Set<String> PROD_ENVIRONMENTS = Sets.newHashSet("node1", "node2", "node3");

	private static String environmentGroup;
	static final Set<String> ENVIRONMENT_GROUPS = Sets.newHashSet("prod", "unknown");
	static final String PROD_GROUP = "prod";
	static final Object UNKNOWN_GROUP = "unknown";

	private static final String CONFIG = "config";
	private static final String SECRETS = "secrets";

	static final String WEBAPP = "webapp";

	static final Set<String> ARTIFACTS = Sets.newHashSet(WEBAPP);

	private static EnvironmentResolver environmentResolver = new EnvironmentResolver();

	private SystemPropertiesLoader() {

	}

	public static void loadWebConfig() throws IOException, ConfigurationException {
		loadConfig(WEBAPP);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void loadSecrets() throws IOException, ConfigurationException {
		File configFile = new File(getConfigPath(SECRETS));
		if (configFile.exists()) {
			Properties properties = loadProperties(configFile);
			environment = environmentResolver.getEnvironment();
			if (ENVIRONMENTS.contains(environment)) {
				String propertyHostName = properties.getProperty("hostname");
				if (!environment.equals(propertyHostName)) {
					String message = "System property hostname does not match the real hostname. " + "Property file says "
							+ propertyHostName + ", but the real hostname is " + environment + ".";
					throw new ConfigurationException(message);
				}
			}

			setSecretProperties(properties);

			getLogger().info("Secret properties loaded from {}: {}", configFile.getAbsolutePath(),
					propsToString(new HashMap(properties), true));
		} else {
			throw new ConfigurationException("File not found: " + configFile.getAbsolutePath());
		}

	}

	private static void loadConfig(final String artifact) throws IOException, ConfigurationException {
		if (!ARTIFACTS.contains(artifact)) {
			throw new ConfigurationException("Artifact must be one of " + ARTIFACTS.toString() + ", but was " + artifact);
		}
		File configFile = new File(getConfigPath(CONFIG));
		if (configFile.isDirectory()) {
			configFile = new File(configFile, artifact + ".properties");
		}
		if (configFile.exists()) {
			Map<String, String> systemPropertiesMap = getConfigProperties(configFile, artifact, environmentResolver.getEnvironment());
			setSystemProperties(systemPropertiesMap);

			getLogger().info("System properties loaded from {}: {}", configFile.getAbsolutePath(),
					propsToString(systemPropertiesMap, false));
		} else {
			throw new ConfigurationException("File not found: " + configFile.getAbsolutePath());
		}

	}

	private static String propsToString(final Map<String, String> systemPropertiesMap, final boolean secret) {
		StringBuilder propsString = new StringBuilder();
		TreeSet<String> keys = new TreeSet<String>(systemPropertiesMap.keySet());
		for (String key : keys) {
			String value = systemPropertiesMap.get(key);
			propsString.append("\n");
			propsString.append(key);
			propsString.append("=");
			if (secret) {
				propsString.append("******");
			} else {
				propsString.append(value);
			}
		}
		return propsString.toString();
	}

	static Map<String, String> getConfigProperties(final File configFile, final String artifact, final String environment)
			throws IOException, ConfigurationException {
		Properties properties = loadProperties(configFile);

		checkFormat(properties);

		Map<String, String> environmentPropertiesPri1 = new HashMap<String, String>();
		Map<String, String> environmentPropertiesPri2 = new HashMap<String, String>();
		Map<String, String> environmentGroupPropertiesPri3 = new HashMap<String, String>();
		Map<String, String> environmentGroupPropertiesPri4 = new HashMap<String, String>();
		Map<String, String> artifactPropertiesPri5 = new HashMap<String, String>();
		Map<String, String> defaultPropertiesPri6 = new HashMap<String, String>();

		environmentGroup = findEnviromentGroup(environment);

		System.setProperty(ENVIRONMENT_GROUP_KEY, environmentGroup);

		Enumeration<Object> propEnum = properties.keys();
		while (propEnum.hasMoreElements()) {
			String property = (String) propEnum.nextElement();
			String originalProperty = property;
			if (propertyForOtherEnvironment(property, environment, environmentGroup)) {
				continue;
			}
			if (property.matches(environment + DOT + artifact + "\\..*")) {
				property = property.replaceFirst(environment + DOT + artifact + DOT, "");
				environmentPropertiesPri1.put(property, properties.getProperty(originalProperty));
				continue;
			}
			if (property.matches(environment + "\\..*")) {
				property = property.replaceFirst(environment + DOT, "");
				if (!isArtifactProperty(property)) {
					environmentPropertiesPri2.put(property, properties.getProperty(originalProperty));
				}
				continue;
			}
			if (property.matches(environmentGroup + DOT + artifact + "\\..*")) {
				property = property.replaceFirst(environmentGroup + DOT + artifact + DOT, "");
				environmentGroupPropertiesPri3.put(property, properties.getProperty(originalProperty));
				continue;
			}
			if (property.matches(environmentGroup + "\\..*")) {
				property = property.replaceFirst(environmentGroup + DOT, "");
				if (!isArtifactProperty(property)) {
					environmentGroupPropertiesPri4.put(property, properties.getProperty(originalProperty));
				}
				continue;
			}
			if (property.matches(artifact + "\\..*")) {
				property = property.replaceFirst(artifact + DOT, "");
				artifactPropertiesPri5.put(property, properties.getProperty(originalProperty));
				continue;
			}
			if (!isArtifactProperty(property)) {
				defaultPropertiesPri6.put(property, properties.getProperty(originalProperty));
			}
		}
		Map<String, String> systemPropertiesMap = new HashMap<String, String>();
		systemPropertiesMap.putAll(defaultPropertiesPri6);
		systemPropertiesMap.putAll(artifactPropertiesPri5);
		systemPropertiesMap.putAll(environmentGroupPropertiesPri4);
		systemPropertiesMap.putAll(environmentGroupPropertiesPri3);
		systemPropertiesMap.putAll(environmentPropertiesPri2);
		systemPropertiesMap.putAll(environmentPropertiesPri1);
		return systemPropertiesMap;
	}

	private static String getConfigPath(final String property) throws ConfigurationException {
		final String path = System.getProperty(property);
		if (path == null) {
			throw new ConfigurationException("System property \"" + property + "\" undefined.");
		}
		return path;
	}

	static Properties loadProperties(final File configFile) throws IOException {
		FileInputStream configStream = new FileInputStream(configFile);
		Properties properties = new Properties();
		try {
			properties.load(configStream);
		} finally {
			configStream.close();
		}
		return properties;
	}

	private static void checkFormat(final Properties properties) throws ConfigurationException {
		Map<String, String> errors = new HashMap<String, String>();
		Enumeration<Object> propEnum = properties.keys();
		while (propEnum.hasMoreElements()) {
			String property = (String) propEnum.nextElement();
			String artefaktOrRegex = StringUtils.join(ARTIFACTS, '|');
			if (property.matches(".*(" + artefaktOrRegex + ")\\..*(" + artefaktOrRegex + ").*")) {
				errors.put(property, "artefakt (" + StringUtils.join(ARTIFACTS, ',')
						+ ") can only be specified once per property and never together!");
			}
			for (String environmentGroup : ENVIRONMENT_GROUPS) {
				if (property.matches(".*\\." + environmentGroup + ".*")) {
					errors
							.put(property, "environment group (" + StringUtils.join(ENVIRONMENT_GROUPS, ',')
									+ ") is only allowed as prefix!");
				}
			}
			for (String miljo : ENVIRONMENTS) {
				if (property.matches(".*\\." + miljo + ".*")) {
					errors.put(property, "environment (" + StringUtils.join(ENVIRONMENTS, ',') + ") is only allowed as prefix!");
				}
			}
		}
		if (!errors.isEmpty()) {
			throw new ConfigurationException(errors.size() + " error in properties: " + propsToString(errors, false));
		}

	}

	private static void setSystemProperties(final Map<String, String> systemPropertiesMap) {
		for (String key : systemPropertiesMap.keySet()) {
			System.setProperty(key, systemPropertiesMap.get(key));
		}

	}

	private static String findEnviromentGroup(final String miljo) {
		if (PROD_ENVIRONMENTS.contains(miljo)) {
			return PROD_GROUP;
		}
		return "unknown";
	}

	private static boolean propertyForOtherEnvironment(final String property, final String environment, final String environmentGroup) {
		String[] propertySplit = property.split(DOT);
		if (ENVIRONMENTS.contains(propertySplit[0]) && !propertySplit[0].equals(environment)) {
			return true;
		}
		if (ENVIRONMENT_GROUPS.contains(propertySplit[0]) && !propertySplit[0].equals(environmentGroup)) {
			return true;
		}
		return false;
	}

	static boolean isArtifactProperty(final String property) {
		String artifacts = StringUtils.join(ARTIFACTS, '|');
		String artifactMiddle = ".*\\.(" + artifacts + ")\\..*";
		String artifactStart = "^(" + artifacts + ")\\..*";
		if (property.matches("(" + artifactStart + "|" + artifactMiddle + ")")) {
			return true;
		}
		return false;
	}

	private static void setSecretProperties(final Properties properties) {
		Enumeration<Object> propEnum = properties.keys();
		while (propEnum.hasMoreElements()) {
			String property = (String) propEnum.nextElement();
			System.setProperty(property, properties.getProperty(property));
		}
	}

	static void setEnvironmentResolver(final EnvironmentResolver environmentResolver) {
		SystemPropertiesLoader.environmentResolver = environmentResolver;
	}

	static class EnvironmentResolver {

		String getEnvironment() throws UnknownHostException, ConfigurationException {
			String environment = hostname();
			return environment;
		}

		String hostname() throws UnknownHostException {
			return InetAddress.getLocalHost().getHostName();
		}
	}

	private static Logger getLogger() {
		return LoggerFactory.getLogger(SystemPropertiesLoader.class);
	}
}
