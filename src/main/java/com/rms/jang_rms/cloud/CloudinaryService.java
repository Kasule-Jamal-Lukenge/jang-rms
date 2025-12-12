package com.rms.jang_rms.cloud;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file){
        try{
            Map uploaded = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", "rental_properties")
            );
            return uploaded.get("secure_url").toString();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
