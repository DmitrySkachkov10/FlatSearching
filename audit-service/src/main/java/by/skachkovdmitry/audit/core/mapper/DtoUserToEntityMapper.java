package by.skachkovdmitry.audit.core.mapper;

import by.skachkovdmitry.audit.core.enums.Roles;
import by.skachkovdmitry.audit.core.dto.User;
import by.skachkovdmitry.audit.repository.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class DtoUserToEntityMapper {

    public DtoUserToEntityMapper() {
    }

    public User mapEntityToDto(UserEntity user) {
        return new User(user.getUuid().toString(), user.getMail(), user.getFio(), user.getRole().toString());
    }

    public UserEntity mapDtoToEntity(User user) {
        if (user != null) {
            return new UserEntity(UUID.fromString(user.getUuid()),
                    user.getMail(),
                    user.getFio(),
                    Roles.valueOf(user.getRole()));
        }
        return null;
    }
}