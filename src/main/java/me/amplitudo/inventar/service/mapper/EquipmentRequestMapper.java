package me.amplitudo.inventar.service.mapper;


import me.amplitudo.inventar.domain.*;
import me.amplitudo.inventar.service.dto.EquipmentRequestDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link EquipmentRequest} and its DTO {@link EquipmentRequestDTO}.
 */
@Mapper(componentModel = "spring", uses = {EquipmentMapper.class, EmployeeMapper.class})
public interface EquipmentRequestMapper extends EntityMapper<EquipmentRequestDTO, EquipmentRequest> {

    @Mapping(source = "equipment.id", target = "equipmentId")
    @Mapping(source = "employee.id", target = "employeeId")
    EquipmentRequestDTO toDto(EquipmentRequest equipmentRequest);

    @Mapping(source = "equipmentId", target = "equipment")
    @Mapping(source = "employeeId", target = "employee")
    EquipmentRequest toEntity(EquipmentRequestDTO equipmentRequestDTO);

    default EquipmentRequest fromId(Long id) {
        if (id == null) {
            return null;
        }
        EquipmentRequest equipmentRequest = new EquipmentRequest();
        equipmentRequest.setId(id);
        return equipmentRequest;
    }
}
