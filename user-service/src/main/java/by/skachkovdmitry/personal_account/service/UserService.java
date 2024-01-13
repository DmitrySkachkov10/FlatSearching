package by.skachkovdmitry.personal_account.service;


import by.skachkovdmitry.personal_account.core.dto.User;
import by.skachkovdmitry.personal_account.core.dto.UserLogin;
import by.skachkovdmitry.personal_account.core.dto.UserRegistration;
import by.skachkovdmitry.personal_account.core.exception.*;
import by.skachkovdmitry.personal_account.core.role.Roles;
import by.skachkovdmitry.personal_account.core.status.Status;
import by.skachkovdmitry.personal_account.repo.api.IUserRepo;
import by.skachkovdmitry.personal_account.repo.entity.UserEntity;
import by.skachkovdmitry.personal_account.service.api.IUserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class UserService implements IUserService {

    private final IUserRepo userRepo;

    public UserService(IUserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User getUserByMail(String mail) {
        UserEntity userEntity = userRepo.findByMail(mail)
                .orElse(null);

        if (userEntity != null) {
            User user = new User(userEntity.getUuid(),
                    userEntity.getDtCreate(),
                    userEntity.getDtUpdate(),
                    userEntity.getFio(),
                    userEntity.getMail(),
                    userEntity.getRole().toString(),
                    String.valueOf(userEntity.getStatus()));
            return user;
        } else {
            throw new ValidationError("Запрос содержит некорректные данные. Измените запрос и отправьте его ещё раз");
        }


    }

    @Override
    @Transactional
    public void save(UserRegistration userRegistration) {

        UserEntity userEntity = new UserEntity();
        userEntity.setUuid(UUID.randomUUID().toString());
        userEntity.setDtCreate(LocalDateTime.now());
        userEntity.setDtCreate(LocalDateTime.now());
        userEntity.setMail(userRegistration.getMail());
        userEntity.setPassword(userRegistration.getPassword());
        userEntity.setFio(userRegistration.getFio());
        userEntity.setRole(Roles.USER);
        userEntity.setStatus(Status.WAITING_ACTIVATION);

        if (userRepo.findByMail(userRegistration.getMail()).isPresent()) {
            Errors<ValidationError> validationErrors = new Errors<>();
            ValidationError validationError = new ValidationError("Пользователь с такой почтой уже зарегистрирован", "email");
            validationErrors.add(validationError);
            throw new StructuredError(validationErrors);

        }
        try {
            userRepo.save(userEntity);
        } catch (Exception e) {
            throw new DatabaseError("Проблема в системе обратитеь к администратору");
        }

    }

    @Override
    public boolean exists(UserLogin userLogin) {

        UserEntity userEntity = userRepo.findByMailAndPassword(userLogin.getMail(), userLogin.getPassword()).orElse(null);
        if (userEntity != null) {
            if (userEntity.getStatus() != Status.WAITING_ACTIVATION) {
                return true;
            } else {
                throw new VerificationError("Пользователь не прошел верификацию");
            }
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public void updateUserStatus(String mail, Status status) {
        userRepo.updateStatusByEmail(mail, status);
    }
}
