package com.backend.softue.services;

import com.backend.softue.models.Estudiante;
import com.backend.softue.models.IdeaNegocio;
import com.backend.softue.repositories.EstudianteRepository;
import com.backend.softue.repositories.IdeaNegocioRepository;
import com.backend.softue.security.Hashing;
import com.backend.softue.security.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdeaNegocioServices {

    @Autowired
    private IdeaNegocioRepository ideaNegocioRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private IdeaPlanteadaServices ideaPlanteadaServices;

    @Autowired
    private Hashing encrypt;

    @Autowired
    private Roles roles;

    public void crear(IdeaNegocio ideaNegocio, String JWT) {
        String correo;
        IdeaNegocio resultado = this.ideaNegocioRepository.findByTitulo(ideaNegocio.getTitulo());
        if(resultado != null)
            throw new RuntimeException("Existe otra idea de negocio con el mismo título");
        if(!this.encrypt.getJwt().getValue(JWT).toLowerCase().equals("estudiante"))
            throw new RuntimeException("No se puede crear una idea de negocio si no se es un estudiante");

        this.ideaPlanteadaServices.integrantesExistentes(ideaNegocio);

        correo = this.encrypt.getJwt().getKey(JWT);
        Estudiante estudiante = this.estudianteRepository.findByCorreo(correo);
        ideaNegocio.setEstudianteLider(estudiante);

        this.ideaNegocioRepository.save(ideaNegocio);
    }

    public void actualizarTitulo(IdeaNegocio ideaNegocio) {
        IdeaNegocio resultado = this.ideaNegocioRepository.findById(ideaNegocio.getId()).get();
        if(resultado == null) throw new RuntimeException("No existe la idea de negocio a la cuál se le desea actualizar");
        resultado.setTitulo(ideaNegocio.getTitulo());

        this.ideaNegocioRepository.save(resultado);
    }
}
