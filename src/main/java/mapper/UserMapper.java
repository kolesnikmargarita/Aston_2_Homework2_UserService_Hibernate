package mapper;

import dto.CreateUserDto;
import dto.GetUserDto;
import dto.UpdateUserDto;
import entity.User;

public class UserMapper {

    public GetUserDto toDto(User entity) {
        GetUserDto dto = new GetUserDto();

        dto.setId(entity.getId());
        dto.setAge(entity.getAge());
        dto.setEmail(entity.getEmail());
        dto.setName(entity.getName());
        dto.setCreated_at(entity.getCreated_at());

        return dto;
    }

    public User toEntity(CreateUserDto dto) {
        User entity = new User();

        entity.setAge(dto.getAge());
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());

        return entity;
    }

    public User toEntity(UpdateUserDto dto, User user) {

        if(dto.getAge() != null) {
            user.setAge(dto.getAge());
        }
        if(dto.getName() != null) {
            user.setName(dto.getName());
        }
        if(dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }

        return user;
    }
}
