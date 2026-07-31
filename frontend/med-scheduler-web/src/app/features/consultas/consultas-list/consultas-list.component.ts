import { Component, inject, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import { finalize, forkJoin } from 'rxjs';
import { ConsultaService } from '../../../core/services/consulta.service';
import { MedicoService } from '../../../core/services/medico.service';
import { PacienteService } from '../../../core/services/paciente.service';
import { DadosDetalhamentoConsulta } from '../../../core/models/consulta.model';
import { DadosListagemMedico } from '../../../core/models/medico.model';
import { DadosListagemPaciente } from '../../../core/models/paciente.model';
import { Page } from '../../../core/models/page.model';
import { CancelDialogComponent } from '../cancel-dialog/cancel-dialog.component';

@Component({
  selector: 'app-consultas-list',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatDialogModule
  ],
  templateUrl: './consultas-list.component.html',
  styleUrl: './consultas-list.component.scss'
})
export class ConsultasListComponent implements OnInit {
  private readonly consultaService = inject(ConsultaService);
  private readonly medicoService = inject(MedicoService);
  private readonly pacienteService = inject(PacienteService);
  private readonly snackBar = inject(MatSnackBar);
  private readonly dialog = inject(MatDialog);

  displayedColumns: string[] = ['data', 'medico', 'paciente', 'status', 'acoes'];
  page: Page<DadosDetalhamentoConsulta> | null = null;
  dataSource: DadosDetalhamentoConsulta[] = [];
  medicos: Map<number, DadosListagemMedico> = new Map();
  pacientes: Map<number, DadosListagemPaciente> = new Map();
  loading = true;

  pageIndex = 0;
  pageSize = 10;
  sort = 'data,ASC';

  ngOnInit(): void {
    this.carregarConsultas();
  }

  carregarConsultas(): void {
    this.loading = true;
    forkJoin({
      consultas: this.consultaService.listar(this.pageIndex, this.pageSize, this.sort),
      medicos: this.medicoService.listar(0, 1000, 'nome,ASC'),
      pacientes: this.pacienteService.listar(0, 1000, 'nome,ASC')
    })
      .pipe(finalize(() => this.loading = false))
      .subscribe(({ consultas, medicos, pacientes }) => {
        this.page = consultas;
        this.dataSource = consultas.content;
        this.medicos = new Map(medicos.content.map(m => [m.id, m]));
        this.pacientes = new Map(pacientes.content.map(p => [p.id, p]));
      });
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.carregarConsultas();
  }

  nomeMedico(id: number): string {
    return this.medicos.get(id)?.nome || `Médico ${id}`;
  }

  nomePaciente(id: number): string {
    return this.pacientes.get(id)?.nome || `Paciente ${id}`;
  }

  cancelar(consulta: DadosDetalhamentoConsulta): void {
    const dialogRef = this.dialog.open(CancelDialogComponent, {
      width: '400px',
      data: { consultaId: consulta.id, data: consulta.data }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (!result) return;
      this.consultaService.cancelar(result).subscribe({
        next: () => {
          this.snackBar.open('Consulta cancelada', 'Fechar', { duration: 4000 });
          this.carregarConsultas();
        },
        error: () => {
          this.snackBar.open('Erro ao cancelar consulta', 'Fechar', { duration: 5000 });
        }
      });
    });
  }
}
