package me.amplitudo.inventar.service;

import me.amplitudo.inventar.domain.ProblemReport;
import me.amplitudo.inventar.repository.ProblemReportRepository;
import me.amplitudo.inventar.service.dto.ProblemReportDTO;
import me.amplitudo.inventar.service.mapper.ProblemReportMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ProblemReport}.
 */
@Service
@Transactional
public class ProblemReportService {

    private final Logger log = LoggerFactory.getLogger(ProblemReportService.class);

    private final ProblemReportRepository problemReportRepository;

    private final ProblemReportMapper problemReportMapper;

    public ProblemReportService(ProblemReportRepository problemReportRepository, ProblemReportMapper problemReportMapper) {
        this.problemReportRepository = problemReportRepository;
        this.problemReportMapper = problemReportMapper;
    }

    /**
     * Save a problemReport.
     *
     * @param problemReportDTO the entity to save.
     * @return the persisted entity.
     */
    public ProblemReportDTO save(ProblemReportDTO problemReportDTO) {
        log.debug("Request to save ProblemReport : {}", problemReportDTO);
        ProblemReport problemReport = problemReportMapper.toEntity(problemReportDTO);
        problemReport = problemReportRepository.save(problemReport);
        return problemReportMapper.toDto(problemReport);
    }

    /**
     * Get all the problemReports.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProblemReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProblemReports");
        return problemReportRepository.findAll(pageable)
            .map(problemReportMapper::toDto);
    }


    /**
     * Get one problemReport by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProblemReportDTO> findOne(Long id) {
        log.debug("Request to get ProblemReport : {}", id);
        return problemReportRepository.findById(id)
            .map(problemReportMapper::toDto);
    }

    /**
     * Delete the problemReport by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProblemReport : {}", id);
        problemReportRepository.deleteById(id);
    }
}
