import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { CommonModule } from '@angular/common';
import { finalize } from 'rxjs';
import { MedicoService } from '../../../core/services/medico.service';
import { ESPECIALIDADES } from '../../../core/models/especialidades';

@Component({
  selector: 'app-medico-form',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    ReactiveFormsModule,
    MatCardModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './medico-form.component.html',
  styleUrl: './medico-form.component.scss'
})
export class MedicoFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly medicoService = inject(MedicoService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly snackBar = inject(MatSnackBar);

  medicoForm: FormGroup = this.fb.group({
    nome: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    telefone: ['', Validators.required],
    crm: ['', [Validators.required, Validators.pattern(/^\d{4,6}$/)]],
    especialidade: ['', Validators.required],
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

  especialidades = ESPECIALIDADES;
  loading = false;
  salvando = false;
  medicoId: number | null = null;

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.medicoId = Number(id);
      this.carregarMedico(this.medicoId);
    }
  }

  carregarMedico(id: number): void {
    this.loading = true;
    this.medicoService.detalhar(id)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: medico => {
          this.medicoForm.patchValue({
            nome: medico.nome,
            email: medico.email,
            telefone: medico.telefone,
            crm: medico.crm,
            especialidade: medico.especialidade,
            endereco: medico.endereco
          });
        },
        error: () => {
          this.snackBar.open('Erro ao carregar médico', 'Fechar', { duration: 5000 });
          this.router.navigate(['/medicos']);
        }
      });
  }

  onSubmit(): void {
    if (this.medicoForm.invalid) {
      this.medicoForm.markAllAsTouched();
      return;
    }

    this.salvando = true;
    const dados = this.medicoForm.value;

    const observer = {
      next: () => {
        this.salvando = false;
        this.snackBar.open('Médico salvo com sucesso', 'Fechar', { duration: 4000 });
        this.router.navigate(['/medicos']);
      },
      error: () => {
        this.salvando = false;
        this.snackBar.open('Erro ao salvar médico', 'Fechar', { duration: 5000 });
      }
    };

    if (this.medicoId) {
      this.medicoService.atualizar(this.medicoId, dados).subscribe(observer);
    } else {
      this.medicoService.cadastrar(dados).subscribe(observer);
    }
  }
}
