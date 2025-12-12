package com.rms.jang_rms.modules.property;

import com.rms.jang_rms.modules.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    @PostMapping
    public ResponseEntity<?> createProperty(@RequestBody Property property, @RequestParam Long ownerId){
        return new ResponseEntity<>(propertyService.createProperty(property, ownerId), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAllProperties(){
//        return new ResponseEntity<>(propertyService.getAllProperties(), HttpStatus.OK);
        return ResponseEntity.ok(propertyService.getAllProperties());
    }

    @GetMapping("/{propertyId}")
    public ResponseEntity<?> getPropertyById(@PathVariable Long propertyId){
        return ResponseEntity.ok(propertyService.getPropertyById(propertyId));
    }

    @PutMapping("/{propertyId}")
    public ResponseEntity<?> updateProperty(@PathVariable Long propertyId, @RequestBody Property property){
        return ResponseEntity.ok(propertyService.updateProperty(propertyId, property));
    }

    @DeleteMapping("/{propertyId}")
    public ResponseEntity<?> deleteProperty(@PathVariable Long propertyId){
        propertyService.deleteProperty(propertyId);
        return ResponseEntity.ok("Property Deleted Successfully");
    }

    @PostMapping("/{propertyId}/upload")
    public ResponseEntity<?> uploadImage(@PathVariable Long propertyId, @RequestParam("file") MultipartFile file){
        return ResponseEntity.ok(propertyService.uploadImage(propertyId, file));
    }
}
