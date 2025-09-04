package com.jope.financetracker.exceptions;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(UUID id){
        super("Resultado não encontrado. Id: " + id);
    }

    public ResourceNotFoundException(Long id){
        super("Resultado não encontrado. Id: " + id);
    }

    public ResourceNotFoundException(String nome){
        super("Resultado não encontrado: " + nome);
    }
}
