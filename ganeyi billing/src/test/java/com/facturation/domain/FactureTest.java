package com.facturation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.facturation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FactureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Facture.class);
        Facture facture1 = new Facture();
        facture1.setId("id1");
        Facture facture2 = new Facture();
        facture2.setId(facture1.getId());
        assertThat(facture1).isEqualTo(facture2);
        facture2.setId("id2");
        assertThat(facture1).isNotEqualTo(facture2);
        facture1.setId(null);
        assertThat(facture1).isNotEqualTo(facture2);
    }
}
