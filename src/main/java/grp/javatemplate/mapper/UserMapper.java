package grp.javatemplate.mapper;

import grp.javatemplate.controller.dto.UserDto;
import grp.javatemplate.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDto, User> {
//    If mapper uses other mappers, then add them to the uses attribute.
//    @Mapper(componentModel = "spring", uses = {OrderItemMapper.class, TagMapper.class})

//    If property are nested then use dot notation.
//    @Mapping(target = "stationName", source = "entity.station.name")
//    FlowStepStationDto toDto( FlowStepStation entity );


//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "createdAt", ignore = true)
//    @Mapping(target = "modifiedBy", ignore = true)
//    @Mapping(target = "modifiedAt", ignore = true)
//    User toEntity( UserDto dto );

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    Page<UserDto> toDto( Page<User> entity );
}
