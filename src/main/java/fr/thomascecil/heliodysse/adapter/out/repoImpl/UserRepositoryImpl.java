package fr.thomascecil.heliodysse.adapter.out.repoImpl;

import fr.thomascecil.heliodysse.adapter.out.jpaEntity.UserEntity;
import fr.thomascecil.heliodysse.adapter.out.mapper.UserMapper;
import fr.thomascecil.heliodysse.adapter.out.repository.JpaUserRepository;
import fr.thomascecil.heliodysse.domain.model.entity.User;
import fr.thomascecil.heliodysse.domain.model.enums.userEnum.UserRole;
import fr.thomascecil.heliodysse.domain.model.enums.userEnum.UserStatus;
import fr.thomascecil.heliodysse.domain.port.out.UserRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository{
    private final JpaUserRepository jpaUserRepositoryRepo;
    private final UserMapper mapper;

    public UserRepositoryImpl(JpaUserRepository jpaRepo, JpaUserRepository jpaUserRepositoryRepo, UserMapper mapper) {
        this.jpaUserRepositoryRepo = jpaUserRepositoryRepo;
        this.mapper = mapper;
    }

    public Optional<User> findById(Long id) {
        return jpaUserRepositoryRepo.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        List<UserEntity> entities = jpaUserRepositoryRepo.findAll();
        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(jpaUserRepositoryRepo.findByEmail(email))
                .map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByPhone(String phone) {
        return Optional.ofNullable(jpaUserRepositoryRepo.findByPhone(phone))
                .map(mapper::toDomain);
    }

    @Override
    public List<User> findByLastName(String lastName) {
        List<UserEntity> entities = jpaUserRepositoryRepo.findByLastName(lastName);
        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findByFirstNameAndLastName(String firstName, String lastName) {
        List<UserEntity> entities = jpaUserRepositoryRepo.findByFirstNameAndLastName(firstName, lastName);
        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findByRole(UserRole role) {
        List<UserEntity> entities = jpaUserRepositoryRepo.findByRole(role);
        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findByStatus(UserStatus status) {
        List<UserEntity> entities = jpaUserRepositoryRepo.findByStatus(status);
        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findByDateCreation(LocalDateTime date) {
        List<UserEntity> entities = jpaUserRepositoryRepo.findByDateCreation(date);
        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findByDateCreationAfter(LocalDateTime date) {
        List<UserEntity> entities = jpaUserRepositoryRepo.findByDateCreationAfter(date);
        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findByDateCreationBefore(LocalDateTime date) {
        List<UserEntity> entities = jpaUserRepositoryRepo.findByDateCreationBefore(date);
        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findByDateCreationBetween(LocalDateTime start, LocalDateTime end) {
        List<UserEntity> entities = jpaUserRepositoryRepo.findByDateCreationBetween(start, end);
        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public User save(User user) {
        UserEntity entity = mapper.toEntity(user);
        UserEntity saved = jpaUserRepositoryRepo.save(entity);
        return mapper.toDomain(saved);                       // enregistrement en base
    }

    @Override
    public Optional<User> findByActivationToken(String token) {
        return Optional.ofNullable(jpaUserRepositoryRepo.findByActivationToken(token))
                .map(mapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        jpaUserRepositoryRepo.deleteById(id);
    }
}