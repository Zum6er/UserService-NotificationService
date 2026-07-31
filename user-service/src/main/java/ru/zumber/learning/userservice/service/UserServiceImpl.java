package ru.zumber.learning.userservice.service;

import ru.zumber.learning.formessagedto.MessageDTO;
import ru.zumber.learning.formessagedto.Operation;
import ru.zumber.learning.userservice.dto.UserDTO;
import ru.zumber.learning.userservice.dto.UserDTOForCreateAndUpdate;
import ru.zumber.learning.userservice.entity.User;
import ru.zumber.learning.userservice.exception.NoCorrectUser;
import ru.zumber.learning.userservice.exception.UserNotFound;
import ru.zumber.learning.userservice.mapping.UserMapper;
import ru.zumber.learning.userservice.repository.UserRepository;
import ru.zumber.learning.userservice.validation.UserValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserValidation userValidation;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserEventProducer userEventProducer;

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUser(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new UserNotFound("Пользователь с id = " + id + " не найден"));
        return userMapper.toUserDTO(user);
    }

    @Override
    @Transactional
    public UserDTO save(UserDTOForCreateAndUpdate userDTOForCreateAndUpdate) {
        validate(userDTOForCreateAndUpdate);
        User user = userMapper.toUserFromCreateDTO(userDTOForCreateAndUpdate);
        user.setCreatedAt(LocalDate.now());
        User saveUser = userRepository.save(user);
        userEventProducer.sendMessage(new MessageDTO(Operation.CREATE, user.getEmail()));
        return userMapper.toUserDTO(saveUser);
    }


    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::toUserDTO).toList();
    }

    @Override
    @Transactional
    public UserDTO update(Integer id, UserDTOForCreateAndUpdate userDTOForCreateAndUpdate) {
        validate(userDTOForCreateAndUpdate);
        User userForUpdate = userRepository.findById(id).orElseThrow(() ->
                new UserNotFound("Пользователь с id = " + id + " не найден при обновление данных"));
        userForUpdate.setName(userDTOForCreateAndUpdate.getName());
        userForUpdate.setEmail(userDTOForCreateAndUpdate.getEmail());
        userForUpdate.setAge(userDTOForCreateAndUpdate.getAge());
        User savedUser = userRepository.save(userForUpdate);
        return userMapper.toUserDTO(savedUser);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new UserNotFound("Пользователь с id = " + id + "не найден при удалении пользователя"));
        userRepository.delete(user);
        userEventProducer.sendMessage(new MessageDTO(Operation.DELETE, user.getEmail()));
    }

    private void validate(UserDTOForCreateAndUpdate userDTOForCreateAndUpdate) {
        List<String> errorList = userValidation.validateUser(userDTOForCreateAndUpdate);
        if (!errorList.isEmpty()) {
            throw new NoCorrectUser("Не корректные данные пользователя: " + String.join(". ", errorList));
        }
    }
}
