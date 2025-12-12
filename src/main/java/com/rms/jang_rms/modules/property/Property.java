package com.rms.jang_rms.modules.property;

import com.rms.jang_rms.enums.PropertyStatus;
import com.rms.jang_rms.enums.PropertyType;
import com.rms.jang_rms.modules.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String location;
    private double rentFee;

    @Enumerated(EnumType.STRING)
    private PropertyType type;

    @Enumerated(EnumType.STRING)
    private PropertyStatus status = PropertyStatus.AVAILABLE;

    @ElementCollection
    private List<String> images = new ArrayList<>();

    @ManyToOne
    private User owner;

}
