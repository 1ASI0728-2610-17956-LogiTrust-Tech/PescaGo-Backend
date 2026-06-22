package pe.upc.pescagobackend.fleet;

import org.junit.jupiter.api.Test;
import pe.upc.pescagobackend.fleet.domain.model.FleetPlateRules;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FleetPlateRulesTest {

    @Test
    void normalizeTrimsAndUppercasesPlate() {
        assertThat(FleetPlateRules.normalize(" abc-123 ")).isEqualTo("ABC-123");
    }

    @Test
    void normalizeRejectsBlankPlate() {
        assertThatThrownBy(() -> FleetPlateRules.normalize("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("blank");
    }

    @Test
    void normalizeRejectsPlateLongerThanTwentyCharacters() {
        assertThatThrownBy(() -> FleetPlateRules.normalize("ABCDEFGHIJKLMNOPQRSTU"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("20");
    }
}
