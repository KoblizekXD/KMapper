package io.github.koblizekxd.kmapper.mappings.convert;

import io.github.koblizekxd.kmapper.mappings.IMappable;
import io.github.koblizekxd.kmapper.mappings.types.ClassMapping;
import io.github.koblizekxd.kmapper.mappings.types.FieldMapping;
import io.github.koblizekxd.kmapper.mappings.types.MethodMapping;
import io.github.koblizekxd.kmapper.mappings.util.MappingException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProguardMappings implements IMappable {
    @Override
    public void resolve(File file) {
        try {
            this.resolve(Files.readAllLines(file.toPath()).toArray(String[]::new));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private final Pattern CLASS_PATTERN = Pattern.compile("(?<newname>\\S+) -> (?<oldname>\\S+):");
    private final Pattern FIELD_PATTERN = Pattern.compile("\t(?<type>.+) (?<newname>.+) -> (?<oldname>.+)");
    private final Pattern METHOD_PATTERN = Pattern.compile("\t(?<type>.+) (?<newname>.+)\\((?<params>.*)\\) -> (?<oldname>.+)");
    private final Pattern METHOD_PATTERN_WITH_NUMBERS = Pattern.compile("\t(?<from>.+):(?<to>.+):(?<type>.+) (?<newname>.+)\\((?<params>.*)\\) -> (?<oldname>.+)");

    private final List<ClassMapping> remappableClasses;
    private final List<MethodMapping> remappableMethods;
    private final List<FieldMapping> remappableFields;

    public ProguardMappings() {
        remappableClasses = new ArrayList<>();
        remappableMethods = new ArrayList<>();
        remappableFields = new ArrayList<>();
    }

    public List<ClassMapping> getRemappableClasses() {
        return remappableClasses;
    }

    public List<FieldMapping> getRemappableFields() {
        return remappableFields;
    }

    public List<MethodMapping> getRemappableMethods() {
        return remappableMethods;
    }

    @Override
    public void resolve(String[] content) {
        ClassMapping currentClass = null;
        for (String line : content) {
            Matcher matcher;
            if ((matcher = CLASS_PATTERN.matcher(line)).matches()) {
                String newName = matcher.group("newname");
                String oldName = matcher.group("oldname");
                ClassMapping mapping = new ClassMapping(oldName, newName);
                remappableClasses.add(mapping);
                currentClass = mapping;
            } else if ((matcher = METHOD_PATTERN.matcher(line)).matches()) {
                String type = matcher.group("type");
                String newName = matcher.group("newname");
                String params = matcher.group("params");
                String oldName = matcher.group("oldname");
                MethodMapping methodMapping = new MethodMapping(oldName, newName, params, currentClass);
                methodMapping.setType(type);
                remappableMethods.add(methodMapping);
            } else if ((matcher = METHOD_PATTERN_WITH_NUMBERS.matcher(line)).matches()) {
                int from = Integer.parseInt(matcher.group("from"));
                int to = Integer.parseInt(matcher.group("to"));
                String type = matcher.group("type");
                String newName = matcher.group("newname");
                String params = matcher.group("params");
                String oldName = matcher.group("oldname");
                MethodMapping methodMapping = new MethodMapping(oldName, newName, params, currentClass, from, to);
                methodMapping.setType(type);
                remappableMethods.add(methodMapping);
            } else if ((matcher = FIELD_PATTERN.matcher(line)).matches()) {
                String type = matcher.group("type");
                String newName = matcher.group("newname");
                String oldName = matcher.group("oldname");
                FieldMapping fieldMapping = new FieldMapping(oldName, newName, currentClass);
                fieldMapping.setType(type);
                remappableFields.add(fieldMapping);
            }
        }
    }
}
