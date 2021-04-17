package me.amplitudo.inventar.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import me.amplitudo.inventar.web.rest.TestUtil;

public class EquipmentServiceTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipmentService.class);
        EquipmentService equipmentService1 = new EquipmentService();
        equipmentService1.setId(1L);
        EquipmentService equipmentService2 = new EquipmentService();
        equipmentService2.setId(equipmentService1.getId());
        assertThat(equipmentService1).isEqualTo(equipmentService2);
        equipmentService2.setId(2L);
        assertThat(equipmentService1).isNotEqualTo(equipmentService2);
        equipmentService1.setId(null);
        assertThat(equipmentService1).isNotEqualTo(equipmentService2);
    }
}
