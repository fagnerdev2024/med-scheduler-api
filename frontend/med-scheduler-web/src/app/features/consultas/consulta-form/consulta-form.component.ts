import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { CommonModule } from '@angular/common';
import { finalize, forkJoin } from 'rxjs';
import { ConsultaService } from '../../../core/services/consulta.service';
import { MedicoService } from '../../../core/services/medico.service';
import { PacienteService } from '../../../core/services/paciente.service';
import { DadosListagemMedico } from '../../../core/models/medico.model';
import { DadosListagemPaciente } from '../../../core/models/paciente.model';
import { ESPECIALIDADES } from '../../../core/models/especialidades';

@Component({
  selector: 'app-consulta-form',
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
    MatDatepickerModule,
    MatNativeDateModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './consulta-form.component.html',
  styleUrl: './consulta-form.component.scss'
})
export class ConsultaFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly consultaService = inject(ConsultaService);
  private readonly medicoService = inject(MedicoService);
  private readonly pacienteService = inject(PacienteService);
  private readonly router = inject(Router);
  private readonly snackBar = inject(MatSnackBar);

  consultaForm: FormGroup = this.fb.group({
    idPaciente: [null, Validators.required],
    idMedico: [null],
    data: ['', Validators.required],
    especialidade: ['']
  });

  medicos: DadosListagemMedico[] = [];
  pacientes: DadosListagemPaciente[] = [];
  especialidades = ESPECIALIDADES;

  loading = true;
  salvando = false;

  ngOnInit(): void {
    forkJoin({
      medicos: this.medicoService.listar(0, 1000, 'nome,ASC'),
      pacientes: this.pacienteService.listar(0, 1000, 'nome,ASC')
    })
      .pipe(finalize(() => this.loading = false))
      .subscribe(({ medicos, pacientes }) => {
        this.medicos = medicos.content;
        this.pacientes = pacientes.content;
      });
  }

  onSubmit(): void {
    if (this.consultaForm.invalid) {
      this.consultaForm.markAllAsTouched();
      return;
    }

    const raw = this.consultaForm.value;
    const dados = {
      idPaciente: raw.idPaciente,
      idMedico: raw.idMedico || null,
      data: raw.data,
      especialidade: raw.especialidade || null
    };

    this.salvando = true;
    this.consultaService.agendar(dados)
      .pipe(finalize(() => this.salvando = false))
      .subscribe({
        next: () => {
          this.snackBar.open('Consulta agendada com sucesso', 'Fechar', { duration: 4000 });
          this.router.navigate(['/consultas']);
        },
        error: () => {
          this.snackBar.open('Erro ao agendar consulta', 'Fechar', { duration: 5000 });
        }
      });
  }
}
