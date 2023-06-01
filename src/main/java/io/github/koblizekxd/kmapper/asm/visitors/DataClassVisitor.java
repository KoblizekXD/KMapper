package io.github.koblizekxd.kmapper.asm.visitors;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.commons.Remapper;

public class DataClassVisitor extends ClassVisitor {
    private final Remapper remapper;

    public DataClassVisitor(int api, Remapper remapper, ClassVisitor classVisitor) {
        super(api, classVisitor);
        this.remapper = remapper;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, remapper.map(name), signature, superName, interfaces);
    }
}
