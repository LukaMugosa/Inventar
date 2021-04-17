package me.amplitudo.inventar.service.mapper;


import me.amplitudo.inventar.domain.*;
import me.amplitudo.inventar.service.dto.EquipmentServiceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link EquipmentService} and its DTO {@link EquipmentServiceDTO}.
 */
@Mapper(componentModel = "spring", uses = {EmployeeMapper.class, EquipmentMapper.class, RepairerMapper.class})
public interface EquipmentServiceMapper extends EntityMapper<EquipmentServiceDTO, EquipmentService> {

    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "equipment.id", target = "equipmentId")
    @Mapping(source = "repairer.id", target = "repairerId")
    EquipmentServiceDTO toDto(EquipmentService equipmentService);

    @Mapping(source = "employeeId", target = "employee")
    @Mapping(source = "equipmentId", target = "equipment")
    @Mapping(source = "repairerId", target = "repairer")
    EquipmentService toEntity(EquipmentServiceDTO equipmentServiceDTO);

    default EquipmentService fromId(Long id) {
        if (id == null) {
            return null;
        }
        EquipmentService equipmentService = new EquipmentService();
        equipmentService.setId(id);
        return equipmentService;
    }
}
