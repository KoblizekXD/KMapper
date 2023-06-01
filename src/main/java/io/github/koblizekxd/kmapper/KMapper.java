package io.github.koblizekxd.kmapper;

import io.github.koblizekxd.kmapper.mappings.Mappings;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class KMapper {
    private final ZipFile zipFile;

    /*public KMapper(byte[] bytecode, Mappings mappings) {
        ClassReader classReader = new ClassReader(bytecode);
        ClassWriter writer = new ClassWriter(classReader, EXPAND_FRAMES);
        DataRemapper remapper = new DataRemapper();
        DataClassVisitor visitor = new DataClassVisitor(Opcodes.ASM9, remapper, writer);
        ClassRemapper remapper1 = new ClassRemapper(visitor, remapper);
        classReader.accept(remapper1, 0);
        write(new File("./src/test/java/Main.class"), writer.toByteArray());
    }*/
    private KMapper(ZipFile file) {
        this.zipFile = file;
    }
    public void remap(Mappings mappings, File outputFile) throws IOException {
        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        if (outputFile.exists()) outputFile.delete();
        outputFile.createNewFile();
        ZipOutputStream output = new ZipOutputStream(new FileOutputStream(outputFile));

        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            byte[] bytes = zipFile.getInputStream(entry).readAllBytes();

            if (entry.getName().endsWith(".class")) {

            } else {
                ZipEntry newEntry = new ZipEntry(entry.getName());
                output.putNextEntry(newEntry);
                copyTo(new ByteArrayInputStream(bytes), output);
                output.closeEntry();
            }
        }
    }
    private long copyTo(ByteArrayInputStream stream, OutputStream out) {
        long bytesCopied = 0;
        byte[] buffer = new byte[8 * 1024];
        try {
            int bytes = stream.read(buffer);
            while (bytes >= 0) {
                out.write(buffer, 0, bytes);
                bytesCopied += bytes;
                bytes = stream.read(buffer);
            }
            return bytesCopied;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static KMapper open(ZipFile jarFile) {
        return new KMapper(jarFile);
    }
}
