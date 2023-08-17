package com.epam.repositories;

import com.epam.collections.EmailLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailLogRepository extends MongoRepository<EmailLog,String> {
}
