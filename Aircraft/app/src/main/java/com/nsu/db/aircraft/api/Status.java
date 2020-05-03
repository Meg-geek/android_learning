package com.nsu.db.aircraft.api;

import com.google.gson.annotations.Expose;


public enum Status {
    @Expose
    OK,
    UNEXPECTED_ERROR, ERROR
}
