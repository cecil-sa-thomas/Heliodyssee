package fr.thomascecil.heliodysse.adapter.in.dto.mapper;

import fr.thomascecil.heliodysse.adapter.in.dto.request.user.UserRequestDTO;
import fr.thomascecil.heliodysse.adapter.in.dto.request.user.UserUpdateDTO;
import fr.thomascecil.heliodysse.adapter.in.dto.response.user.UserResponseDTO;
import fr.thomascecil.heliodysse.domain.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserDtoMapper {

    User toDomain(UserRequestDTO dto);

    UserResponseDTO toDto(User user);

    @Mapping(target = "idUser", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastConnected", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "role", ignore = true)
    void updateUserFromDto(UserUpdateDTO dto, @MappingTarget User user);

}

