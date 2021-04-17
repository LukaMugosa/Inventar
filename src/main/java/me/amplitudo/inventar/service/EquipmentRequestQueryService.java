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

import me.amplitudo.inventar.domain.EquipmentRequest;
import me.amplitudo.inventar.domain.*; // for static metamodels
import me.amplitudo.inventar.repository.EquipmentRequestRepository;
import me.amplitudo.inventar.service.dto.EquipmentRequestCriteria;
import me.amplitudo.inventar.service.dto.EquipmentRequestDTO;
import me.amplitudo.inventar.service.mapper.EquipmentRequestMapper;

/**
 * Service for executing complex queries for {@link EquipmentRequest} entities in the database.
 * The main input is a {@link EquipmentRequestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EquipmentRequestDTO} or a {@link Page} of {@link EquipmentRequestDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EquipmentRequestQueryService extends QueryService<EquipmentRequest> {

    private final Logger log = LoggerFactory.getLogger(EquipmentRequestQueryService.class);

    private final EquipmentRequestRepository equipmentRequestRepository;

    private final EquipmentRequestMapper equipmentRequestMapper;

    public EquipmentRequestQueryService(EquipmentRequestRepository equipmentRequestRepository, EquipmentRequestMapper equipmentRequestMapper) {
        this.equipmentRequestRepository = equipmentRequestRepository;
        this.equipmentRequestMapper = equipmentRequestMapper;
    }

    /**
     * Return a {@link List} of {@link EquipmentRequestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EquipmentRequestDTO> findByCriteria(EquipmentRequestCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EquipmentRequest> specification = createSpecification(criteria);
        return equipmentRequestMapper.toDto(equipmentRequestRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EquipmentRequestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EquipmentRequestDTO> findByCriteria(EquipmentRequestCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EquipmentRequest> specification = createSpecification(criteria);
        return equipmentRequestRepository.findAll(specification, page)
            .map(equipmentRequestMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EquipmentRequestCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EquipmentRequest> specification = createSpecification(criteria);
        return equipmentRequestRepository.count(specification);
    }

    /**
     * Function to convert {@link EquipmentRequestCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EquipmentRequest> createSpecification(EquipmentRequestCriteria criteria) {
        Specification<EquipmentRequest> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EquipmentRequest_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), EquipmentRequest_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), EquipmentRequest_.description));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), EquipmentRequest_.status));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EquipmentRequest_.createdAt));
            }
            if (criteria.getEquipmentId() != null) {
                specification = specification.and(buildSpecification(criteria.getEquipmentId(),
                    root -> root.join(EquipmentRequest_.equipment, JoinType.LEFT).get(Equipment_.id)));
            }
            if (criteria.getEmployeeId() != null) {
                specification = specification.and(buildSpecification(criteria.getEmployeeId(),
                    root -> root.join(EquipmentRequest_.employee, JoinType.LEFT).get(Employee_.id)));
            }
        }
        return specification;
    }
}
