package jo.sm.ship.data;

import jo.vecmath.Point3i;

public class Chunk {

    private long mTimestamp;
    private Point3i mPosition;
    private int mType;
    private Block[][][] mBlocks;

    //
    private int mTypeIndexStart; //0
    private int mTypeIndexEnd; //11 bits -> 2048 (0 incl)
    private int mHitpointsIndexStart; //11 - 8 bits -> 256
    private int mHitpointsIndexEnd; //19
    private int mActiveIndexStart; //19 - 1 bit -> 1
    private int mActiveIndexEnd; //20
    private int mOrientationStart; //20 - 4 bit -> 16
    private int mOrientationEnd; //24 

    public long getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(long timestamp) {
        mTimestamp = timestamp;
    }

    public Point3i getPosition() {
        return mPosition;
    }

    public void setPosition(Point3i position) {
        mPosition = position;
    }

    public int getType() {
        return mType;
    }

    //
    public int getTypeIndexStart() {
        return mTypeIndexStart;
    }

    public int getTypeIndexEnd() {
        return mTypeIndexEnd;
    }

    //
    public void setType(int type) {
        mType = type;
    }

    //
    public void setTypeIndexStart(int typeIndexStart) {
        mTypeIndexStart = typeIndexStart;
    }

    public void setTypeIndexEnd(int typeIndexEnd) {
        mTypeIndexEnd = typeIndexEnd;
    }

    //
    public Block[][][] getBlocks() {
        return mBlocks;
    }

    public void setBlocks(Block[][][] blocks) {
        mBlocks = blocks;
    }

}
