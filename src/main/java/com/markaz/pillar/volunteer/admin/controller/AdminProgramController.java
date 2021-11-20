package com.markaz.pillar.volunteer.admin.controller;

import com.github.slugify.Slugify;
import com.markaz.pillar.config.controller.model.annotation.ResponseMessage;
import com.markaz.pillar.tools.file.impl.StaticImageStorage;
import com.markaz.pillar.volunteer.admin.controller.model.VolunteerProgramDTO;
import com.markaz.pillar.volunteer.admin.controller.model.VolunteerProgramRequestDTO;
import com.markaz.pillar.volunteer.repository.ProgramRepository;
import com.markaz.pillar.volunteer.repository.model.VolunteerProgram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public VolunteerProgramDTO createProgram(@RequestPart MultipartFile thumbnail,
                                             @RequestPart @Valid VolunteerProgramRequestDTO detail)
            throws IOException {
        if(repository.existsByName(detail.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Program with the same name exists");
        }

        String thumbnailURL = fileStorage.saveFile(thumbnail, Paths.get("volunteer", "program", "thumbnail"));

        VolunteerProgram entity = new VolunteerProgram();
        entity.setName(detail.getName());
        entity.setSlug(new Slugify().slugify(detail.getName()));
        entity.setThumbnailURL(thumbnailURL);
        entity.setDescription(detail.getDescription());
        entity.setBenefit(detail.getBenefit());
        entity.setTerm(detail.getTerm());
        entity.setVolunteerNeeded(detail.getVolunteerNeeded());
        entity.setLocation(detail.getLocation());
        entity.setSchedule(detail.getSchedule());

        return VolunteerProgramDTO.mapFrom(repository.save(entity));
    }

    @PostMapping(
            value = "/edit",
            params = {"id"},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @ResponseMessage("Program is updated!")
    public VolunteerProgramDTO updateById(@RequestParam int id,
                                          @RequestPart(required = false) MultipartFile thumbnail,
                                          @RequestPart @Valid VolunteerProgramRequestDTO detail) throws IOException {
        VolunteerProgram entity = repository.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Program not found"));

        if(!entity.getName().equals(detail.getName()) && repository.existsByName(detail.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Program with the same name exists");
        }

        entity.setName(detail.getName());
        entity.setSlug(new Slugify().slugify(detail.getName()));
        entity.setDescription(detail.getDescription());
        entity.setBenefit(detail.getBenefit());
        entity.setTerm(detail.getTerm());
        entity.setVolunteerNeeded(detail.getVolunteerNeeded());
        entity.setLocation(detail.getLocation());
        entity.setSchedule(detail.getSchedule());

        if(thumbnail != null) {
            String thumbnailURL = fileStorage.saveFile(thumbnail, Paths.get("markaz", "thumbnail"));
            entity.setThumbnailURL(thumbnailURL);
        }

        return VolunteerProgramDTO.mapFrom(repository.save(entity));
    }
}
