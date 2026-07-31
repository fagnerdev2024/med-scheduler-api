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
import { MedicoService } from '../../../core/services/medico.service';
import { DadosListagemMedico } from '../../../core/models/medico.model';
import { Page } from '../../../core/models/page.model';

@Component({
  selector: 'app-medicos-list',
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
  templateUrl: './medicos-list.component.html',
  styleUrl: './medicos-list.component.scss'
})
export class MedicosListComponent implements OnInit {
  private readonly medicoService = inject(MedicoService);
  private readonly snackBar = inject(MatSnackBar);

  displayedColumns: string[] = ['nome', 'email', 'crm', 'especialidade', 'acoes'];
  page: Page<DadosListagemMedico> | null = null;
  dataSource: DadosListagemMedico[] = [];
  loading = true;

  filtro = new FormControl('');
  pageIndex = 0;
  pageSize = 10;
  sort = 'nome,ASC';

  ngOnInit(): void {
    this.carregarMedicos();
    this.filtro.valueChanges.pipe(debounceTime(400)).subscribe(() => {
      this.pageIndex = 0;
      this.carregarMedicos();
    });
  }

  carregarMedicos(): void {
    this.loading = true;
    this.medicoService.listar(this.pageIndex, this.pageSize, this.sort)
      .pipe(finalize(() => this.loading = false))
      .subscribe(page => {
        this.page = page;
        this.dataSource = this.filtrar(page.content);
      });
  }

  filtrar(medicos: DadosListagemMedico[]): DadosListagemMedico[] {
    const termo = this.filtro.value?.toLowerCase() || '';
    if (!termo) return medicos;
    return medicos.filter(m =>
      m.nome.toLowerCase().includes(termo) ||
      m.email.toLowerCase().includes(termo) ||
      m.crm.toLowerCase().includes(termo) ||
      m.especialidade.toLowerCase().includes(termo)
    );
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.carregarMedicos();
  }

  excluir(id: number): void {
    if (!confirm('Deseja realmente excluir este médico?')) return;
    this.medicoService.excluir(id).subscribe({
      next: () => {
        this.snackBar.open('Médico excluído com sucesso', 'Fechar', { duration: 4000 });
        this.carregarMedicos();
      },
      error: () => {
        this.snackBar.open('Erro ao excluir médico', 'Fechar', { duration: 4000 });
      }
    });
  }
}
