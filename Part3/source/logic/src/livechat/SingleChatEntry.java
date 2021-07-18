package livechat;

import datafiles.dto.UserDto;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SingleChatEntry {
    private final String messageString;
    private final UserDto user;
    private final String time;

    public SingleChatEntry(String messageString, UserDto user) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();

        this.messageString = messageString;
        this.user = user;
        this.time = dtf.format(now);
    }

    public String getMessageString() {
        return messageString;
    }

    public String getTime() {
        return time;
    }

    public UserDto getUser() {
        return user;
    }

    @Override
    public String toString() {
        return (user != null ? user + ": " : "") + messageString;
    }
}
