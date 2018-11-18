package gui.utils;

import java.io.IOException;

public class FXMLErrorException extends IOException {
    public FXMLErrorException(String s, IOException e) {
        super(s, e);
    }
}
