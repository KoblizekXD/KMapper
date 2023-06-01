package io.github.koblizekxd.kmapper.mappings;

import org.apache.commons.lang3.NotImplementedException;

import java.io.File;

public interface IMappable {
    void resolve(File file);
    void resolve(String[] content);
    default String write() {
        throw new NotImplementedException();
    }
}
