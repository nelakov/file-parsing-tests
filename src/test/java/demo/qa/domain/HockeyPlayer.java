package demo.qa.domain;

import java.util.List;

public record HockeyPlayer(String id, String name, String lastName, boolean captain, List<String> teams) {
}
