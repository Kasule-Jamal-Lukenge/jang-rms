package com.rms.jang_rms.modules.property;

import com.rms.jang_rms.cloud.CloudinaryService;
import com.rms.jang_rms.modules.user.User;
import com.rms.jang_rms.modules.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final CloudinaryService cloudinaryService;
    private final UserRepository userRepository;

    public Property createProperty(Property property, Long ownerId){
        User owner = userRepository.findById(ownerId).orElseThrow(()->new RuntimeException("User Not Found"));
        property.setOwner(owner);
        return propertyRepository.save(property);
    }

    public List<Property> getAllProperties(){
        return propertyRepository.findAll();
    }

    public Property getPropertyById(Long propertyId){
        return propertyRepository.findById(propertyId).orElseThrow(()->new RuntimeException("Property Not Found"));
    }

    public Property updateProperty(Long propertyId, Property updatedProperty){
        Property property = getPropertyById(propertyId);
        property.setTitle(updatedProperty.getTitle());
        property.setDescription(updatedProperty.getDescription());
        property.setLocation(updatedProperty.getLocation());
        property.setRentFee(updatedProperty.getRentFee());
        property.setType(updatedProperty.getType());
        property.setStatus(updatedProperty.getStatus());

        return propertyRepository.save(property);
    }

    public void deleteProperty(Long propertyId){
        propertyRepository.deleteById(propertyId);
    }

    public Property uploadImage(Long propertyId, MultipartFile file){
        Property property = getPropertyById(propertyId);
        String imageUrl = cloudinaryService.uploadImage(file);
        property.getImages().add(imageUrl);
        return propertyRepository.save(property);
    }
}
