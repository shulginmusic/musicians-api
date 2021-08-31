package com.example.MusiciansAPI.payload.request.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class APIResponse <T>{
    T data;
    String error;
}
