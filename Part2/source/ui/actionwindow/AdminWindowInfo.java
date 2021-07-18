package ui.actionwindow;

import datafiles.dto.StockDto;
import javafx.scene.image.Image;

import java.util.Collection;

public class AdminWindowInfo{
    private final Collection<StockDto> stocks;
    private final Image profileImage;


    public AdminWindowInfo(Collection<StockDto> stocks, Image profileImage) {
        this.stocks = stocks;
        this.profileImage = profileImage;
    }

    public Collection<StockDto> getStocks() {
        return stocks;
    }

    public Image getProfileImage() {
        return profileImage;
    }
}
