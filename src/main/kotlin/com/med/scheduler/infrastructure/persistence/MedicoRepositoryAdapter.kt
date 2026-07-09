package com.med.scheduler.infrastructure.persistence

import com.med.scheduler.domain.model.Medico
import com.med.scheduler.domain.model.enums.Especialidade
import com.med.scheduler.domain.repository.MedicoRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class MedicoRepositoryAdapter(
    private val jpaRepository: MedicoJpaRepository
) : MedicoRepository {
    override fun save(medico: Medico): Medico {
        return jpaRepository.save(medico)
    }

    override fun findById(id: Long): Medico? {
        return jpaRepository.findById(id).orElse(null)
    }

    override fun findAllByAtivoTrue(paginacao: Pageable): Page<Medico> {
        return jpaRepository.findAllByAtivoTrue(paginacao)
    }

    override fun findAtivoById(id: Long): Boolean? {
        return jpaRepository.findAtivoById(id)
    }

    override fun escolherMedicoAleatorioLivreNaData(especialidade: Especialidade, data: LocalDateTime): Medico? {
        return jpaRepository.escolherMedicoAleatorioLivreNaData(especialidade, data)
    }

    override fun delete(id: Long) {
        jpaRepository.deleteById(id)
    }
}