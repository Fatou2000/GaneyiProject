package com.facturation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.facturation.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductLicenseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductLicense.class);
        ProductLicense productLicense1 = new ProductLicense();
        productLicense1.setId("id1");
        ProductLicense productLicense2 = new ProductLicense();
        productLicense2.setId(productLicense1.getId());
        assertThat(productLicense1).isEqualTo(productLicense2);
        productLicense2.setId("id2");
        assertThat(productLicense1).isNotEqualTo(productLicense2);
        productLicense1.setId(null);
        assertThat(productLicense1).isNotEqualTo(productLicense2);
    }
}
