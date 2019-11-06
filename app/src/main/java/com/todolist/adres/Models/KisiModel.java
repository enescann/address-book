package com.todolist.adres.Models;

public class KisiModel {
    private Long id;
    private String ad;
    private String mail;
    private String telefon;
    private String adres;
    private byte [] profil;

    public KisiModel() {
    }

    public KisiModel(String ad, String mail, String telefon, String adres) {
        this.ad = ad;
        this.mail = mail;
        this.telefon = telefon;
        this.adres = adres;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public byte[] getProfil() {
        return profil;
    }

    public void setProfil(byte[] profil) {
        this.profil = profil;
    }
}
