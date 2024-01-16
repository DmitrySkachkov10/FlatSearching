package by.skachkovdmitry.personal_account.service;


import by.skachkovdmitry.personal_account.core.dto.*;
import by.skachkovdmitry.personal_account.core.exception.*;
import by.skachkovdmitry.personal_account.core.role.Roles;
import by.skachkovdmitry.personal_account.core.status.Status;
import by.skachkovdmitry.personal_account.repo.api.IUserRepo;
import by.skachkovdmitry.personal_account.repo.entity.UserEntity;
import by.skachkovdmitry.personal_account.service.api.IUserService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class UserService implements IUserService {

    private final IUserRepo userRepo;

    public UserService(IUserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserEntity getUserByMail(String mail) {
        UserEntity userEntity = userRepo.findByMail(mail)
                .orElse(null);

        if (userEntity != null) {
            return userEntity;
        } else {
            throw new ValidationError("Запрос содержит некорректные данные. Измените запрос и отправьте его ещё раз");
        }
    }

    @Override
    @Transactional
    public void save(UserEntity userEntity) {

        if (userRepo.findByMail(userEntity.getMail()).isPresent()) {
            Errors<ValidationError> validationErrors = new Errors<>();
            ValidationError validationError = new ValidationError("Пользователь с такой почтой уже зарегистрирован", "email");
            validationErrors.add(validationError);
            throw new StructuredError(validationErrors);
        }

        if (userEntity.getStatus() == null) {
            userEntity.setRole(Roles.USER);
        }
        if (userEntity.getRole() == null) {
            userEntity.setStatus(Status.WAITING_ACTIVATION);
        }

        try {
            userRepo.save(userEntity);
        } catch (Exception e) {
            throw new DatabaseError("Проблема в системе обратитеь к администратору");
        }
    }


    @Override
    public boolean exists(UserEntity userEntity) {

        UserEntity user = userRepo.findByMailAndPassword(userEntity.getMail(), userEntity.getPassword()).orElse(null);
        if (user != null) {
            if (user.getStatus() != Status.WAITING_ACTIVATION) {
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

    @Override
    public PageOfUser getUsers(Pageable pageable) {
        Page<UserEntity> userEntities = userRepo.findAll(pageable);

        List<User> users = userEntities.stream().map(userEntity -> new User(userEntity.getUuid().toString(),
                        userEntity.getDtCreate(),
                        userEntity.getDtUpdate(),
                        userEntity.getFio(),
                        userEntity.getMail(),
                        userEntity.getRole().toString(),
                        userEntity.getStatus().toString()))
                .toList();

        PageOfUser pageOfUser = new PageOfUser(userEntities.getNumber(),
                userEntities.getSize(),
                userEntities.getTotalPages(),
                userEntities.getTotalElements(),
                userEntities.isFirst(),
                userEntities.getNumberOfElements(),
                userEntities.isLast(),
                users);

        pageOfUser.getUserList().forEach(System.out::println);

        return pageOfUser;
    }


    @Override
    public UserEntity getUserByUuid(UUID uuid) {
        UserEntity userEntity = userRepo.findById(uuid).orElse(null);
        if (userEntity != null) {
            return userEntity;
        } else {
            throw new ValidationError("Неверное данные");
        }
    }

    @Override
    @Transactional
    public void update(UserEntity userEntity) {
        userRepo.save(userEntity);
    }
}
