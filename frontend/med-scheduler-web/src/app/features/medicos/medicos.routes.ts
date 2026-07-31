import { Routes } from '@angular/router';

export const MEDICOS_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./medicos-list/medicos-list.component').then(m => m.MedicosListComponent)
  },
  {
    path: 'novo',
    loadComponent: () => import('./medico-form/medico-form.component').then(m => m.MedicoFormComponent)
  },
  {
    path: ':id/editar',
    loadComponent: () => import('./medico-form/medico-form.component').then(m => m.MedicoFormComponent)
  }
];
