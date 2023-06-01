package io.github.koblizekxd.kmapper.asm.visitors;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
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

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (true) {
            return new  MethodVisitor(Opcodes.ASM9, super.visitMethod(access, name, descriptor, signature, exceptions)) {
                @Override
                public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                    super.visitMethodInsn(opcode, owner, remapper.mapMethodName(owner, name, descriptor), descriptor, isInterface);
                }
            };
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}
