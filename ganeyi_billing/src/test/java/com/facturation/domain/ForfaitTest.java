package com.facturation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.facturation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ForfaitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Forfait.class);
        Forfait forfait1 = new Forfait();
        forfait1.setId("id1");
        Forfait forfait2 = new Forfait();
        forfait2.setId(forfait1.getId());
        assertThat(forfait1).isEqualTo(forfait2);
        forfait2.setId("id2");
        assertThat(forfait1).isNotEqualTo(forfait2);
        forfait1.setId(null);
        assertThat(forfait1).isNotEqualTo(forfait2);
    }
}
