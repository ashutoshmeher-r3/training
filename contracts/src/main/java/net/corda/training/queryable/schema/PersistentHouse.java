package net.corda.training.queryable.schema;

import net.corda.core.identity.Party;
import net.corda.core.schemas.PersistentState;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "HOUSE_DETAIL")
public class PersistentHouse extends PersistentState {

    // Add Columns required in the table
    @Column private final String address;
    @Column private final Integer totalAreaInSqft;
    @Column private final Integer carpetAreaInSqft;
    @Column private final Integer floor;
    @Column private final Party owner;

    // Default Constructor
    public PersistentHouse() {
        this.address = null;
        this.totalAreaInSqft = null;
        this.carpetAreaInSqft = null;
        this.floor = null;
        this.owner = null;
    }

    // Parameterized Constructor
    public PersistentHouse(String address, int totalAreaInSqft, int carpetAreaInSqft, int floor, Party owner) {
        this.address = address;
        this.totalAreaInSqft = totalAreaInSqft;
        this.carpetAreaInSqft = carpetAreaInSqft;
        this.floor = floor;
        this.owner = owner;
    }

    // Getters
    public String getAddress() {
        return address;
    }

    public Integer getTotalAreaInSqft() {
        return totalAreaInSqft;
    }

    public Integer getCarpetAreaInSqft() {
        return carpetAreaInSqft;
    }

    public Integer getFloor() {
        return floor;
    }

    public Party getOwner() {
        return owner;
    }

    // toString()

    @Override
    public String toString() {
        return "PersistentHouse{" +
                "address='" + address + '\'' +
                ", totalAreaInSqft=" + totalAreaInSqft +
                ", carpetAreaInSqft=" + carpetAreaInSqft +
                ", floor=" + floor +
                ", owner=" + owner +
                '}';
    }
}
