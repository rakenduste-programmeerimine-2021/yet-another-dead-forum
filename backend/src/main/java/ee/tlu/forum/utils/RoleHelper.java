package ee.tlu.forum.utils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RoleHelper {

    public static String toDisplayName(String roleFullName) {
        String display = roleFullName.replace("ROLE_", "").toLowerCase();

        return Stream.of(display.trim().split("_"))
                .filter(d -> d.length() > 0)
                .map(d -> d.substring(0, 1).toUpperCase() + d.substring(1))
                .collect(Collectors.joining(" "));
    }

    public static String toRoleName(String name) {
        name = name.toUpperCase()
                .replaceAll("\\s+"," ")
                .replace("ROLE_", "")
                .trim()
                .replace(" ", "_");

        return new StringBuilder(name)
                .insert(0, "ROLE_")
                .toString();
    }

}
