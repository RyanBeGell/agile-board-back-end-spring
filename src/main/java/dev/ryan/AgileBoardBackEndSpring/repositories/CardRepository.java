package dev.ryan.AgileBoardBackEndSpring.repositories;

import dev.ryan.AgileBoardBackEndSpring.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByColumnIdOrderByPosition(Long columnId);

}
