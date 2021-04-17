package me.amplitudo.inventar.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class RepairerMapperTest {

    private RepairerMapper repairerMapper;

    @BeforeEach
    public void setUp() {
        repairerMapper = new RepairerMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(repairerMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(repairerMapper.fromId(null)).isNull();
    }
}
