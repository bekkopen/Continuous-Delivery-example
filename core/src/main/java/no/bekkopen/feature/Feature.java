package no.bekkopen.feature;

/**
 * @author Eirik Wang - eirik.wang@bekk.no
 */
public final class Feature {
    private Feature() {
    }

    public enum Course implements Enabled{
        Save(System.getProperty("course.save.enabled", "false")),
        Delete(System.getProperty("course.delete.enabled", "false"));
        private boolean courseEnabled = true;
        private boolean enabled;

        Course(String enabled) {
            this.enabled = new Boolean(enabled);
        }

        public boolean isEnabled() {
            return courseEnabled && enabled;
        }
    }

    public static void main(String[] args) {
        System.out.println(Course.class);
    }
}
