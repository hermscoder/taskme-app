package com.herms.taskme.repository;

import com.herms.taskme.model.Media;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MediaRepository extends CrudRepository<Media, Long> {

    @Query(value = "select m from Media m where m.type = :type")
    public Media findByType(@org.springframework.data.repository.query.Param("type") String type);

    @Modifying
    @Transactional
    @Query("delete from Media m where m.id = :id")
    void delete(@org.springframework.data.repository.query.Param("id") Long id);

}
