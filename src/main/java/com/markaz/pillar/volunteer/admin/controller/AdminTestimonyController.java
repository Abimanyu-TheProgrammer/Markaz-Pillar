package com.markaz.pillar.volunteer.admin.controller;

import com.markaz.pillar.config.controller.model.annotation.ResponseMessage;
import com.markaz.pillar.tools.file.impl.StaticImageStorage;
import com.markaz.pillar.volunteer.admin.controller.model.TestimonyDTO;
import com.markaz.pillar.volunteer.admin.controller.model.TestimonyRequestDTO;
import com.markaz.pillar.volunteer.repository.ProgramRepository;
import com.markaz.pillar.volunteer.repository.TestimonyRepository;
import com.markaz.pillar.volunteer.repository.model.ProgramTestimony;
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

    @GetMapping(params = {"id"})
    public TestimonyDTO getTestimonyByID(@RequestParam int id) {
        return TestimonyDTO.mapFrom(
                testimonyRepository.getById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Testimony not found!"))
        );
    }

    @PostMapping(params = {"program_id"}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public TestimonyDTO createTestimony(@RequestParam(name = "program_id") Integer id,
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

        program.getTestimonies().add(entity);
        entity.setProgram(program);

        return TestimonyDTO.mapFrom(testimonyRepository.save(entity));
    }

    @PostMapping(
            value = "/edit",
            params = {"id"},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @ResponseMessage("Testimony is updated!")
    public TestimonyDTO updateById(@RequestParam int id,
                                   @RequestPart(required = false) MultipartFile thumbnail,
                                   @RequestPart @Valid TestimonyRequestDTO detail) throws IOException {
        ProgramTestimony entity = testimonyRepository.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Testimony not found"));

        entity.setName(detail.getName());
        entity.setDescription(detail.getDescription());

        if(thumbnail != null) {
            String thumbnailURL = fileStorage.saveFile(thumbnail, Paths.get("markaz", "thumbnail"));
            entity.setThumbnailURL(thumbnailURL);
        }

        return TestimonyDTO.mapFrom(testimonyRepository.save(entity));
    }

    @DeleteMapping(params = {"id"})
    @ResponseMessage("Entity is deleted")
    public void softDeleteByID(@RequestParam int id) {
        testimonyRepository.deleteById(id);
    }
}
