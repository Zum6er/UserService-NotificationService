package ru.zumber.learning.userservice.service;



import ru.zumber.learning.userservice.dto.UserDTO;
import ru.zumber.learning.userservice.dto.UserDTOForCreateAndUpdate;

import java.util.List;

public interface UserService {

    UserDTO getUser(Integer id);

    List<UserDTO> findAll();

    UserDTO save(UserDTOForCreateAndUpdate userDTOForCreateAndUpdate);

    UserDTO update(Integer id, UserDTOForCreateAndUpdate userDTOForCreateAndUpdate);

    void delete(Integer id);

    boolean emailExists(String email);
}
