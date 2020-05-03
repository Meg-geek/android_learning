package com.nsu.db.aircraft.api;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GeneralResponse<T> {
    @Expose
    @SerializedName("status")
    private Status status;

    @Expose
    @SerializedName("data")
    private T data;

    @Expose
    @SerializedName("cause")
    private ErrorCause cause;

    public GeneralResponse(Status status, ErrorCause cause) {
        this.status = status;
        this.cause = cause;
        this.data = null;
    }

    public GeneralResponse(Status status) {
        this.status = status;
        this.data = null;
        this.cause = null;
    }

    public GeneralResponse(T data) {
        this.status = Status.OK;
        this.data = data;
        this.cause = null;
    }
}

