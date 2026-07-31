import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { CommonModule } from '@angular/common';
import { finalize } from 'rxjs';
import { PacienteService } from '../../../core/services/paciente.service';

@Component({
  selector: 'app-paciente-form',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    ReactiveFormsModule,
    MatCardModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './paciente-form.component.html',
  styleUrl: './paciente-form.component.scss'
})
export class PacienteFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly pacienteService = inject(PacienteService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly snackBar = inject(MatSnackBar);

  pacienteForm: FormGroup = this.fb.group({
    nome: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    telefone: ['', Validators.required],
    cpf: ['', [Validators.required, Validators.pattern(/^\d{3}\.?\d{3}\.?\d{3}-?\d{2}$/)]],
    endereco: this.fb.group({
      logradouro: ['', Validators.required],
      numero: ['', Validators.required],
      complemento: [''],
      bairro: ['', Validators.required],
      cep: ['', [Validators.required, Validators.pattern(/^\d{8}$/)]],
      cidade: ['', Validators.required],
      uf: ['', [Validators.required, Validators.maxLength(2)]]
    })
  });

  loading = false;
  salvando = false;
  pacienteId: number | null = null;

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.pacienteId = Number(id);
      this.carregarPaciente(this.pacienteId);
    }
  }

  carregarPaciente(id: number): void {
    this.loading = true;
    this.pacienteService.detalhar(id)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: paciente => {
          this.pacienteForm.patchValue({
            nome: paciente.nome,
            email: paciente.email,
            telefone: paciente.telefone,
            cpf: paciente.cpf,
            endereco: paciente.endereco
          });
        },
        error: () => {
          this.snackBar.open('Erro ao carregar paciente', 'Fechar', { duration: 5000 });
          this.router.navigate(['/pacientes']);
        }
      });
  }

  onSubmit(): void {
    if (this.pacienteForm.invalid) {
      this.pacienteForm.markAllAsTouched();
      return;
    }

    this.salvando = true;
    const dados = this.pacienteForm.value;

    const observer = {
      next: () => {
        this.salvando = false;
        this.snackBar.open('Paciente salvo com sucesso', 'Fechar', { duration: 4000 });
        this.router.navigate(['/pacientes']);
      },
      error: () => {
        this.salvando = false;
        this.snackBar.open('Erro ao salvar paciente', 'Fechar', { duration: 5000 });
      }
    };

    if (this.pacienteId) {
      this.pacienteService.atualizar(this.pacienteId, dados).subscribe(observer);
    } else {
      this.pacienteService.cadastrar(dados).subscribe(observer);
    }
  }
}
