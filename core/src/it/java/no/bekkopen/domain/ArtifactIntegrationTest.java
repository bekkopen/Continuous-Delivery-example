package no.bekkopen.domain;

import java.util.List;
import java.util.logging.Logger;

import no.bekkopen.dao.ArtifactDao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@IfProfileValue(name = "integration", value = "true")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:testApplicationContext.xml" })
public class ArtifactIntegrationTest {

	@Autowired
	private ArtifactDao artifactDao;
	private final Logger logger = Logger.getLogger("ArtifactIntegrationTest.class");
	private Long id;

	@Before
	public void init() {
		id = 1L;
	}

	@Test
	public void listArtifactsTest() {
		List<Artifact> artifacts = artifactDao.getArtifacts();
		logger.info("Artifacts: " + artifacts.size());
		Assert.assertNotNull(artifacts);
		Assert.assertEquals(4, artifacts.size());
	}

	@Test
	public void getArtifactTest() {
		Artifact artifact = artifactDao.getArtifact(id);
		Assert.assertEquals(id.longValue(), artifact.getId());
		Assert.assertEquals("no.bekkopen", artifact.getGropId());
		Assert.assertEquals("webapp", artifact.getArtifactId());
		Assert.assertEquals("0.1-SNAPSHOT", artifact.getVersion());
		Assert.assertEquals("zip", artifact.getPackaging());
	}
}
