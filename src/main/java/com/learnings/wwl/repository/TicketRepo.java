package com.learnings.wwl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learnings.wwl.model.Ticket;

@Repository
public interface TicketRepo extends JpaRepository<Ticket, Long>  {

}
