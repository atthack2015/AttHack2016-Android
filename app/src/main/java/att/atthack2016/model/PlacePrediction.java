package att.atthack2016.model;

import android.support.annotation.Nullable;

/**
 * Created by Pedro Hernández on 09/2015.
 */
public class PlacePrediction {

    public static final int SEPARATOR_LENGTH = 2;
    public static final String DESCRIPTION_SEPARATOR = ", ";

    public String placeId;
    public String description;

    public PlacePrediction(String placeId, String description) {
        this.placeId = placeId;
        this.description = description;
    }

    @Nullable
    public String getShortName(){
        return description.split(",")[0];

    }

    public String getDescription(){
        int indexOfSeparator = description.indexOf(DESCRIPTION_SEPARATOR) + SEPARATOR_LENGTH;
        return Math.abs(indexOfSeparator) != 1? description.substring(indexOfSeparator) : description;
    }
}
