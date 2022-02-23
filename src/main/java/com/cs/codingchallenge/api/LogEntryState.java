package com.cs.codingchallenge.api;

import com.google.gson.annotations.SerializedName;

public enum LogEntryState {

    @SerializedName("STARTED")
    STARTED,

    @SerializedName("FINISHED")
    FINISHED,

}
