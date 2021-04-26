package me.amplitudo.inventar.service;

import me.amplitudo.inventar.domain.EquipmentServicing;
import me.amplitudo.inventar.repository.EquipmentServicingRepository;
import me.amplitudo.inventar.service.dto.EquipmentServicingDTO;
import me.amplitudo.inventar.service.mapper.EquipmentServicingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link EquipmentServicing}.
 */
@Service
@Transactional
public class EquipmentServicingService {

    private final Logger log = LoggerFactory.getLogger(EquipmentServicingService.class);

    private final EquipmentServicingRepository equipmentServicingRepository;

    private final EquipmentServicingMapper equipmentServicingMapper;

    public EquipmentServicingService(EquipmentServicingRepository equipmentServicingRepository, EquipmentServicingMapper equipmentServicingMapper) {
        this.equipmentServicingRepository = equipmentServicingRepository;
        this.equipmentServicingMapper = equipmentServicingMapper;
    }

    /**
     * Save a equipmentServicing.
     *
     * @param equipmentServicingDTO the entity to save.
     * @return the persisted entity.
     */
    public EquipmentServicingDTO save(EquipmentServicingDTO equipmentServicingDTO) {
        log.debug("Request to save EquipmentServicing : {}", equipmentServicingDTO);
        EquipmentServicing equipmentServicing = equipmentServicingMapper.toEntity(equipmentServicingDTO);
        equipmentServicing = equipmentServicingRepository.save(equipmentServicing);
        return equipmentServicingMapper.toDto(equipmentServicing);
    }

    /**
     * Get all the equipmentServicings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EquipmentServicingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EquipmentServicings");
        return equipmentServicingRepository.findAll(pageable)
            .map(equipmentServicingMapper::toDto);
    }


    /**
     * Get one equipmentServicing by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EquipmentServicingDTO> findOne(Long id) {
        log.debug("Request to get EquipmentServicing : {}", id);
        return equipmentServicingRepository.findById(id)
            .map(equipmentServicingMapper::toDto);
    }

    /**
     * Delete the equipmentServicing by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EquipmentServicing : {}", id);
        equipmentServicingRepository.deleteById(id);
    }
}
