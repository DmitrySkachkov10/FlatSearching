package by.skachkovdmitry.personal_account.service;

import by.dmitryskachkov.entity.ValidationError;
import by.skachkovdmitry.personal_account.core.dto.PageOfUser;
import by.skachkovdmitry.personal_account.core.dto.User;
import by.skachkovdmitry.personal_account.core.dto.UserCreate;
import by.skachkovdmitry.personal_account.core.role.Roles;
import by.skachkovdmitry.personal_account.core.status.Status;
import by.skachkovdmitry.personal_account.repo.entity.UserEntity;
import by.skachkovdmitry.personal_account.service.api.IAdminService;
import by.skachkovdmitry.personal_account.service.api.IUserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@Slf4j
public class AdminService implements IAdminService {

    private final IUserService userService;

    public AdminService(IUserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional
    public void create(UserCreate userCreate) {

        UserEntity userEntity = new UserEntity();
        userEntity.setUuid(UUID.randomUUID());
        userEntity.setMail(userCreate.getMail());
        userEntity.setPassword(userCreate.getPassword());
        userEntity.setFio(userCreate.getFio());

        userEntity.setRole(Roles.valueOf(userCreate.getRole()));
        userEntity.setStatus(Status.valueOf(userCreate.getStatus()));

        userEntity.setDtCreate(LocalDateTime.now());
        userEntity.setDtUpdate(userEntity.getDtCreate());

        userService.save(userEntity);
    }

    @Override
    public User getUser(UUID uuid) {
        UserEntity userEntity = userService.getUserByUuid(uuid);

        return new User(userEntity.getUuid().toString(),
                userEntity.getDtCreate(),
                userEntity.getDtUpdate(),
                userEntity.getFio(),
                userEntity.getMail(),
                userEntity.getRole().toString(),
                userEntity.getStatus().toString());
    }

    @Override
    public PageOfUser getUserList(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return userService.getUsers(pageable);
    }

    @Override
    @Transactional
    public void update(UserCreate userCreate, UUID uuid, long lastUpdate) {

        UserEntity userEntity= userService.getUserByUuid(uuid);

        LocalDateTime latestUpdateTime = userEntity.getDtUpdate().truncatedTo(ChronoUnit.MILLIS);
        LocalDateTime lastUpdateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(lastUpdate), ZoneOffset.UTC);

        System.out.println(latestUpdateTime + " " + lastUpdateTime);

        if (!latestUpdateTime.equals(lastUpdateTime)) {
            throw new ValidationError("Файл уже редактировался");
        }

        userEntity.setFio(userCreate.getFio());
        userEntity.setMail(userCreate.getMail());
        userEntity.setPassword(userCreate.getPassword());
        userEntity.setRole(Roles.valueOf(userCreate.getRole()));
        userEntity.setStatus(Status.valueOf(userCreate.getStatus()));
        userEntity.setDtUpdate(LocalDateTime.now());

        try {
            userService.update(userEntity);
        } catch (OptimisticEntityLockException e) {
            throw new ValidationError("Файл уже редактировался");
        }
    }
}
