package com.personnel_accounting.profile;

import com.personnel_accounting.domain.Profile;

import java.util.List;

public interface ProfileDAO {
    Profile find(Long id);
    List<Profile> findAll();
    List<Profile> findByEducation(String education);
    Profile findByAddress(String address);
    Profile findByPhone(String phone);
    Profile findByEmail(String email);

    List<Profile> findByPhonePart(String phonePart);
    List<Profile> findByEmailPart(String emailPart);

    Profile save(Profile profile);
    Profile merge(Profile profile);
    boolean removeById(Long id);
    boolean remove(Profile profile);
}
