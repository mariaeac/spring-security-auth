package com.meac.springsecurity.repositories;

import com.meac.springsecurity.entities.Post;
import com.meac.springsecurity.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

}
