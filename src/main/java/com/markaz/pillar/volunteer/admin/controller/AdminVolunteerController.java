package com.markaz.pillar.volunteer.admin.controller;

import com.github.slugify.Slugify;
import com.markaz.pillar.tools.file.impl.StaticImageStorage;
import com.markaz.pillar.volunteer.admin.controller.model.TestimonyDTO;
import com.markaz.pillar.volunteer.admin.controller.model.TestimonyRequestDTO;
import com.markaz.pillar.volunteer.admin.controller.model.VolunteerProgramRequestDTO;
import com.markaz.pillar.volunteer.admin.controller.model.VolunteerProgramSimpleDTO;
import com.markaz.pillar.volunteer.repository.ProgramRepository;
import com.markaz.pillar.volunteer.repository.TestimonyRepository;
import com.markaz.pillar.volunteer.repository.model.ProgramTestimony;
import com.markaz.pillar.volunteer.repository.model.VolunteerProgram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Paths;

@RestController
@RequestMapping("/admin/volunteer/program")
@PreAuthorize("isAuthenticated() and hasAnyAuthority('CRUD_VOLUNTEER', 'CRUD_TESTIMONY')")
public class AdminVolunteerController {
    private ProgramRepository repository;
    private TestimonyRepository testimonyRepository;
    private StaticImageStorage fileStorage;

    @Autowired
    public void setRepository(ProgramRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setTestimonyRepository(TestimonyRepository testimonyRepository) {
        this.testimonyRepository = testimonyRepository;
    }

    @Autowired
    public void setFileStorage(StaticImageStorage fileStorage) {
        this.fileStorage = fileStorage;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CRUD_VOLUNTEER')")
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

    @PostMapping(value = "/testimony", params = {"id"})
    @PreAuthorize("hasAuthority('CRUD_TESTIMONY')")
    public TestimonyDTO createTestimony(@RequestParam Integer id,
                                        @RequestPart MultipartFile thumbnail,
                                        @RequestPart @Valid TestimonyRequestDTO detail) throws IOException {
        VolunteerProgram program = repository.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Program doesn't exist"));

        String thumbnailURL = fileStorage.saveFile(
                thumbnail, Paths.get("volunteer", "testimony", "thumbnail")
        );

        ProgramTestimony entity = new ProgramTestimony();
        entity.setName(detail.getName());
        entity.setThumbnailURL(thumbnailURL);
        entity.setDescription(detail.getDescription());

        entity.setProgram(program);
        program.getTestimonies().add(entity);

        return TestimonyDTO.mapFrom(testimonyRepository.save(entity));
    }
}
