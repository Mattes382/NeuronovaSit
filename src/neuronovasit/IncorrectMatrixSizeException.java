package neuronovasit;

public class IncorrectMatrixSizeException extends RuntimeException{
    public IncorrectMatrixSizeException(String errorMessage) {
        super(errorMessage);
    }
}
