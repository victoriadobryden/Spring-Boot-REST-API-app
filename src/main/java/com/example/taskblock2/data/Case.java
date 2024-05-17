package com.example.taskblock2.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Case {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "investigator_id")
    private Investigator investigator;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date;

    @JsonProperty("placeOfEvent")
    private String placeOfEvent;

    @ElementCollection
    @JsonProperty("namesOfVictims")
    private List<String> namesOfVictims;

    @ElementCollection
    @JsonProperty("charges")
    private List<String> charges;

}
