package me.amplitudo.inventar.service.mapper;


import me.amplitudo.inventar.domain.*;
import me.amplitudo.inventar.service.dto.EmployeeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Employee} and its DTO {@link EmployeeDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, TenantMapper.class, PositionMapper.class})
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "tenant.id", target = "tenantId")
    @Mapping(source = "position.id", target = "positionId")
    EmployeeDTO toDto(Employee employee);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "equipmentRequests", ignore = true)
    @Mapping(target = "removeEquipmentRequests", ignore = true)
    @Mapping(target = "problemReports", ignore = true)
    @Mapping(target = "removeProblemReports", ignore = true)
    @Mapping(target = "equipmentServicings", ignore = true)
    @Mapping(target = "removeEquipmentServicing", ignore = true)
    @Mapping(target = "equipmentEmployees", ignore = true)
    @Mapping(target = "removeEquipmentEmployees", ignore = true)
    @Mapping(source = "tenantId", target = "tenant")
    @Mapping(source = "positionId", target = "position")
    Employee toEntity(EmployeeDTO employeeDTO);

    default Employee fromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }
}
