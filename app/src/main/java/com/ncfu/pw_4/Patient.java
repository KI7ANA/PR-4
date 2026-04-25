package com.ncfu.pw_4;

public class Patient {
    private int id;
    private String fullName;
    private String birthDate;
    private String policyNumber;
    private String diagnosis;

    public Patient(int id, String fullName, String birthDate, String policyNumber, String diagnosis) {
        this.id = id;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.policyNumber = policyNumber;
        this.diagnosis = diagnosis;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
}