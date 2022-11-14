package com.facturation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.facturation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResultatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Resultat.class);
        Resultat resultat1 = new Resultat();
        resultat1.setId("id1");
        Resultat resultat2 = new Resultat();
        resultat2.setId(resultat1.getId());
        assertThat(resultat1).isEqualTo(resultat2);
        resultat2.setId("id2");
        assertThat(resultat1).isNotEqualTo(resultat2);
        resultat1.setId(null);
        assertThat(resultat1).isNotEqualTo(resultat2);
    }
}
