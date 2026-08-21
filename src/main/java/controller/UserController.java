package controller;

import dto.*;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

import java.util.List;

public class UserController {

    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(SessionFactory sessionFactory) {
        userService = new UserService(sessionFactory);
    }

    public GetUserDto create(CreateUserDto dto) {
        log.info("Creating user with name: {}", dto.getName());
        return userService.create(dto);
    }

    public List<GetUserDto> readAll() {
        log.info("Reading all users");
        return userService.findAll();
    }

    public GetUserDto readById(ReadUserByIdDto dto) {
        log.info("Reading user with id: {}", dto.getId());
        return userService.findById(dto);
    }

    public GetUserDto update(UpdateUserDto dto) {
        log.info("Updating user with id: {}", dto.getId());
        return userService.update(dto);
    }

    public void delete(DeleteUserDto dto) {
        log.info("Deleting user with id: {}", dto.getId());
        userService.delete(dto);
    }

}
