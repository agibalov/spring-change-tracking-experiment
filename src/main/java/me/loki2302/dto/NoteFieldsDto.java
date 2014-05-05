package me.loki2302.dto;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class NoteFieldsDto {
    @NotEmpty
    @Length(max = 5)
    public String text;
}
