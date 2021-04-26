package me.amplitudo.inventar.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import me.amplitudo.inventar.web.rest.TestUtil;

public class EquipmentServicingDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipmentServicingDTO.class);
        EquipmentServicingDTO equipmentServicingDTO1 = new EquipmentServicingDTO();
        equipmentServicingDTO1.setId(1L);
        EquipmentServicingDTO equipmentServicingDTO2 = new EquipmentServicingDTO();
        assertThat(equipmentServicingDTO1).isNotEqualTo(equipmentServicingDTO2);
        equipmentServicingDTO2.setId(equipmentServicingDTO1.getId());
        assertThat(equipmentServicingDTO1).isEqualTo(equipmentServicingDTO2);
        equipmentServicingDTO2.setId(2L);
        assertThat(equipmentServicingDTO1).isNotEqualTo(equipmentServicingDTO2);
        equipmentServicingDTO1.setId(null);
        assertThat(equipmentServicingDTO1).isNotEqualTo(equipmentServicingDTO2);
    }
}
