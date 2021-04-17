package me.amplitudo.inventar.service.mapper;


import me.amplitudo.inventar.domain.*;
import me.amplitudo.inventar.service.dto.EquipmentEmployeeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link EquipmentEmployee} and its DTO {@link EquipmentEmployeeDTO}.
 */
@Mapper(componentModel = "spring", uses = {EquipmentMapper.class, EmployeeMapper.class})
public interface EquipmentEmployeeMapper extends EntityMapper<EquipmentEmployeeDTO, EquipmentEmployee> {

    @Mapping(source = "equipment.id", target = "equipmentId")
    @Mapping(source = "employee.id", target = "employeeId")
    EquipmentEmployeeDTO toDto(EquipmentEmployee equipmentEmployee);

    @Mapping(source = "equipmentId", target = "equipment")
    @Mapping(source = "employeeId", target = "employee")
    EquipmentEmployee toEntity(EquipmentEmployeeDTO equipmentEmployeeDTO);

    default EquipmentEmployee fromId(Long id) {
        if (id == null) {
            return null;
        }
        EquipmentEmployee equipmentEmployee = new EquipmentEmployee();
        equipmentEmployee.setId(id);
        return equipmentEmployee;
    }
}
