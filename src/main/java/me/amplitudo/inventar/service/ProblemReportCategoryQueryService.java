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

import me.amplitudo.inventar.domain.ProblemReportCategory;
import me.amplitudo.inventar.domain.*; // for static metamodels
import me.amplitudo.inventar.repository.ProblemReportCategoryRepository;
import me.amplitudo.inventar.service.dto.ProblemReportCategoryCriteria;
import me.amplitudo.inventar.service.dto.ProblemReportCategoryDTO;
import me.amplitudo.inventar.service.mapper.ProblemReportCategoryMapper;

/**
 * Service for executing complex queries for {@link ProblemReportCategory} entities in the database.
 * The main input is a {@link ProblemReportCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProblemReportCategoryDTO} or a {@link Page} of {@link ProblemReportCategoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProblemReportCategoryQueryService extends QueryService<ProblemReportCategory> {

    private final Logger log = LoggerFactory.getLogger(ProblemReportCategoryQueryService.class);

    private final ProblemReportCategoryRepository problemReportCategoryRepository;

    private final ProblemReportCategoryMapper problemReportCategoryMapper;

    public ProblemReportCategoryQueryService(ProblemReportCategoryRepository problemReportCategoryRepository, ProblemReportCategoryMapper problemReportCategoryMapper) {
        this.problemReportCategoryRepository = problemReportCategoryRepository;
        this.problemReportCategoryMapper = problemReportCategoryMapper;
    }

    /**
     * Return a {@link List} of {@link ProblemReportCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProblemReportCategoryDTO> findByCriteria(ProblemReportCategoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProblemReportCategory> specification = createSpecification(criteria);
        return problemReportCategoryMapper.toDto(problemReportCategoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProblemReportCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProblemReportCategoryDTO> findByCriteria(ProblemReportCategoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProblemReportCategory> specification = createSpecification(criteria);
        return problemReportCategoryRepository.findAll(specification, page)
            .map(problemReportCategoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProblemReportCategoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProblemReportCategory> specification = createSpecification(criteria);
        return problemReportCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link ProblemReportCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProblemReportCategory> createSpecification(ProblemReportCategoryCriteria criteria) {
        Specification<ProblemReportCategory> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProblemReportCategory_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ProblemReportCategory_.name));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), ProblemReportCategory_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), ProblemReportCategory_.updatedAt));
            }
            if (criteria.getProblemsId() != null) {
                specification = specification.and(buildSpecification(criteria.getProblemsId(),
                    root -> root.join(ProblemReportCategory_.problems, JoinType.LEFT).get(ProblemReport_.id)));
            }
        }
        return specification;
    }
}
