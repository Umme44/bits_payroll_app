package com.bits.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Room.
 */
@Entity
@Table(name = "room")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 0, max = 250)
    @Column(name = "room_name", length = 250, nullable = false)
    private String roomName;

    @Size(min = 0, max = 250)
    @Column(name = "remarks", length = 250)
    private String remarks;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @NotNull
    @Min(value = 1)
    @Max(value = 100)
    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Size(min = 0, max = 500)
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "facilities", length = 500)
    private String facilities;

    @ManyToOne
    private User createdBy;

    @ManyToOne
    private User updatedBy;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "createdBy", "updatedBy" }, allowSetters = true)
    private Building building;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "createdBy", "updatedBy", "building" }, allowSetters = true)
    private Floor floor;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "createdBy", "updatedBy" }, allowSetters = true)
    private RoomType roomType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Room id(Long id) {
        this.setId(id);
        return this;
    }

    public Room roomName(String roomName) {
        this.setRoomName(roomName);
        return this;
    }

    public Room remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public Room createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public Room updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public Room capacity(Integer capacity) {
        this.setCapacity(capacity);
        return this;
    }

    public Room facilities(String facilities) {
        this.setFacilities(facilities);
        return this;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public Room createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    public User getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(User user) {
        this.updatedBy = user;
    }

    public Room updatedBy(User user) {
        this.setUpdatedBy(user);
        return this;
    }

    public Building getBuilding() {
        return this.building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Room building(Building building) {
        this.setBuilding(building);
        return this;
    }

    public Floor getFloor() {
        return this.floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    public Room floor(Floor floor) {
        this.setFloor(floor);
        return this;
    }

    public RoomType getRoomType() {
        return this.roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public Room roomType(RoomType roomType) {
        this.setRoomType(roomType);
        return this;
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Room)) {
            return false;
        }
        return id != null && id.equals(((Room) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Room{" +
            "id=" + getId() +
            ", roomName='" + getRoomName() + "'" +
            ", remarks='" + getRemarks() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", capacity=" + getCapacity() +
            ", facilities='" + getFacilities() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomName() {
        return this.roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getCapacity() {
        return this.capacity;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getFacilities() {
        return this.facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }
}
