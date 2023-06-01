package io.github.koblizekxd.kmapper.mappings.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public final class Bytecode {
    private Bytecode() {}

    public static byte[] getBytecode(File file) {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void write(File file, byte[] bytecode) {
        try (FileOutputStream stream = new FileOutputStream(file)) {
            stream.write(bytecode);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
