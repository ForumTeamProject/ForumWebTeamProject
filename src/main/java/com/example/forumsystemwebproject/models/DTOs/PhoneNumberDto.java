package com.example.forumsystemwebproject.models.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PhoneNumberDto {

    @NotNull(message = "You cannot add an empty number!")
    @Size(min = 10, max = 13, message = "The phone number must be between 10 and 13 characters")
    @Pattern(regexp = "\\+?[0-9]+", message = "Phone number must be a numeric value and can start with a '+'")
    private String number;

    public PhoneNumberDto() {

    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
