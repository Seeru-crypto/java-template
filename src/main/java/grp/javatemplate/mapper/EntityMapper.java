package grp.javatemplate.mapper;

import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface EntityMapper<D, E> {
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    E toEntity( D dto );

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    List<E> toEntities( Collection<D> dtos );

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    Set<E> toEntitySet( Set<D> dtos );

    D toDto( E entity );

    List<D> toDto( Collection<E> entities );

    Page<D> toDto(Page<E> entities );
}
