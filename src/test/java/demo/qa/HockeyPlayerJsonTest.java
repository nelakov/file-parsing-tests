package demo.qa;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.qa.domain.HockeyPlayer;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

class HockeyPlayerJsonTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    void deserializesPlayerFromJson() throws Exception {
        try (InputStream playerJson = classpathResource("json/player.json")) {
            HockeyPlayer player = OBJECT_MAPPER.readValue(playerJson, HockeyPlayer.class);

            assertThat(player.name()).isEqualTo("Jaromir");
            assertThat(player.lastName()).isEqualTo("Jagr");
            assertThat(player.captain()).isTrue();
            assertThat(player.teams()).contains("Hawks", "Penguins");
        }
    }

    private static InputStream classpathResource(String path) {
        InputStream resource = HockeyPlayerJsonTest.class.getClassLoader().getResourceAsStream(path);
        assertThat(resource).as("classpath resource %s", path).isNotNull();
        return resource;
    }
}
