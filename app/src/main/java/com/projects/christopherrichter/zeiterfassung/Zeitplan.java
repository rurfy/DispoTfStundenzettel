package com.projects.christopherrichter.zeiterfassung;

import java.util.Calendar;
import java.util.Date;

public class Zeitplan {


    private String pause;

    private Calendar dienstBeginn;
    private Calendar dienstEnde;
    private Calendar abfahrt;
    private Calendar ankunft;

    public Zeitplan() {

    }

    public Calendar getDienstBeginn() {
        return dienstBeginn;
    }

    public void setDienstBeginn(Calendar dienstBeginn) {
        this.dienstBeginn = dienstBeginn;
    }

    public Calendar getDienstEnde() {
        return dienstEnde;
    }

    public void setDienstEnde(Calendar dienstEnde) {
        this.dienstEnde = dienstEnde;
    }

    public Calendar getAbfahrt() {
        return abfahrt;
    }

    public void setAbfahrt(Calendar abfahrt) {
        this.abfahrt = abfahrt;
    }

    public Calendar getAnkunft() {
        return ankunft;
    }

    public void setAnkunft(Calendar ankunft) {
        this.ankunft = ankunft;
    }

    public String getPause() {
        return pause;
    }

    public void setPause(String pause) {
        this.pause = pause;
    }


}
