package me.amplitudo.inventar.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import me.amplitudo.inventar.web.rest.TestUtil;

public class EquipmentCategoryDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipmentCategoryDTO.class);
        EquipmentCategoryDTO equipmentCategoryDTO1 = new EquipmentCategoryDTO();
        equipmentCategoryDTO1.setId(1L);
        EquipmentCategoryDTO equipmentCategoryDTO2 = new EquipmentCategoryDTO();
        assertThat(equipmentCategoryDTO1).isNotEqualTo(equipmentCategoryDTO2);
        equipmentCategoryDTO2.setId(equipmentCategoryDTO1.getId());
        assertThat(equipmentCategoryDTO1).isEqualTo(equipmentCategoryDTO2);
        equipmentCategoryDTO2.setId(2L);
        assertThat(equipmentCategoryDTO1).isNotEqualTo(equipmentCategoryDTO2);
        equipmentCategoryDTO1.setId(null);
        assertThat(equipmentCategoryDTO1).isNotEqualTo(equipmentCategoryDTO2);
    }
}
