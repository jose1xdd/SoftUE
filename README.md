# SoftUE

Sofware de gestión de unidades de emprendimiento

**Tabla de Contenido**
* [Requisitos del proyecto](#Requisitos)
* [Paquetes](#Paquetes)
    * [Cotrollers](#Controllers)
    * [Models](#Modelos)
    * [Repositories](#Repositorios)
    * [Security](#Seguridad)
    * [Services](#Servicios)
    * [Utils](#Utils)
        * [SubPaquetes](#PaquetesUtil)
            * [BeansAuxiliares](#beansAuxiliares)
            * [CheckSession](#CheckSession)
            * [Config](#Config)
            * [EmailModule](#EmailModule)
            * [Response](#Responses)
        * [Clases](#ClasesUtil)
    * [Resources](#Resources)
* [Funcionamiento](#Funcionamiento)

<div id='Requisitos'></div>

## **Requisitos para la correcta ejecucion del proyecto**


### `Dependencias`
Dependencias utilizadas por el proyecto
>- com.fasterxml.jackson.core:jackson-databind
>- com.mysql:mysql-connector-j
>- de.mkammerer:argon2-jvm
>- io.jsonwebtoken:jjwt-api
>- io.jsonwebtoken:jjwt-impl
>- io.jsonwebtoken:jjwt-jackson
>- jakarta.xml.bind:jakarta.xml.bind-api
>- org.projectlombok:lombok
>- org.springframework.boot:spring-boot-devtools
>- org.springframework.boot:spring-boot-maven-plugin
>- org.springframework.boot:spring-boot-starter-data-jpa
>- org.springframework.boot:spring-boot-starter-mail
>- org.springframework.boot:spring-boot-starter-test
>- org.springframework.boot:spring-boot-starter-validation
>- org.springframework.boot:spring-boot-starter-web
>- org.springframework:spring-context


### `Java `
La version de java utilizada fue la version 17, en caso de no tenerla se puede descargar en el siguiente enlace [JAVA 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html).


<div id='Paquetes'></div>

## **Paquetes del proyecto**
El proyecto esta dividido en los siguientes paquetes:

<div id='Controllers'></div>

### `Controllers`
En estos se encuentran los endpoints necesarios por el aplicativo, el paquete esta compuesto por los siguientes controllers:
>- CalificacionIdeaController.java
>- CalificacionPlanController.java
>- DocenteApoyoIdeaController.java
>- DocenteApoyoPlanController.java
>- DocenteController.java
>- EntidadFinanciadoraController.java
>- EstudianteController.java
>- EvaluacionIdeaController.java
>- EvaluacionPlanController.java
>- FormatoController.java
>- IdeaNegocioController.java
>- IdeaPlanteadaController.java
>- ObservacionIdeaController.java
>- ObservacionPlanController.java
>- PlanNegocioController.java
>- LoginController.java
>- PeriodoController.java
>- RegisterController.java
>- UserController.java


<div id='Modelos'></div>

### `Models`
En estos se encuentran los modelos del negocio necesarios por el aplicativo, el paquete esta compuesto por los siguientes Models:
>- CalificacionIdea.java
>- CalificacionIdeaKey.java
>- CalificacionPlan.java
>- CalificacionPlanKey.java
>- Docente.java
>- DocenteApoyoIdea.java
>- DocenteApoyoPlan.java
>- DocenteIdeaKey.java
>- DocentePlanKey.java
>- DocumentoIdea.java
>- DocumentoPlan.java
>- EntidadFinanciadora.java
>- Estudiante.java
>- EstudianteIdeaKey.java
>- EstudiantePlanKey.java
>- EvaluacionIdea.java
>- EvaluacionPlan.java
>- Formato.java
>- FotoEntidadFinanciadora.java
>- FotoUsuario.java
>- IdeaNegocio.java
>- IdeaPlanteada.java
>- ObservacionIdea.java
>- ObservacionPlan.java
>- PlanNegocio.java
>- PlanPresentado.java
>- ResetToken.java
>- SingInToken.java
>- User.java
>- UsuarioDeshabilitado.java


<div id='Repositorios'></div>

### `Respositories`
En estos se encuentran las clases necesarias para interactuar con la base de datos, el paquete esta compuesto por los siguientes Repository:
>- CalificacionIdeaRepository.java
>- CalificacionPlanRepository.java
>- DocenteApoyoIdeaRepository.java
>- DocenteApoyoPlanRepository.java
>- DocenteRepository.java
>- DocumentoIdeaRepository.java
>- DocumentoPlanRepository.java
>- EntidadFinanciadoraRepository.java
>- EstudianteRepository.java
>- EvaluacionIdeaRepository.java
>- EvaluacionPlanRepository.java
>- FormatoRepository.java
>- FotoEntidadFinanciadoraRepository.java
>- FotoRepository.java
>- IdeaNegocioRepository.java
>- IdeaPlanteadaRepository.java
>- ObservacionIdeaRepository.java
>- ObservacionPlanRepository.java
>- PlanNegocioRepository.java
>- PlanPresentadoRepository.java
>- ResetTokenRepository.java
>- SingInTokenRepository.java
>- UserRepository.java
>- UsuarioDeshabilitadoRepository.java

<div id='Seguridad'></div>

### `Security`
En estos se encuentran las clases necesarias para la seguridad del aplicativo, el paquete esta compuesto por las siguientes clases:
>- Hashing.java
>- JWTUtil.java
>- Roles.java

<div id='Servicios'></div>

### `Services`
En estos se encuentran las clases encargadas de la logica y el flujo de las funcionalidades del proyecto, el paquete esta compuesto por las siguientes clases:
>- CalificacionIdeaServices.java
>- CalificacionPlanServices.java
>- DocenteApoyoIdeaServices.java
>- DocenteApoyoPlanServices.java
>- DocenteServices.java
>- DocumentoIdeaServices.java
>- DocumentoPlanServices.java
>- EntidadFinanciadoraServices.java
>- EstudianteServices.java
>- EvaluacionIdeaServices.java
>- EvaluacionPlanServices.java
>- FormatoServices.java
>- IdeaNegocioServices.java
>- IdeaPlanteadaServices.java
>- ObservacionIdeaServices.java
>- ObservacionPlanServices.java
>- PeriodoServices.java
>- PlanNegocioServices.java
>- PlanPresentadoServices.java
>- UserServices.java


<div id='Utils'></div>

### `Utils`
En estos se encuentran los elementos auxiliares necesitados por el aplicativo, los cuales no forman parte del negocio, el paquete esta compuesto por los siguientes elementos:

<div id='PaquetesUtil'></div>

* ####  `Paquetes`

    <div id='beansAuxiliares'></div>

    * #### `BeansAuxiliares`
      Son los beans utilizados por el proyecto atraves de spring, los cuales son:
      >- AreasConocimiento.java
      >- EstadosCalificacion.java
      >- EstadosIdeaPlanNegocio.java
      >- GradosPermitidos.java
      >- PeriodosDeEvaluacion.java

    <br>
    <div id='CheckSession'></div>

    * #### `CheckSession`

      Esta compuesta de las clases necesarias para el manejo de la sesión de los multiples usuarios del aplicativo, los cuales son:
      >- CheckSession.java
      >- CheckSessionInterceptor.java

    <br>
    <div id='Config'></div>

    * ####    `Config`
      Se compone de las clases encargadas de administrar la configuracion general del aplicativo, las cuales son:
      >- AppConfig.java
      >- CorsConfig.java
      >- WebConfig.java

    <br>
    <div id='EmailModule'></div>

    * ####    `EmailModule`
      Se compone de las clases encargadas de las funcionalidades relacionadas con el envio de correos elextronicos, las cuales son:
      >- EmailService.java

    <br>
    <div id='Responses'></div>

    * ####    `Response`
      Se compone de las clases encargadas de administración y el encapsulamiento de errores que se pueden presentar en el aplicativo, las cuales son:
      >- ErrorFactory.java
      >- LoginResponse.java
      >- RequestPassword.java
      >- ResponseConfirmation.java
      >- ResponseError.java
      >- ResponseToken.java

<div id='ClasesUtil'></div>

* ####  `Clases`
  Se compone de otras clases complementarias secundarias, las cuales son:

  >- FormatoPeriodo.java


<div id='Resources'></div>

###  `Resources`
Contiene la configuracion del proyecto, se compone de:

>- application.properties

<div id='Funcionamiento'></div>

## **Funcionamiento**
Para poner en funcionamiento los servicios del ofrecidos por los paquetes y las clases del aplicativo se debe seleccionar la clase main (SoftueApplication.java) ubicada en la raiz del proyecto y ponerla en ejecucion, utilizando el comando mvnw spring-boot:run.
