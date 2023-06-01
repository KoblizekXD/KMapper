package io.github.koblizekxd.kmapper.mappings;

import io.github.koblizekxd.kmapper.mappings.convert.ProguardMappings;
import io.github.koblizekxd.kmapper.mappings.types.ClassMapping;
import io.github.koblizekxd.kmapper.mappings.types.FieldMapping;
import io.github.koblizekxd.kmapper.mappings.types.MethodMapping;
import io.github.koblizekxd.kmapper.mappings.util.MappingException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mappings implements IMappable {
    private static final Pattern CLASS_PATTERN
            = Pattern.compile("(?<newname>.+) -> (?<oldname>.+) \\{");
    private static final Pattern CLASS_PATTERN_END
            = Pattern.compile("}");
    private static final Pattern METHOD_PATTERN
            = Pattern.compile("\t(?<type>.+) (?<newname>.+)\\((?<params>.*)\\) = (?<oldname>.+)");
    private static final Pattern METHOD_PATTERN_WITH_NUMBERS
            = Pattern.compile("\t\\[(?<from>.+) to (?<to>.+)] (?<type>.+) (?<newname>.+)\\((?<params>.*)\\) = (?<oldname>.+)");
    private static final Pattern FIELD_PATTERN_WITH_NUMBERS
            = Pattern.compile("\t\\[(?<from>.+) to (?<to>.+)] (?<type>.+) (?<newname>.+) = (?<oldname>.+)");
    private static final Pattern FIELD_PATTERN
            = Pattern.compile("\t(?<type>.+) (?<newname>.+) = (?<oldname>.+)");

    private final List<ClassMapping> remappableClasses;
    private final List<MethodMapping> remappableMethods;
    private final List<FieldMapping> remappableFields;

    public Mappings() {
        remappableClasses = new ArrayList<>();
        remappableMethods = new ArrayList<>();
        remappableFields = new ArrayList<>();
    }
    private Mappings(List<ClassMapping> remappableClasses, List<MethodMapping> remappableMethods, List<FieldMapping> remappableFields) {
        this.remappableClasses = remappableClasses;
        this.remappableMethods = remappableMethods;
        this.remappableFields = remappableFields;
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
    public void resolve(File file) {
        try {
            this.resolve(Files.readAllLines(file.toPath()).toArray(String[]::new));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resolve(String[] content) {
        boolean inClass = false;
        ClassMapping currentClass = null;
        for (String line : content) {
            Matcher matcher;
            if ((matcher = CLASS_PATTERN.matcher(line)).matches()) {
                String newName = matcher.group("newname");
                String oldName = matcher.group("oldname");
                ClassMapping mapping = new ClassMapping(oldName, newName);
                remappableClasses.add(mapping);
                currentClass = mapping;
                inClass = true;
            } else if ((matcher = CLASS_PATTERN_END.matcher(line)).matches()) {
                currentClass = null;
                inClass = false;
            } else if ((matcher = METHOD_PATTERN.matcher(line)).matches()) {
                if (!inClass) throw new MappingException("Method mapping can't be out of Class mapping!");
                String type = matcher.group("type");
                String newName = matcher.group("newname");
                String params = matcher.group("params");
                String oldName = matcher.group("oldname");
                MethodMapping methodMapping = new MethodMapping(oldName, newName, params, currentClass);
                methodMapping.setType(type);
                remappableMethods.add(methodMapping);
            } else if ((matcher = METHOD_PATTERN_WITH_NUMBERS.matcher(line)).matches()) {
                if (!inClass) throw new MappingException("Method mapping can't be out of Class mapping!");
                int from = Integer.parseInt(matcher.group("from"));
                int to = Integer.parseInt(matcher.group("to"));
                String type = matcher.group("type");
                String newName = matcher.group("newname");
                String params = matcher.group("params");
                String oldName = matcher.group("oldname");
                MethodMapping methodMapping = new MethodMapping(oldName, newName, params, currentClass, from, to);
                methodMapping.setType(type);
                remappableMethods.add(methodMapping);
            } else if ((matcher = FIELD_PATTERN_WITH_NUMBERS.matcher(line)).matches()) {
                if (!inClass) throw new MappingException("Method mapping can't be out of Class mapping!");
                int from = Integer.parseInt(matcher.group("from"));
                int to = Integer.parseInt(matcher.group("to"));
                String type = matcher.group("type");
                String newName = matcher.group("newname");
                String oldName = matcher.group("oldname");
                FieldMapping fieldMapping = new FieldMapping(oldName, newName, currentClass, from, to);
                fieldMapping.setType(type);
                remappableFields.add(fieldMapping);
            } else if ((matcher = FIELD_PATTERN.matcher(line)).matches()) {
                if (!inClass) throw new MappingException("Method mapping can't be out of Class mapping!");
                String type = matcher.group("type");
                String newName = matcher.group("newname");
                String oldName = matcher.group("oldname");
                FieldMapping fieldMapping = new FieldMapping(oldName, newName, currentClass);
                fieldMapping.setType(type);
                remappableFields.add(fieldMapping);
            }
        }
    }

    @Override
    public String write() {
        StringBuilder builder = new StringBuilder("# Using KMaps\n");
        for (ClassMapping classMapping : remappableClasses) {
            builder.append(classMapping.getNewName())
                    .append(" -> ")
                    .append(classMapping.getOldName())
                    .append(" {\n");
            remappableFields.stream().filter(f -> f.getClassMapping().equals(classMapping))
                    .forEach(fieldMapping -> {
                        builder.append("\t")
                                .append(fieldMapping.getType())
                                .append(" ")
                                .append(fieldMapping.getNewName())
                                .append(" = ")
                                .append(fieldMapping.getOldName())
                                .append("\n");
                    });
            remappableMethods.stream().filter(m -> m.getClassMapping().equals(classMapping))
                    .forEach(methodMapping -> {
                        builder.append("\t");
                        if (methodMapping.hasLineRange()) {
                            builder.append("[")
                                    .append(methodMapping.getLineFrom())
                                    .append(" to ")
                                    .append(methodMapping.getLineTo())
                                    .append("] ");
                        }
                        builder.append(methodMapping.getType())
                                .append(" ")
                                .append(methodMapping.getNewName())
                                .append("(")
                                .append(methodMapping.getParameters())
                                .append(")\n");

                    });
            builder.append("}\n");
        }
        return builder.toString();
    }

    public static Mappings from(ProguardMappings mappings) {
        return new Mappings(mappings.getRemappableClasses(), mappings.getRemappableMethods(), mappings.getRemappableFields());
    }
}
