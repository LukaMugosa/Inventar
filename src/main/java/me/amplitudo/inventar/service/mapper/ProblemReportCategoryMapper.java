package me.amplitudo.inventar.service.mapper;


import me.amplitudo.inventar.domain.*;
import me.amplitudo.inventar.service.dto.ProblemReportCategoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProblemReportCategory} and its DTO {@link ProblemReportCategoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProblemReportCategoryMapper extends EntityMapper<ProblemReportCategoryDTO, ProblemReportCategory> {


    @Mapping(target = "problems", ignore = true)
    @Mapping(target = "removeProblems", ignore = true)
    ProblemReportCategory toEntity(ProblemReportCategoryDTO problemReportCategoryDTO);

    default ProblemReportCategory fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProblemReportCategory problemReportCategory = new ProblemReportCategory();
        problemReportCategory.setId(id);
        return problemReportCategory;
    }
}
