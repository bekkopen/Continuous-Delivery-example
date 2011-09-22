package no.bekkopen.feature;

/**
 * @author Eirik Wang - eirik.wang@bekk.no
 */
public final class Feature {
    private Feature() {
    }

    public enum Course implements Enabled{
        Save(true),
        Delete(false);
        private boolean courseEnabled = true;
        private boolean enabled;

        Course(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isEnabled() {
            return courseEnabled && enabled;
        }
    }

    public static void main(String[] args) {
        System.out.println(Course.class);
    }
}
