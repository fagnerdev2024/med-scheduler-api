import { Routes } from '@angular/router';

export const PACIENTES_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./pacientes-list/pacientes-list.component').then(m => m.PacientesListComponent)
  },
  {
    path: 'novo',
    loadComponent: () => import('./paciente-form/paciente-form.component').then(m => m.PacienteFormComponent)
  },
  {
    path: ':id/editar',
    loadComponent: () => import('./paciente-form/paciente-form.component').then(m => m.PacienteFormComponent)
  }
];
