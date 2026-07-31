import { Component, inject, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { finalize, forkJoin } from 'rxjs';
import { CommonModule } from '@angular/common';
import { MedicoService } from '../../core/services/medico.service';
import { PacienteService } from '../../core/services/paciente.service';
import { ConsultaService } from '../../core/services/consulta.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    MatCardModule,
    MatIconModule,
    MatButtonModule,
    MatGridListModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  private readonly medicoService = inject(MedicoService);
  private readonly pacienteService = inject(PacienteService);
  private readonly consultaService = inject(ConsultaService);

  loading = true;
  totalMedicos = 0;
  totalPacientes = 0;
  totalConsultas = 0;

  ngOnInit(): void {
    forkJoin({
      medicos: this.medicoService.listar(0, 1),
      pacientes: this.pacienteService.listar(0, 1),
      consultas: this.consultaService.listar(0, 1)
    })
      .pipe(finalize(() => this.loading = false))
      .subscribe(({ medicos, pacientes, consultas }) => {
        this.totalMedicos = medicos.totalElements;
        this.totalPacientes = pacientes.totalElements;
        this.totalConsultas = consultas.totalElements;
      });
  }
}
