package com.example.MusiciansAPI.payload.request.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class APIResponse <T>{
    T data;
    String error;
}
