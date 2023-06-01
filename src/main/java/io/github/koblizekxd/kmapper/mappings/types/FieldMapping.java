package io.github.koblizekxd.kmapper.mappings.types;

public class FieldMapping extends MethodMapping {
    public FieldMapping(String oldName, String newName, ClassMapping classMapping) {
        super(oldName, newName, "", classMapping);
    }

    public FieldMapping(String oldName, String newName, ClassMapping classMapping, int lineFrom, int lineTo) {
        super(oldName, newName, classMapping, lineFrom, lineTo);
    }

    @Override
    public boolean hasParameters() {
        return false;
    }
}
