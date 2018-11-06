package model;

import utils.IndexedSerializable;
import utils.OneToOneHash;

/**
 * A generic twitter object.
 * It always has an integer identifier
 * It may have a literal identifier
 */
public abstract class ObjectModel implements IndexedSerializable {
    /**
     * The integer identifier. It is globally unique inside the same class
     * and automatically generated.
     */
    private int seqId;

    public ObjectModel(int seqId){
        this.seqId = seqId;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ObjectModel)) return false;
        return seqId == ((ObjectModel) obj).seqId;
    }

    public int getId() {
        assert seqId >= 0;

        return seqId;
    }

    public String getName(OneToOneHash<Integer, String> idMap) {
        return idMap.getA(getId());
    }
}
