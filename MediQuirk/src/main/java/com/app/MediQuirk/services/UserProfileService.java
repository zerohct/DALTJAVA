package com.app.MediQuirk.services;

import com.app.MediQuirk.model.UserProfile;
import com.app.MediQuirk.model.Users;
import com.app.MediQuirk.repository.UserProfileRepository;
import com.app.MediQuirk.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private  testservice fileStorageService;


    public UserProfile addUserProfile(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    public List<UserProfile> getAllUserProfiles() {
        return userProfileRepository.findAll();
    }

    public Optional<UserProfile> getUserProfileById(Long id) {
        return userProfileRepository.findById(id);
    }

    public UserProfile updateUserProfile(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    public void deleteUserProfileById(Long id) {
        userProfileRepository.deleteById(id);
    }

    public Page<UserProfile> getAllUserProfiles(Pageable pageable) {
        return userProfileRepository.findAll(pageable);
    }
    public UserProfile updateUserProfileWithAvatar(UserProfile userProfile, MultipartFile avatar, boolean useExternalStorage) throws IOException {
        if (avatar != null && !avatar.isEmpty()) {
            String avatarUrl = fileStorageService.storeFile(avatar, useExternalStorage);
            userProfile.setProfilePictureUrl(avatarUrl);
        }
        return userProfileRepository.save(userProfile);
    }

    public UserProfile removeAvatar(UserProfile userProfile) {
        userProfile.setProfilePictureUrl(null);
        return userProfileRepository.save(userProfile);
    }

}