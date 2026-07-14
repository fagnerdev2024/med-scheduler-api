package com.med.scheduler.infrastructure.persistence

import com.med.scheduler.domain.model.Medico
import com.med.scheduler.domain.model.enums.Especialidade
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface MedicoJpaRepository : JpaRepository<Medico, Long> {
    fun findAllByAtivoTrue(paginacao: Pageable): Page<Medico>

    @Query(
        """
        select m from Medico m
        where
        m.ativo = true
        and
        m.especialidade = :especialidade
        and
        m.id not in(
            select c.medico.id from Consulta c
            where
            c.data = :data
            and
            c.motivoCancelamento is null
        )
        order by rand()
        limit 1
    """,
    )
    fun escolherMedicoAleatorioLivreNaData(
        especialidade: Especialidade,
        data: LocalDateTime,
    ): Medico?

    @Query(
        """
        select m.ativo
        from Medico m
        where
        m.id = :id
    """,
    )
    fun findAtivoById(id: Long): Boolean?

    @Query("SELECT DISTINCT m.nome FROM Medico m WHERE m.ativo = true")
    fun findDistinctByNome(): List<String>
}
