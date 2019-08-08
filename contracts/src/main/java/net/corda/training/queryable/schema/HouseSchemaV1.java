package net.corda.training.queryable.schema;

import com.google.common.collect.ImmutableList;
import net.corda.core.schemas.MappedSchema;

public class HouseSchemaV1 extends MappedSchema {
    public HouseSchemaV1() {
        super(HouseSchema.class, 1, ImmutableList.of(PersistentHouse.class));
    }
}
