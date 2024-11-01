package core.sittings;

public enum ApiEndpoints {
    PING("/ping"),
    BOOKING("/booking"),
    BOOKINGBYID("/booking/");

    private final String path;

    ApiEndpoints(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
