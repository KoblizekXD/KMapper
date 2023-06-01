package io.github.koblizekxd.kmapper;

import io.github.koblizekxd.kmapper.mappings.Mappings;

public class KMapper {
    public KMapper() {
    }

    public static void main(String[] args) {
        Mappings mappings = new Mappings();
        mappings.resolve(new String[] { "String -> uwu {" });
    }
}
