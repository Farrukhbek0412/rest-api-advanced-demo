package com.epam.esm.service;

public interface BaseService<Post, Get> {
    Get create(Post p);

    Get get(Long id);


}
