package com.perivoliotis.app.eSymposium.responses;

import java.util.List;

public class HomePageResponse {

    private List<String> eSymposiumUsernames;

    public List<String> geteSymposiumUsernames() {
        return eSymposiumUsernames;
    }

    public void seteSymposiumUsernames(List<String> eSymposiumUsernames) {
        this.eSymposiumUsernames = eSymposiumUsernames;
    }
}
