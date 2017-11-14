package predigsystem.udl.org.predigsystem.JavaClasses;

/**
 * Created by dflorensa on 14/11/2017.
 */

public class Session {

    public String nif, name, email;

    public Session(String nif, String name, String email){
        this.nif = nif;
        this.name = name;
        this.email = email;
    }

    public void setNif (String nif) {
        this.nif = nif;
    }

    public String getNif () {
        return this.nif;
    }

    public void setName (String name){
        this.name = name;
    }

    public String getName () {
        return this.name;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public String getEmail () {
        return this.email;
    }
}
