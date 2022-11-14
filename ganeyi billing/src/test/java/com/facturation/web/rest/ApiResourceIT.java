package com.facturation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.facturation.IntegrationTest;
import com.facturation.domain.Api;
import com.facturation.repository.ApiRepository;
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
 * Integration tests for the {@link ApiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApiResourceIT {

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_URL = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_DOC_URL = "AAAAAAAAAA";
    private static final String UPDATED_DOC_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTICE = false;
    private static final Boolean UPDATED_IS_ACTICE = true;

    private static final String ENTITY_API_URL = "/api/apis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ApiRepository apiRepository;

    @Autowired
    private MockMvc restApiMockMvc;

    private Api api;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Api createEntity() {
        Api api = new Api().version(DEFAULT_VERSION).serviceURL(DEFAULT_SERVICE_URL).docURL(DEFAULT_DOC_URL).isActice(DEFAULT_IS_ACTICE);
        return api;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Api createUpdatedEntity() {
        Api api = new Api().version(UPDATED_VERSION).serviceURL(UPDATED_SERVICE_URL).docURL(UPDATED_DOC_URL).isActice(UPDATED_IS_ACTICE);
        return api;
    }

    @BeforeEach
    public void initTest() {
        apiRepository.deleteAll();
        api = createEntity();
    }

    @Test
    void createApi() throws Exception {
        int databaseSizeBeforeCreate = apiRepository.findAll().size();
        // Create the Api
        restApiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(api)))
            .andExpect(status().isCreated());

        // Validate the Api in the database
        List<Api> apiList = apiRepository.findAll();
        assertThat(apiList).hasSize(databaseSizeBeforeCreate + 1);
        Api testApi = apiList.get(apiList.size() - 1);
        assertThat(testApi.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testApi.getServiceURL()).isEqualTo(DEFAULT_SERVICE_URL);
        assertThat(testApi.getDocURL()).isEqualTo(DEFAULT_DOC_URL);
        assertThat(testApi.getIsActice()).isEqualTo(DEFAULT_IS_ACTICE);
    }

    @Test
    void createApiWithExistingId() throws Exception {
        // Create the Api with an existing ID
        api.setId("existing_id");

        int databaseSizeBeforeCreate = apiRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(api)))
            .andExpect(status().isBadRequest());

        // Validate the Api in the database
        List<Api> apiList = apiRepository.findAll();
        assertThat(apiList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllApis() throws Exception {
        // Initialize the database
        apiRepository.save(api);

        // Get all the apiList
        restApiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(api.getId())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].serviceURL").value(hasItem(DEFAULT_SERVICE_URL)))
            .andExpect(jsonPath("$.[*].docURL").value(hasItem(DEFAULT_DOC_URL)))
            .andExpect(jsonPath("$.[*].isActice").value(hasItem(DEFAULT_IS_ACTICE.booleanValue())));
    }

    @Test
    void getApi() throws Exception {
        // Initialize the database
        apiRepository.save(api);

        // Get the api
        restApiMockMvc
            .perform(get(ENTITY_API_URL_ID, api.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(api.getId()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.serviceURL").value(DEFAULT_SERVICE_URL))
            .andExpect(jsonPath("$.docURL").value(DEFAULT_DOC_URL))
            .andExpect(jsonPath("$.isActice").value(DEFAULT_IS_ACTICE.booleanValue()));
    }

    @Test
    void getNonExistingApi() throws Exception {
        // Get the api
        restApiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingApi() throws Exception {
        // Initialize the database
        apiRepository.save(api);

        int databaseSizeBeforeUpdate = apiRepository.findAll().size();

        // Update the api
        Api updatedApi = apiRepository.findById(api.getId()).get();
        updatedApi.version(UPDATED_VERSION).serviceURL(UPDATED_SERVICE_URL).docURL(UPDATED_DOC_URL).isActice(UPDATED_IS_ACTICE);

        restApiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApi))
            )
            .andExpect(status().isOk());

        // Validate the Api in the database
        List<Api> apiList = apiRepository.findAll();
        assertThat(apiList).hasSize(databaseSizeBeforeUpdate);
        Api testApi = apiList.get(apiList.size() - 1);
        assertThat(testApi.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testApi.getServiceURL()).isEqualTo(UPDATED_SERVICE_URL);
        assertThat(testApi.getDocURL()).isEqualTo(UPDATED_DOC_URL);
        assertThat(testApi.getIsActice()).isEqualTo(UPDATED_IS_ACTICE);
    }

    @Test
    void putNonExistingApi() throws Exception {
        int databaseSizeBeforeUpdate = apiRepository.findAll().size();
        api.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, api.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(api))
            )
            .andExpect(status().isBadRequest());

        // Validate the Api in the database
        List<Api> apiList = apiRepository.findAll();
        assertThat(apiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchApi() throws Exception {
        int databaseSizeBeforeUpdate = apiRepository.findAll().size();
        api.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(api))
            )
            .andExpect(status().isBadRequest());

        // Validate the Api in the database
        List<Api> apiList = apiRepository.findAll();
        assertThat(apiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamApi() throws Exception {
        int databaseSizeBeforeUpdate = apiRepository.findAll().size();
        api.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(api)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Api in the database
        List<Api> apiList = apiRepository.findAll();
        assertThat(apiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateApiWithPatch() throws Exception {
        // Initialize the database
        apiRepository.save(api);

        int databaseSizeBeforeUpdate = apiRepository.findAll().size();

        // Update the api using partial update
        Api partialUpdatedApi = new Api();
        partialUpdatedApi.setId(api.getId());

        partialUpdatedApi.serviceURL(UPDATED_SERVICE_URL);

        restApiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApi.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApi))
            )
            .andExpect(status().isOk());

        // Validate the Api in the database
        List<Api> apiList = apiRepository.findAll();
        assertThat(apiList).hasSize(databaseSizeBeforeUpdate);
        Api testApi = apiList.get(apiList.size() - 1);
        assertThat(testApi.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testApi.getServiceURL()).isEqualTo(UPDATED_SERVICE_URL);
        assertThat(testApi.getDocURL()).isEqualTo(DEFAULT_DOC_URL);
        assertThat(testApi.getIsActice()).isEqualTo(DEFAULT_IS_ACTICE);
    }

    @Test
    void fullUpdateApiWithPatch() throws Exception {
        // Initialize the database
        apiRepository.save(api);

        int databaseSizeBeforeUpdate = apiRepository.findAll().size();

        // Update the api using partial update
        Api partialUpdatedApi = new Api();
        partialUpdatedApi.setId(api.getId());

        partialUpdatedApi.version(UPDATED_VERSION).serviceURL(UPDATED_SERVICE_URL).docURL(UPDATED_DOC_URL).isActice(UPDATED_IS_ACTICE);

        restApiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApi.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApi))
            )
            .andExpect(status().isOk());

        // Validate the Api in the database
        List<Api> apiList = apiRepository.findAll();
        assertThat(apiList).hasSize(databaseSizeBeforeUpdate);
        Api testApi = apiList.get(apiList.size() - 1);
        assertThat(testApi.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testApi.getServiceURL()).isEqualTo(UPDATED_SERVICE_URL);
        assertThat(testApi.getDocURL()).isEqualTo(UPDATED_DOC_URL);
        assertThat(testApi.getIsActice()).isEqualTo(UPDATED_IS_ACTICE);
    }

    @Test
    void patchNonExistingApi() throws Exception {
        int databaseSizeBeforeUpdate = apiRepository.findAll().size();
        api.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, api.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(api))
            )
            .andExpect(status().isBadRequest());

        // Validate the Api in the database
        List<Api> apiList = apiRepository.findAll();
        assertThat(apiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchApi() throws Exception {
        int databaseSizeBeforeUpdate = apiRepository.findAll().size();
        api.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(api))
            )
            .andExpect(status().isBadRequest());

        // Validate the Api in the database
        List<Api> apiList = apiRepository.findAll();
        assertThat(apiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamApi() throws Exception {
        int databaseSizeBeforeUpdate = apiRepository.findAll().size();
        api.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(api)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Api in the database
        List<Api> apiList = apiRepository.findAll();
        assertThat(apiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteApi() throws Exception {
        // Initialize the database
        apiRepository.save(api);

        int databaseSizeBeforeDelete = apiRepository.findAll().size();

        // Delete the api
        restApiMockMvc.perform(delete(ENTITY_API_URL_ID, api.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Api> apiList = apiRepository.findAll();
        assertThat(apiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
