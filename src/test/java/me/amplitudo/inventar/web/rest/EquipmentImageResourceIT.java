package me.amplitudo.inventar.web.rest;

import me.amplitudo.inventar.InventarApp;
import me.amplitudo.inventar.domain.EquipmentImage;
import me.amplitudo.inventar.domain.Equipment;
import me.amplitudo.inventar.repository.EquipmentImageRepository;
import me.amplitudo.inventar.service.EquipmentImageService;
import me.amplitudo.inventar.service.dto.EquipmentImageDTO;
import me.amplitudo.inventar.service.mapper.EquipmentImageMapper;
import me.amplitudo.inventar.service.dto.EquipmentImageCriteria;
import me.amplitudo.inventar.service.EquipmentImageQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link EquipmentImageResource} REST controller.
 */
@SpringBootTest(classes = InventarApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class EquipmentImageResourceIT {

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    @Autowired
    private EquipmentImageRepository equipmentImageRepository;

    @Autowired
    private EquipmentImageMapper equipmentImageMapper;

    @Autowired
    private EquipmentImageService equipmentImageService;

    @Autowired
    private EquipmentImageQueryService equipmentImageQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEquipmentImageMockMvc;

    private EquipmentImage equipmentImage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EquipmentImage createEntity(EntityManager em) {
        EquipmentImage equipmentImage = new EquipmentImage()
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return equipmentImage;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EquipmentImage createUpdatedEntity(EntityManager em) {
        EquipmentImage equipmentImage = new EquipmentImage()
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return equipmentImage;
    }

    @BeforeEach
    public void initTest() {
        equipmentImage = createEntity(em);
    }

    @Test
    @Transactional
    public void createEquipmentImage() throws Exception {
        int databaseSizeBeforeCreate = equipmentImageRepository.findAll().size();
        // Create the EquipmentImage
        EquipmentImageDTO equipmentImageDTO = equipmentImageMapper.toDto(equipmentImage);
        restEquipmentImageMockMvc.perform(post("/api/equipment-images")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentImageDTO)))
            .andExpect(status().isCreated());

        // Validate the EquipmentImage in the database
        List<EquipmentImage> equipmentImageList = equipmentImageRepository.findAll();
        assertThat(equipmentImageList).hasSize(databaseSizeBeforeCreate + 1);
        EquipmentImage testEquipmentImage = equipmentImageList.get(equipmentImageList.size() - 1);
        assertThat(testEquipmentImage.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testEquipmentImage.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createEquipmentImageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = equipmentImageRepository.findAll().size();

        // Create the EquipmentImage with an existing ID
        equipmentImage.setId(1L);
        EquipmentImageDTO equipmentImageDTO = equipmentImageMapper.toDto(equipmentImage);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEquipmentImageMockMvc.perform(post("/api/equipment-images")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentImageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EquipmentImage in the database
        List<EquipmentImage> equipmentImageList = equipmentImageRepository.findAll();
        assertThat(equipmentImageList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllEquipmentImages() throws Exception {
        // Initialize the database
        equipmentImageRepository.saveAndFlush(equipmentImage);

        // Get all the equipmentImageList
        restEquipmentImageMockMvc.perform(get("/api/equipment-images?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipmentImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }
    
    @Test
    @Transactional
    public void getEquipmentImage() throws Exception {
        // Initialize the database
        equipmentImageRepository.saveAndFlush(equipmentImage);

        // Get the equipmentImage
        restEquipmentImageMockMvc.perform(get("/api/equipment-images/{id}", equipmentImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(equipmentImage.getId().intValue()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }


    @Test
    @Transactional
    public void getEquipmentImagesByIdFiltering() throws Exception {
        // Initialize the database
        equipmentImageRepository.saveAndFlush(equipmentImage);

        Long id = equipmentImage.getId();

        defaultEquipmentImageShouldBeFound("id.equals=" + id);
        defaultEquipmentImageShouldNotBeFound("id.notEquals=" + id);

        defaultEquipmentImageShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEquipmentImageShouldNotBeFound("id.greaterThan=" + id);

        defaultEquipmentImageShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEquipmentImageShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEquipmentImagesByEquipmentIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentImageRepository.saveAndFlush(equipmentImage);
        Equipment equipment = EquipmentResourceIT.createEntity(em);
        em.persist(equipment);
        em.flush();
        equipmentImage.setEquipment(equipment);
        equipmentImageRepository.saveAndFlush(equipmentImage);
        Long equipmentId = equipment.getId();

        // Get all the equipmentImageList where equipment equals to equipmentId
        defaultEquipmentImageShouldBeFound("equipmentId.equals=" + equipmentId);

        // Get all the equipmentImageList where equipment equals to equipmentId + 1
        defaultEquipmentImageShouldNotBeFound("equipmentId.equals=" + (equipmentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEquipmentImageShouldBeFound(String filter) throws Exception {
        restEquipmentImageMockMvc.perform(get("/api/equipment-images?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipmentImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));

        // Check, that the count call also returns 1
        restEquipmentImageMockMvc.perform(get("/api/equipment-images/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEquipmentImageShouldNotBeFound(String filter) throws Exception {
        restEquipmentImageMockMvc.perform(get("/api/equipment-images?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEquipmentImageMockMvc.perform(get("/api/equipment-images/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingEquipmentImage() throws Exception {
        // Get the equipmentImage
        restEquipmentImageMockMvc.perform(get("/api/equipment-images/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEquipmentImage() throws Exception {
        // Initialize the database
        equipmentImageRepository.saveAndFlush(equipmentImage);

        int databaseSizeBeforeUpdate = equipmentImageRepository.findAll().size();

        // Update the equipmentImage
        EquipmentImage updatedEquipmentImage = equipmentImageRepository.findById(equipmentImage.getId()).get();
        // Disconnect from session so that the updates on updatedEquipmentImage are not directly saved in db
        em.detach(updatedEquipmentImage);
        updatedEquipmentImage
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        EquipmentImageDTO equipmentImageDTO = equipmentImageMapper.toDto(updatedEquipmentImage);

        restEquipmentImageMockMvc.perform(put("/api/equipment-images")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentImageDTO)))
            .andExpect(status().isOk());

        // Validate the EquipmentImage in the database
        List<EquipmentImage> equipmentImageList = equipmentImageRepository.findAll();
        assertThat(equipmentImageList).hasSize(databaseSizeBeforeUpdate);
        EquipmentImage testEquipmentImage = equipmentImageList.get(equipmentImageList.size() - 1);
        assertThat(testEquipmentImage.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testEquipmentImage.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingEquipmentImage() throws Exception {
        int databaseSizeBeforeUpdate = equipmentImageRepository.findAll().size();

        // Create the EquipmentImage
        EquipmentImageDTO equipmentImageDTO = equipmentImageMapper.toDto(equipmentImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipmentImageMockMvc.perform(put("/api/equipment-images")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentImageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EquipmentImage in the database
        List<EquipmentImage> equipmentImageList = equipmentImageRepository.findAll();
        assertThat(equipmentImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEquipmentImage() throws Exception {
        // Initialize the database
        equipmentImageRepository.saveAndFlush(equipmentImage);

        int databaseSizeBeforeDelete = equipmentImageRepository.findAll().size();

        // Delete the equipmentImage
        restEquipmentImageMockMvc.perform(delete("/api/equipment-images/{id}", equipmentImage.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EquipmentImage> equipmentImageList = equipmentImageRepository.findAll();
        assertThat(equipmentImageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
