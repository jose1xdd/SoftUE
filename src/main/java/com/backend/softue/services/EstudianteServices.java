package com.backend.softue.services;

import com.backend.softue.models.*;
import com.backend.softue.repositories.EstudianteRepository;
import com.backend.softue.repositories.SingInTokenRepository;
import com.backend.softue.repositories.UsuarioDeshabilitadoRepository;
import com.backend.softue.utils.beansAuxiliares.GradosPermitidos;
import com.backend.softue.utils.beansAuxiliares.UsuariosValidos;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.*;

@Getter
@Service
public class EstudianteServices {
    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private UsuarioDeshabilitadoRepository usuarioDeshabilitadoRepository;

    @Autowired
    private UserServices usuarioServices;

    @Autowired
    private SingInTokenRepository singInTokenRepository;

    @Autowired
    private UsuariosValidos usuariosValidos;

    private final String ENCABEZADO_VALIDO = "[CODIGO, GRADO_COD, GRUPO, NOMBRE1, NOMBRE2, APELLIDO1, APELLIDO2, NOMBRE1_ACUDIENTE, NOMBRE2_ACUDIENTE, APELLIDO1_ACUDIENTE, APELLIDO2_ACUDIENTE, GENERO]";

    public void registrarEstudiante(Estudiante estudiante) {
        if (!gradoPermitido(estudiante.getCurso()))
            throw new RuntimeException("No se puede registrar este usuario, ya que el curso diligenciado no es valido");
        if (!estudiante.getTipoUsuario().equals("estudiante"))
            throw new RuntimeException("No se puede registrar este usuario, no es un estudiante");
        usuarioServices.registerUser((User) estudiante);
        estudianteRepository.save(estudiante);
    }

    public void actualizarEstudiante(Estudiante estudiante, String jwt) {
        if (!gradoPermitido(estudiante.getCurso()))
            throw new RuntimeException("No se puede actualizar este usuario, ya que el curso diligenciado no es valido");
        if (!estudiante.getTipoUsuario().equals("estudiante"))
            throw new RuntimeException("No se puede actualizar este usuario, no se puede cambiar de rol");
        usuarioServices.actualizarUsuario((User) estudiante, jwt);
        Estudiante result = this.estudianteRepository.findByCorreo(estudiante.getCorreo());
        estudiante.setCodigo(result.getCodigo());
        estudiante.setContrasenia(result.getContrasenia());
        this.estudianteRepository.save(estudiante);
    }
    public void actualizarEstudiante(Estudiante estudiante) {
        this.estudianteRepository.save(estudiante);
    }

    public Estudiante obtenerEstudiante(String email) {
        if (email != null) {
            Estudiante result = this.estudianteRepository.findByCorreo(email);
            if (result == null) throw new RuntimeException("El estudiante no existe");
            if (result.getFoto_usuario() != null) result.setFotoUsuarioId(result.getFoto_usuario().getId());
            return result;
        }
        throw new RuntimeException("No se envió información con la que buscar al usuario");
    }

    public Estudiante obtenerEstudiante(Integer codigo) {
        if (codigo != null) {
            Estudiante result = this.estudianteRepository.findById(codigo).get();
            if (result == null)
                throw new RuntimeException("El estudiante no existe");
            if (result.getFoto_usuario() != null)
                result.setFotoUsuarioId(result.getFoto_usuario().getId());
            return result;
        }
        throw new RuntimeException("No se envió información con la que buscar al usuario");
    }

    public void deshabilitarEstudiante(String email) {
        if (email != null) {
            Estudiante result = this.estudianteRepository.findByCorreo(email);
            if (result == null) throw new RuntimeException("El usuario no existe");
            this.usuarioDeshabilitadoRepository.save(new UsuarioDeshabilitado(result));
            SingInToken singInToken = this.singInTokenRepository.findTokenByEmail(email);
            if (singInToken != null) this.singInTokenRepository.delete(singInToken);
            this.estudianteRepository.delete(result);
        } else throw new RuntimeException("No se envió información con la que buscar al usuario");
    }

    public List<Estudiante> listarEstudiantes() {
        return this.estudianteRepository.findAll();
    }

    public List<Estudiante> listarEstudiantesCurso(String curso) {
        if (!gradoPermitido(curso))
            throw new RuntimeException("No se puede registrar este usuario, ya que el curso diligenciado no es valido");
        return this.estudianteRepository.findByCurso(curso);

    }

    public Set<String> listarCursos() {
        return this.estudianteRepository.findCursos();
    }


    private boolean gradoPermitido(String grado) {
        boolean ok = true;
        int cnt = 0;
        String [] arr = grado.split("-");
        for(int i = 0; i < arr.length && ok; i++) {
            ok = isNumeric(arr[i]);
            cnt++;
        }
        if(ok && cnt == 2)
            return true;
        return false;
    }

    private boolean isNumeric(String numero) {
        try {
            Integer num = Integer.parseInt(numero);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public void cargarEstudiantes(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(0);
        LinkedList<String> encabezados = new LinkedList<>();
        for(Cell celda : row) {
            if (celda.getStringCellValue().isBlank())
                break;
            encabezados.add(celda.getStringCellValue());
        }
        if (!this.ENCABEZADO_VALIDO.equals(encabezados.toString()))
            throw new RuntimeException("El encabezado en el excel debe ser el siguiente: " + this.ENCABEZADO_VALIDO);
        int iterador = 1;
        LinkedList<Integer> filasErradas = new LinkedList<>();
        Map<Long, Estudiante> estudiantesValidos = new HashMap<>();
        for (Row fila : sheet) {
            try {
                Long codigo = (long) Double.parseDouble(fila.getCell(0).toString());
                String grado = this.concetenarCeldas(fila, 1, 2, '-');
                String nombre = this.concetenarCeldas(fila, 3, 4, ' ');
                String apellido = this.concetenarCeldas(fila, 5, 6, ' ');
                String acudiente = this.concetenarCeldas(fila, 7, 10, ' ');
                String genero = fila.getCell(11).toString().substring(0, 1);
                if (estudiantesValidos.containsKey(codigo))
                    throw new RemoteException("El código de estudiante ya se registro");
                estudiantesValidos.put(codigo, new Estudiante(null, nombre, apellido, genero, true, "correoNoRegistrado@usurio.correo", null, "SIN CONTRASENIA", "estudiante", grado, acudiente, "reprobada", codigo));
            }
            catch (Exception e) {
                filasErradas.add(iterador);
            }
            iterador++;
        }
        List<Estudiante> estudiantesRegistrados = this.listarEstudiantes();
        for (Estudiante estudiante : estudiantesRegistrados) {
            if (estudiantesValidos.containsKey(estudiante.getCodigoInstitucional()))
                estudiante.setUsuarioActivo(true);
            else estudiante.setUsuarioActivo(false);
            this.estudianteRepository.save(estudiante);
        }
        System.out.println(estudiantesValidos);
        usuariosValidos.setEstudianteMap(estudiantesValidos);
    }

    private String concetenarCeldas(Row row, int begin, int end, char divisor) {
        String resultado = "";
        do {
            resultado += row.getCell(begin).toString();
            begin++;
            if (begin < end) resultado += divisor;
        }
        while (begin <= end);
        return resultado;
    }
}
