package com.markaz.pillar.volunteer.admin.controller;

import com.github.slugify.Slugify;
import com.markaz.pillar.tools.file.impl.StaticImageStorage;
import com.markaz.pillar.volunteer.admin.controller.model.VolunteerProgramRequestDTO;
import com.markaz.pillar.volunteer.admin.controller.model.VolunteerProgramSimpleDTO;
import com.markaz.pillar.volunteer.repository.ProgramRepository;
import com.markaz.pillar.volunteer.repository.model.VolunteerProgram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Paths;

@RestController
@RequestMapping("/admin/volunteer")
@PreAuthorize("isAuthenticated() and hasAuthority('CRUD_PROGRAM')")
public class AdminProgramController {
    private ProgramRepository repository;
    private StaticImageStorage fileStorage;

    @Autowired
    public void setRepository(ProgramRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setFileStorage(StaticImageStorage fileStorage) {
        this.fileStorage = fileStorage;
    }

    @PostMapping
    public VolunteerProgramSimpleDTO createProgram(@RequestPart MultipartFile thumbnail,
                                                   @RequestPart @Valid VolunteerProgramRequestDTO detail)
            throws IOException {
        if(repository.existsByName(detail.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Program with the same name exists");
        }

        String slug = new Slugify().slugify(detail.getName());
        String thumbnailURL = fileStorage.saveFile(thumbnail, Paths.get("volunteer", "program", "thumbnail"));

        VolunteerProgram entity = new VolunteerProgram();
        entity.setName(detail.getName());
        entity.setSlug(slug);
        entity.setThumbnailURL(thumbnailURL);
        entity.setDescription(detail.getDescription());
        entity.setBenefit(detail.getBenefit());
        entity.setVolunteerNeeded(detail.getVolunteerNeeded());
        entity.setLocation(detail.getLocation());
        entity.setSchedule(detail.getSchedule());

        return VolunteerProgramSimpleDTO.mapFrom(repository.save(entity));
    }
}
