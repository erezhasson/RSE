package ui.actionwindow;

import datafiles.dto.UserDto;
import javafx.scene.image.Image;

public class UserWindowInfo {
    private final UserDto user;
    private final Image profileImage;

    public UserWindowInfo(UserDto user, Image profileImage) {
        this.user = user;
        this.profileImage = profileImage;
    }

    public Image getProfileImage() {
        return profileImage;
    }

    public UserDto getUser() {
        return user;
    }

}
