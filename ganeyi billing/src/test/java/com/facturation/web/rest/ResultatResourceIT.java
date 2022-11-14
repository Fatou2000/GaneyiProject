package com.facturation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.facturation.IntegrationTest;
import com.facturation.domain.Resultat;
import com.facturation.repository.ResultatRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ResultatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ResultatResourceIT {

    private static final byte[] DEFAULT_VALUE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_VALUE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_VALUE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_VALUE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/resultats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ResultatRepository resultatRepository;

    @Autowired
    private MockMvc restResultatMockMvc;

    private Resultat resultat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resultat createEntity() {
        Resultat resultat = new Resultat().value(DEFAULT_VALUE).valueContentType(DEFAULT_VALUE_CONTENT_TYPE);
        return resultat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resultat createUpdatedEntity() {
        Resultat resultat = new Resultat().value(UPDATED_VALUE).valueContentType(UPDATED_VALUE_CONTENT_TYPE);
        return resultat;
    }

    @BeforeEach
    public void initTest() {
        resultatRepository.deleteAll();
        resultat = createEntity();
    }

    @Test
    void createResultat() throws Exception {
        int databaseSizeBeforeCreate = resultatRepository.findAll().size();
        // Create the Resultat
        restResultatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resultat)))
            .andExpect(status().isCreated());

        // Validate the Resultat in the database
        List<Resultat> resultatList = resultatRepository.findAll();
        assertThat(resultatList).hasSize(databaseSizeBeforeCreate + 1);
        Resultat testResultat = resultatList.get(resultatList.size() - 1);
        assertThat(testResultat.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testResultat.getValueContentType()).isEqualTo(DEFAULT_VALUE_CONTENT_TYPE);
    }

    @Test
    void createResultatWithExistingId() throws Exception {
        // Create the Resultat with an existing ID
        resultat.setId("existing_id");

        int databaseSizeBeforeCreate = resultatRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResultatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resultat)))
            .andExpect(status().isBadRequest());

        // Validate the Resultat in the database
        List<Resultat> resultatList = resultatRepository.findAll();
        assertThat(resultatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllResultats() throws Exception {
        // Initialize the database
        resultatRepository.save(resultat);

        // Get all the resultatList
        restResultatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resultat.getId())))
            .andExpect(jsonPath("$.[*].valueContentType").value(hasItem(DEFAULT_VALUE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(Base64Utils.encodeToString(DEFAULT_VALUE))));
    }

    @Test
    void getResultat() throws Exception {
        // Initialize the database
        resultatRepository.save(resultat);

        // Get the resultat
        restResultatMockMvc
            .perform(get(ENTITY_API_URL_ID, resultat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resultat.getId()))
            .andExpect(jsonPath("$.valueContentType").value(DEFAULT_VALUE_CONTENT_TYPE))
            .andExpect(jsonPath("$.value").value(Base64Utils.encodeToString(DEFAULT_VALUE)));
    }

    @Test
    void getNonExistingResultat() throws Exception {
        // Get the resultat
        restResultatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingResultat() throws Exception {
        // Initialize the database
        resultatRepository.save(resultat);

        int databaseSizeBeforeUpdate = resultatRepository.findAll().size();

        // Update the resultat
        Resultat updatedResultat = resultatRepository.findById(resultat.getId()).get();
        updatedResultat.value(UPDATED_VALUE).valueContentType(UPDATED_VALUE_CONTENT_TYPE);

        restResultatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedResultat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedResultat))
            )
            .andExpect(status().isOk());

        // Validate the Resultat in the database
        List<Resultat> resultatList = resultatRepository.findAll();
        assertThat(resultatList).hasSize(databaseSizeBeforeUpdate);
        Resultat testResultat = resultatList.get(resultatList.size() - 1);
        assertThat(testResultat.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testResultat.getValueContentType()).isEqualTo(UPDATED_VALUE_CONTENT_TYPE);
    }

    @Test
    void putNonExistingResultat() throws Exception {
        int databaseSizeBeforeUpdate = resultatRepository.findAll().size();
        resultat.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResultatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resultat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resultat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resultat in the database
        List<Resultat> resultatList = resultatRepository.findAll();
        assertThat(resultatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchResultat() throws Exception {
        int databaseSizeBeforeUpdate = resultatRepository.findAll().size();
        resultat.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resultat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resultat in the database
        List<Resultat> resultatList = resultatRepository.findAll();
        assertThat(resultatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamResultat() throws Exception {
        int databaseSizeBeforeUpdate = resultatRepository.findAll().size();
        resultat.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultatMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resultat)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resultat in the database
        List<Resultat> resultatList = resultatRepository.findAll();
        assertThat(resultatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateResultatWithPatch() throws Exception {
        // Initialize the database
        resultatRepository.save(resultat);

        int databaseSizeBeforeUpdate = resultatRepository.findAll().size();

        // Update the resultat using partial update
        Resultat partialUpdatedResultat = new Resultat();
        partialUpdatedResultat.setId(resultat.getId());

        restResultatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResultat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResultat))
            )
            .andExpect(status().isOk());

        // Validate the Resultat in the database
        List<Resultat> resultatList = resultatRepository.findAll();
        assertThat(resultatList).hasSize(databaseSizeBeforeUpdate);
        Resultat testResultat = resultatList.get(resultatList.size() - 1);
        assertThat(testResultat.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testResultat.getValueContentType()).isEqualTo(DEFAULT_VALUE_CONTENT_TYPE);
    }

    @Test
    void fullUpdateResultatWithPatch() throws Exception {
        // Initialize the database
        resultatRepository.save(resultat);

        int databaseSizeBeforeUpdate = resultatRepository.findAll().size();

        // Update the resultat using partial update
        Resultat partialUpdatedResultat = new Resultat();
        partialUpdatedResultat.setId(resultat.getId());

        partialUpdatedResultat.value(UPDATED_VALUE).valueContentType(UPDATED_VALUE_CONTENT_TYPE);

        restResultatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResultat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResultat))
            )
            .andExpect(status().isOk());

        // Validate the Resultat in the database
        List<Resultat> resultatList = resultatRepository.findAll();
        assertThat(resultatList).hasSize(databaseSizeBeforeUpdate);
        Resultat testResultat = resultatList.get(resultatList.size() - 1);
        assertThat(testResultat.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testResultat.getValueContentType()).isEqualTo(UPDATED_VALUE_CONTENT_TYPE);
    }

    @Test
    void patchNonExistingResultat() throws Exception {
        int databaseSizeBeforeUpdate = resultatRepository.findAll().size();
        resultat.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResultatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, resultat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resultat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resultat in the database
        List<Resultat> resultatList = resultatRepository.findAll();
        assertThat(resultatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchResultat() throws Exception {
        int databaseSizeBeforeUpdate = resultatRepository.findAll().size();
        resultat.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resultat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resultat in the database
        List<Resultat> resultatList = resultatRepository.findAll();
        assertThat(resultatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamResultat() throws Exception {
        int databaseSizeBeforeUpdate = resultatRepository.findAll().size();
        resultat.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultatMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(resultat)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resultat in the database
        List<Resultat> resultatList = resultatRepository.findAll();
        assertThat(resultatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteResultat() throws Exception {
        // Initialize the database
        resultatRepository.save(resultat);

        int databaseSizeBeforeDelete = resultatRepository.findAll().size();

        // Delete the resultat
        restResultatMockMvc
            .perform(delete(ENTITY_API_URL_ID, resultat.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Resultat> resultatList = resultatRepository.findAll();
        assertThat(resultatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
