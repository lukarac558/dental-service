package com.student.reservationservice.user.address.voivodeship.service;

import com.student.api.VoivodeshipDTO;
import com.student.reservationservice.common.exception.entity.NotFoundException;
import com.student.reservationservice.user.address.voivodeship.entity.Voivodeship;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.student.reservationservice.common.exception.entity.ErrorConstants.VOIVODESHIP_ID_NOT_FOUND_MESSAGE;
import static com.student.reservationservice.common.exception.entity.ErrorConstants.VOIVODESHIP_NAME_NOT_FOUND_MESSAGE;

@RestController
@RequestMapping("/voivodeship")
@Tag(name = "Voivodeship")
public class VoivodeshipResource {
    private final ModelMapper modelMapper;
    private final VoivodeshipService voivodeshipService;

    @Autowired
    public VoivodeshipResource(VoivodeshipService voivodeshipService, ModelMapper modelMapper) {
        this.voivodeshipService = voivodeshipService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    @Operation(summary = "Find all available voivodeships.")
    public ResponseEntity<List<VoivodeshipDTO>> getAllVoivodeships() {
        List<Voivodeship> voivodeships = voivodeshipService.findAllVoivodeships();
        List<VoivodeshipDTO> voivodeshipsResponse = voivodeships.stream().map(v -> modelMapper.map(v, VoivodeshipDTO.class)).toList();
        return new ResponseEntity<>(voivodeshipsResponse, HttpStatus.OK);
    }

    @GetMapping("/find-by/{name}")
    @ApiResponse(responseCode = "404", description = "Voivodeship not found")
    @Operation(summary = "Find voivodeship by name.")
    public ResponseEntity<VoivodeshipDTO> getVoivodeshipByName(@PathVariable("name") String name) {
        Voivodeship voivodeship = voivodeshipService.findVoivodeshipByName(name)
                .orElseThrow(() -> new NotFoundException(String.format(VOIVODESHIP_NAME_NOT_FOUND_MESSAGE, name)));
        return new ResponseEntity<>(modelMapper.map(voivodeship, VoivodeshipDTO.class), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiResponse(responseCode = "404", description = "Voivodeship not found")
    @Operation(summary = "Find voivodeship by id.")
    public ResponseEntity<VoivodeshipDTO> getVoivodeshipById(@PathVariable("id") int id) {
        Voivodeship voivodeship = voivodeshipService.findVoivodeshipById(id)
                .orElseThrow(() -> new NotFoundException(String.format(VOIVODESHIP_ID_NOT_FOUND_MESSAGE, id)));
        return new ResponseEntity<>(modelMapper.map(voivodeship, VoivodeshipDTO.class), HttpStatus.OK);
    }

    @PostMapping("/")
    @ApiResponse(responseCode = "409", description = "Voivodeship with given name already exists")
    @Operation(summary = "Add new voivodeship.")
    public ResponseEntity<VoivodeshipDTO> addVoivodeship(@RequestParam String name) {
        Voivodeship addedVoivodeship = voivodeshipService.addOrUpdateVoivodeship(new Voivodeship(name));
        return new ResponseEntity<>(modelMapper.map(addedVoivodeship, VoivodeshipDTO.class), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiResponse(responseCode = "404", description = "Voivodeship not found")
    @ApiResponse(responseCode = "409", description = "Voivodeship with given name already exists")
    @Operation(summary = "Update voivodeship by id.")
    public ResponseEntity<VoivodeshipDTO> updateVoivodeship(@PathVariable("id") int id,
                                                            @RequestParam String name) {
        Voivodeship voivodeship = voivodeshipService.findVoivodeshipById(id)
                .map(v -> {
                    v.setName(name);
                    return v;
                })
                .orElseThrow(() -> new NotFoundException(String.format(VOIVODESHIP_ID_NOT_FOUND_MESSAGE, id)));

        Voivodeship updatedVoivodeship = voivodeshipService.addOrUpdateVoivodeship(voivodeship);
        return new ResponseEntity<>(modelMapper.map(updatedVoivodeship, VoivodeshipDTO.class), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "409", description = "Deletion is impossible because voivodeship with given id is used by some city")
    @Operation(summary = "Delete voivodeship by id.")
    public ResponseEntity<?> deleteVoivodeship(@PathVariable("id") int id) {
        voivodeshipService.deleteVoivodeship(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
