package com.example.supportservice.service;

import com.example.supportservice.dto.TicketRequest;
import com.example.supportservice.dto.TicketResponse;
import com.example.supportservice.enums.Role;
import com.example.supportservice.enums.TicketStatus;
import com.example.supportservice.exception.ResourceNotFoundException;
import com.example.supportservice.exception.TicketNotFoundException;
import com.example.supportservice.model.Attachment;
import com.example.supportservice.model.Ticket;
import com.example.supportservice.model.User;
import com.example.supportservice.repository.TicketRepository;
import com.example.supportservice.repository.UserRepository;
import com.example.supportservice.utils.FileStorageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final FileStorageUtil fileStorageUtil;
    private final UserRepository userRepository;

    /**
     * Create a new ticket.
     * @param request TicketRequest
     * @return TicketResponse
     */
    public TicketResponse createTicket( TicketRequest request) {
        log.info("Creating new ticket with subject: {}", request.getSubject());

        Ticket ticket = Ticket.builder()
                              .subject(request.getSubject())
                              .description(request.getDescription())
                              .priority(request.getPriority())
                              .status(TicketStatus.OPEN)
                              .createdAt(LocalDateTime.now())
                              .updatedAt(LocalDateTime.now())
                              .build();

        ticketRepository.save(ticket);

        return TicketResponse.from(ticket);
    }

    /**
     * Fetch a ticket by its ID.
     * @param id Ticket ID
     * @return TicketResponse
     */
    public TicketResponse getTicketById(Long id) {
        log.info("Fetching ticket with id: {}", id);

        Ticket ticket = ticketRepository.findById(id)
                                        .orElseThrow(() -> new TicketNotFoundException("Ticket not found with id: " + id));

        return TicketResponse.from(ticket);
    }

    public void saveAttachment(Long ticketId, MultipartFile file) {
        Ticket ticket = ticketRepository.findById(ticketId)
                                        .orElseThrow(() -> new TicketNotFoundException("Ticket not found with ID " + ticketId));

        // Save the file to local or cloud and return its URL
        String fileUrl = fileStorageUtil.save(file); // Implement this

        Attachment attachment = Attachment.builder()
                                          .fileName(file.getOriginalFilename())
                                          .fileType(file.getContentType())
                                          .fileUrl(fileUrl)
                                          .ticket(ticket)
                                          .build();

        ticket.getAttachments().add(attachment);
        ticketRepository.save(ticket);
    }

    public TicketResponse updateTicket(Long id, TicketRequest request) {
        Ticket ticket = ticketRepository.findById(id)
                                        .orElseThrow(() -> new TicketNotFoundException("Ticket not found with ID " + id));

        ticket.setSubject(request.getSubject());
        ticket.setDescription(request.getDescription());
        ticket.setPriority(request.getPriority());
        ticket.setAssignedTo(request.getAssignedTo());
        ticket.setUpdatedAt(LocalDateTime.now());

        ticketRepository.save(ticket);
        return TicketResponse.from(ticket);
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public void assignTicketToAgent(Long ticketId, Long agentId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                                        .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        User agent = userRepository.findById(agentId)
                                   .orElseThrow(() -> new ResourceNotFoundException("Agent not found"));

        if (agent.getRole() != Role.AGENT) {
            throw new IllegalArgumentException("User is not an agent");
        }

        ticket.setAssignedAgent(agent);
        ticketRepository.save(ticket);
    }


    public Long autoAssignAgent(Long ticketId) {
        List<User> agents = userRepository.findByRole(Role.AGENT);
        if (agents.isEmpty()) {
            throw new RuntimeException("No agents available for assignment");
        }

        // Example logic: pick random agent (can be least-loaded, round-robin, etc.)
        User chosen = agents.get(new Random().nextInt(agents.size()));

        assignTicketToAgent(ticketId, chosen.getId());
        return chosen.getId();
    }

}
