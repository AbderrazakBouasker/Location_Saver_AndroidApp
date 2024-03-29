package com.example.friendslocationv1;

public class Position {

    int idposition;
    String longitude;
    String latitude;
    String pseudo;

    public Position() {
    }

    public Position(int idposition, String longitude, String latitude, String pseudo) {
        this.idposition = idposition;
        this.longitude = longitude;
        this.latitude = latitude;
        this.pseudo = pseudo;
    }

    public int getIdposition() {
        return idposition;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getPseudo() {
        return pseudo;
    }

    @Override
    public String toString() {
        return "Position{" +
                "idposition=" + idposition +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", pseudo='" + pseudo + '\'' +
                '}';
    }
}
