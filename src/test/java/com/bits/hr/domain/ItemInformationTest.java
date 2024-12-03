package com.bits.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bits.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemInformationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemInformation.class);
        ItemInformation itemInformation1 = new ItemInformation();
        itemInformation1.setId(1L);
        ItemInformation itemInformation2 = new ItemInformation();
        itemInformation2.setId(itemInformation1.getId());
        assertThat(itemInformation1).isEqualTo(itemInformation2);
        itemInformation2.setId(2L);
        assertThat(itemInformation1).isNotEqualTo(itemInformation2);
        itemInformation1.setId(null);
        assertThat(itemInformation1).isNotEqualTo(itemInformation2);
    }
}
