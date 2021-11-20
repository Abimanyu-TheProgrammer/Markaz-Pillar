package com.markaz.pillar.volunteer.controller;

import com.github.slugify.Slugify;
import com.markaz.pillar.auth.repository.UserRepository;
import com.markaz.pillar.auth.repository.models.AuthUser;
import com.markaz.pillar.tools.file.impl.RollingFileStorage;
import com.markaz.pillar.tools.file.impl.RollingImageStorage;
import com.markaz.pillar.volunteer.admin.controller.model.RegistrationDTO;
import com.markaz.pillar.volunteer.controller.model.ProgramRegistrationRequestDTO;
import com.markaz.pillar.volunteer.repository.ProgramRepository;
import com.markaz.pillar.volunteer.repository.VolunteerRepository;
import com.markaz.pillar.volunteer.repository.model.VolunteerProgram;
import com.markaz.pillar.volunteer.repository.model.VolunteerRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

@RestController
@RequestMapping("/volunteer/registration")
@PreAuthorize("isAuthenticated()")
public class UserRegistrationController {
    private VolunteerRepository repository;
    private ProgramRepository programRepository;
    private UserRepository userRepository;
    private RollingImageStorage imageStorage;
    private RollingFileStorage fileStorage;

    @Autowired
    public void setRepository(VolunteerRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setProgramRepository(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setFileStorage(RollingFileStorage fileStorage) {
        this.fileStorage = fileStorage;
    }

    @Autowired
    public void setImageStorage(RollingImageStorage imageStorage) {
        this.imageStorage = imageStorage;
    }

    @PostMapping(params = {"program_id"})
    public RegistrationDTO registerVolunteer(@RequestParam(name = "program_id") int id,
                                             @RequestPart @Valid ProgramRegistrationRequestDTO detail,
                                             @RequestPart MultipartFile picture,
                                             @RequestPart MultipartFile essay,
                                             @RequestPart MultipartFile cv,
                                             Principal principal) throws IOException {
        VolunteerProgram program = programRepository.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Program not found!"));
        AuthUser user = userRepository.findByEmail(principal.getName())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized!"));

        imageStorage.validateFile(picture);
        fileStorage.validateFile(essay);
        fileStorage.validateFile(cv);

        Path uploadPath = Paths.get(
                "volunteer", "program", String.valueOf(id), new Slugify().slugify(detail.getName())
        );
        String pictureURL = imageStorage.saveFile(picture, uploadPath);
        String essayURL = fileStorage.saveFile(essay, uploadPath);
        String cvURL = fileStorage.saveFile(cv, uploadPath);

        VolunteerRegistration entity = new VolunteerRegistration();
        entity.setName(detail.getName());
        entity.setKtp(detail.getKtp());
        entity.setPhoneNum(detail.getPhoneNum());
        entity.setEmail(detail.getEmail());
        entity.setAddress(detail.getAddress());
        entity.setPictureURL(pictureURL);
        entity.setEssayURL(essayURL);
        entity.setCvURL(cvURL);

        program.getRegistrations().add(entity);
        user.getRegistrations().add(entity);
        entity.setProgram(program);
        entity.setUser(user);

        return RegistrationDTO.mapFrom(repository.save(entity));
    }

    @GetMapping
    public Page<RegistrationDTO> fetchUserRegistrations(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int n,
                                                        Principal principal) {
        AuthUser user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is Unauthorized!"));

        return repository.findAllByUser(user, PageRequest.of(page, n))
                .map(RegistrationDTO::mapFrom);
    }
}
