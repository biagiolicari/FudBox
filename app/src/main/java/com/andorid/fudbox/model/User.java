package com.andorid.fudbox.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Builder
@Getter
@Setter
public class User {
    private String username ;
    @Nullable
    private String pwd;
    private String uuid;
    @Builder.Default private Boolean logged = Boolean.FALSE;

}
