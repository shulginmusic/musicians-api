package com.example.MusiciansAPI.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter

@Entity
public class Musician {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 25)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String famousAlbum;

    public Musician(@NotBlank @Size(max = 25) String name, @NotBlank @Size(max = 50) String famousAlbum) {
        this.name = name;
        this.famousAlbum = famousAlbum;
    }
}
