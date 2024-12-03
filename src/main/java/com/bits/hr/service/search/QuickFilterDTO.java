package com.bits.hr.service.search;

import com.bits.hr.util.annotation.ValidateNaturalText;

public class QuickFilterDTO {

    @ValidateNaturalText
    private String searchText;

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}
