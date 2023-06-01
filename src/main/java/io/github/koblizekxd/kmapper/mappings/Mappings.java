package io.github.koblizekxd.kmapper.mappings;

import io.github.koblizekxd.kmapper.mappings.types.ClassMapping;
import io.github.koblizekxd.kmapper.mappings.types.MethodMapping;

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
        for (int i = 0; i < content.length; i++) {
            String line = content[i];
            Matcher matcher;
            if ((matcher = CLASS_PATTERN.matcher(line)).matches()) {
                String newName = matcher.group("newname");
                String oldName = matcher.group("oldname");
                remappableClasses.add(new ClassMapping(oldName, newName));
            }
        }
    }
}
