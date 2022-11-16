package com.facturation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.facturation.IntegrationTest;
import com.facturation.domain.Facture;
import com.facturation.domain.enumeration.FactureStatus;
import com.facturation.domain.enumeration.TypeFacturation;
import com.facturation.repository.FactureRepository;
import com.facturation.service.FactureService;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link FactureResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FactureResourceIT {

    private static final Double DEFAULT_RABAIS = 1D;
    private static final Double UPDATED_RABAIS = 2D;

    private static final Double DEFAULT_TVA = 1D;
    private static final Double UPDATED_TVA = 2D;

    private static final Double DEFAULT_SOUS_TOTAL = 1D;
    private static final Double UPDATED_SOUS_TOTAL = 2D;

    private static final Double DEFAULT_TOTAL = 1D;
    private static final Double UPDATED_TOTAL = 2D;

    private static final TypeFacturation DEFAULT_TYPE_FACTURATION = TypeFacturation.FACTURATION_PAR_REQUETE;
    private static final TypeFacturation UPDATED_TYPE_FACTURATION = TypeFacturation.FACTURATION_PAR_PERIODE;

    private static final FactureStatus DEFAULT_STATUS = FactureStatus.PAYE;
    private static final FactureStatus UPDATED_STATUS = FactureStatus.NON_PAYE;

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/factures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private FactureRepository factureRepository;

    @Mock
    private FactureRepository factureRepositoryMock;

    @Mock
    private FactureService factureServiceMock;

    @Autowired
    private MockMvc restFactureMockMvc;

    private Facture facture;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facture createEntity() {
        Facture facture = new Facture()
            .rabais(DEFAULT_RABAIS)
            .tva(DEFAULT_TVA)
            .sousTotal(DEFAULT_SOUS_TOTAL)
            .total(DEFAULT_TOTAL)
            .typeFacturation(DEFAULT_TYPE_FACTURATION)
            .status(DEFAULT_STATUS)
            .reference(DEFAULT_REFERENCE)
            .date(DEFAULT_DATE)
            .numero(DEFAULT_NUMERO);
        return facture;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facture createUpdatedEntity() {
        Facture facture = new Facture()
            .rabais(UPDATED_RABAIS)
            .tva(UPDATED_TVA)
            .sousTotal(UPDATED_SOUS_TOTAL)
            .total(UPDATED_TOTAL)
            .typeFacturation(UPDATED_TYPE_FACTURATION)
            .status(UPDATED_STATUS)
            .reference(UPDATED_REFERENCE)
            .date(UPDATED_DATE)
            .numero(UPDATED_NUMERO);
        return facture;
    }

    @BeforeEach
    public void initTest() {
        factureRepository.deleteAll();
        facture = createEntity();
    }

    @Test
    void createFacture() throws Exception {
        int databaseSizeBeforeCreate = factureRepository.findAll().size();
        // Create the Facture
        restFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facture)))
            .andExpect(status().isCreated());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeCreate + 1);
        Facture testFacture = factureList.get(factureList.size() - 1);
        assertThat(testFacture.getRabais()).isEqualTo(DEFAULT_RABAIS);
        assertThat(testFacture.getTva()).isEqualTo(DEFAULT_TVA);
        assertThat(testFacture.getSousTotal()).isEqualTo(DEFAULT_SOUS_TOTAL);
        assertThat(testFacture.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testFacture.getTypeFacturation()).isEqualTo(DEFAULT_TYPE_FACTURATION);
        assertThat(testFacture.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testFacture.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testFacture.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testFacture.getNumero()).isEqualTo(DEFAULT_NUMERO);
    }

    @Test
    void createFactureWithExistingId() throws Exception {
        // Create the Facture with an existing ID
        facture.setId("existing_id");

        int databaseSizeBeforeCreate = factureRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facture)))
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllFactures() throws Exception {
        // Initialize the database
        factureRepository.save(facture);

        // Get all the factureList
        restFactureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facture.getId())))
            .andExpect(jsonPath("$.[*].rabais").value(hasItem(DEFAULT_RABAIS.doubleValue())))
            .andExpect(jsonPath("$.[*].tva").value(hasItem(DEFAULT_TVA.doubleValue())))
            .andExpect(jsonPath("$.[*].sousTotal").value(hasItem(DEFAULT_SOUS_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].typeFacturation").value(hasItem(DEFAULT_TYPE_FACTURATION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFacturesWithEagerRelationshipsIsEnabled() throws Exception {
        when(factureServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFactureMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(factureServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFacturesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(factureServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFactureMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(factureRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getFacture() throws Exception {
        // Initialize the database
        factureRepository.save(facture);

        // Get the facture
        restFactureMockMvc
            .perform(get(ENTITY_API_URL_ID, facture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facture.getId()))
            .andExpect(jsonPath("$.rabais").value(DEFAULT_RABAIS.doubleValue()))
            .andExpect(jsonPath("$.tva").value(DEFAULT_TVA.doubleValue()))
            .andExpect(jsonPath("$.sousTotal").value(DEFAULT_SOUS_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.typeFacturation").value(DEFAULT_TYPE_FACTURATION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO));
    }

    @Test
    void getNonExistingFacture() throws Exception {
        // Get the facture
        restFactureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingFacture() throws Exception {
        // Initialize the database
        factureRepository.save(facture);

        int databaseSizeBeforeUpdate = factureRepository.findAll().size();

        // Update the facture
        Facture updatedFacture = factureRepository.findById(facture.getId()).get();
        updatedFacture
            .rabais(UPDATED_RABAIS)
            .tva(UPDATED_TVA)
            .sousTotal(UPDATED_SOUS_TOTAL)
            .total(UPDATED_TOTAL)
            .typeFacturation(UPDATED_TYPE_FACTURATION)
            .status(UPDATED_STATUS)
            .reference(UPDATED_REFERENCE)
            .date(UPDATED_DATE)
            .numero(UPDATED_NUMERO);

        restFactureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFacture.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFacture))
            )
            .andExpect(status().isOk());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeUpdate);
        Facture testFacture = factureList.get(factureList.size() - 1);
        assertThat(testFacture.getRabais()).isEqualTo(UPDATED_RABAIS);
        assertThat(testFacture.getTva()).isEqualTo(UPDATED_TVA);
        assertThat(testFacture.getSousTotal()).isEqualTo(UPDATED_SOUS_TOTAL);
        assertThat(testFacture.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testFacture.getTypeFacturation()).isEqualTo(UPDATED_TYPE_FACTURATION);
        assertThat(testFacture.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFacture.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testFacture.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testFacture.getNumero()).isEqualTo(UPDATED_NUMERO);
    }

    @Test
    void putNonExistingFacture() throws Exception {
        int databaseSizeBeforeUpdate = factureRepository.findAll().size();
        facture.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facture.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facture))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFacture() throws Exception {
        int databaseSizeBeforeUpdate = factureRepository.findAll().size();
        facture.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facture))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFacture() throws Exception {
        int databaseSizeBeforeUpdate = factureRepository.findAll().size();
        facture.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactureMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facture)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFactureWithPatch() throws Exception {
        // Initialize the database
        factureRepository.save(facture);

        int databaseSizeBeforeUpdate = factureRepository.findAll().size();

        // Update the facture using partial update
        Facture partialUpdatedFacture = new Facture();
        partialUpdatedFacture.setId(facture.getId());

        partialUpdatedFacture
            .tva(UPDATED_TVA)
            .sousTotal(UPDATED_SOUS_TOTAL)
            .total(UPDATED_TOTAL)
            .status(UPDATED_STATUS)
            .reference(UPDATED_REFERENCE)
            .date(UPDATED_DATE);

        restFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacture.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFacture))
            )
            .andExpect(status().isOk());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeUpdate);
        Facture testFacture = factureList.get(factureList.size() - 1);
        assertThat(testFacture.getRabais()).isEqualTo(DEFAULT_RABAIS);
        assertThat(testFacture.getTva()).isEqualTo(UPDATED_TVA);
        assertThat(testFacture.getSousTotal()).isEqualTo(UPDATED_SOUS_TOTAL);
        assertThat(testFacture.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testFacture.getTypeFacturation()).isEqualTo(DEFAULT_TYPE_FACTURATION);
        assertThat(testFacture.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFacture.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testFacture.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testFacture.getNumero()).isEqualTo(DEFAULT_NUMERO);
    }

    @Test
    void fullUpdateFactureWithPatch() throws Exception {
        // Initialize the database
        factureRepository.save(facture);

        int databaseSizeBeforeUpdate = factureRepository.findAll().size();

        // Update the facture using partial update
        Facture partialUpdatedFacture = new Facture();
        partialUpdatedFacture.setId(facture.getId());

        partialUpdatedFacture
            .rabais(UPDATED_RABAIS)
            .tva(UPDATED_TVA)
            .sousTotal(UPDATED_SOUS_TOTAL)
            .total(UPDATED_TOTAL)
            .typeFacturation(UPDATED_TYPE_FACTURATION)
            .status(UPDATED_STATUS)
            .reference(UPDATED_REFERENCE)
            .date(UPDATED_DATE)
            .numero(UPDATED_NUMERO);

        restFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacture.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFacture))
            )
            .andExpect(status().isOk());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeUpdate);
        Facture testFacture = factureList.get(factureList.size() - 1);
        assertThat(testFacture.getRabais()).isEqualTo(UPDATED_RABAIS);
        assertThat(testFacture.getTva()).isEqualTo(UPDATED_TVA);
        assertThat(testFacture.getSousTotal()).isEqualTo(UPDATED_SOUS_TOTAL);
        assertThat(testFacture.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testFacture.getTypeFacturation()).isEqualTo(UPDATED_TYPE_FACTURATION);
        assertThat(testFacture.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFacture.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testFacture.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testFacture.getNumero()).isEqualTo(UPDATED_NUMERO);
    }

    @Test
    void patchNonExistingFacture() throws Exception {
        int databaseSizeBeforeUpdate = factureRepository.findAll().size();
        facture.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, facture.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facture))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFacture() throws Exception {
        int databaseSizeBeforeUpdate = factureRepository.findAll().size();
        facture.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facture))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFacture() throws Exception {
        int databaseSizeBeforeUpdate = factureRepository.findAll().size();
        facture.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactureMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(facture)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFacture() throws Exception {
        // Initialize the database
        factureRepository.save(facture);

        int databaseSizeBeforeDelete = factureRepository.findAll().size();

        // Delete the facture
        restFactureMockMvc
            .perform(delete(ENTITY_API_URL_ID, facture.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
