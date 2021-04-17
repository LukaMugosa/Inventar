package me.amplitudo.inventar.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class EquipmentCategoryMapperTest {

    private EquipmentCategoryMapper equipmentCategoryMapper;

    @BeforeEach
    public void setUp() {
        equipmentCategoryMapper = new EquipmentCategoryMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(equipmentCategoryMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(equipmentCategoryMapper.fromId(null)).isNull();
    }
}
