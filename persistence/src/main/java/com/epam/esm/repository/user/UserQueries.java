package com.epam.esm.repository.user;

public interface UserQueries
{
    String GET_ALL = "select u from UserEntity u";
    String FIND_BY_EMAIL = "select u from UserEntity u where u.email = :email";
}
