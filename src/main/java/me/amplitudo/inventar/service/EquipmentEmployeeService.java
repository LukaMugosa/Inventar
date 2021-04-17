package me.amplitudo.inventar.service;

import me.amplitudo.inventar.domain.EquipmentEmployee;
import me.amplitudo.inventar.repository.EquipmentEmployeeRepository;
import me.amplitudo.inventar.service.dto.EquipmentEmployeeDTO;
import me.amplitudo.inventar.service.mapper.EquipmentEmployeeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link EquipmentEmployee}.
 */
@Service
@Transactional
public class EquipmentEmployeeService {

    private final Logger log = LoggerFactory.getLogger(EquipmentEmployeeService.class);

    private final EquipmentEmployeeRepository equipmentEmployeeRepository;

    private final EquipmentEmployeeMapper equipmentEmployeeMapper;

    public EquipmentEmployeeService(EquipmentEmployeeRepository equipmentEmployeeRepository, EquipmentEmployeeMapper equipmentEmployeeMapper) {
        this.equipmentEmployeeRepository = equipmentEmployeeRepository;
        this.equipmentEmployeeMapper = equipmentEmployeeMapper;
    }

    /**
     * Save a equipmentEmployee.
     *
     * @param equipmentEmployeeDTO the entity to save.
     * @return the persisted entity.
     */
    public EquipmentEmployeeDTO save(EquipmentEmployeeDTO equipmentEmployeeDTO) {
        log.debug("Request to save EquipmentEmployee : {}", equipmentEmployeeDTO);
        EquipmentEmployee equipmentEmployee = equipmentEmployeeMapper.toEntity(equipmentEmployeeDTO);
        equipmentEmployee = equipmentEmployeeRepository.save(equipmentEmployee);
        return equipmentEmployeeMapper.toDto(equipmentEmployee);
    }

    /**
     * Get all the equipmentEmployees.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EquipmentEmployeeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EquipmentEmployees");
        return equipmentEmployeeRepository.findAll(pageable)
            .map(equipmentEmployeeMapper::toDto);
    }


    /**
     * Get one equipmentEmployee by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EquipmentEmployeeDTO> findOne(Long id) {
        log.debug("Request to get EquipmentEmployee : {}", id);
        return equipmentEmployeeRepository.findById(id)
            .map(equipmentEmployeeMapper::toDto);
    }

    /**
     * Delete the equipmentEmployee by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EquipmentEmployee : {}", id);
        equipmentEmployeeRepository.deleteById(id);
    }
}
