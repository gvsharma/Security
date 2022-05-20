package com.security.security.entities;

import lombok.*;

import javax.persistence.Entity;

@Entity(name = "tags")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TagEntity extends BaseEntity {
    private  String name;
}
