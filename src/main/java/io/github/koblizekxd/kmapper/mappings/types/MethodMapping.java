package io.github.koblizekxd.kmapper.mappings.types;

import java.util.Objects;

public class MethodMapping implements IMappingType {
    private final String oldName;
    private final String newName;
    private final String params;
    private final int lineFrom;
    private final int lineTo;

    public MethodMapping(String oldName, String newName, String params, int lineFrom, int lineTo) {
        this.oldName = oldName;
        this.newName = newName;
        this.params = params;
        this.lineFrom = lineFrom;
        this.lineTo = lineTo;
    }
    public MethodMapping(String oldName, String newName, String params) {
        this.oldName = oldName;
        this.newName = newName;
        this.lineFrom = -1;
        this.lineTo = -1;
        this.params = params;
    }
    public MethodMapping(String oldName, String newName, int lineFrom, int lineTo) {
        this.oldName = oldName;
        this.newName = newName;
        this.lineFrom = lineFrom;
        this.lineTo = lineTo;
        this.params = "";
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
        return lineFrom;
    }

    @Override
    public int getLineTo() {
        return lineTo;
    }

    @Override
    public String getParameters() {
        return params;
    }

    @Override
    public boolean hasParameters() {
        return !Objects.equals(params, "");
    }

    @Override
    public boolean hasLineRange() {
        return lineFrom != -1;
    }
}
