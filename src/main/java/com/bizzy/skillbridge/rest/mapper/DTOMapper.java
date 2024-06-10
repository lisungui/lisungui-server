package com.bizzy.skillbridge.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.bizzy.skillbridge.entity.Gig;
import com.bizzy.skillbridge.entity.User;
import com.bizzy.skillbridge.rest.dto.GigGetDTO;
import com.bizzy.skillbridge.rest.dto.GigPostDTO;
import com.bizzy.skillbridge.rest.dto.UserGetDTO;
import com.bizzy.skillbridge.rest.dto.UserPostDTO;

@Mapper(componentModel = "spring")
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "email", target = "email")
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source="title", target="title")
    @Mapping(source="description", target="description")
    @Mapping(source="category", target="category")
    @Mapping(source="price", target="price")
    @Mapping(source="status", target="status")
    @Mapping(source="duration", target="duration")
    Gig convertGigPostDTOtoEntity(GigPostDTO gigPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "email", target = "email")
    UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source="title", target="title")
    @Mapping(source="description", target="description")
    @Mapping(source="category", target="category")
    @Mapping(source="price", target="price")
    @Mapping(source="status", target="status")
    @Mapping(source="createdDate", target="createdDate")
    @Mapping(source="updatedDate", target="updatedDate")
    @Mapping(source="duration", target="duration")
    GigGetDTO convertEntityToGigGetDTO(Gig gig);

}
