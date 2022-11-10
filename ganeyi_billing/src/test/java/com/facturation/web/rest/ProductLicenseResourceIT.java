package com.facturation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.facturation.IntegrationTest;
import com.facturation.domain.ProductLicense;
import com.facturation.repository.ProductLicenseRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link ProductLicenseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductLicenseResourceIT {

    private static final String DEFAULT_ACCESS_KEY = "AAAAAAAAAA";
    private static final String UPDATED_ACCESS_KEY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/product-licenses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ProductLicenseRepository productLicenseRepository;

    @Autowired
    private MockMvc restProductLicenseMockMvc;

    private ProductLicense productLicense;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductLicense createEntity() {
        ProductLicense productLicense = new ProductLicense()
            .accessKey(DEFAULT_ACCESS_KEY)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .isActive(DEFAULT_IS_ACTIVE);
        return productLicense;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductLicense createUpdatedEntity() {
        ProductLicense productLicense = new ProductLicense()
            .accessKey(UPDATED_ACCESS_KEY)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isActive(UPDATED_IS_ACTIVE);
        return productLicense;
    }

    @BeforeEach
    public void initTest() {
        productLicenseRepository.deleteAll();
        productLicense = createEntity();
    }

    @Test
    void createProductLicense() throws Exception {
        int databaseSizeBeforeCreate = productLicenseRepository.findAll().size();
        // Create the ProductLicense
        restProductLicenseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productLicense))
            )
            .andExpect(status().isCreated());

        // Validate the ProductLicense in the database
        List<ProductLicense> productLicenseList = productLicenseRepository.findAll();
        assertThat(productLicenseList).hasSize(databaseSizeBeforeCreate + 1);
        ProductLicense testProductLicense = productLicenseList.get(productLicenseList.size() - 1);
        assertThat(testProductLicense.getAccessKey()).isEqualTo(DEFAULT_ACCESS_KEY);
        assertThat(testProductLicense.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testProductLicense.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testProductLicense.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    void createProductLicenseWithExistingId() throws Exception {
        // Create the ProductLicense with an existing ID
        productLicense.setId("existing_id");

        int databaseSizeBeforeCreate = productLicenseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductLicenseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productLicense))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductLicense in the database
        List<ProductLicense> productLicenseList = productLicenseRepository.findAll();
        assertThat(productLicenseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllProductLicenses() throws Exception {
        // Initialize the database
        productLicenseRepository.save(productLicense);

        // Get all the productLicenseList
        restProductLicenseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productLicense.getId())))
            .andExpect(jsonPath("$.[*].accessKey").value(hasItem(DEFAULT_ACCESS_KEY)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    void getProductLicense() throws Exception {
        // Initialize the database
        productLicenseRepository.save(productLicense);

        // Get the productLicense
        restProductLicenseMockMvc
            .perform(get(ENTITY_API_URL_ID, productLicense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productLicense.getId()))
            .andExpect(jsonPath("$.accessKey").value(DEFAULT_ACCESS_KEY))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    void getNonExistingProductLicense() throws Exception {
        // Get the productLicense
        restProductLicenseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingProductLicense() throws Exception {
        // Initialize the database
        productLicenseRepository.save(productLicense);

        int databaseSizeBeforeUpdate = productLicenseRepository.findAll().size();

        // Update the productLicense
        ProductLicense updatedProductLicense = productLicenseRepository.findById(productLicense.getId()).get();
        updatedProductLicense
            .accessKey(UPDATED_ACCESS_KEY)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isActive(UPDATED_IS_ACTIVE);

        restProductLicenseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductLicense.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProductLicense))
            )
            .andExpect(status().isOk());

        // Validate the ProductLicense in the database
        List<ProductLicense> productLicenseList = productLicenseRepository.findAll();
        assertThat(productLicenseList).hasSize(databaseSizeBeforeUpdate);
        ProductLicense testProductLicense = productLicenseList.get(productLicenseList.size() - 1);
        assertThat(testProductLicense.getAccessKey()).isEqualTo(UPDATED_ACCESS_KEY);
        assertThat(testProductLicense.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testProductLicense.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testProductLicense.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    void putNonExistingProductLicense() throws Exception {
        int databaseSizeBeforeUpdate = productLicenseRepository.findAll().size();
        productLicense.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductLicenseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productLicense.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productLicense))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductLicense in the database
        List<ProductLicense> productLicenseList = productLicenseRepository.findAll();
        assertThat(productLicenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProductLicense() throws Exception {
        int databaseSizeBeforeUpdate = productLicenseRepository.findAll().size();
        productLicense.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductLicenseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productLicense))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductLicense in the database
        List<ProductLicense> productLicenseList = productLicenseRepository.findAll();
        assertThat(productLicenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProductLicense() throws Exception {
        int databaseSizeBeforeUpdate = productLicenseRepository.findAll().size();
        productLicense.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductLicenseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productLicense)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductLicense in the database
        List<ProductLicense> productLicenseList = productLicenseRepository.findAll();
        assertThat(productLicenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProductLicenseWithPatch() throws Exception {
        // Initialize the database
        productLicenseRepository.save(productLicense);

        int databaseSizeBeforeUpdate = productLicenseRepository.findAll().size();

        // Update the productLicense using partial update
        ProductLicense partialUpdatedProductLicense = new ProductLicense();
        partialUpdatedProductLicense.setId(productLicense.getId());

        partialUpdatedProductLicense.accessKey(UPDATED_ACCESS_KEY).endDate(UPDATED_END_DATE);

        restProductLicenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductLicense.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductLicense))
            )
            .andExpect(status().isOk());

        // Validate the ProductLicense in the database
        List<ProductLicense> productLicenseList = productLicenseRepository.findAll();
        assertThat(productLicenseList).hasSize(databaseSizeBeforeUpdate);
        ProductLicense testProductLicense = productLicenseList.get(productLicenseList.size() - 1);
        assertThat(testProductLicense.getAccessKey()).isEqualTo(UPDATED_ACCESS_KEY);
        assertThat(testProductLicense.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testProductLicense.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testProductLicense.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    void fullUpdateProductLicenseWithPatch() throws Exception {
        // Initialize the database
        productLicenseRepository.save(productLicense);

        int databaseSizeBeforeUpdate = productLicenseRepository.findAll().size();

        // Update the productLicense using partial update
        ProductLicense partialUpdatedProductLicense = new ProductLicense();
        partialUpdatedProductLicense.setId(productLicense.getId());

        partialUpdatedProductLicense
            .accessKey(UPDATED_ACCESS_KEY)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isActive(UPDATED_IS_ACTIVE);

        restProductLicenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductLicense.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductLicense))
            )
            .andExpect(status().isOk());

        // Validate the ProductLicense in the database
        List<ProductLicense> productLicenseList = productLicenseRepository.findAll();
        assertThat(productLicenseList).hasSize(databaseSizeBeforeUpdate);
        ProductLicense testProductLicense = productLicenseList.get(productLicenseList.size() - 1);
        assertThat(testProductLicense.getAccessKey()).isEqualTo(UPDATED_ACCESS_KEY);
        assertThat(testProductLicense.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testProductLicense.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testProductLicense.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    void patchNonExistingProductLicense() throws Exception {
        int databaseSizeBeforeUpdate = productLicenseRepository.findAll().size();
        productLicense.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductLicenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productLicense.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productLicense))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductLicense in the database
        List<ProductLicense> productLicenseList = productLicenseRepository.findAll();
        assertThat(productLicenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProductLicense() throws Exception {
        int databaseSizeBeforeUpdate = productLicenseRepository.findAll().size();
        productLicense.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductLicenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productLicense))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductLicense in the database
        List<ProductLicense> productLicenseList = productLicenseRepository.findAll();
        assertThat(productLicenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProductLicense() throws Exception {
        int databaseSizeBeforeUpdate = productLicenseRepository.findAll().size();
        productLicense.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductLicenseMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(productLicense))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductLicense in the database
        List<ProductLicense> productLicenseList = productLicenseRepository.findAll();
        assertThat(productLicenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProductLicense() throws Exception {
        // Initialize the database
        productLicenseRepository.save(productLicense);

        int databaseSizeBeforeDelete = productLicenseRepository.findAll().size();

        // Delete the productLicense
        restProductLicenseMockMvc
            .perform(delete(ENTITY_API_URL_ID, productLicense.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductLicense> productLicenseList = productLicenseRepository.findAll();
        assertThat(productLicenseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
