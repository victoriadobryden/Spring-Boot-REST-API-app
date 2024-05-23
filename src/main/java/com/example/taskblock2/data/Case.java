package com.example.taskblock2.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "case_entity")
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

    @Column(name = "place_of_event")
    @JsonProperty("placeOfEvent")
    private String placeOfEvent;

    @ElementCollection
    @CollectionTable(name = "case_entity_names_of_victims", joinColumns = @JoinColumn(name = "case_id"))
    @Column(name = "names_of_victims")
    @JsonProperty("namesOfVictims")
    private List<String> namesOfVictims;

    @ElementCollection
    @CollectionTable(name = "case_entity_charges", joinColumns = @JoinColumn(name = "case_id"))
    @Column(name = "charges")
    @JsonProperty("charges")
    private List<String> charges;
}


