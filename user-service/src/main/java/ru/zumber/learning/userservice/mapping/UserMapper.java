package ru.zumber.learning.userservice.mapping;

import org.mapstruct.Mapping;
import ru.zumber.learning.userservice.dto.UserDTO;
import ru.zumber.learning.userservice.dto.UserDTOForCreateAndUpdate;
import ru.zumber.learning.userservice.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toUserDTO(User user);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User toUserFromCreateDTO(UserDTOForCreateAndUpdate userDTOForCreateAndUpdate);
}
