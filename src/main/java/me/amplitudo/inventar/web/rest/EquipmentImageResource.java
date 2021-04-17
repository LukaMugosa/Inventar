package me.amplitudo.inventar.web.rest;

import me.amplitudo.inventar.service.EquipmentImageService;
import me.amplitudo.inventar.web.rest.errors.BadRequestAlertException;
import me.amplitudo.inventar.service.dto.EquipmentImageDTO;
import me.amplitudo.inventar.service.dto.EquipmentImageCriteria;
import me.amplitudo.inventar.service.EquipmentImageQueryService;

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
 * REST controller for managing {@link me.amplitudo.inventar.domain.EquipmentImage}.
 */
@RestController
@RequestMapping("/api")
public class EquipmentImageResource {

    private final Logger log = LoggerFactory.getLogger(EquipmentImageResource.class);

    private static final String ENTITY_NAME = "equipmentImage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EquipmentImageService equipmentImageService;

    private final EquipmentImageQueryService equipmentImageQueryService;

    public EquipmentImageResource(EquipmentImageService equipmentImageService, EquipmentImageQueryService equipmentImageQueryService) {
        this.equipmentImageService = equipmentImageService;
        this.equipmentImageQueryService = equipmentImageQueryService;
    }

    /**
     * {@code POST  /equipment-images} : Create a new equipmentImage.
     *
     * @param equipmentImageDTO the equipmentImageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new equipmentImageDTO, or with status {@code 400 (Bad Request)} if the equipmentImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/equipment-images")
    public ResponseEntity<EquipmentImageDTO> createEquipmentImage(@Valid @RequestBody EquipmentImageDTO equipmentImageDTO) throws URISyntaxException {
        log.debug("REST request to save EquipmentImage : {}", equipmentImageDTO);
        if (equipmentImageDTO.getId() != null) {
            throw new BadRequestAlertException("A new equipmentImage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EquipmentImageDTO result = equipmentImageService.save(equipmentImageDTO);
        return ResponseEntity.created(new URI("/api/equipment-images/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /equipment-images} : Updates an existing equipmentImage.
     *
     * @param equipmentImageDTO the equipmentImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated equipmentImageDTO,
     * or with status {@code 400 (Bad Request)} if the equipmentImageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the equipmentImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/equipment-images")
    public ResponseEntity<EquipmentImageDTO> updateEquipmentImage(@Valid @RequestBody EquipmentImageDTO equipmentImageDTO) throws URISyntaxException {
        log.debug("REST request to update EquipmentImage : {}", equipmentImageDTO);
        if (equipmentImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EquipmentImageDTO result = equipmentImageService.save(equipmentImageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, equipmentImageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /equipment-images} : get all the equipmentImages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of equipmentImages in body.
     */
    @GetMapping("/equipment-images")
    public ResponseEntity<List<EquipmentImageDTO>> getAllEquipmentImages(EquipmentImageCriteria criteria, Pageable pageable) {
        log.debug("REST request to get EquipmentImages by criteria: {}", criteria);
        Page<EquipmentImageDTO> page = equipmentImageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /equipment-images/count} : count all the equipmentImages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/equipment-images/count")
    public ResponseEntity<Long> countEquipmentImages(EquipmentImageCriteria criteria) {
        log.debug("REST request to count EquipmentImages by criteria: {}", criteria);
        return ResponseEntity.ok().body(equipmentImageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /equipment-images/:id} : get the "id" equipmentImage.
     *
     * @param id the id of the equipmentImageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the equipmentImageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/equipment-images/{id}")
    public ResponseEntity<EquipmentImageDTO> getEquipmentImage(@PathVariable Long id) {
        log.debug("REST request to get EquipmentImage : {}", id);
        Optional<EquipmentImageDTO> equipmentImageDTO = equipmentImageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(equipmentImageDTO);
    }

    /**
     * {@code DELETE  /equipment-images/:id} : delete the "id" equipmentImage.
     *
     * @param id the id of the equipmentImageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/equipment-images/{id}")
    public ResponseEntity<Void> deleteEquipmentImage(@PathVariable Long id) {
        log.debug("REST request to delete EquipmentImage : {}", id);
        equipmentImageService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
