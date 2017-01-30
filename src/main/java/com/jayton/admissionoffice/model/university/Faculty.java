package com.jayton.admissionoffice.model.university;

public class Faculty {
    private long id;
    private String name;
    private String officePhone;
    private String officeEmail;
    private String officeAddress;
    private long universityId;

    public Faculty(String name, String officePhone, String officeEmail, String officeAddress, long universityId) {
        this.name = name;
        this.officePhone = officePhone;
        this.officeEmail = officeEmail;
        this.officeAddress = officeAddress;
        this.universityId = universityId;
    }

    public Faculty(long id, String name, String officePhone, String officeEmail, String officeAddress, long universityId) {
        this.id = id;
        this.name = name;
        this.officePhone = officePhone;
        this.officeEmail = officeEmail;
        this.officeAddress = officeAddress;
        this.universityId = universityId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public String getOfficeEmail() {
        return officeEmail;
    }

    public long getUniversityId() {
        return universityId;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Faculty faculty = (Faculty) o;

        if (id != faculty.id) return false;
        if (universityId != faculty.universityId) return false;
        if (name != null ? !name.equals(faculty.name) : faculty.name != null) return false;
        if (officePhone != null ? !officePhone.equals(faculty.officePhone) : faculty.officePhone != null) return false;
        if (officeEmail != null ? !officeEmail.equals(faculty.officeEmail) : faculty.officeEmail != null) return false;
        return officeAddress != null ? officeAddress.equals(faculty.officeAddress) : faculty.officeAddress == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (officePhone != null ? officePhone.hashCode() : 0);
        result = 31 * result + (officeEmail != null ? officeEmail.hashCode() : 0);
        result = 31 * result + (officeAddress != null ? officeAddress.hashCode() : 0);
        result = 31 * result + (int) (universityId ^ (universityId >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", officePhone='" + officePhone + '\'' +
                ", officeEmail='" + officeEmail + '\'' +
                ", officeAddress='" + officeAddress + '\'' +
                ", universityId=" + universityId +
                '}';
    }
}