import { Component, inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MotivoCancelamento } from '../../../core/models/consulta.model';

export interface CancelDialogData {
  consultaId: number;
  data: string;
}

@Component({
  selector: 'app-cancel-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatSelectModule
  ],
  templateUrl: './cancel-dialog.component.html',
  styleUrl: './cancel-dialog.component.scss'
})
export class CancelDialogComponent {
  readonly dialogRef = inject(MatDialogRef<CancelDialogComponent>);
  readonly data = inject<CancelDialogData>(MAT_DIALOG_DATA);

  motivo = new FormControl<MotivoCancelamento | null>(null, Validators.required);

  motivos: { valor: MotivoCancelamento; label: string }[] = [
    { valor: 'PACIENTE_DESISTIU - Paciente desistiu', label: 'Paciente desistiu' },
    { valor: 'MEDICO_CANCELOU - Médico cancelou', label: 'Médico cancelou' },
    { valor: 'OUTROS - Outros motivos', label: 'Outros' }
  ];

  cancelar(): void {
    this.dialogRef.close();
  }

  confirmar(): void {
    if (this.motivo.invalid) return;
    this.dialogRef.close({
      idConsulta: this.data.consultaId,
      motivo: this.motivo.value
    } as { idConsulta: number; motivo: MotivoCancelamento });
  }
}
