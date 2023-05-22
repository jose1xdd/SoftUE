package com.backend.softue.services;

import com.backend.softue.models.Estudiante;
import com.backend.softue.models.IdeaNegocio;
import com.backend.softue.repositories.EstudianteRepository;
import com.backend.softue.repositories.IdeaNegocioRepository;
import com.backend.softue.security.Hashing;
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

    public void crear(IdeaNegocio ideaNegocio, String JWT) {
        String correo;
        IdeaNegocio resultado = this.ideaNegocioRepository.findByTitulo(ideaNegocio.getTitulo());
        if(resultado != null) throw new RuntimeException("Existe otra idea de negocio con el mismo título");

        this.ideaPlanteadaServices.integrantesExistentes(ideaNegocio);

        correo = this.encrypt.getJwt().getKey(JWT);
        Estudiante estudiante = this.estudianteRepository.findByCorreo(correo);
        ideaNegocio.setEstudianteLider(estudiante);

        this.ideaNegocioRepository.save(ideaNegocio);
    }
}
