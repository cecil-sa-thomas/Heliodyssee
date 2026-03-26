package fr.thomascecil.heliodysse.adapter.out.repository;

import aj.org.objectweb.asm.commons.Remapper;
import fr.thomascecil.heliodysse.adapter.out.jpaEntity.UserEntity;

import fr.thomascecil.heliodysse.domain.model.enums.userEnum.UserRole;
import fr.thomascecil.heliodysse.domain.model.enums.userEnum.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
    public UserEntity findByEmail(String email);
    public UserEntity findByPhone(String phone);
    public List<UserEntity> findByLastName(String lastName);
    public List<UserEntity> findByFirstNameAndLastName(String firstName, String lastName);
    public List<UserEntity> findByRole(UserRole role);
    public List<UserEntity> findByStatus(UserStatus status);
    public List<UserEntity> findByDateCreation(LocalDateTime dateCreation);
    public List<UserEntity> findByDateCreationAfter(LocalDateTime date);
    public List<UserEntity> findByDateCreationBefore(LocalDateTime date);
    public List<UserEntity> findByDateCreationBetween(LocalDateTime start, LocalDateTime end);

    public UserEntity findByActivationToken(String token);
}
