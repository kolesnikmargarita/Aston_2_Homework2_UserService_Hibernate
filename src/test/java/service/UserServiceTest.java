package service;

import dto.*;
import entity.User;
import exception.UserNotFoundException;
import mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, userMapper);
    }

    @Test
    void testCreateUser_Success() {
        CreateUserDto dto = new CreateUserDto("Alice", "alice@test.com", 25);

        User user = new User();
        user.setName("Alice");
        user.setEmail("alice@test.com");
        user.setAge(25);

        User created = new User();
        created.setId(1L);
        created.setName("Alice");
        created.setEmail("alice@test.com");
        created.setAge(25);
        created.setCreated_at(LocalDate.now());

        GetUserDto expectedDto = new GetUserDto(1L, "Alice", "alice@test.com", 25, LocalDate.now());

        when(userMapper.toEntity(any(CreateUserDto.class))).thenReturn(user);
        when(userRepository.create(any(User.class))).thenReturn(created);
        when(userMapper.toDto(any(User.class))).thenReturn(expectedDto);

        GetUserDto result = userService.create(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Alice", result.getName());

        verify(userMapper).toEntity(any(CreateUserDto.class));
        verify(userRepository).create(any(User.class));
        verify(userMapper).toDto(any(User.class));
    }

    @Test
    void testCreateUser_WithId_ShouldThrowException() {
        CreateUserDto dto = new CreateUserDto("Alice", "alice@test.com", 25);

        User user = new User();
        user.setId(1L);
        user.setName("Alice");

        when(userMapper.toEntity(any(CreateUserDto.class))).thenReturn(user);

        assertThrows(IllegalArgumentException.class, () -> userService.create(dto));

        verify(userMapper).toEntity(any(CreateUserDto.class));
        verify(userRepository, never()).create(any());
    }

    @Test
    void testFindById_UserExists() {
        Long id = 1L;
        ReadUserByIdDto dto = new ReadUserByIdDto(id);

        User user = new User();
        user.setId(id);
        user.setName("Bob");
        user.setEmail("bob@test.com");
        user.setAge(30);

        GetUserDto expectedDto = new GetUserDto(id, "Bob", "bob@test.com", 30, LocalDate.now());

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(userMapper.toDto(any(User.class))).thenReturn(expectedDto);

        GetUserDto result = userService.findById(dto);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Bob", result.getName());

        verify(userRepository).findById(any(Long.class));
        verify(userMapper).toDto(any(User.class));
    }

    @Test
    void testFindById_UserNotFound_ShouldThrowException() {
        Long id = 99L;
        ReadUserByIdDto dto = new ReadUserByIdDto(id);

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(dto));

        verify(userRepository).findById(any(Long.class));
        verify(userMapper, never()).toDto(any());
    }

    @Test
    void testFindAll_Success() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("Alice");
        user1.setEmail("alice@test.com");
        user1.setAge(25);

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Bob");
        user2.setEmail("bob@test.com");
        user2.setAge(30);

        List<User> users = List.of(user1, user2);

        GetUserDto dto1 = new GetUserDto(1L, "Alice", "alice@test.com", 25, LocalDate.now());
        GetUserDto dto2 = new GetUserDto(2L, "Bob", "bob@test.com", 30, LocalDate.now());

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDto(user1)).thenReturn(dto1);
        when(userMapper.toDto(user2)).thenReturn(dto2);

        List<GetUserDto> result = userService.findAll();

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getName());

        verify(userRepository).findAll();
        verify(userMapper, times(2)).toDto(any(User.class));
    }

    @Test
    void testUpdateUser_Success() {
        Long id = 1L;
        UpdateUserDto dto = new UpdateUserDto(id, "Charles", "charles@test.com", 28);

        User existingUser = new User();
        existingUser.setId(id);
        existingUser.setName("Charlie");
        existingUser.setEmail("charlie@test.com");
        existingUser.setAge(22);

        User updatedUser = new User();
        updatedUser.setId(id);
        updatedUser.setName("Charles");
        updatedUser.setEmail("charles@test.com");
        updatedUser.setAge(28);

        GetUserDto expectedDto = new GetUserDto(id, "Charles", "charles@test.com", 28, LocalDate.now());

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(existingUser));
        when(userMapper.toEntity(any(UpdateUserDto.class), any(User.class))).thenReturn(updatedUser);
        doReturn(updatedUser).when(userRepository).update(any(User.class));
        when(userMapper.toDto(any(User.class))).thenReturn(expectedDto);

        GetUserDto result = userService.update(dto);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Charles", result.getName());

        ArgumentCaptor<UpdateUserDto> dtoCaptor = ArgumentCaptor.forClass(UpdateUserDto.class);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userMapper).toEntity(dtoCaptor.capture(), userCaptor.capture());

        assertEquals("Charles", dtoCaptor.getValue().getName());
        assertEquals("Charlie", userCaptor.getValue().getName());

        verify(userRepository).findById(any(Long.class));
        verify(userRepository).update(any(User.class));
        verify(userMapper).toDto(any(User.class));
    }

    @Test
    void testUpdateUser_WithNullId_ShouldThrowException() {
        UpdateUserDto dto = new UpdateUserDto(null, "Charles", "charles@test.com", 28);

        assertThrows(IllegalArgumentException.class, () -> userService.update(dto));

        verify(userRepository, never()).findById(any());
        verify(userRepository, never()).update(any());
        verify(userMapper, never()).toEntity(any(), any());
    }

    @Test
    void testUpdateUser_UserNotFound_ShouldThrowException() {
        Long id = 99L;
        UpdateUserDto dto = new UpdateUserDto(id, "Charles", "charles@test.com", 28);

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.update(dto));

        verify(userRepository).findById(any(Long.class));
        verify(userRepository, never()).update(any());
        verify(userMapper, never()).toEntity(any(), any());
    }

    @Test
    void testDeleteUser_Success() {
        Long id = 1L;
        DeleteUserDto dto = new DeleteUserDto(id);

        doNothing().when(userRepository).removeById(any(Long.class));

        userService.delete(dto);

        verify(userRepository).removeById(any(Long.class));
    }
}