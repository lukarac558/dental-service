package com.student.reservationservice.user.address.voivodeship.service;

import com.student.api.VoivodeshipCreationDTO;
import com.student.api.VoivodeshipDTO;
import com.student.reservationservice.user.address.voivodeship.entity.Voivodeship;
import com.student.reservationservice.user.address.voivodeship.exception.VoivodeshipNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/voivodeship")
public class VoivodeshipResource {
    private final ModelMapper modelMapper;
    private final VoivodeshipService voivodeshipService;

    @Autowired
    public VoivodeshipResource(VoivodeshipService voivodeshipService, ModelMapper modelMapper) {
        this.voivodeshipService = voivodeshipService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    public ResponseEntity<List<VoivodeshipDTO>> getAllVoivodeships() {
        List<Voivodeship> voivodeships = voivodeshipService.findAllVoivodeships();
        List<VoivodeshipDTO> voivodeshipsResponse = voivodeships.stream().map(v -> modelMapper.map(v, VoivodeshipDTO.class)).toList();
        return new ResponseEntity<>(voivodeshipsResponse, HttpStatus.OK);
    }

    @GetMapping("/find/by-name/{name}")
    public ResponseEntity<VoivodeshipDTO> getVoivodeshipByName(@PathVariable("name") String name) {
        Voivodeship voivodeship = voivodeshipService.findVoivodeshipByName(name)
                .orElseThrow(() -> new VoivodeshipNotFoundException(name));
        return new ResponseEntity<>(modelMapper.map(voivodeship, VoivodeshipDTO.class), HttpStatus.OK);
    }

    @GetMapping("/find/by-id/{id}")
    public ResponseEntity<VoivodeshipDTO> getVoivodeshipById(@PathVariable("id") int id) {
        Voivodeship voivodeship = voivodeshipService.findVoivodeshipById(id)
                .orElseThrow(() -> new VoivodeshipNotFoundException(id));
        return new ResponseEntity<>(modelMapper.map(voivodeship, VoivodeshipDTO.class), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<VoivodeshipDTO> addVoivodeship(@RequestBody VoivodeshipCreationDTO voivodeshipCreationDTO) {
        Voivodeship voivodeship = modelMapper.map(voivodeshipCreationDTO, Voivodeship.class);
        Voivodeship addedVoivodeship = voivodeshipService.addOrUpdateVoivodeship(voivodeship);
        return new ResponseEntity<>(modelMapper.map(addedVoivodeship, VoivodeshipDTO.class), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<VoivodeshipDTO> updateVoivodeship(@PathVariable("id") int id,
                                                            @RequestParam String name) {
        Voivodeship voivodeship = voivodeshipService.findVoivodeshipById(id)
                .map(v -> {
                    v.setName(name);
                    return v;
                })
                .orElseThrow(() -> new VoivodeshipNotFoundException(id));

        Voivodeship updatedVoivodeship = voivodeshipService.addOrUpdateVoivodeship(voivodeship);
        return new ResponseEntity<>(modelMapper.map(updatedVoivodeship, VoivodeshipDTO.class), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVoivodeship(@PathVariable("id") int id) {
        voivodeshipService.deleteVoivodeship(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
