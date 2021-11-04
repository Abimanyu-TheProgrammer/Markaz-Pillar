package com.markaz.pillar.transaction.controller.admin;

import com.markaz.pillar.markaz.repository.MarkazRepository;
import com.markaz.pillar.markaz.repository.model.Markaz;
import com.markaz.pillar.santri.repository.SantriRepository;
import com.markaz.pillar.santri.repository.model.Santri;
import com.markaz.pillar.transaction.controller.admin.model.EditStatusRequestDTO;
import com.markaz.pillar.transaction.controller.model.TransactionDTO;
import com.markaz.pillar.transaction.repository.UserTransactionRepository;
import com.markaz.pillar.transaction.repository.model.TransactionStatus;
import com.markaz.pillar.transaction.repository.model.UserTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/transaction")
@PreAuthorize("isAuthenticated() and hasAuthority('CRUD_DONATION')")
public class AdminTransactionController {
    private UserTransactionRepository repository;
    private MarkazRepository markazRepository;
    private SantriRepository santriRepository;

    @Autowired
    public void setMarkazRepository(MarkazRepository markazRepository) {
        this.markazRepository = markazRepository;
    }

    @Autowired
    public void setSantriRepository(SantriRepository santriRepository) {
        this.santriRepository = santriRepository;
    }

    @Autowired
    public void setRepository(UserTransactionRepository repository) {
        this.repository = repository;
    }

    @PostMapping(value = "/status", params = {"id"})
    public TransactionDTO editTransactionStatus(@RequestParam(name = "id") String trxId,
                                                @RequestBody @Valid EditStatusRequestDTO requestDTO) {
        UserTransaction transaction = repository.findByTrxId(trxId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transaction not found!"));

        if(requestDTO.getAmount() != null) {
            transaction.setAmount(requestDTO.getAmount());
        }

        transaction.setStatus(requestDTO.getStatus());
        if(requestDTO.getReason() != null
                && !requestDTO.getReason().isEmpty()
                && TransactionStatus.DONASI_DITOLAK.equals(requestDTO.getStatus())) {
            transaction.setReason(requestDTO.getReason());
        }

        return TransactionDTO.mapFrom(repository.save(transaction));
    }

    @GetMapping(params = {"markaz"})
    public Page<TransactionDTO> fetchTransactionsByMarkaz(@RequestParam(name = "markaz") int id,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int n) {
        Markaz markaz = markazRepository.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Markaz doesn't exist!"));

        return repository.findAllByDonationDetail_Markaz(markaz, PageRequest.of(page, n))
                .map(TransactionDTO::mapFrom);
    }

    @GetMapping(params = {"santri"})
    public Page<TransactionDTO> fetchTransactionsBySantri(@RequestParam(name = "santri") int id,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int n) {
        Santri santri = santriRepository.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Santri doesn't exist!"));

        return repository.findAllByDonationDetail_Santri(santri, PageRequest.of(page, n))
                .map(TransactionDTO::mapFrom);
    }
}
