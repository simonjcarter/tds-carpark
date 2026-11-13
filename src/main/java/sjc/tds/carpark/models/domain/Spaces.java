package sjc.tds.carpark.models.domain;

import sjc.tds.carpark.models.dto.SpacesResponse;

public record Spaces(int availableSpaces, int occupiedSpaces) {

    public SpacesResponse toSpacesResponse() {
        return new SpacesResponse(availableSpaces, occupiedSpaces);
    }

}
