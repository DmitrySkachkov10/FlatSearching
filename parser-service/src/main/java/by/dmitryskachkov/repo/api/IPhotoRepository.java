package by.dmitryskachkov.repo.api;

import by.dmitryskachkov.repo.entity.Photo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface IPhotoRepository extends JpaRepository<Photo, UUID> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Photo p1 " +
            "WHERE p1.photo_uuid NOT IN (" +
            "    SELECT MIN(p2.photo_uuid) " +
            "    FROM Photo p2 " +
            "    GROUP BY p2.photoUrl" +
            ")")
    void removeDuplicatePhotos();
}
