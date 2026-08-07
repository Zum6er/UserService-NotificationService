package ru.zumber.learning.userservice.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.zumber.learning.userservice.controller.UserRestController;
import ru.zumber.learning.userservice.dto.UserDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserDTO, EntityModel<UserDTO>> {
    @Override
    public EntityModel<UserDTO> toModel(UserDTO userDTO) {
        return EntityModel.of(userDTO)
                .add(linkTo(methodOn(UserRestController.class)
                        .getUserById(userDTO.getId()))
                        .withSelfRel())
                .add(linkTo(methodOn(UserRestController.class)
                        .getAllUsers())
                        .withRel("collection"))
                .add(linkTo(methodOn(UserRestController.class)
                        .updateUser(userDTO.getId(), null))
                        .withRel("update"))
                .add(linkTo(methodOn(UserRestController.class)
                        .deleteUserById(userDTO.getId()))
                        .withRel("delete"));
    }
}
