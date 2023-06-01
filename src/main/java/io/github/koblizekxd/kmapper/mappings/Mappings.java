package io.github.koblizekxd.kmapper.mappings;

import io.github.koblizekxd.kmapper.mappings.types.ClassMapping;
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

    public Mappings() {
        remappableClasses = new ArrayList<>();
        remappableMethods = new ArrayList<>();
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
        for (int i = 0; i < content.length; i++) {
            String line = content[i];
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
            }
        }
    }
}
