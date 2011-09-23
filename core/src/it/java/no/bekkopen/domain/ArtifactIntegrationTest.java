package no.bekkopen.domain;

import java.util.List;

import no.bekkopen.dao.ArtifactDao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@IfProfileValue(name = "integration", value = "true")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:testApplicationContext.xml" })
public class ArtifactIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private ArtifactDao artifactDao;
	Logger logger = LoggerFactory.getLogger(ArtifactIntegrationTest.class);
	private Long id;
	private int numberOfArtefacts = 0;

	@Before
	public void init() {
		Artifact artifact = new Artifact();
		artifact.setGroupId("no.bekkopen");
		artifact.setArtifactId("webapp");
		artifact.setVersion("0.1-SNAPSHOT");
		artifact.setPackaging("zip");
		id = artifactDao.save(artifact).getId();
	}
	
	@Test
	public void getArtifactTest() {
		Artifact artifact = artifactDao.findArtifact(id);
		Assert.assertEquals(id, artifact.getId());
		Assert.assertEquals("no.bekkopen", artifact.getGroupId());
		Assert.assertEquals("webapp", artifact.getArtifactId());
		Assert.assertEquals("0.1-SNAPSHOT", artifact.getVersion());
		Assert.assertEquals("zip", artifact.getPackaging());
	}
	
	@Test
	public void saveArtifactTest() {
		numberOfArtefacts = artifactDao.findArtifacts().size();
		Artifact newArtifact = new Artifact();
		newArtifact.setGroupId("no.bekkopen");
		newArtifact.setArtifactId("webapp");
		newArtifact.setVersion("0.2-SNAPSHOT");
		newArtifact.setPackaging("war");
		artifactDao.save(newArtifact);
		List<Artifact> artifacts = artifactDao.findArtifacts();
        logger.info("Artifacts: " + artifacts.size());
		Assert.assertNotNull(artifacts);
		Assert.assertEquals(numberOfArtefacts + 1, artifacts.size());
	}
}
