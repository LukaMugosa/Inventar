package me.amplitudo.inventar.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import me.amplitudo.inventar.web.rest.TestUtil;

public class EquipmentServiceDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipmentServiceDTO.class);
        EquipmentServiceDTO equipmentServiceDTO1 = new EquipmentServiceDTO();
        equipmentServiceDTO1.setId(1L);
        EquipmentServiceDTO equipmentServiceDTO2 = new EquipmentServiceDTO();
        assertThat(equipmentServiceDTO1).isNotEqualTo(equipmentServiceDTO2);
        equipmentServiceDTO2.setId(equipmentServiceDTO1.getId());
        assertThat(equipmentServiceDTO1).isEqualTo(equipmentServiceDTO2);
        equipmentServiceDTO2.setId(2L);
        assertThat(equipmentServiceDTO1).isNotEqualTo(equipmentServiceDTO2);
        equipmentServiceDTO1.setId(null);
        assertThat(equipmentServiceDTO1).isNotEqualTo(equipmentServiceDTO2);
    }
}
