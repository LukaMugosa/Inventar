package me.amplitudo.inventar.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ProblemReportCategoryMapperTest {

    private ProblemReportCategoryMapper problemReportCategoryMapper;

    @BeforeEach
    public void setUp() {
        problemReportCategoryMapper = new ProblemReportCategoryMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(problemReportCategoryMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(problemReportCategoryMapper.fromId(null)).isNull();
    }
}
