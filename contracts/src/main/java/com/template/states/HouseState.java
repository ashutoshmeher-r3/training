package com.template.states;

import com.google.common.collect.ImmutableList;
import com.template.contracts.HouseContract;
import com.template.schema.HouseSchemaV1;
import com.template.schema.PersistentHouse;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;
import net.corda.core.schemas.QueryableState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

// *********
// * HouseState *
// *********
@BelongsToContract(HouseContract.class)
public class HouseState implements ContractState {

    // Properties of House
    private final String address;
    private final int totalAreaInSqft;
    private final int carpetAreaInSqft;
    private final int floor;

    //Participants
    private final Party owner;
    private final Party registrar;

    public HouseState(String address, int totalAreaInSqft, int carpetAreaInSqft, int floor, Party owner, Party registrar) {
        this.address = address;
        this.totalAreaInSqft = totalAreaInSqft;
        this.carpetAreaInSqft = carpetAreaInSqft;
        this.floor = floor;
        this.owner = owner;
        this.registrar = registrar;
    }

    @NotNull
    @Override
    public List<AbstractParty> getParticipants() {
        return ImmutableList.of(owner, registrar);
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

    public Party getOwner() {
        return owner;
    }

    public Party getRegistrar() {
        return registrar;
    }
}