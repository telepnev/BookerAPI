package core.sittings;

public enum ApiEndpoints {
    PING("/ping"),
    BOOKING("/booking"),
    AUTH("/auth");


    private final String path;

    ApiEndpoints(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
