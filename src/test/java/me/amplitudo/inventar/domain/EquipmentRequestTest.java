package me.amplitudo.inventar.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import me.amplitudo.inventar.web.rest.TestUtil;

public class EquipmentRequestTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipmentRequest.class);
        EquipmentRequest equipmentRequest1 = new EquipmentRequest();
        equipmentRequest1.setId(1L);
        EquipmentRequest equipmentRequest2 = new EquipmentRequest();
        equipmentRequest2.setId(equipmentRequest1.getId());
        assertThat(equipmentRequest1).isEqualTo(equipmentRequest2);
        equipmentRequest2.setId(2L);
        assertThat(equipmentRequest1).isNotEqualTo(equipmentRequest2);
        equipmentRequest1.setId(null);
        assertThat(equipmentRequest1).isNotEqualTo(equipmentRequest2);
    }
}
