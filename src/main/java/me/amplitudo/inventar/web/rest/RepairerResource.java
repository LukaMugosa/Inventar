package me.amplitudo.inventar.web.rest;

import me.amplitudo.inventar.service.RepairerService;
import me.amplitudo.inventar.web.rest.errors.BadRequestAlertException;
import me.amplitudo.inventar.service.dto.RepairerDTO;
import me.amplitudo.inventar.service.dto.RepairerCriteria;
import me.amplitudo.inventar.service.RepairerQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link me.amplitudo.inventar.domain.Repairer}.
 */
@RestController
@RequestMapping("/api")
public class RepairerResource {

    private final Logger log = LoggerFactory.getLogger(RepairerResource.class);

    private static final String ENTITY_NAME = "repairer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RepairerService repairerService;

    private final RepairerQueryService repairerQueryService;

    public RepairerResource(RepairerService repairerService, RepairerQueryService repairerQueryService) {
        this.repairerService = repairerService;
        this.repairerQueryService = repairerQueryService;
    }

    /**
     * {@code POST  /repairers} : Create a new repairer.
     *
     * @param repairerDTO the repairerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new repairerDTO, or with status {@code 400 (Bad Request)} if the repairer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/repairers")
    public ResponseEntity<RepairerDTO> createRepairer(@Valid @RequestBody RepairerDTO repairerDTO) throws URISyntaxException {
        log.debug("REST request to save Repairer : {}", repairerDTO);
        if (repairerDTO.getId() != null) {
            throw new BadRequestAlertException("A new repairer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RepairerDTO result = repairerService.save(repairerDTO);
        return ResponseEntity.created(new URI("/api/repairers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /repairers} : Updates an existing repairer.
     *
     * @param repairerDTO the repairerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated repairerDTO,
     * or with status {@code 400 (Bad Request)} if the repairerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the repairerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/repairers")
    public ResponseEntity<RepairerDTO> updateRepairer(@Valid @RequestBody RepairerDTO repairerDTO) throws URISyntaxException {
        log.debug("REST request to update Repairer : {}", repairerDTO);
        if (repairerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RepairerDTO result = repairerService.save(repairerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, repairerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /repairers} : get all the repairers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of repairers in body.
     */
    @GetMapping("/repairers")
    public ResponseEntity<List<RepairerDTO>> getAllRepairers(RepairerCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Repairers by criteria: {}", criteria);
        Page<RepairerDTO> page = repairerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /repairers/count} : count all the repairers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/repairers/count")
    public ResponseEntity<Long> countRepairers(RepairerCriteria criteria) {
        log.debug("REST request to count Repairers by criteria: {}", criteria);
        return ResponseEntity.ok().body(repairerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /repairers/:id} : get the "id" repairer.
     *
     * @param id the id of the repairerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the repairerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/repairers/{id}")
    public ResponseEntity<RepairerDTO> getRepairer(@PathVariable Long id) {
        log.debug("REST request to get Repairer : {}", id);
        Optional<RepairerDTO> repairerDTO = repairerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(repairerDTO);
    }

    /**
     * {@code DELETE  /repairers/:id} : delete the "id" repairer.
     *
     * @param id the id of the repairerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/repairers/{id}")
    public ResponseEntity<Void> deleteRepairer(@PathVariable Long id) {
        log.debug("REST request to delete Repairer : {}", id);
        repairerService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
