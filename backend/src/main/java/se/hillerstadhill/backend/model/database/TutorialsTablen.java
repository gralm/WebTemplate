package se.hillerstadhill.backend.model.database;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
public class TutorialsTablen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tutorialId;
    private String tutorialTitle;
    private String tutorialAuthor;

    // name must not be set,
    // @Column(name = "submission_dat", columnDefinition = "DATE")
    // this will set submission_dat in table to present date of some strange reason
    // @Column(columnDefinition = "DATE")
    // this does not affect outcome
    // @Temporal(TemporalType.DATE)
    // submissionDate must not have the same name as table date-column, of some strange reason.
    @Column(columnDefinition = "TIMESTAMP")
    private Date submissionDate;

    /*
    Used for relations to other tables
        @OneToOne
    @JoinColumn(name = "address_id")
    @RestResource(path = "libraryAddress", rel="address")
    private Address address;
     */

    protected TutorialsTablen() {
    }

    public TutorialsTablen(String tutorialTitle, String tutorialAuthor) {
        this.tutorialTitle = tutorialTitle;
        this.tutorialAuthor = tutorialAuthor;
        this.submissionDate = new Date();
    }
}