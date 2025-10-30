package utils;

public enum SmtpMessage {

    // 2xx Success
    GREET(220, "myserver.com Service Ready"),
    OK(250, "OK"),
    MSG_RECEIVED(250, "Message accepted"),
    CLOSING_TRANSMISSION(221,"myserver.com Service closing transmission channel"),

    // 3xx Intermediate
    START_MAIL_INPUT(354, "End data with <CR><LF>.<CR><LF>"),

    // 4xx Temporary failures
    BUSY(421, "Service not available, closing connection"),

    // 5xx Permanent failures
    SYNTAX_ERROR(500, "Syntax error, command unrecognized"),
    BAD_SEQUENCE(503, "Bad sequence of commands"),
    MAILBOX_UNAVAILABLE(550, "Requested action not taken: mailbox unavailable");


    private final int code;
    private final String text;

    SmtpMessage(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public String getFullText() {
        return code + " " + text + "\r\n";
    }

    // Optional helper to check type
    public boolean isSuccess() {
        return code >= 200 && code < 300;
    }

    public boolean isIntermediate() {
        return code >= 300 && code < 400;
    }

    public boolean isTemporaryFailure() {
        return code >= 400 && code < 500;
    }

    public boolean isPermanentFailure() {
        return code >= 500 && code < 600;
    }

}
