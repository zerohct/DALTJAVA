package com.app.MediQuirk.services;

import com.app.MediQuirk.model.UserProfile;
import com.app.MediQuirk.model.Users;
import com.app.MediQuirk.repository.UserProfileRepository;
import com.app.MediQuirk.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    public Users addUser(Users user) {
        return usersRepository.save(user);
    }

    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    public Optional<Users> getUserById(Long id) {
        return usersRepository.findById(id);
    }

    public Users updateUser(Users user) {
        return usersRepository.save(user);
    }

    public void deleteUserById(Long id) {
        usersRepository.deleteById(id);
    }
}