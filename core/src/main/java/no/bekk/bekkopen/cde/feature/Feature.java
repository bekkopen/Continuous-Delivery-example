package no.bekk.bekkopen.cde.feature;

public final class Feature {
	public static final String ARTIFACT_DELETE_ENABLED = "artifact.delete.enabled";
	public static final String ARTIFACT_SAVE_ENABLED = "artifact.save.enabled";

	private Feature() {
	}

	public enum Artifact implements Enabled {
		Save(System.getProperty(ARTIFACT_SAVE_ENABLED, "false")), Delete(System.getProperty(ARTIFACT_DELETE_ENABLED, "false"));
		private boolean enabled;

		Artifact(String enabled) {
			this.enabled = new Boolean(enabled);
		}

		public boolean isEnabled() {
			return enabled;
		}

		// Only for testing
		public void reset(String property) {
			enabled = new Boolean(System.getProperty(property, "false"));
		}
	}

	public static void main(String[] args) {
		System.out.println(Artifact.class);
	}
}
