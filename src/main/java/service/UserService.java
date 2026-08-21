package service;

import dto.*;
import entity.User;
import exception.UserNotFoundException;
import mapper.UserMapper;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.UserRepository;

import java.util.List;

public class UserService implements UserServiceInterface {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(SessionFactory sessionFactory) {
        userRepository = new UserRepository(sessionFactory);
        userMapper = new UserMapper();
    }

    // Конструктор для тестов
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public GetUserDto findById(ReadUserByIdDto dto) {
        Long id = dto.getId();
        log.info("Finding user with id : {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id '" + id + "' not found!"));

        log.info("User found: {}", user.getName());
        return userMapper.toDto(user);
    }

    @Override
    public List<GetUserDto> findAll() {
        log.info("Finding all users");
        List<User> users = userRepository.findAll();

        log.info("Method findAll find {} users", users.size());
        return users.stream().map(userMapper::toDto).toList();
    }

    @Override
    public GetUserDto create(CreateUserDto dto) {
        log.info("Creating user: {}", dto.getName());

        User user = userMapper.toEntity(dto);
        if(user.getId() != null) {
            throw new IllegalArgumentException("ID must be null for new user");
        }

        User created = userRepository.create(user);
        log.info("User created with id: {}", created.getId());

        return userMapper.toDto(created);
    }

    @Override
    public GetUserDto update(UpdateUserDto dto) {
        log.info("Updating user: {}", dto.getId());

        Long id = dto.getId();
        if(id == null) {
            throw new IllegalArgumentException("ID must not be null for update");
        }

        User changeableUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id '" + id + "' not found!"));

        log.info("Changeable user found: {}", changeableUser.getName());

        User user = userMapper.toEntity(dto,changeableUser );
        User updatedUser = userRepository.update(user);
        log.info("User id : {} updated with name : {}, email: {}, age: {}",
                updatedUser.getId(), updatedUser.getName(), updatedUser.getEmail(), updatedUser.getAge());

        return userMapper.toDto(updatedUser);
    }

    @Override
    public void delete(DeleteUserDto dto) {
        log.info("Deleting user: {}", dto.getId());
        userRepository.removeById(dto.getId());
        log.info("User deleted: {}", dto.getId());
    }
}
