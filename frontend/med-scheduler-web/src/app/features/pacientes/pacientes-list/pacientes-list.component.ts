import { Component, inject, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { debounceTime } from 'rxjs/operators';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { CommonModule } from '@angular/common';
import { finalize } from 'rxjs';
import { PacienteService } from '../../../core/services/paciente.service';
import { DadosListagemPaciente } from '../../../core/models/paciente.model';
import { Page } from '../../../core/models/page.model';

@Component({
  selector: 'app-pacientes-list',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    ReactiveFormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatPaginatorModule,
    MatFormFieldModule,
    MatInputModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './pacientes-list.component.html',
  styleUrl: './pacientes-list.component.scss'
})
export class PacientesListComponent implements OnInit {
  private readonly pacienteService = inject(PacienteService);
  private readonly snackBar = inject(MatSnackBar);

  displayedColumns: string[] = ['nome', 'email', 'cpf', 'acoes'];
  page: Page<DadosListagemPaciente> | null = null;
  dataSource: DadosListagemPaciente[] = [];
  loading = true;

  filtro = new FormControl('');
  pageIndex = 0;
  pageSize = 10;
  sort = 'nome,ASC';

  ngOnInit(): void {
    this.carregarPacientes();
    this.filtro.valueChanges.pipe(debounceTime(400)).subscribe(() => {
      this.pageIndex = 0;
      this.carregarPacientes();
    });
  }

  carregarPacientes(): void {
    this.loading = true;
    this.pacienteService.listar(this.pageIndex, this.pageSize, this.sort)
      .pipe(finalize(() => this.loading = false))
      .subscribe(page => {
        this.page = page;
        this.dataSource = this.filtrar(page.content);
      });
  }

  filtrar(pacientes: DadosListagemPaciente[]): DadosListagemPaciente[] {
    const termo = this.filtro.value?.toLowerCase() || '';
    if (!termo) return pacientes;
    return pacientes.filter(p =>
      p.nome.toLowerCase().includes(termo) ||
      p.email.toLowerCase().includes(termo) ||
      p.cpf.toLowerCase().includes(termo)
    );
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.carregarPacientes();
  }

  excluir(id: number): void {
    if (!confirm('Deseja realmente excluir este paciente?')) return;
    this.pacienteService.excluir(id).subscribe({
      next: () => {
        this.snackBar.open('Paciente excluído com sucesso', 'Fechar', { duration: 4000 });
        this.carregarPacientes();
      },
      error: () => {
        this.snackBar.open('Erro ao excluir paciente', 'Fechar', { duration: 4000 });
      }
    });
  }
}
