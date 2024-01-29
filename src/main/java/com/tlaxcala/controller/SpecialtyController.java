package com.tlaxcala.controller;

import java.net.URI;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tlaxcala.dto.SpecialtyDTO;
import com.tlaxcala.model.Specialty;
import com.tlaxcala.service.ISpecialtyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.dispatcher.JavaDispatcher.Container;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController

@RequestMapping("/specialties")
@RequiredArgsConstructor
@SecurityRequirement(name = "Seguridad_Token")//este manda a traer la seguridad de token y si no cuenta con un usuario no podra visualizar ni detonar los servicios dentro de la interfaces
public class SpecialtyController {

    private final ISpecialtyService service;

    @Qualifier("defaultMapper")
    private final ModelMapper mapper;

    private SpecialtyDTO convertToDto(Specialty obj) {
        return mapper.map(obj, SpecialtyDTO.class);
    }

    private Specialty convertToEntity(SpecialtyDTO dto) {
        return mapper.map(dto, Specialty.class);
    }




    
    @Operation(summary = "Servicio para listar las Especialidades.",
           description= "(  http://localhost:8080/specialties  )Este servicio es para poder visualisar en lista las espcialidades que ya se encuentran registradas en la BD.  ")
    @ApiResponse(responseCode = "200", description = "recupercaion de la especialides en lista con toda su informacion.")
    @ApiResponse(responseCode = "401", description = "Usuario no registrado en la BD / No Autorizado / No usando Token "+"(RESPUESTA DEL SERVICIO :)"+ "{\r\n" + //
            "  \"datetime\": [\r\n" + //
            "    2024,\r\n" + //
            "    1,\r\n" + //
            "    26,\r\n" + //
            "    12,\r\n" + //
            "    55,\r\n" + //
            "    46,\r\n" + //
            "    513046800\r\n" + //
            "  ],\r\n" + //
            "  \"message\": \"Token not found or invalid\",\r\n" + //
            "  \"details\": \"/specialties/1\"\r\n" + //
            "}", content = @Content )
     @ApiResponse(responseCode = "405", description = "(Método no permitido) / Este error: el servicio no se está consumiendo como su forma nativa, la cual puede ser. (POST,GET,PUT,PATCH,HEAD)", content= @Content)
    @ApiResponse(responseCode = "500", description = "500 Internal Server Error / Este error puede ser algo común en servicios que no son estables de donde uno los consume. ", content= @Content)
    @GetMapping
    public ResponseEntity<List<SpecialtyDTO>> findAll() {
        // podemos ocupar el api de programación funcional para el manejo de listas
        // mediante
        // la propiedad stream
        // cuando se hace referencia de un método dentro de un lambda yo puedo aplicar
        // una abreviación
        // List<SpecialtyDTO> listExample = service.findAll().stream().map(e ->
        // convertToDto(e)).toList();
        List<SpecialtyDTO> list = service.findAll().stream().map(this::convertToDto).toList();
        // List<SpecialtyDTO> list = service.findAll().stream().map(e ->
        /*
         * List<SpecialtyRecord> list = service.findAll()
         * .stream().
         * map(e ->
         * new SpecialtyRecord(e.getIdSpecialty(), e.getFirstName(), e.getLastName(),
         * e.getDni(),
         * e.getAddress(), e.getPhone(), e.getEmail())
         * ).toList();
         */
        /*
         * {
         * 
         * SpecialtyDTO dto = new SpecialtyDTO();
         * dto.setIdSpecialty(e.getIdSpecialty());
         * dto.setFirstName(e.getFirstName());
         * dto.setLastName(e.getLastName());
         * dto.setDni(e.getDni());
         * dto.setPhone(e.getPhone());
         * dto.setEmail(e.getEmail());
         * dto.setAddress(e.getAddress());
         * return dto;
         * }
         * ).toList();
         */
        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    @Operation(summary = "Servicio de Registro de Especialidad  ",
    description= "(  http://localhost:8080/specialties )Este servicio es para poder registrar una nueva especialidad.")
@ApiResponse(responseCode = "200", description = "El Registro se a realizado Exitosamente en la BD")
@ApiResponse(responseCode = "201", description = "indica que la solicitud se realizó correctamente  aunque en el servicio no conteste nada en la consola  internamente se realizo correctamente. ",content= @Content)
@ApiResponse(responseCode = "400", description = "indica que la solicitud no se realizo correctamente ya que tiene un error de sintaxis (Verificar todo los parametros) para realizar su ejecucion. ",content= @Content)
@ApiResponse(responseCode = "401", description = "Usuario no registrado en la BD / No Autorizado / No usando Token "+"(RESPUESTA DEL SERVICIO :)"+ "{\r\n" + //
     "  \"datetime\": [\r\n" + //
     "    2024,\r\n" + //
     "    1,\r\n" + //
     "    26,\r\n" + //
     "    12,\r\n" + //
     "    55,\r\n" + //
     "    46,\r\n" + //
     "    513046800\r\n" + //
     "  ],\r\n" + //
     "  \"message\": \"Token not found or invalid\",\r\n" + //
     "  \"details\": \"/specialties/1\"\r\n" + //
     "}", content = @Content )
@ApiResponse(responseCode = "403", description = "Este error es similar al 401 pero con la diferencia de que este no se soluciona con una re-autenticación,  como el no tener los permisos necesarios para acceder al recurso.", content = @Content )
@ApiResponse(responseCode = "404", description = "(No Encontrado) / Este error es por que encuentra una especialidad con ese numero de ID / identificador"+
     "{\r\n" + //
     "    \"datetime\": \"2024-01-26T17:24:15.3702769\",\r\n" + //
     "    \"message\": \"ID NOT FOUND: 4\",\r\n" + //
     "    \"details\": \"uri=/specialties/MiServicio/4\"\r\n" + //
     "}", content = @Content )
@ApiResponse(responseCode = "405", description = "(Método no permitido) / Este error: el servicio no se está consumiendo como su forma nativa, la cual puede ser. (POST,GET,PUT,PATCH,HEAD)", content= @Content)
@ApiResponse(responseCode = "500", description = "500 Internal Server Error / Este error puede ser algo común en servicios que no son estables de donde uno los consume. ", content= @Content)
    @PostMapping
    public ResponseEntity<SpecialtyDTO> save(@Valid @RequestBody SpecialtyDTO dto) {
        Specialty obj = service.save(convertToEntity(dto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdSpecialty())
                .toUri();
        return ResponseEntity.created(location).build();
    }



    
    @Operation(summary = "Servicio de Seleccion de Especialidad mediante ID / Identificador",
           description= "(   http://localhost:8080/specialties/1   )Este servicio es para poder  seleccionar alguna especialidad ya registrada que se encuentren en la base de datos, Como dato el siguiente servicio solo servira si el usuario es autenticado mediante su Token de lo contrario no podra hacer uso del mismo")
    @ApiResponse(responseCode = "200", description = "Seleccion Exitosa mediante su id del de la Especialidad")
    @ApiResponse(responseCode = "401", description = "Usuario no registrado en la BD / No Autorizado / No usando Token "+"(RESPUESTA DEL SERVICIO :)"+ "{\r\n" + //
            "  \"datetime\": [\r\n" + //
            "    2024,\r\n" + //
            "    1,\r\n" + //
            "    26,\r\n" + //
            "    12,\r\n" + //
            "    55,\r\n" + //
            "    46,\r\n" + //
            "    513046800\r\n" + //
            "  ],\r\n" + //
            "  \"message\": \"Token not found or invalid\",\r\n" + //
            "  \"details\": \"/specialties/1\"\r\n" + //
            "}", content = @Content )
    @ApiResponse(responseCode = "403", description = "Este error es similar al 401 pero con la diferencia de que este no se soluciona con una re-autenticación,  como el no tener los permisos necesarios para acceder al recurso.", content = @Content )
    @ApiResponse(responseCode = "405", description = "(Método no permitido) / Este error: el servicio no se está consumiendo como su forma nativa, la cual puede ser. (POST,GET,PUT,PATCH,HEAD)", content= @Content)
    @ApiResponse(responseCode = "500", description = "500 Internal Server Error / Este error puede ser algo común en servicios que no son estables de donde uno los consume. ", content= @Content)
    @GetMapping("/{id}")
    public ResponseEntity<SpecialtyDTO> findById(@PathVariable("id") Integer id) {
        Specialty obj = service.findById(id);
        return new ResponseEntity<>(convertToDto(obj), HttpStatus.OK);
    }







    @Operation(summary = "Servicio de actualizacion del informacion  mediante ID / Identificador",
    description= "(  http://localhost:8080/specialties/1   )Este servicio es para poder  seleccionar alguna especialidad ya registrada que se encuentren en la base de datos"+
    " Como dato el siguiente servicio solo servira si el usuario es autenticado mediante su Token de lo contrario no podra hacer uso del mismo"+
    "solo se requiere la informacion que se desea cambiar ya el nombre o la descripción de la especialidad, "+
    "Este servicio de actualizacion es muy bueno para que el mismo usuario pueda modificar alguna informacion de alguna de las especialidades sin mucho esfuerzo, ")
    @ApiResponse(responseCode = "200", description = "Seleccion y modificacion Exitosa en la base de datos")
    @ApiResponse(responseCode = "401", description = "Usuario no registrado en la BD / No Autorizado / No usando Token "+"(RESPUESTA DEL SERVICIO :)"+ "{\r\n" + //

     "  \"datetime\": [\r\n" + //
     "    2024,\r\n" + //
     "    1,\r\n" + //
     "    26,\r\n" + //
     "    12,\r\n" + //
     "    55,\r\n" + //
     "    46,\r\n" + //
     "    513046800\r\n" + //
     "  ],\r\n" + //
     "  \"message\": \"Token not found or invalid\",\r\n" + //
     "  \"details\": \"/specialties/1\"\r\n" + //
     "}", content = @Content )
    @ApiResponse(responseCode = "403", description = "Este error es similar al 401 pero con la diferencia de que este no se soluciona con una re-autenticación,  como el no tener los permisos necesarios para acceder al recurso.", content = @Content )
    @ApiResponse(responseCode = "405", description = "(Método no permitido) / Este error: el servicio no se está consumiendo como su forma nativa, la cual puede ser. (POST,GET,PUT,PATCH,HEAD)", content= @Content)
    @ApiResponse(responseCode = "500", description = "500 Internal Server Error / Este error puede ser algo común en servicios que no son estables de donde uno los consume. ", content= @Content)
    @PutMapping("/{id}")
    public ResponseEntity<Specialty> update(@PathVariable("id") Integer id, @RequestBody SpecialtyDTO dto) throws Exception {
        Specialty obj = service.update(convertToEntity(dto), id);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }



    @Operation(summary = "Servicio de eliminacion de Especialidad",
           description= "(  http://localhost:8080/specialties/1   )Este servicio es para poder  elimina alguna especialidad ya registrada que se encuentren en la base de datos, Como dato el siguiente servicio solo servira si el usaurio es autenticado mediante su Token de lo contrario no podra hacer uso del mismo")
    @ApiResponse(responseCode = "200", description = "Eliminacion Exitosa mediante su id del de la espacialidad")
    @ApiResponse(responseCode = "401", description = "Usuario no registrado en la BD / No Autorizado / No usando Token "+"(RESPUESTA DEL SERVICIO :)"+ "{\r\n" + //
            "  \"datetime\": [\r\n" + //
            "    2024,\r\n" + //
            "    1,\r\n" + //
            "    26,\r\n" + //
            "    12,\r\n" + //
            "    55,\r\n" + //
            "    46,\r\n" + //
            "    513046800\r\n" + //
            "  ],\r\n" + //
            "  \"message\": \"Token not found or invalid\",\r\n" + //
            "  \"details\": \"/specialties/1\"\r\n" + //
            "}", content = @Content )
    @ApiResponse(responseCode = "403", description = "Este error es similar al 401 pero con la diferencia de que este no se soluciona con una re-autenticación,  como el no tener los permisos necesarios para acceder al recurso.", content = @Content )
    @ApiResponse(responseCode = "405", description = "(Método no permitido) / Este error: el servicio no se está consumiendo como su forma nativa, la cual puede ser. (POST,GET,PUT,PATCH,HEAD)", content= @Content)
    @ApiResponse(responseCode = "500", description = "500 Internal Server Error / Este error puede ser algo común en servicios que no son estables de donde uno los consume. ", content= @Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<Specialty> delete(@PathVariable("id") Integer id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    




    @Operation(summary = "Servicio es para poder ver los diferentes link de ese mismo servicios ",
           description= "(  http://localhost:8080/specialties/hateoas/1  )Este servicio es para poder que servicios sobre esa espcialidad hay o tambien para poner ligas de redireccion a paginas con informacion de las especialidades.  ")
    @ApiResponse(responseCode = "200", description = "recupercaion de la especialidad con sus diferentes rutas de servicios o accesos a paginas")
    @ApiResponse(responseCode = "401", description = "Usuario no registrado en la BD / No Autorizado / No usando Token "+"(RESPUESTA DEL SERVICIO :)"+ "{\r\n" + //
            "  \"datetime\": [\r\n" + //
            "    2024,\r\n" + //
            "    1,\r\n" + //
            "    26,\r\n" + //
            "    12,\r\n" + //
            "    55,\r\n" + //
            "    46,\r\n" + //
            "    513046800\r\n" + //
            "  ],\r\n" + //
            "  \"message\": \"Token not found or invalid\",\r\n" + //
            "  \"details\": \"/specialties/1\"\r\n" + //
            "}", content = @Content )
    @ApiResponse(responseCode = "403", description = "Este error es similar al 401 pero con la diferencia de que este no se soluciona con una re-autenticación,  como el no tener los permisos necesarios para acceder al recurso.", content = @Content )
    @ApiResponse(responseCode = "404", description = "(No Encontrado) / Este error es por que encuentra una especialidad con ese numero de ID / identificador"+
            "{\r\n" + //
            "    \"datetime\": \"2024-01-26T17:24:15.3702769\",\r\n" + //
            "    \"message\": \"ID NOT FOUND: 4\",\r\n" + //
            "    \"details\": \"uri=/specialties/MiServicio/4\"\r\n" + //
            "}", content = @Content )
    @ApiResponse(responseCode = "405", description = "(Método no permitido) / Este error: el servicio no se está consumiendo como su forma nativa, la cual puede ser. (POST,GET,PUT,PATCH,HEAD)", content= @Content)
    @ApiResponse(responseCode = "500", description = "500 Internal Server Error / Este error puede ser algo común en servicios que no son estables de donde uno los consume. ", content= @Content)
    @GetMapping("/hateoas/{id}")
    // @GetMapping("/MiServicio/{id}")
    public EntityModel<SpecialtyDTO> findByHateoas(@PathVariable("id") Integer id) {
        EntityModel<SpecialtyDTO> resource = EntityModel.of(convertToDto(service.findById(id))); // la salida

        WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id)); // la generación del link

        resource.add(link1.withRel("specialty-info1")); // agregamos el link con una llave
        resource.add(link1.withRel("specialty-info2"));

        return resource;
    }

}

