package com.markaz.pillar.volunteer.admin.controller;

import com.markaz.pillar.tools.file.impl.StaticImageStorage;
import com.markaz.pillar.volunteer.admin.controller.model.TestimonyDTO;
import com.markaz.pillar.volunteer.admin.controller.model.TestimonyRequestDTO;
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
@RequestMapping("/admin/volunteer/testimony")
@PreAuthorize("isAuthenticated() and hasAuthority('CRUD_TESTIMONY')")
public class AdminTestimonyController {
    private ProgramRepository repository;
    private TestimonyRepository testimonyRepository;
    private StaticImageStorage fileStorage;

    @Autowired
    public void setRepository(ProgramRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setFileStorage(StaticImageStorage fileStorage) {
        this.fileStorage = fileStorage;
    }

    @Autowired
    public void setTestimonyRepository(TestimonyRepository testimonyRepository) {
        this.testimonyRepository = testimonyRepository;
    }

    @PostMapping(params = {"id"})
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
