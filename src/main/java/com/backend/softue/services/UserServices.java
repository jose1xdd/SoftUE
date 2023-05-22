package com.backend.softue.services;

import com.backend.softue.models.FotoUsuario;
import com.backend.softue.models.ResetToken;
import com.backend.softue.models.SingInToken;
import com.backend.softue.models.User;
import com.backend.softue.models.UsuarioDeshabilitado;
import com.backend.softue.repositories.FotoRepository;
import com.backend.softue.repositories.ResetTokenRepository;
import com.backend.softue.repositories.SingInTokenRepository;
import com.backend.softue.repositories.UserRepository;
import com.backend.softue.repositories.UsuarioDeshabilitadoRepository;
import com.backend.softue.security.Hashing;
import com.backend.softue.security.Roles;
import com.backend.softue.utils.emailModule.EmailService;
import com.backend.softue.utils.response.LoginResponse;
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

    @Autowired
    private Roles roles;
    @Autowired
    private SingInTokenRepository singInTokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Hashing encrypt;
    @Autowired
    private ResetTokenRepository resetTokenRepository;
    @Autowired
    private FotoRepository fotoRepository;
    @Autowired
    private EmailService emailGenericMessages;
    @Autowired
    private UsuarioDeshabilitadoRepository usuarioDeshabilitadoRepository;

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

    public void actualizarUsuario(User user, String JWT) {
        User result = this.userRepository.findByCorreo(user.getCorreo());
        if (result == null) throw new RuntimeException("El usuario no existe");
        if (this.encrypt.getJwt().getKey(JWT).equals(user.getCorreo())) {
            user.setCodigo(result.getCodigo());
            user.setContrasenia(result.getContrasenia());
            this.userRepository.save(user);
        } else {
            if (this.roles.getPermisosDeEdicion().get(this.encrypt.getJwt().getValue(JWT)).contains(user.getTipo_usuario())) {
                user.setCodigo(result.getCodigo());
                user.setContrasenia(result.getContrasenia());
                this.userRepository.save(user);
            } else throw new RuntimeException("Las credenciales de rol no permiten modifcar este usuario");
        }
    }

    public User obtenerUsuario(String email) {
        if (email != null) {
            User result = this.userRepository.findByCorreo(email);
            if(result == null) throw new RuntimeException("El usuario no existe");
            if(result.getFoto_usuario() != null) result.setFotoUsuarioId(result.getFoto_usuario().getId());
            return result;
        }
        throw new RuntimeException("No se envió información con la que buscar al usuario");
    }

    private Boolean validateUserRol(String rol) {
        return this.roles.getNombreRoles().contains(rol.toLowerCase());
    }

    public String savePicture(MultipartFile file, String id) throws IOException, SQLException {
        User user = userRepository.findByCorreo(id);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
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

    public void logout(String jwt) {
        SingInToken token = this.singInTokenRepository.findByToken(jwt);
        this.singInTokenRepository.delete(token);
    }

    public String forgotPassword(String email) {
        User user = this.userRepository.findByCorreo(email);
        if (user == null) throw new RuntimeException("No hay ningun usuario asignado a ese email");
        ResetToken resetToken = this.resetTokenRepository.findTokenByEmail(email);
        if (resetToken != null) {
            this.resetTokenRepository.delete(resetToken);

        } else {
            resetToken = new ResetToken();
        }
        String token = this.encrypt.generarJWT(user.getCorreo(), user.getTipo_usuario());
        LocalDateTime newDateTime = LocalDateTime.now().plus(Duration.ofHours(1));
        resetToken.setToken(token);
        resetToken.setFecha_caducidad(newDateTime);
        resetToken.setUsuario_codigo(user);
        this.resetTokenRepository.save(resetToken);
        this.emailGenericMessages.enviarEmailRegistro(email);
        return token;
    }
    public void resetPassword(String token,String password){
        ResetToken resetToken = this.resetTokenRepository.findByToken(token);
        if(resetToken == null) throw  new RuntimeException("El ResetToken no existe");
        User user = resetToken.getUsuario_codigo();
        user.setContrasenia(this.encrypt.hash(password));
        this.userRepository.save(user);
        this.resetTokenRepository.delete(resetToken);


    }

    public byte[] obtenerFoto(String id) throws SQLException, IOException {
        if(id != null) {
            FotoUsuario result = this.fotoRepository.getReferenceById(Integer.parseInt(id));
            if(result == null) throw new RuntimeException("La foto no existe");
            return result.getFoto().getBytes(1, (int) result.getFoto().length());
        }
        throw new RuntimeException("No se envió información con la que buscar la foto");
    }

    public void deshabilitarUsuario(String email) {
        if (email != null) {
            User result = this.userRepository.findByCorreo(email);
            if(result == null) throw new RuntimeException("El usuario no existe");
            this.usuarioDeshabilitadoRepository.save(new UsuarioDeshabilitado(result));
            SingInToken singInToken = this.singInTokenRepository.findTokenByEmail(email);
            if(singInToken != null) this.singInTokenRepository.delete(singInToken);
            this.userRepository.delete(result);
        }
        else throw new RuntimeException("No se envió información con la que buscar al usuario");
    }
}
