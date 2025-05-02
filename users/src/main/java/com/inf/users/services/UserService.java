package com.inf.users.services;

import com.inf.users.dtos.GetUserDto;
import com.inf.users.dtos.PostUserDto;

public interface UserService {
    GetUserDto create(PostUserDto userDTO);
}
