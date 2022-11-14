package com.facturation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.facturation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PricingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pricing.class);
        Pricing pricing1 = new Pricing();
        pricing1.setId("id1");
        Pricing pricing2 = new Pricing();
        pricing2.setId(pricing1.getId());
        assertThat(pricing1).isEqualTo(pricing2);
        pricing2.setId("id2");
        assertThat(pricing1).isNotEqualTo(pricing2);
        pricing1.setId(null);
        assertThat(pricing1).isNotEqualTo(pricing2);
    }
}
