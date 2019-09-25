package de.johanneswirth.tac.authservice;

import org.apache.commons.lang3.tuple.Pair;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface AuthDAO {

    @SqlQuery("select id from users where username = :username")
    int getID(@Bind("username") String username);

    @SqlQuery("select password, salt from users where username = :username")
    Pair<String, String> getPasswordHash(@Bind("username") String username);

    @SqlQuery("select count(*) from users where username = :username")
    int userExists(@Bind("username") String username);

    @SqlUpdate("insert into users (username, email, password, salt) values (:username, :email, :password, :salt)")
    void registerUser(@Bind("username") String username, @Bind("email") String email, @Bind("password") String password, @Bind("salt") String salt);

    @SqlUpdate("update users set password = :password, salt = :salt where id = :id")
    void setPassword(@Bind("password") String password, @Bind("salt") String salt, @Bind("id") int id);
}
