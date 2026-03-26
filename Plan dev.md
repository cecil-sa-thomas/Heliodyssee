![[Pasted image 20250509021328.png]]



[1] UserApplicationService
    → Appelle :
      userRepository.findById(Long id)

↓

[2] UserRepository (interface métier dans le domaine)
    → Déclare (mais ne code pas) :
      Optional<User> findById(Long id);

↓

[3] UserRepositoryImpl (classe dans adapter.out.impl)
    → Implémente :
      public Optional<User> findById(Long id) {
          return jpaRepo.findById(id)
                        .map(mapper::toDomain);
      }

↓

[4] JpaUserRepository (interface dans adapter.out.repository)
    → Hérite automatiquement de :
      Optional<UserEntity> findById(Long id);
      (via JpaRepository<UserEntity, Long>)

↓

[5] Base de données
    → Exécute :
      SELECT * FROM user_ WHERE id_user = ?
