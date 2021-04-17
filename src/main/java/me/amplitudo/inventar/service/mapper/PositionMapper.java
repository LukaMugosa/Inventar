package me.amplitudo.inventar.service.mapper;


import me.amplitudo.inventar.domain.*;
import me.amplitudo.inventar.service.dto.PositionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Position} and its DTO {@link PositionDTO}.
 */
@Mapper(componentModel = "spring", uses = {SectorMapper.class})
public interface PositionMapper extends EntityMapper<PositionDTO, Position> {

    @Mapping(source = "sector.id", target = "sectorId")
    PositionDTO toDto(Position position);

    @Mapping(target = "employees", ignore = true)
    @Mapping(target = "removeEmployees", ignore = true)
    @Mapping(source = "sectorId", target = "sector")
    Position toEntity(PositionDTO positionDTO);

    default Position fromId(Long id) {
        if (id == null) {
            return null;
        }
        Position position = new Position();
        position.setId(id);
        return position;
    }
}
