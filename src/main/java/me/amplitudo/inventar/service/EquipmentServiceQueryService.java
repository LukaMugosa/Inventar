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

import me.amplitudo.inventar.domain.EquipmentService;
import me.amplitudo.inventar.domain.*; // for static metamodels
import me.amplitudo.inventar.repository.EquipmentServiceRepository;
import me.amplitudo.inventar.service.dto.EquipmentServiceCriteria;
import me.amplitudo.inventar.service.dto.EquipmentServiceDTO;
import me.amplitudo.inventar.service.mapper.EquipmentServiceMapper;

/**
 * Service for executing complex queries for {@link EquipmentService} entities in the database.
 * The main input is a {@link EquipmentServiceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EquipmentServiceDTO} or a {@link Page} of {@link EquipmentServiceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EquipmentServiceQueryService extends QueryService<EquipmentService> {

    private final Logger log = LoggerFactory.getLogger(EquipmentServiceQueryService.class);

    private final EquipmentServiceRepository equipmentServiceRepository;

    private final EquipmentServiceMapper equipmentServiceMapper;

    public EquipmentServiceQueryService(EquipmentServiceRepository equipmentServiceRepository, EquipmentServiceMapper equipmentServiceMapper) {
        this.equipmentServiceRepository = equipmentServiceRepository;
        this.equipmentServiceMapper = equipmentServiceMapper;
    }

    /**
     * Return a {@link List} of {@link EquipmentServiceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EquipmentServiceDTO> findByCriteria(EquipmentServiceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EquipmentService> specification = createSpecification(criteria);
        return equipmentServiceMapper.toDto(equipmentServiceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EquipmentServiceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EquipmentServiceDTO> findByCriteria(EquipmentServiceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EquipmentService> specification = createSpecification(criteria);
        return equipmentServiceRepository.findAll(specification, page)
            .map(equipmentServiceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EquipmentServiceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EquipmentService> specification = createSpecification(criteria);
        return equipmentServiceRepository.count(specification);
    }

    /**
     * Function to convert {@link EquipmentServiceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EquipmentService> createSpecification(EquipmentServiceCriteria criteria) {
        Specification<EquipmentService> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EquipmentService_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), EquipmentService_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), EquipmentService_.description));
            }
            if (criteria.getDateSent() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateSent(), EquipmentService_.dateSent));
            }
            if (criteria.getEta() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEta(), EquipmentService_.eta));
            }
            if (criteria.getEmployeeId() != null) {
                specification = specification.and(buildSpecification(criteria.getEmployeeId(),
                    root -> root.join(EquipmentService_.employee, JoinType.LEFT).get(Employee_.id)));
            }
            if (criteria.getEquipmentId() != null) {
                specification = specification.and(buildSpecification(criteria.getEquipmentId(),
                    root -> root.join(EquipmentService_.equipment, JoinType.LEFT).get(Equipment_.id)));
            }
            if (criteria.getRepairerId() != null) {
                specification = specification.and(buildSpecification(criteria.getRepairerId(),
                    root -> root.join(EquipmentService_.repairer, JoinType.LEFT).get(Repairer_.id)));
            }
        }
        return specification;
    }
}
