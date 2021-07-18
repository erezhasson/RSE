package livechat;

import datafiles.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

public class ChatManager {

    private final List<SingleChatEntry> chatDataList;

    public ChatManager() {
        chatDataList = new ArrayList<>();
    }

    public synchronized void addChatString(String chatMessage, UserDto user) {
        chatDataList.add(new SingleChatEntry(chatMessage, user));
    }

    public synchronized List<SingleChatEntry> getChatEntries(int fromIndex){
        if (fromIndex < 0 || fromIndex > chatDataList.size()) {
            fromIndex = 0;
        }
        return chatDataList.subList(fromIndex, chatDataList.size());
    }

    public int getVersion() {
        return chatDataList.size();
    }
}