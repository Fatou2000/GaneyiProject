package com.facturation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.facturation.IntegrationTest;
import com.facturation.domain.Forfait;
import com.facturation.repository.ForfaitRepository;
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
 * Integration tests for the {@link ForfaitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ForfaitResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_NUMBER_OF_QUERIES = 1D;
    private static final Double UPDATED_NUMBER_OF_QUERIES = 2D;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final String DEFAULT_PERIODE = "AAAAAAAAAA";
    private static final String UPDATED_PERIODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    private static final String ENTITY_API_URL = "/api/forfaits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ForfaitRepository forfaitRepository;

    @Autowired
    private MockMvc restForfaitMockMvc;

    private Forfait forfait;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Forfait createEntity() {
        Forfait forfait = new Forfait()
            .nom(DEFAULT_NOM)
            .description(DEFAULT_DESCRIPTION)
            .numberOfQueries(DEFAULT_NUMBER_OF_QUERIES)
            .price(DEFAULT_PRICE)
            .periode(DEFAULT_PERIODE)
            .actif(DEFAULT_ACTIF);
        return forfait;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Forfait createUpdatedEntity() {
        Forfait forfait = new Forfait()
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .numberOfQueries(UPDATED_NUMBER_OF_QUERIES)
            .price(UPDATED_PRICE)
            .periode(UPDATED_PERIODE)
            .actif(UPDATED_ACTIF);
        return forfait;
    }

    @BeforeEach
    public void initTest() {
        forfaitRepository.deleteAll();
        forfait = createEntity();
    }

    @Test
    void createForfait() throws Exception {
        int databaseSizeBeforeCreate = forfaitRepository.findAll().size();
        // Create the Forfait
        restForfaitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(forfait)))
            .andExpect(status().isCreated());

        // Validate the Forfait in the database
        List<Forfait> forfaitList = forfaitRepository.findAll();
        assertThat(forfaitList).hasSize(databaseSizeBeforeCreate + 1);
        Forfait testForfait = forfaitList.get(forfaitList.size() - 1);
        assertThat(testForfait.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testForfait.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testForfait.getNumberOfQueries()).isEqualTo(DEFAULT_NUMBER_OF_QUERIES);
        assertThat(testForfait.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testForfait.getPeriode()).isEqualTo(DEFAULT_PERIODE);
        assertThat(testForfait.getActif()).isEqualTo(DEFAULT_ACTIF);
    }

    @Test
    void createForfaitWithExistingId() throws Exception {
        // Create the Forfait with an existing ID
        forfait.setId("existing_id");

        int databaseSizeBeforeCreate = forfaitRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restForfaitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(forfait)))
            .andExpect(status().isBadRequest());

        // Validate the Forfait in the database
        List<Forfait> forfaitList = forfaitRepository.findAll();
        assertThat(forfaitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllForfaits() throws Exception {
        // Initialize the database
        forfaitRepository.save(forfait);

        // Get all the forfaitList
        restForfaitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(forfait.getId())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].numberOfQueries").value(hasItem(DEFAULT_NUMBER_OF_QUERIES.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].periode").value(hasItem(DEFAULT_PERIODE)))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF.booleanValue())));
    }

    @Test
    void getForfait() throws Exception {
        // Initialize the database
        forfaitRepository.save(forfait);

        // Get the forfait
        restForfaitMockMvc
            .perform(get(ENTITY_API_URL_ID, forfait.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(forfait.getId()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.numberOfQueries").value(DEFAULT_NUMBER_OF_QUERIES.doubleValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.periode").value(DEFAULT_PERIODE))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF.booleanValue()));
    }

    @Test
    void getNonExistingForfait() throws Exception {
        // Get the forfait
        restForfaitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingForfait() throws Exception {
        // Initialize the database
        forfaitRepository.save(forfait);

        int databaseSizeBeforeUpdate = forfaitRepository.findAll().size();

        // Update the forfait
        Forfait updatedForfait = forfaitRepository.findById(forfait.getId()).get();
        updatedForfait
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .numberOfQueries(UPDATED_NUMBER_OF_QUERIES)
            .price(UPDATED_PRICE)
            .periode(UPDATED_PERIODE)
            .actif(UPDATED_ACTIF);

        restForfaitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedForfait.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedForfait))
            )
            .andExpect(status().isOk());

        // Validate the Forfait in the database
        List<Forfait> forfaitList = forfaitRepository.findAll();
        assertThat(forfaitList).hasSize(databaseSizeBeforeUpdate);
        Forfait testForfait = forfaitList.get(forfaitList.size() - 1);
        assertThat(testForfait.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testForfait.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testForfait.getNumberOfQueries()).isEqualTo(UPDATED_NUMBER_OF_QUERIES);
        assertThat(testForfait.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testForfait.getPeriode()).isEqualTo(UPDATED_PERIODE);
        assertThat(testForfait.getActif()).isEqualTo(UPDATED_ACTIF);
    }

    @Test
    void putNonExistingForfait() throws Exception {
        int databaseSizeBeforeUpdate = forfaitRepository.findAll().size();
        forfait.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restForfaitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, forfait.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(forfait))
            )
            .andExpect(status().isBadRequest());

        // Validate the Forfait in the database
        List<Forfait> forfaitList = forfaitRepository.findAll();
        assertThat(forfaitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchForfait() throws Exception {
        int databaseSizeBeforeUpdate = forfaitRepository.findAll().size();
        forfait.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restForfaitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(forfait))
            )
            .andExpect(status().isBadRequest());

        // Validate the Forfait in the database
        List<Forfait> forfaitList = forfaitRepository.findAll();
        assertThat(forfaitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamForfait() throws Exception {
        int databaseSizeBeforeUpdate = forfaitRepository.findAll().size();
        forfait.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restForfaitMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(forfait)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Forfait in the database
        List<Forfait> forfaitList = forfaitRepository.findAll();
        assertThat(forfaitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateForfaitWithPatch() throws Exception {
        // Initialize the database
        forfaitRepository.save(forfait);

        int databaseSizeBeforeUpdate = forfaitRepository.findAll().size();

        // Update the forfait using partial update
        Forfait partialUpdatedForfait = new Forfait();
        partialUpdatedForfait.setId(forfait.getId());

        partialUpdatedForfait.description(UPDATED_DESCRIPTION).price(UPDATED_PRICE).periode(UPDATED_PERIODE).actif(UPDATED_ACTIF);

        restForfaitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedForfait.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedForfait))
            )
            .andExpect(status().isOk());

        // Validate the Forfait in the database
        List<Forfait> forfaitList = forfaitRepository.findAll();
        assertThat(forfaitList).hasSize(databaseSizeBeforeUpdate);
        Forfait testForfait = forfaitList.get(forfaitList.size() - 1);
        assertThat(testForfait.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testForfait.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testForfait.getNumberOfQueries()).isEqualTo(DEFAULT_NUMBER_OF_QUERIES);
        assertThat(testForfait.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testForfait.getPeriode()).isEqualTo(UPDATED_PERIODE);
        assertThat(testForfait.getActif()).isEqualTo(UPDATED_ACTIF);
    }

    @Test
    void fullUpdateForfaitWithPatch() throws Exception {
        // Initialize the database
        forfaitRepository.save(forfait);

        int databaseSizeBeforeUpdate = forfaitRepository.findAll().size();

        // Update the forfait using partial update
        Forfait partialUpdatedForfait = new Forfait();
        partialUpdatedForfait.setId(forfait.getId());

        partialUpdatedForfait
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .numberOfQueries(UPDATED_NUMBER_OF_QUERIES)
            .price(UPDATED_PRICE)
            .periode(UPDATED_PERIODE)
            .actif(UPDATED_ACTIF);

        restForfaitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedForfait.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedForfait))
            )
            .andExpect(status().isOk());

        // Validate the Forfait in the database
        List<Forfait> forfaitList = forfaitRepository.findAll();
        assertThat(forfaitList).hasSize(databaseSizeBeforeUpdate);
        Forfait testForfait = forfaitList.get(forfaitList.size() - 1);
        assertThat(testForfait.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testForfait.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testForfait.getNumberOfQueries()).isEqualTo(UPDATED_NUMBER_OF_QUERIES);
        assertThat(testForfait.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testForfait.getPeriode()).isEqualTo(UPDATED_PERIODE);
        assertThat(testForfait.getActif()).isEqualTo(UPDATED_ACTIF);
    }

    @Test
    void patchNonExistingForfait() throws Exception {
        int databaseSizeBeforeUpdate = forfaitRepository.findAll().size();
        forfait.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restForfaitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, forfait.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(forfait))
            )
            .andExpect(status().isBadRequest());

        // Validate the Forfait in the database
        List<Forfait> forfaitList = forfaitRepository.findAll();
        assertThat(forfaitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchForfait() throws Exception {
        int databaseSizeBeforeUpdate = forfaitRepository.findAll().size();
        forfait.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restForfaitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(forfait))
            )
            .andExpect(status().isBadRequest());

        // Validate the Forfait in the database
        List<Forfait> forfaitList = forfaitRepository.findAll();
        assertThat(forfaitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamForfait() throws Exception {
        int databaseSizeBeforeUpdate = forfaitRepository.findAll().size();
        forfait.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restForfaitMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(forfait)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Forfait in the database
        List<Forfait> forfaitList = forfaitRepository.findAll();
        assertThat(forfaitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteForfait() throws Exception {
        // Initialize the database
        forfaitRepository.save(forfait);

        int databaseSizeBeforeDelete = forfaitRepository.findAll().size();

        // Delete the forfait
        restForfaitMockMvc
            .perform(delete(ENTITY_API_URL_ID, forfait.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Forfait> forfaitList = forfaitRepository.findAll();
        assertThat(forfaitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
