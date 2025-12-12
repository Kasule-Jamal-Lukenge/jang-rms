package com.rms.jang_rms.modules.property;

import com.rms.jang_rms.modules.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final CloudinaryService cloudinaryService;
    private final UserRepository userRepository;

}
