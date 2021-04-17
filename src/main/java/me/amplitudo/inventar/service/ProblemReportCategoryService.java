package me.amplitudo.inventar.service;

import me.amplitudo.inventar.domain.ProblemReportCategory;
import me.amplitudo.inventar.repository.ProblemReportCategoryRepository;
import me.amplitudo.inventar.service.dto.ProblemReportCategoryDTO;
import me.amplitudo.inventar.service.mapper.ProblemReportCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ProblemReportCategory}.
 */
@Service
@Transactional
public class ProblemReportCategoryService {

    private final Logger log = LoggerFactory.getLogger(ProblemReportCategoryService.class);

    private final ProblemReportCategoryRepository problemReportCategoryRepository;

    private final ProblemReportCategoryMapper problemReportCategoryMapper;

    public ProblemReportCategoryService(ProblemReportCategoryRepository problemReportCategoryRepository, ProblemReportCategoryMapper problemReportCategoryMapper) {
        this.problemReportCategoryRepository = problemReportCategoryRepository;
        this.problemReportCategoryMapper = problemReportCategoryMapper;
    }

    /**
     * Save a problemReportCategory.
     *
     * @param problemReportCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public ProblemReportCategoryDTO save(ProblemReportCategoryDTO problemReportCategoryDTO) {
        log.debug("Request to save ProblemReportCategory : {}", problemReportCategoryDTO);
        ProblemReportCategory problemReportCategory = problemReportCategoryMapper.toEntity(problemReportCategoryDTO);
        problemReportCategory = problemReportCategoryRepository.save(problemReportCategory);
        return problemReportCategoryMapper.toDto(problemReportCategory);
    }

    /**
     * Get all the problemReportCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProblemReportCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProblemReportCategories");
        return problemReportCategoryRepository.findAll(pageable)
            .map(problemReportCategoryMapper::toDto);
    }


    /**
     * Get one problemReportCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProblemReportCategoryDTO> findOne(Long id) {
        log.debug("Request to get ProblemReportCategory : {}", id);
        return problemReportCategoryRepository.findById(id)
            .map(problemReportCategoryMapper::toDto);
    }

    /**
     * Delete the problemReportCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProblemReportCategory : {}", id);
        problemReportCategoryRepository.deleteById(id);
    }
}
