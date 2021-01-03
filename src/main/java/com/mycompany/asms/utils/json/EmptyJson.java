package com.mycompany.asms.utils.json;

import com.mycompany.asms.model.Entity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class EmptyJson extends Entity {
    @Override
    public String getId() {
        return null;
    }
}
