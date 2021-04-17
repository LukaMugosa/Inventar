package me.amplitudo.inventar.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class EquipmentImageMapperTest {

    private EquipmentImageMapper equipmentImageMapper;

    @BeforeEach
    public void setUp() {
        equipmentImageMapper = new EquipmentImageMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(equipmentImageMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(equipmentImageMapper.fromId(null)).isNull();
    }
}
