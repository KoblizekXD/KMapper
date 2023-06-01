package io.github.koblizekxd.kmapper.mappings.types;

public interface IMappingType {
    String getNewName();
    String getOldName();
    int getLineFrom();
    int getLineTo();
    String getParameters();
    boolean hasParameters();
    boolean hasLineRange();
}
