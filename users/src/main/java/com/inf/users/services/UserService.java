package com.inf.users.services;

import com.inf.users.dtos.EditUserDto;
import com.inf.users.dtos.GetUserDto;
import com.inf.users.dtos.LoginDto;
import com.inf.users.dtos.PostUserDto;
import com.inf.users.models.User;

import java.util.List;

public interface UserService {
    GetUserDto create(PostUserDto userDTO);
    GetUserDto login(LoginDto loginDto);
    List<GetUserDto> getAllUsers();
    GetUserDto getUserById(Long id);
    GetUserDto editUser(Long id, EditUserDto editUserDto);
}
