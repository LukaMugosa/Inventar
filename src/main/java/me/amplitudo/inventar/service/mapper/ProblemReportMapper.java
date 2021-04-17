package me.amplitudo.inventar.service.mapper;


import me.amplitudo.inventar.domain.*;
import me.amplitudo.inventar.service.dto.ProblemReportDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProblemReport} and its DTO {@link ProblemReportDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProblemReportCategoryMapper.class, EmployeeMapper.class})
public interface ProblemReportMapper extends EntityMapper<ProblemReportDTO, ProblemReport> {

    @Mapping(source = "problemReportCategory.id", target = "problemReportCategoryId")
    @Mapping(source = "employee.id", target = "employeeId")
    ProblemReportDTO toDto(ProblemReport problemReport);

    @Mapping(source = "problemReportCategoryId", target = "problemReportCategory")
    @Mapping(source = "employeeId", target = "employee")
    ProblemReport toEntity(ProblemReportDTO problemReportDTO);

    default ProblemReport fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProblemReport problemReport = new ProblemReport();
        problemReport.setId(id);
        return problemReport;
    }
}
