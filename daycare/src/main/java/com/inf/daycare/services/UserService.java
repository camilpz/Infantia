package com.inf.daycare.services;

import com.inf.daycare.dtos.get.GetUserDto;
import com.inf.daycare.dtos.post.LoginDto;
import com.inf.daycare.dtos.post.PostUserDto;
import com.inf.daycare.dtos.put.PutUserDto;
import com.inf.daycare.models.User;

import java.util.List;

public interface UserService {
    GetUserDto createUser(PostUserDto postUserDto);
    List<GetUserDto> getAllUsers();
    GetUserDto getUserById(Long id);
    GetUserDto editUser(Long id, PutUserDto putUserDto);
    User getUserOrThrow(Long id);
}
