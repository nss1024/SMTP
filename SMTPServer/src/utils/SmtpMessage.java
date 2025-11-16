package utils;

public enum SmtpMessage {

    // 2xx Success
    GREET(220, "myserver.com Service Ready"),
    OK(250, "OK"),
    MSG_RECEIVED(250, "Message accepted"),
    CLOSING_TRANSMISSION(221,"myserver.com Service closing transmission channel"),
    READY_FOR_TLS(220,"Ready to start TLS"),

    // 3xx Intermediate
    START_MAIL_INPUT(354, "End data with <CR><LF>.<CR><LF>"),

    // 4xx Temporary failures
    BUSY(421, "Service not available, closing connection"),
    TLS_FAILURE(454, "TLS not available due to temporary reason\r\n"),
    // 5xx Permanent failures
    INVALID_RECIPIENT_SYNTAX(501,"Invalid recipient syntax"),
    SYNTAX_ERROR(500, "Syntax error, command unrecognized"),
    BAD_SEQUENCE(503, "Bad sequence of commands"),
    MAILBOX_UNAVAILABLE(550, "Requested action not taken: mailbox unavailable"),
    BAD_SENDER_ADDRESS(550, "Bad sender address");


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
