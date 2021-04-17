package me.amplitudo.inventar.service;

import me.amplitudo.inventar.domain.EquipmentService;
import me.amplitudo.inventar.repository.EquipmentServiceRepository;
import me.amplitudo.inventar.service.dto.EquipmentServiceDTO;
import me.amplitudo.inventar.service.mapper.EquipmentServiceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link EquipmentService}.
 */
@Service
@Transactional
public class EquipmentServiceService {

    private final Logger log = LoggerFactory.getLogger(EquipmentServiceService.class);

    private final EquipmentServiceRepository equipmentServiceRepository;

    private final EquipmentServiceMapper equipmentServiceMapper;

    public EquipmentServiceService(EquipmentServiceRepository equipmentServiceRepository, EquipmentServiceMapper equipmentServiceMapper) {
        this.equipmentServiceRepository = equipmentServiceRepository;
        this.equipmentServiceMapper = equipmentServiceMapper;
    }

    /**
     * Save a equipmentService.
     *
     * @param equipmentServiceDTO the entity to save.
     * @return the persisted entity.
     */
    public EquipmentServiceDTO save(EquipmentServiceDTO equipmentServiceDTO) {
        log.debug("Request to save EquipmentService : {}", equipmentServiceDTO);
        EquipmentService equipmentService = equipmentServiceMapper.toEntity(equipmentServiceDTO);
        equipmentService = equipmentServiceRepository.save(equipmentService);
        return equipmentServiceMapper.toDto(equipmentService);
    }

    /**
     * Get all the equipmentServices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EquipmentServiceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EquipmentServices");
        return equipmentServiceRepository.findAll(pageable)
            .map(equipmentServiceMapper::toDto);
    }


    /**
     * Get one equipmentService by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EquipmentServiceDTO> findOne(Long id) {
        log.debug("Request to get EquipmentService : {}", id);
        return equipmentServiceRepository.findById(id)
            .map(equipmentServiceMapper::toDto);
    }

    /**
     * Delete the equipmentService by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EquipmentService : {}", id);
        equipmentServiceRepository.deleteById(id);
    }
}
