package config;

public final class Environment {

    private static final class LazyHolder {
        private static final Environment INSTANCE = new Environment();
    }

    public static final int PORT = 4221;

    public static final String HTTP_VERSION = "HTTP/1.1";

    private String directory;

    private Environment() {
    }

    public static Environment getInstance() {
        return LazyHolder.INSTANCE;
    }

    public String getDirectory() {
        return directory;
    }

    public void parseArgs(String[] args) {
        if (args.length == 0) {
            return;
        }

        for (int i = 0; i < args.length; i += 2) {
            if (!args[i].isBlank() && "--directory".equals(args[i])) {
                this.directory = args[i + 1];
                break;
            }
        }
    }

}
