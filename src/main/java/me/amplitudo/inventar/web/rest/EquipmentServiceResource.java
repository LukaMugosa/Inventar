package me.amplitudo.inventar.web.rest;

import me.amplitudo.inventar.service.EquipmentServiceService;
import me.amplitudo.inventar.web.rest.errors.BadRequestAlertException;
import me.amplitudo.inventar.service.dto.EquipmentServiceDTO;
import me.amplitudo.inventar.service.dto.EquipmentServiceCriteria;
import me.amplitudo.inventar.service.EquipmentServiceQueryService;

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
 * REST controller for managing {@link me.amplitudo.inventar.domain.EquipmentService}.
 */
@RestController
@RequestMapping("/api")
public class EquipmentServiceResource {

    private final Logger log = LoggerFactory.getLogger(EquipmentServiceResource.class);

    private static final String ENTITY_NAME = "equipmentService";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EquipmentServiceService equipmentServiceService;

    private final EquipmentServiceQueryService equipmentServiceQueryService;

    public EquipmentServiceResource(EquipmentServiceService equipmentServiceService, EquipmentServiceQueryService equipmentServiceQueryService) {
        this.equipmentServiceService = equipmentServiceService;
        this.equipmentServiceQueryService = equipmentServiceQueryService;
    }

    /**
     * {@code POST  /equipment-services} : Create a new equipmentService.
     *
     * @param equipmentServiceDTO the equipmentServiceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new equipmentServiceDTO, or with status {@code 400 (Bad Request)} if the equipmentService has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/equipment-services")
    public ResponseEntity<EquipmentServiceDTO> createEquipmentService(@Valid @RequestBody EquipmentServiceDTO equipmentServiceDTO) throws URISyntaxException {
        log.debug("REST request to save EquipmentService : {}", equipmentServiceDTO);
        if (equipmentServiceDTO.getId() != null) {
            throw new BadRequestAlertException("A new equipmentService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EquipmentServiceDTO result = equipmentServiceService.save(equipmentServiceDTO);
        return ResponseEntity.created(new URI("/api/equipment-services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /equipment-services} : Updates an existing equipmentService.
     *
     * @param equipmentServiceDTO the equipmentServiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated equipmentServiceDTO,
     * or with status {@code 400 (Bad Request)} if the equipmentServiceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the equipmentServiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/equipment-services")
    public ResponseEntity<EquipmentServiceDTO> updateEquipmentService(@Valid @RequestBody EquipmentServiceDTO equipmentServiceDTO) throws URISyntaxException {
        log.debug("REST request to update EquipmentService : {}", equipmentServiceDTO);
        if (equipmentServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EquipmentServiceDTO result = equipmentServiceService.save(equipmentServiceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, equipmentServiceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /equipment-services} : get all the equipmentServices.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of equipmentServices in body.
     */
    @GetMapping("/equipment-services")
    public ResponseEntity<List<EquipmentServiceDTO>> getAllEquipmentServices(EquipmentServiceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get EquipmentServices by criteria: {}", criteria);
        Page<EquipmentServiceDTO> page = equipmentServiceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /equipment-services/count} : count all the equipmentServices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/equipment-services/count")
    public ResponseEntity<Long> countEquipmentServices(EquipmentServiceCriteria criteria) {
        log.debug("REST request to count EquipmentServices by criteria: {}", criteria);
        return ResponseEntity.ok().body(equipmentServiceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /equipment-services/:id} : get the "id" equipmentService.
     *
     * @param id the id of the equipmentServiceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the equipmentServiceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/equipment-services/{id}")
    public ResponseEntity<EquipmentServiceDTO> getEquipmentService(@PathVariable Long id) {
        log.debug("REST request to get EquipmentService : {}", id);
        Optional<EquipmentServiceDTO> equipmentServiceDTO = equipmentServiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(equipmentServiceDTO);
    }

    /**
     * {@code DELETE  /equipment-services/:id} : delete the "id" equipmentService.
     *
     * @param id the id of the equipmentServiceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/equipment-services/{id}")
    public ResponseEntity<Void> deleteEquipmentService(@PathVariable Long id) {
        log.debug("REST request to delete EquipmentService : {}", id);
        equipmentServiceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
