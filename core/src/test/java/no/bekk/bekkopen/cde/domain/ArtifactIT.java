package no.bekk.bekkopen.cde.domain;

import java.util.List;

import no.bekk.bekkopen.cde.dao.ArtifactDao;
import no.bekk.bekkopen.cde.feature.Feature;

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
public class ArtifactIT extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private ArtifactDao artifactDao;
	Logger logger = LoggerFactory.getLogger(ArtifactIT.class);
	private Long id;
	Artifact artifact;
	private int numberOfArtifacts = 0;

	@Before
	public void saveAnArtifact() {
		System.setProperty("artifact.save.enabled", "true");
		Feature.Artifact.Save.reset(Feature.ARTIFACT_SAVE_ENABLED);
		System.setProperty("artifact.delete.enabled", "true");
		Feature.Artifact.Delete.reset(Feature.ARTIFACT_DELETE_ENABLED);
		artifact = new Artifact();
		artifact.setGroupId("no.bekk.bekkopen.cde");
		artifact.setArtifactId("webapp");
		artifact.setVersion("0.1-SNAPSHOT");
		artifact.setPackaging("zip");
		id = artifactDao.save(artifact).getId();
		Assert.assertEquals(1, artifactDao.findArtifacts().size());
	}

	@Test
	public void getArtifact() {
		Artifact artifact = artifactDao.findArtifact(id);
		Assert.assertEquals(id, artifact.getId());
		Assert.assertEquals("no.bekk.bekkopen.cde", artifact.getGroupId());
		Assert.assertEquals("webapp", artifact.getArtifactId());
		Assert.assertEquals("0.1-SNAPSHOT", artifact.getVersion());
		Assert.assertEquals("zip", artifact.getPackaging());
	}

	@Test
	public void saveArtifact() {
		numberOfArtifacts = artifactDao.findArtifacts().size();
		Artifact newArtifact = new Artifact();
		newArtifact.setGroupId("no.bekk.bekkopen.cde");
		newArtifact.setArtifactId("webapp");
		newArtifact.setVersion("0.2-SNAPSHOT");
		newArtifact.setPackaging("war");
		artifactDao.save(newArtifact);
		List<Artifact> artifacts = artifactDao.findArtifacts();
		logger.info("Artifacts: " + artifacts.size());
		Assert.assertNotNull(artifacts);
		Assert.assertEquals(numberOfArtifacts + 1, artifacts.size());
	}

	@Test
	public void saveArtifactShouldFailIfNotEnabled() {
		System.setProperty("artifact.save.enabled", "false");
		Feature.Artifact.Save.reset(Feature.ARTIFACT_SAVE_ENABLED);
		numberOfArtifacts = artifactDao.findArtifacts().size();
		Artifact newArtifact = new Artifact();
		newArtifact.setGroupId("no.bekk.bekkopen.cde");
		newArtifact.setArtifactId("webapp");
		newArtifact.setVersion("0.2-SNAPSHOT");
		newArtifact.setPackaging("war");
		artifactDao.save(newArtifact);
		List<Artifact> artifacts = artifactDao.findArtifacts();
		logger.info("Artifacts: " + artifacts.size());
		Assert.assertNotNull(artifacts);
		Assert.assertEquals(numberOfArtifacts + 0, artifacts.size());
	}

	@Test
	public void deleteArtifact() {
		numberOfArtifacts = artifactDao.findArtifacts().size();
		logger.info("Artifacts: " + numberOfArtifacts);
		artifactDao.delete(artifactDao.findArtifact(id));
		List<Artifact> artifacts = artifactDao.findArtifacts();
		logger.info("Artifacts: " + artifacts.size());
		Assert.assertNotNull(artifacts);
		Assert.assertEquals(numberOfArtifacts - 1, artifacts.size());
	}

	@Test
	public void deleteArtifactShouldFailIfNotEnabled() {
		System.setProperty("artifact.delete.enabled", "false");
		Feature.Artifact.Delete.reset(Feature.ARTIFACT_DELETE_ENABLED);
		numberOfArtifacts = artifactDao.findArtifacts().size();
		artifactDao.delete(artifactDao.findArtifact(id));
		List<Artifact> artifacts = artifactDao.findArtifacts();
		logger.info("Artifacts: " + artifacts.size());
		Assert.assertNotNull(artifacts);
		Assert.assertEquals(numberOfArtifacts + 0, artifacts.size());
	}
}
