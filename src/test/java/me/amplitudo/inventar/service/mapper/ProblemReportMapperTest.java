package me.amplitudo.inventar.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ProblemReportMapperTest {

    private ProblemReportMapper problemReportMapper;

    @BeforeEach
    public void setUp() {
        problemReportMapper = new ProblemReportMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(problemReportMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(problemReportMapper.fromId(null)).isNull();
    }
}
