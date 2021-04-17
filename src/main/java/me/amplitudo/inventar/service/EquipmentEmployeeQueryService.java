package me.amplitudo.inventar.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import me.amplitudo.inventar.domain.EquipmentEmployee;
import me.amplitudo.inventar.domain.*; // for static metamodels
import me.amplitudo.inventar.repository.EquipmentEmployeeRepository;
import me.amplitudo.inventar.service.dto.EquipmentEmployeeCriteria;
import me.amplitudo.inventar.service.dto.EquipmentEmployeeDTO;
import me.amplitudo.inventar.service.mapper.EquipmentEmployeeMapper;

/**
 * Service for executing complex queries for {@link EquipmentEmployee} entities in the database.
 * The main input is a {@link EquipmentEmployeeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EquipmentEmployeeDTO} or a {@link Page} of {@link EquipmentEmployeeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EquipmentEmployeeQueryService extends QueryService<EquipmentEmployee> {

    private final Logger log = LoggerFactory.getLogger(EquipmentEmployeeQueryService.class);

    private final EquipmentEmployeeRepository equipmentEmployeeRepository;

    private final EquipmentEmployeeMapper equipmentEmployeeMapper;

    public EquipmentEmployeeQueryService(EquipmentEmployeeRepository equipmentEmployeeRepository, EquipmentEmployeeMapper equipmentEmployeeMapper) {
        this.equipmentEmployeeRepository = equipmentEmployeeRepository;
        this.equipmentEmployeeMapper = equipmentEmployeeMapper;
    }

    /**
     * Return a {@link List} of {@link EquipmentEmployeeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EquipmentEmployeeDTO> findByCriteria(EquipmentEmployeeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EquipmentEmployee> specification = createSpecification(criteria);
        return equipmentEmployeeMapper.toDto(equipmentEmployeeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EquipmentEmployeeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EquipmentEmployeeDTO> findByCriteria(EquipmentEmployeeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EquipmentEmployee> specification = createSpecification(criteria);
        return equipmentEmployeeRepository.findAll(specification, page)
            .map(equipmentEmployeeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EquipmentEmployeeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EquipmentEmployee> specification = createSpecification(criteria);
        return equipmentEmployeeRepository.count(specification);
    }

    /**
     * Function to convert {@link EquipmentEmployeeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EquipmentEmployee> createSpecification(EquipmentEmployeeCriteria criteria) {
        Specification<EquipmentEmployee> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EquipmentEmployee_.id));
            }
            if (criteria.getDateOfRent() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateOfRent(), EquipmentEmployee_.dateOfRent));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), EquipmentEmployee_.active));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), EquipmentEmployee_.status));
            }
            if (criteria.getEquipmentId() != null) {
                specification = specification.and(buildSpecification(criteria.getEquipmentId(),
                    root -> root.join(EquipmentEmployee_.equipment, JoinType.LEFT).get(Equipment_.id)));
            }
            if (criteria.getEmployeeId() != null) {
                specification = specification.and(buildSpecification(criteria.getEmployeeId(),
                    root -> root.join(EquipmentEmployee_.employee, JoinType.LEFT).get(Employee_.id)));
            }
        }
        return specification;
    }
}
