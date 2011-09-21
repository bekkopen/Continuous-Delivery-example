package no.posten.dpost.jetty;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import no.bekkopen.jetty.WebServerMain;

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

	public void setUp() throws IOException {
		final File webappDir = lagFilITempDirOgReturnerParent("testwebapp/test/test/test.war");
		configFile = TMP.newFile("testwebapp/dpost.properties");
		secretsFile = TMP.newFile("testwebapp/secrets.properties");
		System.setProperty("config", configFile.getAbsolutePath());
		System.setProperty("secrets", secretsFile.getAbsolutePath());
		System.setProperty("basedir", webappDir.getAbsolutePath());
		port1 = finnLedigPort(port1);
		FileUtils.writeStringToFile(configFile, "jetty.port=" + port1);
		FileUtils.writeStringToFile(secretsFile, "secret.inside=secret");
	}

	@After
	public void tearDown() throws Exception {
		if (WebServerMain.getJettyServer() != null) {
			WebServerMain.stop();
			assertTrue(WebServerMain.getJettyServer().isStopped());
		}
	}

	@Test
	public void skalKasteExceptionOmConfigIkkeErSattSomSystemProperty() throws Exception {
		setUp();
		try {
			System.clearProperty("config");
			startOgKobleTilSocket();
			fail("Skulle kastet Exception.");
		} catch (final Exception e) {
			assertTrue(StringUtils.contains(e.getCause().getMessage(), "System property \"config\" undefined."));
		}
	}

	@Test
	public void skalKasteExceptionOmSecretsIkkeErSattSomSystemProperty() throws Exception {
		setUp();
		try {
			System.clearProperty("secrets");
			startOgKobleTilSocket();
			fail("Skulle kastet Exception.");
		} catch (final Exception e) {
			assertTrue(StringUtils.contains(e.getCause().getMessage(), "System property \"secrets\" undefined."));
		}
	}

	@Test
	public void skalKasteExceptionDersomConfigFilIkkeEksisterer() throws Exception {
		setUp();
		FileUtils.forceDelete(configFile);
		try {
			startOgKobleTilSocket();
			fail("Skulle kastet Exception");
		} catch (final Exception e) {
			final String msg = e.getCause().getMessage();
			logger.info(msg);

			assertTrue(StringUtils.contains(msg, "File not found:"));
		}
		System.clearProperty("jetty.port");
	}

	@Test
	public void skalKasteExceptionDersomSecretsFilIkkeEksisterer() throws Exception {
		setUp();
		FileUtils.forceDelete(secretsFile);
		try {
			startOgKobleTilSocket();
			fail("Skulle kastet Exception");
		} catch (final Exception e) {
			final String msg = e.getCause().getMessage();
			logger.info(msg);

			assertTrue(StringUtils.contains(msg, "File not found:"));

		}
		System.clearProperty("jetty.port");
	}

	@Test
	public void validerKonfigurasjon() throws Exception {
		setUp();
		startOgKobleTilSocket();

		// Server
		final Server server = WebServerMain.getJettyServer();
		assertFalse(server.getSendServerVersion());
		assertTrue("Port skulle vært " + port1 + ", men var " + server.getConnectors()[0].getPort(),
				port1 == server.getConnectors()[0].getPort());
		assertEquals("secret", System.getProperty("secret.inside"));
		assertTrue(1000 == server.getGracefulShutdown());
		assertTrue(server.getStopAtShutdown());

		WebServerMain.stop();
	}

	@Test
	public void testStart() throws Exception {
		setUp();
		startOgKobleTilSocket();
		final int hashcode = WebServerMain.getJettyServer().hashCode();
		logger.info("Server hashcode: " + hashcode);
		startOgKobleTilSocket();
		assertTrue(hashcode == WebServerMain.getJettyServer().hashCode());
		WebServerMain.stop();
		startOgKobleTilSocket();
		logger.info("Ikke samme server, hashcode: " + WebServerMain.getJettyServer().hashCode());
		assertFalse(hashcode == WebServerMain.getJettyServer().hashCode());
	}

	@Test
	public void testStartMedEttNivaasBasedir() throws IOException {
		setUp();
		final File baseDir = lagFilITempDirOgReturnerParent("/test/test.war");
		System.setProperty("basedir", baseDir.getAbsolutePath());
		try {
			startOgKobleTilSocket();
		} catch (final Exception e) {
			assertEquals(
					"artifactId (context.root) er : null. 'basedir' må ha minst to nivåer (f. eks. /stiTil/basedir). Kan ikke starte.",
					e.getMessage());
		} finally {
			FileUtils.forceDelete(baseDir);
		}
	}

	@Test
	public void testStartMedToNivaasBasedir() throws IOException {
		setUp();
		final File baseDir = lagFilITempDirOgReturnerParent("/test/test.war");
		System.setProperty("basedir", baseDir.getAbsolutePath());
		try {
			startOgKobleTilSocket();
		} catch (final Exception e) {
			assertEquals(
					"artifactId (context.root) er : null. 'basedir' må ha minst to nivåer (f. eks. /stiTil/basedir). Kan ikke starte.",
					e.getMessage());
		} finally {
			FileUtils.forceDelete(baseDir);
		}
	}

	@Test
	public void testStartMedTreNivaasBasedir() throws Exception {
		setUp();
		final File baseDir = lagFilITempDirOgReturnerParent("/test/test/test.war");
		System.setProperty("basedir", baseDir.getAbsolutePath());
		startOgKobleTilSocket();
		FileUtils.forceDelete(baseDir);
	}

	private File lagFilITempDirOgReturnerParent(final String stiTilFil) throws IOException {
		final String[] stielementer = stiTilFil.split("/");
		final int sisteElement = stielementer.length - 1;
		final StringBuilder sti = new StringBuilder();
		for (int i = 0; i < sisteElement; i++) {
			sti.append(stielementer[i]);
			TMP.newFolder(sti.toString());
			sti.append("/");
		}
		final File fil = TMP.newFile(sti + stielementer[sisteElement]);
		return fil.getParentFile();
	}

	private void startOgKobleTilSocket() throws Exception {
		final String[] args = {};
		WebServerMain.main(args);
		assertTrue(WebServerMain.getJettyServer().isRunning());
		final int localPort = WebServerMain.getJettyServer().getConnectors()[0].getLocalPort();
		try {
			final Socket socket = new Socket("127.0.0.1", localPort);
			socket.close();
		} catch (final Exception e) {
			fail("Klarte ikke koble til JettyServer sin port: " + localPort);
		}
	}

	private int finnLedigPort(final int port) {
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
