package com.herms.taskme.repository;

import com.herms.taskme.model.Param;
import com.herms.taskme.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParamRepository extends CrudRepository<Param, Long> {

    public static final String DEFAULT_PROFILE_IMG_URL  = "https://res.cloudinary.com/dsonk49e9/image/upload/v1578403951/default_profile_img.png";
    
    @Query(value = "select p from Param p where p.code = :code")
    public Param findByCode(@org.springframework.data.repository.query.Param("code") String code);
}
