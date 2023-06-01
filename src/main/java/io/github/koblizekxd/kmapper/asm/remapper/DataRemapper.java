package io.github.koblizekxd.kmapper.asm.remapper;

import org.objectweb.asm.commons.Remapper;

public class DataRemapper extends Remapper {
    @Override
    public String map(String internalName) {
        if (internalName.equals("Main"))
            return super.map("NewNameLol");
        return super.map(internalName);
    }
}
