package me.amplitudo.inventar.service;

import me.amplitudo.inventar.domain.EquipmentRequest;
import me.amplitudo.inventar.repository.EquipmentRequestRepository;
import me.amplitudo.inventar.service.dto.EquipmentRequestDTO;
import me.amplitudo.inventar.service.mapper.EquipmentRequestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link EquipmentRequest}.
 */
@Service
@Transactional
public class EquipmentRequestService {

    private final Logger log = LoggerFactory.getLogger(EquipmentRequestService.class);

    private final EquipmentRequestRepository equipmentRequestRepository;

    private final EquipmentRequestMapper equipmentRequestMapper;

    public EquipmentRequestService(EquipmentRequestRepository equipmentRequestRepository, EquipmentRequestMapper equipmentRequestMapper) {
        this.equipmentRequestRepository = equipmentRequestRepository;
        this.equipmentRequestMapper = equipmentRequestMapper;
    }

    /**
     * Save a equipmentRequest.
     *
     * @param equipmentRequestDTO the entity to save.
     * @return the persisted entity.
     */
    public EquipmentRequestDTO save(EquipmentRequestDTO equipmentRequestDTO) {
        log.debug("Request to save EquipmentRequest : {}", equipmentRequestDTO);
        EquipmentRequest equipmentRequest = equipmentRequestMapper.toEntity(equipmentRequestDTO);
        equipmentRequest = equipmentRequestRepository.save(equipmentRequest);
        return equipmentRequestMapper.toDto(equipmentRequest);
    }

    /**
     * Get all the equipmentRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EquipmentRequestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EquipmentRequests");
        return equipmentRequestRepository.findAll(pageable)
            .map(equipmentRequestMapper::toDto);
    }


    /**
     * Get one equipmentRequest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EquipmentRequestDTO> findOne(Long id) {
        log.debug("Request to get EquipmentRequest : {}", id);
        return equipmentRequestRepository.findById(id)
            .map(equipmentRequestMapper::toDto);
    }

    /**
     * Delete the equipmentRequest by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EquipmentRequest : {}", id);
        equipmentRequestRepository.deleteById(id);
    }
}
