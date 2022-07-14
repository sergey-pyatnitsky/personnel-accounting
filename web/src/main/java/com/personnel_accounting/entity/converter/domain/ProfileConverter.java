package com.personnel_accounting.entity.converter.domain;

import com.personnel_accounting.domain.Profile;
import com.personnel_accounting.entity.dto.ProfileDTO;
import org.springframework.core.convert.converter.Converter;

public class ProfileConverter implements Converter<ProfileDTO, Profile> {

    @Override
    public Profile convert(ProfileDTO source) {
        Profile profile = new Profile();
        profile.setId(source.getId());
        profile.setEducation(source.getEducation());
        profile.setAddress(source.getAddress());
        profile.setPhone(source.getPhone());
        profile.setEmail(source.getEmail());
        profile.setSkills(source.getSkills());
        profile.setImageId(source.getImageId());
        return profile;
    }
}
