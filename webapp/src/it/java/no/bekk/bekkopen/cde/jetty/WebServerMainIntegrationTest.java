package no.bekk.bekkopen.cde.jetty;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import no.bekk.bekkopen.cde.jetty.WebServerMain;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@IfProfileValue(name = "integration", value = "true")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:testApplicationContext.xml" })
public class WebServerMainIntegrationTest {
	private final Logger logger = Logger.getLogger("WebServerMainITest.class");

	@Rule
	public static final TemporaryFolder TMP = new TemporaryFolder();
	private int port1 = 10000;
	private File configFile;
	private File secretsFile;

	private String hostname;

	public void setUp() throws IOException {
		hostname = InetAddress.getLocalHost().getHostName();
		final File webappDir = createFileInTempDirAndReturnParent("testwebapp/test/test/test.war");
		configFile = TMP.newFile("testwebapp/dpost.properties");
		secretsFile = TMP.newFile("testwebapp/secrets.properties");
		System.setProperty("config", configFile.getAbsolutePath());
		System.setProperty("secrets", secretsFile.getAbsolutePath());
		System.setProperty("basedir", webappDir.getAbsolutePath());
		port1 = findAvailablePort(port1);
		FileUtils.writeStringToFile(configFile, "jetty.port=" + port1);
		FileUtils.writeStringToFile(secretsFile, "hostname=" + hostname + "\nsecret.inside=secret");
		System.out.println("Contents of file: " + FileUtils.readFileToString(secretsFile));
	}

	@After
	public void tearDown() throws Exception {
		if (WebServerMain.getJettyServer() != null) {
			WebServerMain.stop();
			assertTrue(WebServerMain.getJettyServer().isStopped());
		}
	}

	@Test
	public void shouldThrowExceptionIfConfigNotSetAsSystemProperty() throws Exception {
		setUp();
		try {
			System.clearProperty("config");
			startAndConnectToSocket();
			fail("Should have thrown Exception.");
		} catch (final Exception e) {
			assertTrue(StringUtils.contains(e.getCause().getMessage(), "System property \"config\" undefined."));
		}
	}

	@Test
	public void shouldThrowExceptionIfSecretsNotSetAsSystemProperty() throws Exception {
		setUp();
		try {
			System.clearProperty("secrets");
			startAndConnectToSocket();
			fail("Should have thrown Exception.");
		} catch (final Exception e) {
			assertTrue(StringUtils.contains(e.getCause().getMessage(), "System property \"secrets\" undefined."));
		}
	}

	@Test
	public void shouldThrowExceptionIfConfigFileDoesNotExist() throws Exception {
		setUp();
		FileUtils.forceDelete(configFile);
		try {
			startAndConnectToSocket();
			fail("Should have thrown Exception");
		} catch (final Exception e) {
			final String msg = e.getCause().getMessage();
			logger.info(msg);

			assertTrue(StringUtils.contains(msg, "File not found:"));
		}
		System.clearProperty("jetty.port");
	}

	@Test
	public void shouldThrowExceptionIfSecretsFileDoesNotExist() throws Exception {
		setUp();
		FileUtils.forceDelete(secretsFile);
		try {
			startAndConnectToSocket();
			fail("Should have thrown Exception");
		} catch (final Exception e) {
			final String msg = e.getCause().getMessage();
			logger.info(msg);

			assertTrue(StringUtils.contains(msg, "File not found:"));

		}
		System.clearProperty("jetty.port");
	}

	@Test
	public void validateConfig() throws Exception {
		setUp();
		startAndConnectToSocket();

		// Server
		final Server server = WebServerMain.getJettyServer();
		assertFalse(server.getSendServerVersion());
		assertTrue("Expected port " + port1 + ", but was " + server.getConnectors()[0].getPort(),
				port1 == server.getConnectors()[0].getPort());
		assertEquals("secret", System.getProperty("secret.inside"));
		assertTrue(1000 == server.getGracefulShutdown());
		assertTrue(server.getStopAtShutdown());

		WebServerMain.stop();
	}

	@Test
	public void testStart() throws Exception {
		setUp();
		startAndConnectToSocket();
		final int hashcode = WebServerMain.getJettyServer().hashCode();
		logger.info("Server hashcode: " + hashcode);
		startAndConnectToSocket();
		assertTrue(hashcode == WebServerMain.getJettyServer().hashCode());
		WebServerMain.stop();
		startAndConnectToSocket();
		logger.info("Not same server, hashcode: " + WebServerMain.getJettyServer().hashCode());
		assertFalse(hashcode == WebServerMain.getJettyServer().hashCode());
	}

	private File createFileInTempDirAndReturnParent(final String pathToFile) throws IOException {
		final String[] pathElements = pathToFile.split("/");
		final int lastElement = pathElements.length - 1;
		final StringBuilder path = new StringBuilder();
		for (int i = 0; i < lastElement; i++) {
			path.append(pathElements[i]);
			TMP.newFolder(path.toString());
			path.append("/");
		}
		final File fil = TMP.newFile(path + pathElements[lastElement]);
		return fil.getParentFile();
	}

	private void startAndConnectToSocket() throws Exception {
		final String[] args = {};
		WebServerMain.main(args);
		assertTrue(WebServerMain.getJettyServer().isRunning());
		final int localPort = WebServerMain.getJettyServer().getConnectors()[0].getLocalPort();
		try {
			final Socket socket = new Socket("127.0.0.1", localPort);
			socket.close();
		} catch (final Exception e) {
			fail("Could not connect to Jetty port: " + localPort);
		}
	}

	private int findAvailablePort(final int port) {
		int newPort = port;
		while (!isPortAvailable(newPort)) {
			newPort++;
		}
		return newPort;
	}

	private static boolean isPortAvailable(final int port) {
		try {
			ServerSocket srv = new ServerSocket(port);
			srv.close();
			srv = null;
			return true;

		} catch (final IOException e) {
			return false;
		}
	}
}
