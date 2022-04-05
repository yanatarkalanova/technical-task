package com.playtomic.tests.wallet.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.playtomic.tests.wallet.enums.ErrorLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
public class StatusDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private String statusCode;
    private String statusReason;
    private String timestamp;
    private List<String> arguments;
    private ErrorLevel level;

    public StatusDto() {
        this(null, null, null);
    }

    public StatusDto(String statusCode, String statusReason, ErrorLevel level, List<String> arguments) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
        timestamp = now.format(formatter);
        this.statusCode = statusCode;
        this.statusReason = statusReason;
        this.level = level;
        this.arguments = arguments;
    }

    public StatusDto(String statusCode, String statusReason, ErrorLevel level) {
        this(statusCode, statusReason, level, null);
    }

}
