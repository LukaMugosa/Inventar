package me.amplitudo.inventar.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class EquipmentRequestMapperTest {

    private EquipmentRequestMapper equipmentRequestMapper;

    @BeforeEach
    public void setUp() {
        equipmentRequestMapper = new EquipmentRequestMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(equipmentRequestMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(equipmentRequestMapper.fromId(null)).isNull();
    }
}
