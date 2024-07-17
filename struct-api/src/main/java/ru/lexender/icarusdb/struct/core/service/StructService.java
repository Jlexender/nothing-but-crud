package ru.lexender.icarusdb.struct.core.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.lexender.icarusdb.struct.core.model.Struct;
import ru.lexender.icarusdb.struct.core.repository.StructRepository;

import java.nio.ByteBuffer;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class StructService {
    StructRepository structRepository;

    @Transactional(readOnly = true)
    public Flux<Struct> findAll() {
        return structRepository.findAll();
    }

    @Transactional
    @CacheEvict(value = "struct", allEntries = true)
    public Mono<Void> deleteAll() {
        return structRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "struct", key = "#id")
    public Mono<Struct> findById(UUID id) {
        return structRepository.findById(id);
    }

    @Transactional
    @CacheEvict(value = "struct", key = "#id")
    public Mono<Void> deleteById(UUID id) {
        return structRepository.deleteById(id);
    }

    @Transactional
    @CachePut(value = "struct", key = "#struct.id")
    public Mono<Struct> save(Struct struct) {
        return structRepository.save(struct);
    }

    @Transactional
    public Mono<Struct> save(FilePart struct) {
        return struct.content()
                .reduce(DataBuffer::write)
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
                    return Struct.builder()
                            .structName(struct.filename())
                            .structData(byteBuffer)
                            .build();
                })
                .flatMap(this::save);
    }

    @Transactional
    @Cacheable(value = "struct", key = "#id")
    public Mono<Struct> updateById(UUID id, Struct struct) {
        return structRepository.findById(id)
                .map(s -> struct)
                .flatMap(structRepository::save);
    }
}
