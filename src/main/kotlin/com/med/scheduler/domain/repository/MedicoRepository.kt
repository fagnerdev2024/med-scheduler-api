package com.med.scheduler.domain.repository

import com.med.scheduler.domain.model.Medico
import com.med.scheduler.domain.model.enums.Especialidade
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

interface MedicoRepository {
    fun save(medico: Medico): Medico

    fun findById(id: Long): Medico?

    fun findAllByAtivoTrue(paginacao: Pageable): Page<Medico>

    fun findAtivoById(id: Long): Boolean?

    fun escolherMedicoAleatorioLivreNaData(
        especialidade: Especialidade,
        data: LocalDateTime,
    ): Medico?

    fun delete(id: Long)
}
