package com.codeoftheweb.salvo;


import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String name;
    private String email;
    private String password;

    public Player() { }

    public Player(String name, String email, String password ) {

        this.name= name;
        this.email= email;
        this.password=password;
    }


    //Method getter
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }

    //Method setter
    public void setName(String name) {
         this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }


}
