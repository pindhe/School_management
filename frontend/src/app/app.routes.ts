import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { adminGuard } from './core/guards/admin.guard';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
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
        path: 'dashboard',
        loadComponent: () =>
          import('./features/dashboard/dashboard.component').then((m) => m.DashboardComponent),
      },
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
      {
        path: 'reports',
        canActivate: [adminGuard],
        loadComponent: () => import('./features/reports/reports.component').then((m) => m.ReportsComponent),
      },
      {
        path: 'users',
        canActivate: [adminGuard],
        loadComponent: () => import('./features/users/users.component').then((m) => m.UsersComponent),
      },
      {
        path: 'profile',
        loadComponent: () => import('./features/profile/profile.component').then((m) => m.ProfileComponent),
      },
      {
        path: 'settings',
        loadComponent: () => import('./features/settings/settings.component').then((m) => m.SettingsComponent),
      },
      {
        path: 'help',
        loadComponent: () => import('./features/help/help.component').then((m) => m.HelpComponent),
      },
    ],
  },
  { path: '**', redirectTo: 'dashboard' },
];
