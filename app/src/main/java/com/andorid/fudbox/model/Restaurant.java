package com.andorid.fudbox.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Restaurant {
    @Builder.Default private String name = "N/A";
    @Builder.Default private String address = "N/A";
    @Builder.Default private String city = "N/A";
    @Builder.Default private String suburb = "N/A";
    @Builder.Default private String phone = "N/A";
    @Builder.Default private Float lat = 0.0f;
    @Builder.Default private Float log = 0.0f;
}
