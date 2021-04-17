package me.amplitudo.inventar.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ManufacturerMapperTest {

    private ManufacturerMapper manufacturerMapper;

    @BeforeEach
    public void setUp() {
        manufacturerMapper = new ManufacturerMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(manufacturerMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(manufacturerMapper.fromId(null)).isNull();
    }
}
