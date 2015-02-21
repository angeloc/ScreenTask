package screentask;

public class ScreenShotManager {

    private static final ScreenShotManager instance = new ScreenShotManager();
    private byte[] imageBytes;

    protected ScreenShotManager() {
    }

    public static ScreenShotManager getInstance() {
        return instance;
    }

    public void updateImage(byte[] image) {
        imageBytes = image;
    }

    public byte[] getImage() {
        return imageBytes;
    }
}
