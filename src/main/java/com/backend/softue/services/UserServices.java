package com.backend.softue.services;

import com.backend.softue.models.FotoUsuario;
import com.backend.softue.models.SingInToken;
import com.backend.softue.models.User;
import com.backend.softue.repositories.FotoRepository;
import com.backend.softue.repositories.SingInTokenRepository;
import com.backend.softue.repositories.UserRepository;
import com.backend.softue.security.Hashing;
import com.backend.softue.utils.auxiliarClases.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServices {

    private final String[] validRol = {"estudiante", "coordinador", "administrativo", "docente"};
    @Autowired
    private SingInTokenRepository singInTokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Hashing encrypt;

    @Autowired
    private FotoRepository fotoRepository;

    public String login(LoginResponse user) {
        SingInToken token = singInTokenRepository.findTokenByEmail(user.getEmail());
        if (token != null) this.singInTokenRepository.delete(token);
        User userSaved = this.userRepository.findByCorreo(user.getEmail());
        if (userSaved == null) throw new RuntimeException("Unregistered user");
        if (!encrypt.validate(user.getPassword(), userSaved.getContrasenia()))
            throw new RuntimeException("Invalid Password");
        String jwt = this.encrypt.generarJWT(userSaved.getCorreo(), userSaved.getTipo_usuario());
        LocalDateTime newDateTime = LocalDateTime.now().plus(Duration.ofHours(1));
        this.singInTokenRepository.save(new SingInToken(jwt, newDateTime, userSaved));
        return jwt;
    }

    public void registerUser(User user) {
        User result = this.userRepository.findByCorreo(user.getCorreo());
        if (result != null) throw new RuntimeException("User already exists");
        if (!this.validateUserRol(user.getTipo_usuario())) throw new RuntimeException("Use has Invalid Type");
        user.setTipo_usuario(user.getTipo_usuario().toLowerCase());
        user.setContrasenia(encrypt.hash(user.getContrasenia()));
        User userData = this.userRepository.save(user);
    }

    private Boolean validateUserRol(String rol) {
        rol = rol.toLowerCase();
        for (String data : this.validRol) {
            if (data.equals(rol)) return true;
        }
        return false;
    }

    public String savePicture(MultipartFile file, Integer id) throws IOException, SQLException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = optionalUser.get();
        FotoUsuario existingPhoto = user.getFoto_usuario();
        if (existingPhoto != null) {
            user.setFoto_usuario(null);
            existingPhoto.setUsuarioCodigo(null);
            fotoRepository.delete(existingPhoto);
        }
        Blob blob = new SerialBlob(file.getBytes());
        FotoUsuario newPhoto = new FotoUsuario(null, blob, user);
        fotoRepository.save(newPhoto);
        user.setFoto_usuario(newPhoto);
        userRepository.save(user);
        return "Saved";
    }

}
