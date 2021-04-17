package me.amplitudo.inventar.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import me.amplitudo.inventar.web.rest.TestUtil;

public class EquipmentEmployeeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipmentEmployee.class);
        EquipmentEmployee equipmentEmployee1 = new EquipmentEmployee();
        equipmentEmployee1.setId(1L);
        EquipmentEmployee equipmentEmployee2 = new EquipmentEmployee();
        equipmentEmployee2.setId(equipmentEmployee1.getId());
        assertThat(equipmentEmployee1).isEqualTo(equipmentEmployee2);
        equipmentEmployee2.setId(2L);
        assertThat(equipmentEmployee1).isNotEqualTo(equipmentEmployee2);
        equipmentEmployee1.setId(null);
        assertThat(equipmentEmployee1).isNotEqualTo(equipmentEmployee2);
    }
}
