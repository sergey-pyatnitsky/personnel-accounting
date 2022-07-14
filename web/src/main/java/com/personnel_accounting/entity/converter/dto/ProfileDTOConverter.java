package com.personnel_accounting.entity.converter.dto;

import com.personnel_accounting.domain.Profile;
import com.personnel_accounting.entity.dto.ProfileDTO;
import org.springframework.core.convert.converter.Converter;

public class ProfileDTOConverter implements Converter<Profile, ProfileDTO> {

    @Override
    public ProfileDTO convert(Profile source) {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(source.getId());
        profileDTO.setEducation(source.getEducation());
        profileDTO.setAddress(source.getAddress());
        profileDTO.setPhone(source.getPhone());
        profileDTO.setEmail(source.getEmail());
        profileDTO.setSkills(source.getSkills());
        profileDTO.setImageId(source.getImageId());
        return profileDTO;
    }
}
