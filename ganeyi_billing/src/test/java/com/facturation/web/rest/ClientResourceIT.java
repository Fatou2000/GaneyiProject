package com.facturation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.facturation.IntegrationTest;
import com.facturation.domain.Client;
import com.facturation.repository.ClientRepository;
import com.facturation.service.ClientService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link ClientResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ClientResourceIT {

    private static final String DEFAULT_ACCOUNT_ID = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/clients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ClientRepository clientRepository;

    @Mock
    private ClientRepository clientRepositoryMock;

    @Mock
    private ClientService clientServiceMock;

    @Autowired
    private MockMvc restClientMockMvc;

    private Client client;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createEntity() {
        Client client = new Client()
            .accountId(DEFAULT_ACCOUNT_ID)
            .companyName(DEFAULT_COMPANY_NAME)
            .firstName(DEFAULT_FIRST_NAME)
            .address(DEFAULT_ADDRESS)
            .phoneNumber(DEFAULT_PHONE_NUMBER);
        return client;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createUpdatedEntity() {
        Client client = new Client()
            .accountId(UPDATED_ACCOUNT_ID)
            .companyName(UPDATED_COMPANY_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .address(UPDATED_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        return client;
    }

    @BeforeEach
    public void initTest() {
        clientRepository.deleteAll();
        client = createEntity();
    }

    @Test
    void createClient() throws Exception {
        int databaseSizeBeforeCreate = clientRepository.findAll().size();
        // Create the Client
        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isCreated());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeCreate + 1);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getAccountId()).isEqualTo(DEFAULT_ACCOUNT_ID);
        assertThat(testClient.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testClient.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testClient.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testClient.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
    }

    @Test
    void createClientWithExistingId() throws Exception {
        // Create the Client with an existing ID
        client.setId("existing_id");

        int databaseSizeBeforeCreate = clientRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllClients() throws Exception {
        // Initialize the database
        clientRepository.save(client);

        // Get all the clientList
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId())))
            .andExpect(jsonPath("$.[*].accountId").value(hasItem(DEFAULT_ACCOUNT_ID)))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllClientsWithEagerRelationshipsIsEnabled() throws Exception {
        when(clientServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restClientMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(clientServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllClientsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(clientServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restClientMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(clientRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getClient() throws Exception {
        // Initialize the database
        clientRepository.save(client);

        // Get the client
        restClientMockMvc
            .perform(get(ENTITY_API_URL_ID, client.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(client.getId()))
            .andExpect(jsonPath("$.accountId").value(DEFAULT_ACCOUNT_ID))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    void getNonExistingClient() throws Exception {
        // Get the client
        restClientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingClient() throws Exception {
        // Initialize the database
        clientRepository.save(client);

        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Update the client
        Client updatedClient = clientRepository.findById(client.getId()).get();
        updatedClient
            .accountId(UPDATED_ACCOUNT_ID)
            .companyName(UPDATED_COMPANY_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .address(UPDATED_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedClient.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedClient))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getAccountId()).isEqualTo(UPDATED_ACCOUNT_ID);
        assertThat(testClient.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testClient.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testClient.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testClient.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    void putNonExistingClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, client.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(client))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(client))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateClientWithPatch() throws Exception {
        // Initialize the database
        clientRepository.save(client);

        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Update the client using partial update
        Client partialUpdatedClient = new Client();
        partialUpdatedClient.setId(client.getId());

        partialUpdatedClient.accountId(UPDATED_ACCOUNT_ID).companyName(UPDATED_COMPANY_NAME).firstName(UPDATED_FIRST_NAME);

        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClient))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getAccountId()).isEqualTo(UPDATED_ACCOUNT_ID);
        assertThat(testClient.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testClient.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testClient.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testClient.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
    }

    @Test
    void fullUpdateClientWithPatch() throws Exception {
        // Initialize the database
        clientRepository.save(client);

        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Update the client using partial update
        Client partialUpdatedClient = new Client();
        partialUpdatedClient.setId(client.getId());

        partialUpdatedClient
            .accountId(UPDATED_ACCOUNT_ID)
            .companyName(UPDATED_COMPANY_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .address(UPDATED_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClient))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getAccountId()).isEqualTo(UPDATED_ACCOUNT_ID);
        assertThat(testClient.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testClient.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testClient.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testClient.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    void patchNonExistingClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, client.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(client))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(client))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteClient() throws Exception {
        // Initialize the database
        clientRepository.save(client);

        int databaseSizeBeforeDelete = clientRepository.findAll().size();

        // Delete the client
        restClientMockMvc
            .perform(delete(ENTITY_API_URL_ID, client.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
