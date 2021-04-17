package me.amplitudo.inventar.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import me.amplitudo.inventar.web.rest.TestUtil;

public class EquipmentEmployeeDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipmentEmployeeDTO.class);
        EquipmentEmployeeDTO equipmentEmployeeDTO1 = new EquipmentEmployeeDTO();
        equipmentEmployeeDTO1.setId(1L);
        EquipmentEmployeeDTO equipmentEmployeeDTO2 = new EquipmentEmployeeDTO();
        assertThat(equipmentEmployeeDTO1).isNotEqualTo(equipmentEmployeeDTO2);
        equipmentEmployeeDTO2.setId(equipmentEmployeeDTO1.getId());
        assertThat(equipmentEmployeeDTO1).isEqualTo(equipmentEmployeeDTO2);
        equipmentEmployeeDTO2.setId(2L);
        assertThat(equipmentEmployeeDTO1).isNotEqualTo(equipmentEmployeeDTO2);
        equipmentEmployeeDTO1.setId(null);
        assertThat(equipmentEmployeeDTO1).isNotEqualTo(equipmentEmployeeDTO2);
    }
}
