package me.amplitudo.inventar.web.rest;

import me.amplitudo.inventar.service.EquipmentServicingService;
import me.amplitudo.inventar.web.rest.errors.BadRequestAlertException;
import me.amplitudo.inventar.service.dto.EquipmentServicingDTO;
import me.amplitudo.inventar.service.dto.EquipmentServicingCriteria;
import me.amplitudo.inventar.service.EquipmentServicingQueryService;

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
 * REST controller for managing {@link me.amplitudo.inventar.domain.EquipmentServicing}.
 */
@RestController
@RequestMapping("/api")
public class EquipmentServicingResource {

    private final Logger log = LoggerFactory.getLogger(EquipmentServicingResource.class);

    private static final String ENTITY_NAME = "equipmentServicing";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EquipmentServicingService equipmentServicingService;

    private final EquipmentServicingQueryService equipmentServicingQueryService;

    public EquipmentServicingResource(EquipmentServicingService equipmentServicingService, EquipmentServicingQueryService equipmentServicingQueryService) {
        this.equipmentServicingService = equipmentServicingService;
        this.equipmentServicingQueryService = equipmentServicingQueryService;
    }

    /**
     * {@code POST  /equipment-servicings} : Create a new equipmentServicing.
     *
     * @param equipmentServicingDTO the equipmentServicingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new equipmentServicingDTO, or with status {@code 400 (Bad Request)} if the equipmentServicing has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/equipment-servicings")
    public ResponseEntity<EquipmentServicingDTO> createEquipmentServicing(@Valid @RequestBody EquipmentServicingDTO equipmentServicingDTO) throws URISyntaxException {
        log.debug("REST request to save EquipmentServicing : {}", equipmentServicingDTO);
        if (equipmentServicingDTO.getId() != null) {
            throw new BadRequestAlertException("A new equipmentServicing cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EquipmentServicingDTO result = equipmentServicingService.save(equipmentServicingDTO);
        return ResponseEntity.created(new URI("/api/equipment-servicings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /equipment-servicings} : Updates an existing equipmentServicing.
     *
     * @param equipmentServicingDTO the equipmentServicingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated equipmentServicingDTO,
     * or with status {@code 400 (Bad Request)} if the equipmentServicingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the equipmentServicingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/equipment-servicings")
    public ResponseEntity<EquipmentServicingDTO> updateEquipmentServicing(@Valid @RequestBody EquipmentServicingDTO equipmentServicingDTO) throws URISyntaxException {
        log.debug("REST request to update EquipmentServicing : {}", equipmentServicingDTO);
        if (equipmentServicingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EquipmentServicingDTO result = equipmentServicingService.save(equipmentServicingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, equipmentServicingDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /equipment-servicings} : get all the equipmentServicings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of equipmentServicings in body.
     */
    @GetMapping("/equipment-servicings")
    public ResponseEntity<List<EquipmentServicingDTO>> getAllEquipmentServicings(EquipmentServicingCriteria criteria, Pageable pageable) {
        log.debug("REST request to get EquipmentServicings by criteria: {}", criteria);
        Page<EquipmentServicingDTO> page = equipmentServicingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /equipment-servicings/count} : count all the equipmentServicings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/equipment-servicings/count")
    public ResponseEntity<Long> countEquipmentServicings(EquipmentServicingCriteria criteria) {
        log.debug("REST request to count EquipmentServicings by criteria: {}", criteria);
        return ResponseEntity.ok().body(equipmentServicingQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /equipment-servicings/:id} : get the "id" equipmentServicing.
     *
     * @param id the id of the equipmentServicingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the equipmentServicingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/equipment-servicings/{id}")
    public ResponseEntity<EquipmentServicingDTO> getEquipmentServicing(@PathVariable Long id) {
        log.debug("REST request to get EquipmentServicing : {}", id);
        Optional<EquipmentServicingDTO> equipmentServicingDTO = equipmentServicingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(equipmentServicingDTO);
    }

    /**
     * {@code DELETE  /equipment-servicings/:id} : delete the "id" equipmentServicing.
     *
     * @param id the id of the equipmentServicingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/equipment-servicings/{id}")
    public ResponseEntity<Void> deleteEquipmentServicing(@PathVariable Long id) {
        log.debug("REST request to delete EquipmentServicing : {}", id);
        equipmentServicingService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
