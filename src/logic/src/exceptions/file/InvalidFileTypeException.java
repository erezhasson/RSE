package exceptions.file;

public class InvalidFileTypeException extends FileException{
    private final String fileExtension;
    private final String expectedExtension;

    public InvalidFileTypeException(String fileExtension, String expectedExtension) {
        this.fileExtension = fileExtension;
        this.expectedExtension = expectedExtension;
    }

    @Override
    public String getMessage() {
        return "[" + fileExtension + "] is not valid file type (expected " + expectedExtension + ").";
    }
}
