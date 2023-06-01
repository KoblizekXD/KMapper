package io.github.koblizekxd.kmapper.asm;

import io.github.koblizekxd.kmapper.asm.remapper.DataRemapper;
import io.github.koblizekxd.kmapper.asm.visitors.DataClassVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.MethodRemapper;
import org.objectweb.asm.tree.ClassNode;

import static org.objectweb.asm.ClassReader.EXPAND_FRAMES;

public class EntryModifier {
    private final byte[] bytecode;
    private final ClassReader reader;
    private final ClassWriter writer;

    public EntryModifier(byte[] bytecode) {
        this.bytecode = bytecode;
        ClassNode node = new ClassNode();
        reader = new ClassReader(bytecode);
        writer = new ClassWriter(reader, EXPAND_FRAMES);
        DataRemapper remapper = new DataRemapper();
        DataClassVisitor visitor = new DataClassVisitor(Opcodes.ASM9, remapper, writer);
        ClassRemapper cmap = new ClassRemapper(visitor, remapper);
        reader.accept(cmap, 0);
    }
    private void renameClass() {

    }
}
