package de.johanneswirth.tac.authservice;

import org.apache.commons.lang3.tuple.Pair;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface AuthDAO {

    @SqlQuery("select id from users where email = :email")
    int getID(@Bind("email") String email);

    @SqlQuery("select password, salt from users where email = :email")
    Pair<String, String> getPasswordHash(@Bind("email") String email);

    @SqlQuery("select count(*) from users where email = :email")
    int userExists(@Bind("email") String email);

    @SqlUpdate("insert into users (email, password, salt) values (:email, :password, :salt)")
    void registerUser(@Bind("email") String email, @Bind("password") String password, @Bind("salt") String salt);

    @SqlUpdate("update users set password = :password, salt = :salt where id = :id")
    void setPassword(@Bind("password") String password, @Bind("salt") String salt, @Bind("id") int id);
}
