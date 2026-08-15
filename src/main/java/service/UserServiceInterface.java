package service;

import dto.*;

import java.util.List;

public interface UserServiceInterface {

    GetUserDto findById(ReadUserByIdDto dto);
    List<GetUserDto> findAll();
    GetUserDto create(CreateUserDto dto);
    GetUserDto update(UpdateUserDto dto);
    void delete(DeleteUserDto dto);
}
