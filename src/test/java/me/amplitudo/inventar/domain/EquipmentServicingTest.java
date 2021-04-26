package me.amplitudo.inventar.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import me.amplitudo.inventar.web.rest.TestUtil;

public class EquipmentServicingTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipmentServicing.class);
        EquipmentServicing equipmentServicing1 = new EquipmentServicing();
        equipmentServicing1.setId(1L);
        EquipmentServicing equipmentServicing2 = new EquipmentServicing();
        equipmentServicing2.setId(equipmentServicing1.getId());
        assertThat(equipmentServicing1).isEqualTo(equipmentServicing2);
        equipmentServicing2.setId(2L);
        assertThat(equipmentServicing1).isNotEqualTo(equipmentServicing2);
        equipmentServicing1.setId(null);
        assertThat(equipmentServicing1).isNotEqualTo(equipmentServicing2);
    }
}
