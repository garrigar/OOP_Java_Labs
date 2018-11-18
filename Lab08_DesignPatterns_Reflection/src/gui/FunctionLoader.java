package gui;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public class FunctionLoader extends ClassLoader {

    private HashMap<String, Class<?>> loaded;

    public FunctionLoader() {
        super();
        loaded = new HashMap<>();
    }

    public Class<?> loadClassFromFile(File file) throws IOException, ClassFormatError {
        String filename = file.getAbsolutePath();
        if (loaded.containsKey(filename)) {
            return loaded.get(filename);
        } else {
            byte[] bytes = new byte[(int) file.length()];
            try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file))) {
                in.read(bytes);
            }
            Class<?> clazz = defineClass(null, bytes, 0, bytes.length);
            loaded.put(filename, clazz);
            return clazz;
        }
    }
}
