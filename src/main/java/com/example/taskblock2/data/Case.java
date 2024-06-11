package com.example.taskblock2.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "case_entity")
public class Case {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investigator_id")
    private Investigator investigator;

    @Column(name = "date", columnDefinition = "DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Column(name = "place_of_event")
    private String placeOfEvent;

    @ElementCollection
    @CollectionTable(name = "case_entity_names_of_victims", joinColumns = @JoinColumn(name = "case_id"))
    @Column(name = "names_of_victims")
    private List<String> namesOfVictims;

    @ElementCollection
    @CollectionTable(name = "case_entity_charges", joinColumns = @JoinColumn(name = "case_id"))
    @Column(name = "charges")
    private List<String> charges;

}

