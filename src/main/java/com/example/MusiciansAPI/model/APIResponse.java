package com.example.MusiciansAPI.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class APIResponse <T>{
    T data;
    String error;
}
