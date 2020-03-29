package com.perivoliotis.app.eSymposium.repos.custom;

import com.perivoliotis.app.eSymposium.entities.symposium.SymposiumUser;

public interface SymposiumUserRepositoryCustom {

    boolean saveOrUpdate(SymposiumUser symposiumUser);
}
