import { Routes } from '@angular/router';

export const CONSULTAS_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./consultas-list/consultas-list.component').then(m => m.ConsultasListComponent)
  },
  {
    path: 'novo',
    loadComponent: () => import('./consulta-form/consulta-form.component').then(m => m.ConsultaFormComponent)
  }
];
