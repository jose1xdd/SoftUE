package com.backend.softue.services;

import com.backend.softue.models.*;
import com.backend.softue.repositories.DocenteRepository;
import com.backend.softue.repositories.SingInTokenRepository;
import com.backend.softue.repositories.UsuarioDeshabilitadoRepository;
import com.backend.softue.security.Hashing;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DocenteServices {
    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private UsuarioDeshabilitadoRepository usuarioDeshabilitadoRepository;

    @Autowired
    private UserServices usuarioServices;

    @Autowired
    private SingInTokenRepository singInTokenRepository;

    @Autowired
    private Hashing encrypth;

    @Autowired
    private IdeaNegocioServices ideaNegocioServices;

    @Autowired
    private AreaConocimientoServices areaConocimientoServices;

    @PostConstruct
    public void init() {
    this.ideaNegocioServices.setDocenteServices(this);
    }
    public void registrarDocente(Docente docente) {
        if(this.areaConocimientoServices.existe(docente.getNombre()))
            throw  new RuntimeException("No se puede crear este usuario,el area de conocimiento ingresada no es parte de las comtempladas por el sistema");
        if(!docente.getTipoUsuario().equals("docente")) throw new RuntimeException("No se puede registrar este usuario, no es un docente");
        usuarioServices.registerUser((User) docente);
        docenteRepository.save(docente);
    }

    public void actualizarDocente(Docente docente, String jwt) {
        if(!this.areaConocimientoServices.existe(docente.getNombre()))
            throw  new RuntimeException("No se puede crear este usuario,el area de conocimiento ingresada no es parte de las comtempladas por el sistema");
        if(!docente.getTipoUsuario().equals("docente")) throw new RuntimeException("No se puede actualizar este usuario, no se puede cambiar de rol");
        usuarioServices.actualizarUsuario((User) docente, jwt);
        Docente result = this.docenteRepository.findByCorreo(docente.getCorreo());
        docente.setCodigo(result.getCodigo());
        docente.setContrasenia(result.getContrasenia());
        this.docenteRepository.save(docente);
    }
    public Docente obtenerDocente(String email) {
        if (email != null) {
            Docente result = this.docenteRepository.findByCorreo(email);
            if (result == null) throw new RuntimeException("El docente no existe");
            if(result.getFoto_usuario() != null) result.setFotoUsuarioId(result.getFoto_usuario().getId());
            return result;
        }
        throw new RuntimeException("No se envió información con la que buscar al docente");
    }

    public Docente obtenerDocente(Integer id) {
        if(id == null)
            throw new RuntimeException("No se envió información con la que buscar al docente");
        Optional<Docente> resultado = this.docenteRepository.findById(id);
        if(!resultado.isPresent())
            throw new RuntimeException("El docente solicitado no existe");
        if(resultado.get().getFoto_usuario() != null)
            resultado.get().setFotoUsuarioId(resultado.get().getFoto_usuario().getId());
        return resultado.get();
    }

    public void deshabilitarDocente(String email) {
        if (email != null) {
            Docente result = this.docenteRepository.findByCorreo(email);
            if(result == null) throw new RuntimeException("El usuario no existe");
            this.usuarioDeshabilitadoRepository.save(new UsuarioDeshabilitado(result));
            SingInToken singInToken = this.singInTokenRepository.findTokenByEmail(email);
            if(singInToken != null) this.singInTokenRepository.delete(singInToken);
            this.docenteRepository.delete(result);
        }
        else throw new RuntimeException("No se envió información con la que buscar al usuario");
    }

    public  List<Docente> listarDocentes() {
        List<Docente> docentes = this.docenteRepository.findAll();
        if(docentes.isEmpty()) throw new RuntimeException("No hay Docentes registrados");
        return docentes;
    }

    public List<Docente> listarDocentesArea(String area){
        if(!this.areaConocimientoServices.existe(area))
            throw  new RuntimeException("No se puede crear este usuario,el area de conocimiento ingresada no es parte de las comtempladas por el sistema");
        return this.docenteRepository.findByArea(area);
    }

    public String confirmarTutoria (Boolean respuesta,String titulo, String jwt){
        if(respuesta){
            IdeaNegocio ideaNegocio = this.ideaNegocioServices.obtenerIdeaNegocio(titulo);
            if(ideaNegocio == null ) throw  new RuntimeException("se mandó mal el titulo de la idea de negocio");
            Docente docente = this.obtenerDocente(this.encrypth.getJwt().getKey(jwt));
            ideaNegocio.setTutor(docente);
            Set<DocenteApoyoIdea> docentes = ideaNegocio.getDocentesApoyo();
            if(docentePertenece(docentes,docente)) throw new RuntimeException("El docente tutor no puede ser docente de apoyo");
            if(this.ideaNegocioServices.confirmarTutor(ideaNegocio) != null);return "Docente Asignado";
        }
        return "El docente rechazo";
    }
    private Boolean docentePertenece(Set<DocenteApoyoIdea> docentes ,Docente docente){
        Iterator<DocenteApoyoIdea> it = docentes.iterator();
        while (it.hasNext()){
            if(it.next().getDocente().equals(docente)) return true;
        }
        return  false;
    }
}
