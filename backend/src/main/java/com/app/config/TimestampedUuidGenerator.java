package com.app.config;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class TimestampedUuidGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        long timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        UUID uuid = UUID.randomUUID();

        // Combine timestamp and UUID to create a time-ordered UUID
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(timestamp);
        bb.putLong(uuid.getLeastSignificantBits()); // Use least significant bits to avoid collision with timestamp

        return new UUID(bb.getLong(0), bb.getLong(8));
    }
}
