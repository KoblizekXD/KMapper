package io.github.koblizekxd.kmapper.mappings;

import java.io.File;

public interface IMappable {
    void resolve(File file);
    void resolve(String[] content);
}
