package com.example.supportservice.repository;


import com.example.supportservice.model.Ticket;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TicketRepository extends JpaRepository<Ticket, Long> {
}