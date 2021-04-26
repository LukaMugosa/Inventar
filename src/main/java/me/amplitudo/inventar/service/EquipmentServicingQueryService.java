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

import me.amplitudo.inventar.domain.EquipmentServicing;
import me.amplitudo.inventar.domain.*; // for static metamodels
import me.amplitudo.inventar.repository.EquipmentServicingRepository;
import me.amplitudo.inventar.service.dto.EquipmentServicingCriteria;
import me.amplitudo.inventar.service.dto.EquipmentServicingDTO;
import me.amplitudo.inventar.service.mapper.EquipmentServicingMapper;

/**
 * Service for executing complex queries for {@link EquipmentServicing} entities in the database.
 * The main input is a {@link EquipmentServicingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EquipmentServicingDTO} or a {@link Page} of {@link EquipmentServicingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EquipmentServicingQueryService extends QueryService<EquipmentServicing> {

    private final Logger log = LoggerFactory.getLogger(EquipmentServicingQueryService.class);

    private final EquipmentServicingRepository equipmentServicingRepository;

    private final EquipmentServicingMapper equipmentServicingMapper;

    public EquipmentServicingQueryService(EquipmentServicingRepository equipmentServicingRepository, EquipmentServicingMapper equipmentServicingMapper) {
        this.equipmentServicingRepository = equipmentServicingRepository;
        this.equipmentServicingMapper = equipmentServicingMapper;
    }

    /**
     * Return a {@link List} of {@link EquipmentServicingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EquipmentServicingDTO> findByCriteria(EquipmentServicingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EquipmentServicing> specification = createSpecification(criteria);
        return equipmentServicingMapper.toDto(equipmentServicingRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EquipmentServicingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EquipmentServicingDTO> findByCriteria(EquipmentServicingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EquipmentServicing> specification = createSpecification(criteria);
        return equipmentServicingRepository.findAll(specification, page)
            .map(equipmentServicingMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EquipmentServicingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EquipmentServicing> specification = createSpecification(criteria);
        return equipmentServicingRepository.count(specification);
    }

    /**
     * Function to convert {@link EquipmentServicingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EquipmentServicing> createSpecification(EquipmentServicingCriteria criteria) {
        Specification<EquipmentServicing> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EquipmentServicing_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), EquipmentServicing_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), EquipmentServicing_.description));
            }
            if (criteria.getDateSent() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateSent(), EquipmentServicing_.dateSent));
            }
            if (criteria.getEta() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEta(), EquipmentServicing_.eta));
            }
            if (criteria.getEmployeeId() != null) {
                specification = specification.and(buildSpecification(criteria.getEmployeeId(),
                    root -> root.join(EquipmentServicing_.employee, JoinType.LEFT).get(Employee_.id)));
            }
            if (criteria.getEquipmentId() != null) {
                specification = specification.and(buildSpecification(criteria.getEquipmentId(),
                    root -> root.join(EquipmentServicing_.equipment, JoinType.LEFT).get(Equipment_.id)));
            }
            if (criteria.getRepairerId() != null) {
                specification = specification.and(buildSpecification(criteria.getRepairerId(),
                    root -> root.join(EquipmentServicing_.repairer, JoinType.LEFT).get(Repairer_.id)));
            }
        }
        return specification;
    }
}
