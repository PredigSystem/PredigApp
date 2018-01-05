package predigsystem.udl.org.predigsystem.JavaClasses;
import java.sql.Date;
import java.util.UUID;

public class User {
    String name;
    String nif;
    String password;
    String email;
    Integer phone;
    String address;
    Date createdat;
    String role;

    public User() {
    }

    public User(String name, String nif, String password, String email, Integer phone, String address, Date createdat, String role) {
        this.name = name;
        this.nif = nif;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
        if(createdat == null) this.createdat = new Date(new java.util.Date().getTime());
        else this.createdat = createdat;
        this.role = role;
    }

    public String generateId(){
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreatedat() {
        if(createdat == null) this.createdat = new Date(new java.util.Date().getTime());
        return createdat;
    }

    public void setCreatedat(Date createdat) {
        if(createdat == null) this.createdat = new Date(new java.util.Date().getTime());
        else this.createdat = createdat;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                ", name='" + name + '\'' +
                ", nif='" + nif + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone=" + phone +
                ", address='" + address + '\'' +
                ", createdat=" + createdat + '\'' +
                ", role=" + role +
                '}';
    }
}