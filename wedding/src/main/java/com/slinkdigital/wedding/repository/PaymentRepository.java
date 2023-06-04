package com.slinkdigital.wedding.repository;

import com.slinkdigital.wedding.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author TEGA
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{
    
}
