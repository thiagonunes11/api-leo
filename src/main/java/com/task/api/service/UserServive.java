package com.task.api.service;

import com.task.api.dto.UserCreate;
import com.task.api.entity.User;
import com.task.api.repo.UserRepo;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@AllArgsConstructor
@Log4j2
@Service
public class UserServive {

    private final UserRepo repository;
    private final ModelMapper mapper;

    @Transactional
    public void createUser(UserCreate userCreate) {
        log.info("Criar usuário");

        if (repository.findByUsername(userCreate.getUsername()).isPresent()) {
            log.error("Usuário já está presente");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já está presente!");
        }

        // Criptografando a senha
        int cost = 12; // Você pode ajustar o fator de custo conforme necessário
        String senhaCriptografada = BCrypt.withDefaults().hashToString(cost, userCreate.getPassword().toCharArray());

        User user = mapper.map(userCreate, User.class);
        user.setPassword(senhaCriptografada);

        repository.save(user);
    }

    @Transactional
    public List<User> findUserById() {
        return repository.findAll();
    }
}
