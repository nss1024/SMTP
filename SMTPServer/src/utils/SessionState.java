/*Enum holding SMTP states, these are used to indicate current state of a transaction and
 *to specify possible next states once a transaction completed.
 *
 */

package utils;

public enum SessionState {
    INIT, // connection just established, waiting for HELO/EHLO
    HELO,
    MAIL,
    RCPT,
    DATA,
    STARTTLS,
    HELO_RECEIVED,        // HELO/EHLO successfully received
    MAIL_FROM_RECEIVED,   // MAIL FROM command received
    RCPT_TO_RECEIVED,     // at least one RCPT TO received
    DATA_RECEIVING,       // after DATA command, reading message body
    DATA_COMPLETE,        // end of DATA (line with just .)
    QUIT                  // QUIT command received, session ending
}

