package com.example.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.DTO.UserDto;
import com.example.entities.Department;
import com.example.entities.User;
import com.example.entities.Yard;
import com.example.model.FileUploadResponse;
import com.example.services.DepartmentService;
import com.example.services.UserService;
import com.example.services.YardService;
import com.example.utilities.FileDownloadUtil;
import com.example.utilities.FileUploadUtil;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = {"*"})

public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileUploadUtil fileUploadUtil;

    @Autowired
    private FileDownloadUtil fileDownloadUtil;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private YardService yardService;

    @Autowired
    private ModelMapper modelMapper;

    /* UTILIDADES */

    // Método validación email
    public static boolean IsValidEmail(User user) {
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@poliamor.com");
        Matcher matcher = pattern.matcher(user.getEmail());
        if (matcher.find() == true) {
            return true;
        } else {
            return false;
        }
    }

    /* OPCIONES DE ADMINISTRACIÓN */

    /* 1.LISTADO PAGINADO */
    @GetMapping("/admin/all")
    public ResponseEntity<List<User>> findAll(@RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size) {

        ResponseEntity<List<User>> responseEntity = null;
        List<User> users = new ArrayList<>();
        Sort sortByName = Sort.by("name");

        if (page != null && size != null) {
            try {
                Pageable pageable = PageRequest.of(page, size, sortByName);
                Page<User> usersPaginados = userService.findAll(pageable);
                users = usersPaginados.getContent();
                responseEntity = new ResponseEntity<List<User>>(users, HttpStatus.OK);
            } catch (Exception e) {
                responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            try {
                users = userService.findAll(sortByName);
                responseEntity = new ResponseEntity<List<User>>(users, HttpStatus.OK);

            } catch (Exception e) {
                responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return responseEntity;
    }

    /* 2. BUSCAR USER CON TODOS LOS DATOS (ADMIN) */

    /* 2.1. BUSCAR POR ID */

    @GetMapping("admin/find/id/{id}")
    public ResponseEntity<Map<String, Object>> findById(@PathVariable(name = "id") Integer id) {

        ResponseEntity<Map<String, Object>> responseEntity = null;
        Map<String, Object> responseAsMap = new HashMap<>();

        try {
            User user = userService.findbyId(id);
            if (user != null) {
                String successMessage = "Se ha encontrado el Usuario con id: " + id + " correctamente";
                responseAsMap.put("mensaje", successMessage);
                responseAsMap.put("user", user);
                // responseAsMap.put("mascotas", cliente.getMascotas());
                responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.OK);
            } else {
                String errorMessage = "No se ha encontrado el usuario con id: " + id;
                responseAsMap.put("error", errorMessage);
                responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            String errorGrave = "Error grave";
            responseAsMap.put("error", errorGrave);
            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

    /* 2.2. BUSCAR POR EMAIL */

    @GetMapping("admin/find/email/{email}")
    public ResponseEntity<Map<String, Object>> getByEmail(@PathVariable("email") String email) {

        ResponseEntity<Map<String, Object>> responseEntity = null;
        Map<String, Object> responseAsMap = new HashMap<>();

        try {
            User user = userService.findByEmail(email);
            if (user != null) {
                String successMessage = "Se ha encontrado el Usuario con email: " + email + " correctamente";
                responseAsMap.put("mensaje", successMessage);
                responseAsMap.put("user", user);
                // responseAsMap.put("mascotas", cliente.getMascotas());
                responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.OK);
            } else {
                String errorMessage = "No se ha encontrado el usuario con email: " + email;
                responseAsMap.put("error", errorMessage);
                responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            String errorGrave = "Error grave";
            responseAsMap.put("error", errorGrave);
            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

    /* 3. BORRAR USUARIOS (ADMIN) */

    /* 3.1. BORRAR POR ID */
    @DeleteMapping("admin/delete/id/{id}")
    @Transactional
    public ResponseEntity<String> delete(@PathVariable(name = "id") Integer id) {
        ResponseEntity<String> responseEntity = null;

        try {

            User user = userService.findbyId(id);
            if (user != null) {
                userService.delete(user);
                responseEntity = new ResponseEntity<String>("Usuario borrado exitosamente", HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<String>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }

        } catch (DataAccessException e) {
            e.getMostSpecificCause();
            responseEntity = new ResponseEntity<String>("Error fatal", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;

    }

    /* 3.2. BORRAR POR EMAIL */
    @DeleteMapping("admin/delete/email/{email}")
    @Transactional
    public ResponseEntity<String> delete(@PathVariable("email") String email) {
        ResponseEntity<String> responseEntity = null;

        try {

            User user = userService.findByEmail(email);
            if (user != null) {
                userService.delete(user);
                responseEntity = new ResponseEntity<String>("Usuario borrado exitosamente", HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<String>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }

        } catch (DataAccessException e) {
            e.getMostSpecificCause();
            responseEntity = new ResponseEntity<String>("Error fatal", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;

    }

    /* 4. ACTUALIZAR USERS (CUALQUIER USER/TODOS LOS CAMPOS) */
    @PutMapping("/admin/update")
    @Transactional
    public ResponseEntity<Map<String, Object>> update(
            @Valid @RequestPart(name = "user") User user,
            BindingResult result,
            @RequestPart(name = "fileUser", required = false) MultipartFile fileUser)
            // ,
            // @RequestPart(name = "email", required = false) String email)
            throws IOException {

        Map<String, Object> responseAsMap = new HashMap<>();
        ResponseEntity<Map<String, Object>> responseEntity = null;

        // Validación email
        if (IsValidEmail(user) == false) {
            String mensajeError = "Sólo puede utilizar un email del dominio de la empresa";
            responseAsMap.put("error", mensajeError);
            return new ResponseEntity<>(responseAsMap, HttpStatus.BAD_REQUEST);
        }

        if (result.hasErrors()) {

            List<String> errorMessages = new ArrayList<>();

            for (ObjectError error : result.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }

            responseAsMap.put("errores", errorMessages);

            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.BAD_REQUEST);
            return responseEntity;
        }

        if (!fileUser.isEmpty()) {
            String fileCode = fileUploadUtil.saveFile(fileUser.getOriginalFilename(), fileUser);
            user.setImageUser(fileCode + "-" + fileUser.getOriginalFilename());

            FileUploadResponse fileUploadResponse = FileUploadResponse
                    .builder()
                    .fileName(fileCode + "-" + fileUser.getOriginalFilename())
                    .downloadURI("/users/downloadFile/" + fileCode + "-" + fileUser.getOriginalFilename())
                    .size(fileUser.getSize())
                    .build();

            responseAsMap.put("info de la imagen: ", fileUploadResponse);
        }

        User userDB = userService.update(user);

        try {

            if (userDB != null) {

                Department department = userDB.getDepartment();
                List<Yard> yards = userDB.getYards();

                if (department != null) {

                    departmentService.save(department);
                    userDB.setDepartment(department);
                }

                if (yards.size() != 0) {

                    for (Yard yard : yards) {
                        yardService.save(yard);
                    }
                    userDB.setYards(yards);
                }

                String message = "El usuario se ha actualizado correctamente";
                responseAsMap.put("mensaje", message);
                responseAsMap.put("usuario", userDB);
                responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.CREATED);

            } else {

                String errorMensaje = "El usuario no se ha actualizado correctamente";

                responseAsMap.put("mensaje", errorMensaje);

                responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap,
                        HttpStatus.INTERNAL_SERVER_ERROR);

            }

        } catch (DataAccessException e) {
            String errorGrave = "Se ha producido un error grave y la causa puede ser " + e.getMostSpecificCause();
            responseAsMap.put("errorGrave", errorGrave);
            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;

    }

    /* 4. DESCARGAR IMAGENES DEL USER- SOLO PARA ADMINS */
    @GetMapping("admin/downloadFile/{fileCode}")
    public ResponseEntity<?> downloadFile(@PathVariable(name = "fileCode") String fileCode) {

        Resource resource = null;

        try {
            resource = fileDownloadUtil.getFileAsResource(fileCode);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
        if (resource == null) {
            return new ResponseEntity<>("File not found ", HttpStatus.NOT_FOUND);
        }
        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);

    }

    /* OPCIONES HABILITADAS PARA USERS */

    /* 1. AÑADIR USUARIOS (HABILITADA PARA AMBOS) */
    @PostMapping(value = "/add", consumes = "multipart/form-data")
    @Transactional
    public ResponseEntity<Map<String, Object>> insert(@Valid @RequestPart(name = "user") User user,
            BindingResult result,
            @RequestPart(name = "fileUser", required = false) MultipartFile fileUser) throws IOException {

        Map<String, Object> responseAsMap = new HashMap<>();
        ResponseEntity<Map<String, Object>> responseEntity = null;

        // Validación email
        if (IsValidEmail(user) == false) {
            String mensajeError = "Sólo puede utilizar un email del dominio de la empresa";
            responseAsMap.put("error", mensajeError);
            return new ResponseEntity<>(responseAsMap, HttpStatus.BAD_REQUEST);
        }
        if (result.hasErrors()) {
            List<String> errorMessages = new ArrayList<>();
            for (ObjectError error : result.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            responseAsMap.put("errores", errorMessages);
            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.BAD_REQUEST);
            return responseEntity;

        }
        
        User userDB = userService.add(user);
        try {
            if (userDB != null) {
                String message = "El usuario se ha creado correctamente";
                responseAsMap.put("mensaje", message);
                responseAsMap.put("usuario", userDB);
                responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.CREATED);
             
            if (!fileUser.isEmpty()) {
                String fileCode = fileUploadUtil.saveFile(fileUser.getOriginalFilename(), fileUser);
                user.setImageUser(fileCode + "-" + fileUser.getOriginalFilename());
    
                FileUploadResponse fileUploadResponse = FileUploadResponse
                        .builder()
                        .fileName(fileCode + "-" + fileUser.getOriginalFilename())
                        .downloadURI("/users/downloadFile/" + fileCode + "-" + fileUser.getOriginalFilename())
                        .size(fileUser.getSize())
                        .build();
    
                responseAsMap.put("info de la imagen: ", fileUploadResponse);
            }}
            
            else {
                String message = "El usuario no se ha creado correctamente";
                responseAsMap.put("mensaje", message);
                responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.BAD_REQUEST);
            }

        } catch (DataAccessException e) {
            String errorGrave = "Ha tenido lugar un error grave  y, la causa más problable puede ser: "
                    + e.getMostSpecificCause();
            responseAsMap.put("errorGrave", errorGrave);
            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    // BUSQUEDA PARA USERS

    /* 1. LISTADO USER */

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> findAllUsers(@RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size) {

        ResponseEntity<List<UserDto>> responseEntity = null;
        List<UserDto> users = new ArrayList<>();
        Sort sortByName = Sort.by("name");

        if (page != null && size != null) {
            try {
                Pageable pageable = PageRequest.of(page, size, sortByName);
                Page<User> usersPaginados = userService.findAll(pageable);
                users = usersPaginados.getContent().stream().map(p -> modelMapper.map(p, UserDto.class))
                        .collect(Collectors.toList());
                responseEntity = new ResponseEntity<List<UserDto>>(users, HttpStatus.OK);
            } catch (Exception e) {
                responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            try {
                users = userService.findAll(sortByName).stream().map(p -> modelMapper.map(p, UserDto.class))
                        .collect(Collectors.toList());

                responseEntity = new ResponseEntity<List<UserDto>>(users, HttpStatus.OK);

            } catch (Exception e) {
                responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return responseEntity;
    }

    /* 2. BUSCAR USUARIOS */

    /* 2.1. BUSCAR POR EMAIL */
    @GetMapping("/find/{email}")
    public ResponseEntity<UserDto> findByEmail(@PathVariable(name = "email") String email) {
        ResponseEntity<UserDto> responseEntity = null;

        try {
            User userNormal = userService.findByEmail(email);
            UserDto userDtoEmail = modelMapper.map(userNormal, UserDto.class);
            responseEntity = new ResponseEntity<>(userDtoEmail, HttpStatus.OK);

        } catch (Exception e) {
            responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
        return responseEntity;
    }

    /* 3. UPDATE USER */

    @PutMapping("/update/{currentUserEmail}")
    @Transactional
    public ResponseEntity<Map<String, Object>> update(
            @Valid @RequestPart(name = "user") User user,
            BindingResult result,
            @PathVariable(name = "currentUserEmail") String currentUserEmail,
            @RequestPart(name = "fileUser", required = false) MultipartFile fileUser)
            throws IOException {

        Map<String, Object> responseAsMap = new HashMap<>();
        ResponseEntity<Map<String, Object>> responseEntity = null;

        // Validación email
        if (IsValidEmail(user) == false) {
            String mensajeError = "Sólo puede utilizar un email del dominio de la empresa";
            responseAsMap.put("error", mensajeError);
            return new ResponseEntity<>(responseAsMap, HttpStatus.BAD_REQUEST);
        }

        if (!currentUserEmail.equals(user.getEmail())) {
            String mensajeError = "Sólo puede actualizar su perfil";
            responseAsMap.put("error", mensajeError);
            return new ResponseEntity<>(responseAsMap, HttpStatus.BAD_REQUEST);

        }
        if (result.hasErrors()) {
            List<String> errorMessages = new ArrayList<>();
            for (ObjectError error : result.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            responseAsMap.put("errores", errorMessages);
            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.BAD_REQUEST);
            return responseEntity;
        }

        if (!fileUser.isEmpty()) {
            String fileCode = fileUploadUtil.saveFile(fileUser.getOriginalFilename(), fileUser);
            user.setImageUser(fileCode + "-" + fileUser.getOriginalFilename());
            FileUploadResponse fileUploadResponse = FileUploadResponse
                    .builder()
                    .fileName(fileCode + "-" + fileUser.getOriginalFilename())
                    .downloadURI("/users/downloadFile/" + fileCode + "-" + fileUser.getOriginalFilename())
                    .size(fileUser.getSize())
                    .build();
            responseAsMap.put("info de la imagen: ", fileUploadResponse);
        }

        User userDB = userService.update(user);

        try {
            if (userDB != null) {
                Department department = userDB.getDepartment();
                List<Yard> yards = userDB.getYards();

                if (department != null) {
                    departmentService.save(department);
                    userDB.setDepartment(department);
                }

                if (yards.size() != 0) {
                    for (Yard yard : yards) {
                        yardService.save(yard);
                    }
                    userDB.setYards(yards);
                }
                String message = "El usuario se ha actualizado correctamente";
                responseAsMap.put("mensaje", message);
                responseAsMap.put("usuario", userDB);
                responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.CREATED);

            } else {
                String errorMensaje = "El usuario no se ha actualizado correctamente";
                responseAsMap.put("mensaje", errorMensaje);
                responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap,
                        HttpStatus.INTERNAL_SERVER_ERROR);

            }
        } catch (DataAccessException e) {
            String errorGrave = "Se ha producido un error grave y la causa puede ser " + e.getMostSpecificCause();
            responseAsMap.put("errorGrave", errorGrave);
            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    /* 4. ORDENAR POR GRUPOS */

    // ORDENADO POR YARD
    // @GetMapping("/yards")

    // public ResponseEntity <Map <UserDto, List<Post>>> lista() {
    // Map<Object, List<UserDto>> usuariosPorYards = new HashMap<>();
    // List<UserDto> usuariosDto = userService.findAll().stream().map(p ->
    // modelMapper.map(p, UserDto.class))
    // .collect(Collectors.toList());
    // usuariosPorYards =
    // usuariosDto.stream().map(Object).collect(Collectors.groupingBy(p ->
    // p.getPosts());

    // return new ResponseEntity<>(usuariosPorYards, HttpStatus.OK);

    // }

}