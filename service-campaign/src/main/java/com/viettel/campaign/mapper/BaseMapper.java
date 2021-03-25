package com.viettel.campaign.mapper;

import com.viettel.campaign.web.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseMapper<Model extends Object, DTO extends BaseDTO> {

    public abstract DTO toDtoBean(Model model);

    public abstract Model toPersistenceBean(DTO dtoBean);

    public List<DTO> toDtoBean(Iterable<Model> models) {
        List<DTO> dtoBeans = new ArrayList<>();
        if (models == null) return dtoBeans;
        for (Model model : models) {
            dtoBeans.add(toDtoBean(model));
        }
        return dtoBeans;
    }

    public List<DTO> toDtoBean(List<Model> models) {
        List<DTO> dtoBeans = new ArrayList<>();

        if (models == null) return dtoBeans;
        for (Model model : models) {
            dtoBeans.add(toDtoBean(model));
        }
        return dtoBeans;
    }

    public List<Model> toPersistenceBean(List<DTO> dtoBeans) {
        List<Model> models = new ArrayList<>();
        if (dtoBeans == null || dtoBeans.isEmpty()) return models;
        for (DTO dtoBean : dtoBeans) {
            models.add(toPersistenceBean(dtoBean));
        }
        return models;
    }
}
