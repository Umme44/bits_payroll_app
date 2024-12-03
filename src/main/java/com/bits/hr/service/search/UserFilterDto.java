package com.bits.hr.service.search;

import com.bits.hr.util.annotation.ValidateNaturalText;

import java.util.Objects;

public class UserFilterDto {

    @ValidateNaturalText
    private String searchText;
    private String authorities;

    public UserFilterDto() {}

    public UserFilterDto(String searchText, String authorities) {
        this.searchText = searchText;
        this.authorities = authorities;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserFilterDto)) return false;
        UserFilterDto filterDto = (UserFilterDto) o;
        return getSearchText().equals(filterDto.getSearchText()) && getAuthorities().equals(filterDto.getAuthorities());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSearchText(), getAuthorities());
    }
}
