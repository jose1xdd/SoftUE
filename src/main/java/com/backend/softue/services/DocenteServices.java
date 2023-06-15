package com.backend.softue.services;

import com.backend.softue.models.*;
import com.backend.softue.repositories.DocenteRepository;
import com.backend.softue.repositories.SingInTokenRepository;
import com.backend.softue.repositories.UsuarioDeshabilitadoRepository;
import com.backend.softue.security.Hashing;
import com.backend.softue.utils.beansAuxiliares.UsuariosValidos;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
@Setter
@Getter
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

    @Autowired
    private UsuariosValidos usuariosValidos;

    private final String ENCABEZADO_VALIDO = "[CEDULA, NOMBRE1, NOMBRE2, APELLIDO1, APELLIDO2, CORREO, TELEFONO, GENERO, TITULO PROFESIONAL, AREA]";
    @PostConstruct
    public void init() {
    this.ideaNegocioServices.setDocenteServices(this);
    }

    public void registrarDocente(Docente docente) {
        if(!this.areaConocimientoServices.existe(docente.getArea()))
            throw  new RuntimeException("No se puede crear este usuario,el area de conocimiento ingresada no es parte de las comtempladas por el sistema");
        if(!docente.getTipoUsuario().equals("docente")) throw new RuntimeException("No se puede registrar este usuario, no es un docente");
        usuarioServices.registerUser((User) docente);
        docenteRepository.save(docente);
    }

    public void registrarDocente(String correo, String contrasenia) {
        if (correo == null)
            throw new RuntimeException("El correo no puede ser null");
        if (contrasenia == null)
            throw new RuntimeException("La contraseña no puede ser nula");
        if (this.docenteRepository.findByCorreo(correo) != null)
            throw new RuntimeException("El usuario ya existe");
        Docente docente = this.usuariosValidos.getDocenteMap().get(correo);
        if (docente == null)
            throw new RuntimeException("El docente no esta contemplado en los archivos del sistema");
        docente.setContrasenia(contrasenia);
        this.usuarioServices.registerUser((User) docente);
        this.docenteRepository.save(docente);
    }

    public void actualizarDocente(Docente docente, String jwt) {
        if(!this.areaConocimientoServices.existe(docente.getArea()))
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
        return this.docenteRepository.findAll();
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

    public List<Integer> cargarDocentes(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(0);
        LinkedList<String> encabezados = new LinkedList<>();
        for (Cell celda : row) {
            if (celda.toString().isBlank())
                break;
            encabezados.add(celda.getStringCellValue());
        }
        System.out.println(encabezados.toString());
        if (!this.ENCABEZADO_VALIDO.equals(encabezados.toString()))
            throw new RuntimeException("El encabezado en el excel debe ser el siguiente: " + this.ENCABEZADO_VALIDO);
        int iterador = 1;
        LinkedList<Integer> filasErradas = new LinkedList<>();
        Map<String, Docente> docentesValidos = new HashMap<>();
        for (Row fila : sheet) {
            try {
                String cedula = (fila.getCell(0).getCellType().equals(CellType.NUMERIC)) ? "" + (long) fila.getCell(0).getNumericCellValue() : fila.getCell(0).toString();
                String nombre = concetenarCeldas(fila, 1, 2, ' ');
                String apellido = concetenarCeldas(fila, 3, 4, ' ');
                String correo = fila.getCell(5).toString();
                String telefono = (fila.getCell(6).getCellType().equals(CellType.NUMERIC)) ? "" + (long) fila.getCell(6).getNumericCellValue() : fila.getCell(6).toString();
                String genero = fila.getCell(7).toString().substring(0, 1);
                String titulo = fila.getCell(8).toString();
                String area = fila.getCell(9).toString();
                if (docentesValidos.containsKey(correo))
                    throw new RuntimeException("El correo del docente ya se registro");
                docentesValidos.put(correo, new Docente(null, nombre, apellido, genero, true, correo, telefono, "SIN CONTRASENIA", "docente", cedula, titulo, area));
            }
            catch (Exception e) {
                filasErradas.add(iterador);
            }
            iterador++;
        }
        List<Docente> docentesRegistrados = this.listarDocentes();
        for (Docente docente : docentesRegistrados) {
            if (docentesValidos.containsKey(docente.getCorreo()))
                docente.setUsuarioActivo(true);
            else docente.setUsuarioActivo(false);
            this.docenteRepository.save(docente);
        }
        this.usuariosValidos.setDocenteMap(docentesValidos);
        return filasErradas;
    }

    private String concetenarCeldas(Row row, int begin, int end, char divisor) {
        String resultado = "";
        int curso;
        do {
            if(row.getCell(begin).getCellType().equals(CellType.NUMERIC)) {
                curso = (int) row.getCell(begin).getNumericCellValue();
                resultado += Integer.toString(curso);
            }
            else {
                resultado += row.getCell(begin).toString();
            }
            if (begin < end) resultado += divisor;
            begin++;
        }
        while (begin <= end);
        return resultado;
    }
}
