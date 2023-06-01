package io.github.koblizekxd.kmapper.mappings.types;

public class ClassMapping implements IMappingType {
    private final String oldName;
    private final String newName;

    public ClassMapping(String oldName, String newName) {
        this.oldName = oldName;
        this.newName = newName;
    }
    @Override
    public String getNewName() {
        return newName;
    }

    @Override
    public String getOldName() {
        return oldName;
    }

    @Override
    public int getLineFrom() {
        return 0;
    }

    @Override
    public int getLineTo() {
        return 0;
    }

    @Override
    public String getParameters() {
        return null;
    }

    @Override
    public boolean hasParameters() {
        return false;
    }

    @Override
    public boolean hasLineRange() {
        return false;
    }
}
