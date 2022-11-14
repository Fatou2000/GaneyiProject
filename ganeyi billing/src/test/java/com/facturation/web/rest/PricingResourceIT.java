package com.facturation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.facturation.IntegrationTest;
import com.facturation.domain.Pricing;
import com.facturation.repository.PricingRepository;
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
 * Integration tests for the {@link PricingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PricingResourceIT {

    private static final Double DEFAULT_VALUE = 1D;
    private static final Double UPDATED_VALUE = 2D;

    private static final String ENTITY_API_URL = "/api/pricings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private PricingRepository pricingRepository;

    @Autowired
    private MockMvc restPricingMockMvc;

    private Pricing pricing;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pricing createEntity() {
        Pricing pricing = new Pricing().value(DEFAULT_VALUE);
        return pricing;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pricing createUpdatedEntity() {
        Pricing pricing = new Pricing().value(UPDATED_VALUE);
        return pricing;
    }

    @BeforeEach
    public void initTest() {
        pricingRepository.deleteAll();
        pricing = createEntity();
    }

    @Test
    void createPricing() throws Exception {
        int databaseSizeBeforeCreate = pricingRepository.findAll().size();
        // Create the Pricing
        restPricingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pricing)))
            .andExpect(status().isCreated());

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll();
        assertThat(pricingList).hasSize(databaseSizeBeforeCreate + 1);
        Pricing testPricing = pricingList.get(pricingList.size() - 1);
        assertThat(testPricing.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    void createPricingWithExistingId() throws Exception {
        // Create the Pricing with an existing ID
        pricing.setId("existing_id");

        int databaseSizeBeforeCreate = pricingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPricingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pricing)))
            .andExpect(status().isBadRequest());

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll();
        assertThat(pricingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllPricings() throws Exception {
        // Initialize the database
        pricingRepository.save(pricing);

        // Get all the pricingList
        restPricingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pricing.getId())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())));
    }

    @Test
    void getPricing() throws Exception {
        // Initialize the database
        pricingRepository.save(pricing);

        // Get the pricing
        restPricingMockMvc
            .perform(get(ENTITY_API_URL_ID, pricing.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pricing.getId()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()));
    }

    @Test
    void getNonExistingPricing() throws Exception {
        // Get the pricing
        restPricingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingPricing() throws Exception {
        // Initialize the database
        pricingRepository.save(pricing);

        int databaseSizeBeforeUpdate = pricingRepository.findAll().size();

        // Update the pricing
        Pricing updatedPricing = pricingRepository.findById(pricing.getId()).get();
        updatedPricing.value(UPDATED_VALUE);

        restPricingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPricing.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPricing))
            )
            .andExpect(status().isOk());

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll();
        assertThat(pricingList).hasSize(databaseSizeBeforeUpdate);
        Pricing testPricing = pricingList.get(pricingList.size() - 1);
        assertThat(testPricing.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    void putNonExistingPricing() throws Exception {
        int databaseSizeBeforeUpdate = pricingRepository.findAll().size();
        pricing.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPricingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pricing.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pricing))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll();
        assertThat(pricingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPricing() throws Exception {
        int databaseSizeBeforeUpdate = pricingRepository.findAll().size();
        pricing.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPricingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pricing))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll();
        assertThat(pricingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPricing() throws Exception {
        int databaseSizeBeforeUpdate = pricingRepository.findAll().size();
        pricing.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPricingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pricing)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll();
        assertThat(pricingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePricingWithPatch() throws Exception {
        // Initialize the database
        pricingRepository.save(pricing);

        int databaseSizeBeforeUpdate = pricingRepository.findAll().size();

        // Update the pricing using partial update
        Pricing partialUpdatedPricing = new Pricing();
        partialUpdatedPricing.setId(pricing.getId());

        restPricingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPricing.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPricing))
            )
            .andExpect(status().isOk());

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll();
        assertThat(pricingList).hasSize(databaseSizeBeforeUpdate);
        Pricing testPricing = pricingList.get(pricingList.size() - 1);
        assertThat(testPricing.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    void fullUpdatePricingWithPatch() throws Exception {
        // Initialize the database
        pricingRepository.save(pricing);

        int databaseSizeBeforeUpdate = pricingRepository.findAll().size();

        // Update the pricing using partial update
        Pricing partialUpdatedPricing = new Pricing();
        partialUpdatedPricing.setId(pricing.getId());

        partialUpdatedPricing.value(UPDATED_VALUE);

        restPricingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPricing.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPricing))
            )
            .andExpect(status().isOk());

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll();
        assertThat(pricingList).hasSize(databaseSizeBeforeUpdate);
        Pricing testPricing = pricingList.get(pricingList.size() - 1);
        assertThat(testPricing.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    void patchNonExistingPricing() throws Exception {
        int databaseSizeBeforeUpdate = pricingRepository.findAll().size();
        pricing.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPricingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pricing.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pricing))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll();
        assertThat(pricingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPricing() throws Exception {
        int databaseSizeBeforeUpdate = pricingRepository.findAll().size();
        pricing.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPricingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pricing))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll();
        assertThat(pricingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPricing() throws Exception {
        int databaseSizeBeforeUpdate = pricingRepository.findAll().size();
        pricing.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPricingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pricing)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pricing in the database
        List<Pricing> pricingList = pricingRepository.findAll();
        assertThat(pricingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePricing() throws Exception {
        // Initialize the database
        pricingRepository.save(pricing);

        int databaseSizeBeforeDelete = pricingRepository.findAll().size();

        // Delete the pricing
        restPricingMockMvc
            .perform(delete(ENTITY_API_URL_ID, pricing.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pricing> pricingList = pricingRepository.findAll();
        assertThat(pricingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
