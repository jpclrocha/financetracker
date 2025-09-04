package com.jope.financetracker.config.uuid_v7;

import com.fasterxml.uuid.Generators;

import java.util.UUID;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

public class UuidV7Generator extends SequenceStyleGenerator {
    @Override
    public UUID generate(SharedSessionContractImplementor session, Object object) {
        return Generators.timeBasedEpochGenerator().generate();
    }
}