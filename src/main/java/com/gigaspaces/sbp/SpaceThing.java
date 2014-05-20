package com.gigaspaces.sbp;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceRouting;

/**
 * User: jason.nerothin
 * Date: 5/19/14
 * Time: 9:10 PM
 *
 * About as boring a {@link SpaceClass} as can be.
 */
@SpaceClass
public class SpaceThing {

    private String spaceId;
    private Integer routeId;
    private String payload;

    @SpaceId(autoGenerate = true)
    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }

    @SpaceRouting
    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpaceThing that = (SpaceThing) o;

        if (payload != null ? !payload.equals(that.payload) : that.payload != null) return false;
        if (routeId != null ? !routeId.equals(that.routeId) : that.routeId != null) return false;
        if (spaceId != null ? !spaceId.equals(that.spaceId) : that.spaceId != null) return false;

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = spaceId != null ? spaceId.hashCode() : 0;
        result = 31 * result + (routeId != null ? routeId.hashCode() : 0);
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SpaceThing{");
        sb.append("spaceId='").append(spaceId).append('\'');
        sb.append(", routeId=").append(routeId);
        sb.append(", payload='").append(payload).append('\'');
        sb.append('}');
        return sb.toString();
    }

}