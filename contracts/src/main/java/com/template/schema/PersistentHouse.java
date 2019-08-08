package com.template.schema;

import net.corda.core.identity.Party;
import net.corda.core.schemas.PersistentState;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "house_detail")
public class PersistentHouse extends PersistentState {

    @Column private final String address;
    @Column private final Integer totalAreaInSqft;
    @Column private final Integer carpetAreaInSqft;
    @Column private final Integer floor;
    @Column private final Party owner;

    public PersistentHouse(String address, Integer totalAreaInSqft, Integer carpetAreaInSqft, Integer floor, Party owner) {
        this.address = address;
        this.totalAreaInSqft = totalAreaInSqft;
        this.carpetAreaInSqft = carpetAreaInSqft;
        this.floor = floor;
        this.owner = owner;
    }

    public PersistentHouse() {
        this.address = null;
        this.totalAreaInSqft = null;
        this.carpetAreaInSqft = null;
        this.floor = null;
        this.owner = null;
    }

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
