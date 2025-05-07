package com.inf.users.services;

import com.inf.users.dtos.put.EditUserDtoBase;
import com.inf.users.dtos.get.GetUserDto;
import com.inf.users.dtos.post.LoginDto;
import com.inf.users.dtos.post.PostUserTutorDto;
import com.inf.users.dtos.put.PutUserTutorDto;

import java.util.List;

public interface UserService {
    GetUserDto createUserTutor(PostUserTutorDto userDTO);
    GetUserDto login(LoginDto loginDto);
    List<GetUserDto> getAllUsers();
    GetUserDto getUserById(Long id);
    GetUserDto editTutor(Long id, PutUserTutorDto putUserTutorDto);
}
