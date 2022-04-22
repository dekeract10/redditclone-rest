package rs.ac.uns.ftn.redditclonesr272020.configuration;

import java.util.UUID;

public class MyIdGenerator {
    private MyIdGenerator(){
        super();
    };

    public static UUID generateId(){
        return UUID.randomUUID();
    }
}
