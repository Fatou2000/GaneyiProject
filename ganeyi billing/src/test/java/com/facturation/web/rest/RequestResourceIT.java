package com.facturation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.facturation.IntegrationTest;
import com.facturation.domain.Request;
import com.facturation.repository.RequestRepository;
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
 * Integration tests for the {@link RequestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RequestResourceIT {

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_REQUEST_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REQUEST_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private MockMvc restRequestMockMvc;

    private Request request;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Request createEntity() {
        Request request = new Request().duration(DEFAULT_DURATION).status(DEFAULT_STATUS).requestDate(DEFAULT_REQUEST_DATE);
        return request;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Request createUpdatedEntity() {
        Request request = new Request().duration(UPDATED_DURATION).status(UPDATED_STATUS).requestDate(UPDATED_REQUEST_DATE);
        return request;
    }

    @BeforeEach
    public void initTest() {
        requestRepository.deleteAll();
        request = createEntity();
    }

    @Test
    void createRequest() throws Exception {
        int databaseSizeBeforeCreate = requestRepository.findAll().size();
        // Create the Request
        restRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(request)))
            .andExpect(status().isCreated());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeCreate + 1);
        Request testRequest = requestList.get(requestList.size() - 1);
        assertThat(testRequest.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testRequest.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testRequest.getRequestDate()).isEqualTo(DEFAULT_REQUEST_DATE);
    }

    @Test
    void createRequestWithExistingId() throws Exception {
        // Create the Request with an existing ID
        request.setId("existing_id");

        int databaseSizeBeforeCreate = requestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(request)))
            .andExpect(status().isBadRequest());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllRequests() throws Exception {
        // Initialize the database
        requestRepository.save(request);

        // Get all the requestList
        restRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(request.getId())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].requestDate").value(hasItem(DEFAULT_REQUEST_DATE.toString())));
    }

    @Test
    void getRequest() throws Exception {
        // Initialize the database
        requestRepository.save(request);

        // Get the request
        restRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, request.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(request.getId()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.requestDate").value(DEFAULT_REQUEST_DATE.toString()));
    }

    @Test
    void getNonExistingRequest() throws Exception {
        // Get the request
        restRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingRequest() throws Exception {
        // Initialize the database
        requestRepository.save(request);

        int databaseSizeBeforeUpdate = requestRepository.findAll().size();

        // Update the request
        Request updatedRequest = requestRepository.findById(request.getId()).get();
        updatedRequest.duration(UPDATED_DURATION).status(UPDATED_STATUS).requestDate(UPDATED_REQUEST_DATE);

        restRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRequest))
            )
            .andExpect(status().isOk());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
        Request testRequest = requestList.get(requestList.size() - 1);
        assertThat(testRequest.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testRequest.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRequest.getRequestDate()).isEqualTo(UPDATED_REQUEST_DATE);
    }

    @Test
    void putNonExistingRequest() throws Exception {
        int databaseSizeBeforeUpdate = requestRepository.findAll().size();
        request.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, request.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(request))
            )
            .andExpect(status().isBadRequest());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRequest() throws Exception {
        int databaseSizeBeforeUpdate = requestRepository.findAll().size();
        request.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(request))
            )
            .andExpect(status().isBadRequest());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRequest() throws Exception {
        int databaseSizeBeforeUpdate = requestRepository.findAll().size();
        request.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(request)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRequestWithPatch() throws Exception {
        // Initialize the database
        requestRepository.save(request);

        int databaseSizeBeforeUpdate = requestRepository.findAll().size();

        // Update the request using partial update
        Request partialUpdatedRequest = new Request();
        partialUpdatedRequest.setId(request.getId());

        partialUpdatedRequest.duration(UPDATED_DURATION).status(UPDATED_STATUS);

        restRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRequest))
            )
            .andExpect(status().isOk());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
        Request testRequest = requestList.get(requestList.size() - 1);
        assertThat(testRequest.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testRequest.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRequest.getRequestDate()).isEqualTo(DEFAULT_REQUEST_DATE);
    }

    @Test
    void fullUpdateRequestWithPatch() throws Exception {
        // Initialize the database
        requestRepository.save(request);

        int databaseSizeBeforeUpdate = requestRepository.findAll().size();

        // Update the request using partial update
        Request partialUpdatedRequest = new Request();
        partialUpdatedRequest.setId(request.getId());

        partialUpdatedRequest.duration(UPDATED_DURATION).status(UPDATED_STATUS).requestDate(UPDATED_REQUEST_DATE);

        restRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRequest))
            )
            .andExpect(status().isOk());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
        Request testRequest = requestList.get(requestList.size() - 1);
        assertThat(testRequest.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testRequest.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRequest.getRequestDate()).isEqualTo(UPDATED_REQUEST_DATE);
    }

    @Test
    void patchNonExistingRequest() throws Exception {
        int databaseSizeBeforeUpdate = requestRepository.findAll().size();
        request.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, request.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(request))
            )
            .andExpect(status().isBadRequest());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRequest() throws Exception {
        int databaseSizeBeforeUpdate = requestRepository.findAll().size();
        request.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(request))
            )
            .andExpect(status().isBadRequest());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRequest() throws Exception {
        int databaseSizeBeforeUpdate = requestRepository.findAll().size();
        request.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(request)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRequest() throws Exception {
        // Initialize the database
        requestRepository.save(request);

        int databaseSizeBeforeDelete = requestRepository.findAll().size();

        // Delete the request
        restRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, request.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
