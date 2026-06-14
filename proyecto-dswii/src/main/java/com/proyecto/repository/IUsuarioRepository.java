package com.proyecto.repository;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.model.Usuario;
 
@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {
    Usuario findByEmailAndPassword(String email, String password);
    Usuario findByEmail(String email);
    boolean existsByEmail(String email);
}