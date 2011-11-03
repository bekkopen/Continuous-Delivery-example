package no.bekk.bekkopen.cde.feature;

import static org.junit.Assert.*;

import org.junit.Test;

public class FeatureTest {

	@Test
	public void test() {
		System.setProperty(Feature.ARTIFACT_SAVE_ENABLED, "true");
		Feature.Artifact.Save.reset(Feature.ARTIFACT_SAVE_ENABLED);
		assertTrue(Feature.Artifact.Save.isEnabled());
		System.setProperty(Feature.ARTIFACT_SAVE_ENABLED, "false");
		Feature.Artifact.Save.reset(Feature.ARTIFACT_SAVE_ENABLED);
		assertFalse(Feature.Artifact.Save.isEnabled());
		System.setProperty(Feature.ARTIFACT_DELETE_ENABLED, "true");
		Feature.Artifact.Delete.reset(Feature.ARTIFACT_DELETE_ENABLED);
		assertTrue(Feature.Artifact.Delete.isEnabled());
		System.setProperty(Feature.ARTIFACT_DELETE_ENABLED, "false");
		Feature.Artifact.Delete.reset(Feature.ARTIFACT_DELETE_ENABLED);
		assertFalse(Feature.Artifact.Delete.isEnabled());
	}

}
