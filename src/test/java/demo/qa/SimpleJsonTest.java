package demo.qa;

import demo.qa.domain.HockeyPlayer;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleJsonTest {
    ClassLoader classLoader = getClass().getClassLoader();

    @Test
    void jsonTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        HockeyPlayer hockeyPlayer = objectMapper.readValue(Paths.get("src/test/resources/json/player.json").toFile(), HockeyPlayer.class);
        assertThat(hockeyPlayer.name).isEqualTo("Jaromir");
        assertThat(hockeyPlayer.lastname).isEqualTo("Jagr");
        assertThat(hockeyPlayer.isCapitan).isEqualTo(true);
        assertThat(hockeyPlayer.teams).contains("Hawks", "Pinguins");

    }
}

