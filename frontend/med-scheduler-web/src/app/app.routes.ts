import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./features/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: '',
    canActivate: [authGuard],
    loadComponent: () => import('./shared/layout/layout.component').then(m => m.LayoutComponent),
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', loadComponent: () => import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent) },
      { path: 'medicos', loadChildren: () => import('./features/medicos/medicos.routes').then(m => m.MEDICOS_ROUTES) },
      { path: 'pacientes', loadChildren: () => import('./features/pacientes/pacientes.routes').then(m => m.PACIENTES_ROUTES) },
      { path: 'consultas', loadChildren: () => import('./features/consultas/consultas.routes').then(m => m.CONSULTAS_ROUTES) },
    ]
  },
  { path: '**', redirectTo: 'login' }
];
