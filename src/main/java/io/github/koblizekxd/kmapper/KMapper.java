package io.github.koblizekxd.kmapper;

import io.github.koblizekxd.kmapper.asm.remapper.DataRemapper;
import io.github.koblizekxd.kmapper.asm.visitors.DataClassVisitor;
import io.github.koblizekxd.kmapper.mappings.Mappings;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.ClassRemapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.ZipFile;

import static org.objectweb.asm.ClassReader.EXPAND_FRAMES;

public class KMapper {
    public KMapper(byte[] bytecode, Mappings mappings) {
        ClassReader classReader = new ClassReader(bytecode);
        ClassWriter writer = new ClassWriter(classReader, EXPAND_FRAMES);
        DataRemapper remapper = new DataRemapper();
        DataClassVisitor visitor = new DataClassVisitor(Opcodes.ASM9, remapper, writer);
        ClassRemapper remapper1 = new ClassRemapper(visitor, remapper);
        classReader.accept(remapper1, 0);
        write(new File("./src/test/java/Main.class"), writer.toByteArray());
    }

    public static void main(String[] args) {
        KMapper mapper = new KMapper(getBytecode(new File("./src/test/java/Main.class")), null);
    }
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
