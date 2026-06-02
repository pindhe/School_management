import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { adminGuard } from './core/guards/admin.guard';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'students' },
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login/login.component').then((m) => m.LoginComponent),
  },
  {
    path: 'register',
    loadComponent: () => import('./features/auth/register/register.component').then((m) => m.RegisterComponent),
  },
  {
    path: '',
    canActivate: [authGuard],
    loadComponent: () => import('./layout/main-layout/main-layout.component').then((m) => m.MainLayoutComponent),
    children: [
      {
        path: 'students',
        loadComponent: () =>
          import('./features/students/student-list/student-list.component').then((m) => m.StudentListComponent),
      },
      {
        path: 'students/new',
        canActivate: [adminGuard],
        loadComponent: () =>
          import('./features/students/student-form/student-form.component').then((m) => m.StudentFormComponent),
      },
      {
        path: 'students/:id/edit',
        canActivate: [adminGuard],
        loadComponent: () =>
          import('./features/students/student-form/student-form.component').then((m) => m.StudentFormComponent),
      },
    ],
  },
  { path: '**', redirectTo: 'students' },
];
