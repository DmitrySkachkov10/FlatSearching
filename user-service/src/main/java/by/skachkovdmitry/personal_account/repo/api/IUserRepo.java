package by.skachkovdmitry.personal_account.repo.api;

import by.skachkovdmitry.personal_account.core.status.Status;
import by.skachkovdmitry.personal_account.repo.entity.UserEntity;
import feign.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;


public interface IUserRepo extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByMailAndPassword(String mail, String password);

    Optional<UserEntity> findByMail(String mail);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity u SET u.status = :status WHERE u.mail = :email")
    void updateStatusByEmail(@Param("email") String email, @Param("status") Status status);

}
