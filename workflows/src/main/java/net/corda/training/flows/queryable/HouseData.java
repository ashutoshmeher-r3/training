package net.corda.training.flows.queryable;

import net.corda.core.serialization.CordaSerializable;

@CordaSerializable
public class HouseData {

    private String address;
    private int totalAreaInSqft;
    private int carpetAreaInSqft;
    private int floor;

    public HouseData() {}

    public HouseData(String address, int totalAreaInSqft, int carpetAreaInSqft, int floor) {
        this.address = address;
        this.totalAreaInSqft = totalAreaInSqft;
        this.carpetAreaInSqft = carpetAreaInSqft;
        this.floor = floor;
    }

    public String getAddress() {
        return address;
    }

    public int getTotalAreaInSqft() {
        return totalAreaInSqft;
    }

    public int getCarpetAreaInSqft() {
        return carpetAreaInSqft;
    }

    public int getFloor() {
        return floor;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTotalAreaInSqft(int totalAreaInSqft) {
        this.totalAreaInSqft = totalAreaInSqft;
    }

    public void setCarpetAreaInSqft(int carpetAreaInSqft) {
        this.carpetAreaInSqft = carpetAreaInSqft;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }
}
